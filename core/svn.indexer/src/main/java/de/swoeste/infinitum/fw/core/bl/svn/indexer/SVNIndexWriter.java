/*
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

import java.io.File;
import java.io.IOException;
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

    private final File                  location;

    private IndexWriter                 writer;

    private boolean                     completed;

    /**
     * Constructor for a new Indexer.
     *
     * @param location
     * @param reader
     */
    public SVNIndexWriter(final File location) {
        this.location = location;
        this.completed = false;
        this.analyzer = new SVNIndexAnalyzer(SVNConstants.LUCENE_VERSION);
    }

    /** {@inheritDoc} */
    @Override
    public void completed() {
        this.completed = true;
    }

    @Override
    public void addAction(final AbstractAction action) {
        LOG.debug("Adding new action to queue, new size of queue is {}", this.queue.size());
        this.queue.add(action);
    }

    public void startup() {
        LOG.debug("Startup, creating task to write index.");
        this.executor.execute(new SVNIndexWriterJob());
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
            LOG.error("Error while trying to shutdown SVNIndexWriter", e);
        }
    }

    private void open() throws IOException {
        LOG.debug("Connection to lucene index has been opened.");
        final Directory directory = FSDirectory.open(this.location);

        final IndexWriterConfig config = new IndexWriterConfig(SVNConstants.LUCENE_VERSION, this.analyzer);
        config.setOpenMode(OpenMode.CREATE_OR_APPEND);
        this.writer = new IndexWriter(directory, config);
    }

    private void close() {
        LOG.debug("Connection to lucene index has been closed.");
        IOUtils.closeQuietly(SVNIndexWriter.this.writer);
    }

    private final class SVNIndexWriterJob implements Runnable {

        private final static long SIZE100MB = 104857600;

        /** {@inheritDoc} */
        @Override
        public void run() {
            LOG.debug("TASK: Executing actions from queue has been started");

            try {
                // FIXME: If open() fails the svn index reader will not stop!
                // This will lead to a OOM-Exception after some time.
                open();

                do {

                    if (SVNIndexWriter.this.location.getUsableSpace() <= SIZE100MB) {
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

                LOG.debug("TASK: Reader has completed filling the queue, executing remaining actions");
                executeActions();

            } catch (IOException e) {
                LOG.warn("TASK: An error occured while executing actions from queue", e);

            } catch (Exception e) {
                LOG.error("TASK: An error occured while executing actions from queue", e);
                // FIXME
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
                        action.doAction(SVNIndexWriter.this.writer, SVNIndexWriter.this.analyzer);
                    } catch (Exception ex) {
                        // FIXME
                        LOG.debug("Error while trying to perform action: " + action, ex);
                        throw ex;
                    }
                }
            }
        }

        private void logMemoryInformations() {
            // https://stackoverflow.com/questions/12807797/java-get-available-memory/18366283#18366283
            long allocatedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
            long maxMemory = Runtime.getRuntime().maxMemory();
            long presumableFreeMemory = maxMemory - allocatedMemory;
            LOG.debug("Max: {} Allocated: {} Free: {}", maxMemory, allocatedMemory, presumableFreeMemory);
        }

    }

}
