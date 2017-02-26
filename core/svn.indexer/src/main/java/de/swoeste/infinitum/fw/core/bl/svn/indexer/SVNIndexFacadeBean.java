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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNException;

import de.swoeste.infinitum.common.utils.properties.SortedProperties;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexConfiguration;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexSearch;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.exception.SVNIndexException;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexSearchResult;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.util.SVNPropertiesUtil;

public class SVNIndexFacadeBean implements ISVNIndexFacade {

    private static final Logger    LOG     = LoggerFactory.getLogger(SVNRepositoryReader.class);

    private static volatile Object lock    = new Object();

    private static boolean         running = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createOrUpdateIndex(final SVNIndexConfiguration configuration) {

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
        final SVNIndexSearcher reader = new SVNIndexSearcher(getIndexFile(search.getRootPath()));
        return reader.search(search);
    }

    /**
     * @param rootPath
     * @return
     */
    public static File getIndexFile(final String rootPath) {
        return new File(rootPath + SVNConstants.INDEX_PATH);
    }

    public synchronized boolean isRunning() {
        return SVNIndexFacadeBean.running;
    }

    private void secureCreateOrUpdateIndex(final SVNIndexConfiguration configuration) throws SVNException, IOException {

        final String rootPath = configuration.getRootPath();

        // read additional properties
        final SortedProperties properties = SVNPropertiesUtil.openProperties(rootPath);
        final SVNIndexConfiguration updatedConfiguration = updateConfiguration(configuration, properties);

        final SVNRepositoryReader reader = new SVNRepositoryReader(updatedConfiguration);

        SVNIndexWriter writer = null;

        try {

            LOG.info("Connecting to {}", configuration.getSvnInformation().getRepositoryUrl()); //$NON-NLS-1$
            reader.openConnection();

            final long latestRevision = reader.getLatestRevision();
            LOG.info("Latest revision of svn repository is {}", latestRevision); //$NON-NLS-1$

            if (configuration.getStartRevision() < latestRevision) {

                final File indexDestionation = getIndexFile(rootPath);
                LOG.info("Destination for index is {}", indexDestionation); //$NON-NLS-1$

                writer = new SVNIndexWriter(indexDestionation);
                writer.startup();

                long startRevision = configuration.getStartRevision();
                long batchRevision = startRevision + configuration.getBatchSize();

                long readRevision = startRevision; // exit marker

                while (readRevision < latestRevision) {

                    if (batchRevision > latestRevision) {
                        // SVNKit does not allow non existing revisions, so we
                        // reduce it to the head revision
                        batchRevision = latestRevision;
                    }

                    LOG.info("Reading revision {} to {}, current head revision is {}.", startRevision, batchRevision, latestRevision); //$NON-NLS-1$
                    reader.readRepository(writer, startRevision, batchRevision);

                    readRevision = batchRevision;
                    startRevision = batchRevision + 1;
                    batchRevision = batchRevision + configuration.getBatchSize();

                    // FIXME We need a mechanism to gracefully stop everything
                    // if an exception occurs. Example: A new class
                    // "CancellationHandler" which will be injected into the
                    // reader and the writer, if an exception happen in any of
                    // both the handler will be set to stop to that the other
                    // one could gracefully stop execution. Afterwards we store
                    // the last successfully executed revision to the properties
                    // file. If the exception cause is resolved the next
                    // execution will start in the right place.

                }

                writer.completed();

                // store the properties
                // TODO maybe we could improve this ...
                properties.put(SVNConstants.PROPERTY_LAST_REVISION, String.valueOf(latestRevision));
                SVNPropertiesUtil.storeProperties(rootPath, properties);
            }

        } finally {
            reader.closeConnection();

            if (writer != null) {
                writer.shutdown();
            }
        }

        LOG.info("Local index successfully created/updated");
    }

    private synchronized void setRunning(final boolean running) {
        SVNIndexFacadeBean.running = running;
    }

    /**
     * @param configuration
     * @param properties
     */

    // TODO maybe we could improve this ...
    private SVNIndexConfiguration updateConfiguration(final SVNIndexConfiguration configuration, final SortedProperties properties) {
        final long lastRevision = properties.getLongValue(SVNConstants.PROPERTY_LAST_REVISION);
        if (configuration.getStartRevision() < lastRevision) {
            LOG.info("Incremental update of local index will continue at revision {}", lastRevision);
            configuration.setStartRevision(lastRevision);
        }
        return configuration;
    }

}
