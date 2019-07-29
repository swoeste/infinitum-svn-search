/*-
 * Copyright (C) 2017 Sebastian Woeste
 *
 * Licensed to Sebastian Woeste under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership. I license this file to You under
 * the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License
 * at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.swoeste.infinitum.fw.core.bl.svn.indexer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.swoeste.infinitum.common.utils.ThreadUtils;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.action.AbstractAction;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.action.IActionQueue;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.exception.SVNIndexException;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.ext.analyzer.SVNIndexAnalyzer;

public class SVNIndexWriter implements IActionQueue {

    private static final Logger         LOG      = LoggerFactory.getLogger(SVNIndexWriter.class);

    private final Analyzer              analyzer;

    private final ExecutorService       executor = Executors.newSingleThreadExecutor();

    private final Queue<AbstractAction> queue    = new LinkedList<>();

    private final Path                  location;

    private IndexWriter                 writer;

    private SVNIndexWriterJob           writerJob;

    private boolean                     completed;

    /**
     * Constructor for a new Indexer.
     *
     * @param location
     * @param reader
     */
    public SVNIndexWriter(final Path location) {
        this.location = location;
        this.completed = false;
        this.analyzer = new SVNIndexAnalyzer();
    }

    /** {@inheritDoc} */
    @Override
    public void completed() {
        ensureStartup();
        this.completed = true;
    }

    @Override
    public void addAction(final AbstractAction action) {
        ensureStartup();
        ensureRunning();
        LOG.debug("Adding new action to queue, new size of queue is {}", this.queue.size());
        this.queue.add(action);
    }

    /** {@inheritDoc} */
    @Override
    public int size() {
        ensureStartup();
        return this.queue.size();
    }

    private void ensureStartup() {
        if (this.writerJob == null) {
            throw new IllegalStateException("The SVNIndexWriter was not started prior to the execution of this method, starting the SVNIndexWriter is mandatory.");
        }
    }

    private void ensureRunning() {
        if (this.writerJob.isAbortedByException()) {
            throw new IllegalStateException("The SVNIndexWriterJob was aborted by an exception.", this.writerJob.getExecutionException());
        }
    }

    public void startup() {
        LOG.debug("Startup, creating task to write index.");
        this.writerJob = new SVNIndexWriterJob();
        this.executor.execute(this.writerJob);
    }

    public void shutdown() {
        LOG.debug("Shutdown, current task could finish.");
        this.executor.shutdown();
        try {
            LOG.debug("Await termination of executor task.");
            boolean successfullyTerminated = this.executor.awaitTermination(1L, TimeUnit.HOURS);

            if (successfullyTerminated) {
                LOG.debug("Termination of executor task successfull.");
            } else {
                // TODO handle awaitTermination, if we terminate before queue is
                // completed we have deleted some actions!
                LOG.error("Termination of executor task exceeded timeout of 1 hour!");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.error("Error while trying to shutdown SVNIndexWriter", e);
        } finally {
            this.writerJob = null;
        }
    }

    private void open() throws IOException {
        LOG.debug("Connection to lucene index has been opened.");
        final Directory directory = FSDirectory.open(this.location);

        final IndexWriterConfig config = new IndexWriterConfig(this.analyzer);
        config.setOpenMode(OpenMode.CREATE_OR_APPEND);
        this.writer = new IndexWriter(directory, config);
    }

    private void close() {
        LOG.debug("Connection to lucene index has been closed.");
        IOUtils.closeQuietly(this.writer);
    }

    private final class SVNIndexWriterJob implements Runnable {

        private static final long SIZE_100MB = 104857600;

        private Exception         executionException;

        /** {@inheritDoc} */
        @Override
        public void run() {
            LOG.debug("TASK: Executing actions from queue has been started");

            try {
                open();

                do {

                    if (SVNIndexWriter.this.location.toFile().getUsableSpace() <= SIZE_100MB) {
                        throw new SVNIndexException("There is less than 100MB space left for index creation, execution will be stopped!");
                    }

                    if (LOG.isDebugEnabled()) {
                        logMemoryInformations();
                    }

                    LOG.debug("TASK: Execute actions currently in queue, queue size is {}", SVNIndexWriter.this.queue.size());
                    executeActions();
                    LOG.debug("TASK: Sleeping for 2.5 seconds");
                    ThreadUtils.sleep(2500L);

                } while (!SVNIndexWriter.this.completed);

                LOG.debug("TASK: Writer has completed filling the queue, executing remaining actions");
                executeActions();

            } catch (Exception ex) {
                LOG.error("TASK: An error occured while executing actions from queue", ex);
                this.executionException = ex;
            } finally {
                close();
            }

            LOG.debug("TASK: Executing actions from queue has been finished");
        }

        private void executeActions() {
            while (!SVNIndexWriter.this.queue.isEmpty()) {
                final AbstractAction action = SVNIndexWriter.this.queue.poll();
                if (action != null) {
                    try {
                        action.doAction(SVNIndexWriter.this.writer);
                    } catch (Exception ex) {
                        LOG.debug("Error while trying to perform action: {}", action, ex);
                        throw ex;
                    }
                }
            }
        }

        private void logMemoryInformations() {
            final long allocatedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
            final long maxMemory = Runtime.getRuntime().maxMemory();
            final long presumableFreeMemory = maxMemory - allocatedMemory;
            LOG.debug("Max: {} Allocated: {} Free: {}", maxMemory, allocatedMemory, presumableFreeMemory);
        }

        private boolean isAbortedByException() {
            return this.executionException != null;
        }

        private Exception getExecutionException() {
            return this.executionException;
        }

    }

}
