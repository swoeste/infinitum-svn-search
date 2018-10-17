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

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNException;

import de.swoeste.infinitum.common.utils.properties.SortedProperties;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexContext;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexSearch;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.exception.SVNIndexException;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexSearchResult;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.util.SVNPropertiesUtil;

public class SVNIndexFacadeBean implements ISVNIndexFacade {

    private static final Logger    LOG              = LoggerFactory.getLogger(SVNRepositoryReader.class);

    private static final int       MAX_RETRIES      = 3;

    private static final int       SLEEP_TIME_IN_MS = 1000;

    private static volatile Object lock             = new Object();

    private static boolean         running          = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createOrUpdateIndex(final SVNIndexContext configuration) {

        synchronized (lock) {
            if (isRunning()) {
                LOG.info("Creation or update of index is currenlty running, multiple calls are not possible and will be ignored."); //$NON-NLS-1$
                return;
            } else {
                setRunning(true);
            }
        }

        try {
            secureCreateOrUpdateIndex(configuration);
        } catch (SVNException | IOException e) {
            throw new SVNIndexException("Error while trying to create or update an index.", e); //$NON-NLS-1$
        } finally {
            setRunning(false);
        }

    }

    /** {@inheritDoc} */
    @Override
    public ISVNIndexSearchResult searchIndex(final SVNIndexSearch search) {
        final SVNIndexSearcher reader = new SVNIndexSearcher(getIndexLocation(search.getRootPath()));
        return reader.search(search);
    }

    /**
     * @param rootPath
     * @return
     */
    public static File getIndexLocation(final String rootPath) {
        return new File(rootPath + SVNConstants.INDEX_PATH);
    }

    public synchronized boolean isRunning() {
        return SVNIndexFacadeBean.running;
    }

    private void secureCreateOrUpdateIndex(final SVNIndexContext configuration) throws SVNException, IOException {
        final String rootPath = configuration.getRootPath();

        // check if an index was already created and should be continued at a specific revision
        final long currentlyIndexedRevision = readCurrentlyIndexedRevision(rootPath);
        if (configuration.getStartRevision() < currentlyIndexedRevision) {
            LOG.info("Incremental update of local index will continue at revision {}", currentlyIndexedRevision); //$NON-NLS-1$
            configuration.setStartRevision(currentlyIndexedRevision);
        }

        final SVNRepositoryReader reader = new SVNRepositoryReader(configuration);
        SVNIndexWriter writer = null;

        try {
            LOG.info("Connecting to {}", configuration.getSvnInformation().getRepositoryUrl()); //$NON-NLS-1$
            reader.openConnection();

            final long latestRevision = reader.getLatestRevision();
            LOG.info("Latest revision of svn repository is {}", latestRevision); //$NON-NLS-1$

            if (configuration.getStartRevision() < latestRevision) {
                final File indexDestionation = getIndexLocation(rootPath);
                LOG.debug("Destination for index is {}", indexDestionation); //$NON-NLS-1$

                writer = new SVNIndexWriter(indexDestionation);
                writer.startup();

                long startRevision = configuration.getStartRevision();
                long batchRevision = startRevision + configuration.getBatchSize();
                long completedRevision = startRevision; // exit marker

                byte retryCounter = 0;
                boolean abortIndexCreation = false;

                while ((completedRevision < latestRevision) && !abortIndexCreation) {
                    writeCurrentlyIndexedRevision(rootPath, startRevision);

                    if (batchRevision > latestRevision) {
                        // SVNKit does not allow non existing revisions, so we reduce it to the head revision
                        batchRevision = latestRevision;
                    }

                    try {
                        LOG.info("Reading revision {} to {}, current head revision is {}.", //$NON-NLS-1$
                                startRevision, batchRevision, latestRevision);
                        reader.readRepository(writer, startRevision, batchRevision);

                        completedRevision = batchRevision;
                        startRevision = batchRevision + 1;
                        batchRevision = batchRevision + configuration.getBatchSize();
                        retryCounter = 0;
                    } catch (Exception ex) {
                        LOG.error("An error occured while reading revision {} to {}.", startRevision, batchRevision, ex); //$NON-NLS-1$
                        if (retryCounter <= MAX_RETRIES) {
                            LOG.debug("Waiting for {} to perform a retry.", SLEEP_TIME_IN_MS); //$NON-NLS-1$
                            sleep(SLEEP_TIME_IN_MS);
                            retryCounter++;
                        } else {
                            LOG.error("Aborting index creation/update after {} failed retries.", MAX_RETRIES); //$NON-NLS-1$
                            abortIndexCreation = true;
                        }
                    }
                }

                // tell the writer, that all data for the current chunk have been read
                writer.completed();

            } else {
                LOG.debug("The index is currently up to date, nothing to do."); //$NON-NLS-1$
            }

        } finally {
            reader.closeConnection();

            if (writer != null) {
                writer.shutdown();
            }
        }

        LOG.info("Local index successfully created/updated."); //$NON-NLS-1$
    }

    private synchronized void setRunning(final boolean running) {
        SVNIndexFacadeBean.running = running;
    }

    private long readCurrentlyIndexedRevision(final String rootPath) {
        final SortedProperties properties = SVNPropertiesUtil.openProperties(rootPath);
        return properties.getLongValue(SVNConstants.PROPERTY_LAST_REVISION);
    }

    private void writeCurrentlyIndexedRevision(final String rootPath, final long revision) throws IOException {
        final SortedProperties properties = SVNPropertiesUtil.openProperties(rootPath);
        properties.put(SVNConstants.PROPERTY_LAST_REVISION, String.valueOf(revision));
        SVNPropertiesUtil.storeProperties(rootPath, properties);
    }

    private void sleep(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex1) {
            Thread.currentThread().interrupt();
        }
    }

}
