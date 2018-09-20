/*-
 * Copyright (C) 2017 Sebastian Woeste
 *
 * Licensed to Sebastian Woeste under one or more contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership. I license this file to You under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package de.swoeste.infinitum.fw.core.bl.svn.indexer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import de.swoeste.infinitum.common.utils.constants.StringConstants;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.action.ActionCreate;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.action.ActionDelete;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.action.ActionUpdate;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.action.IActionQueue;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexConfiguration;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNInformation;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.filter.ISVNFilter;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.internal.ISVNContentReader;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.manager.FilterManager;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.SVNEntry;

public class SVNRepositoryReader implements ISVNContentReader {

    private static final Logger         LOG                = LoggerFactory.getLogger(SVNRepositoryReader.class);

    private static final int            IGNORE_WRITE_LIMIT = -1;
    private static final int            MAX_FILE_SIZE      = 20;

    private static final String         ENCODING           = "UTF-8";                                                                                      //$NON-NLS-1$

    private final SVNIndexConfiguration configuration;
    private final FilterManager         filter;

    private SVNRepository               repository;
    private SVNURL                      root;

    /**
     * Constructor for a new SVNReader.
     *
     * @param configuration
     */
    public SVNRepositoryReader(final SVNIndexConfiguration configuration) {
        this.configuration = configuration;
        this.filter = new FilterManager();
        initFilter();
    }

    /** {@inheritDoc} */
    @Override
    public void closeConnection() {
        LOG.debug("Close connection to repository");
        if (this.repository != null) {
            this.repository.closeSession();
            this.repository = null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void openConnection() throws SVNException {
        LOG.debug("Open connectin to svn");

        final SVNInformation svnInformation = this.configuration.getSvnInformation();
        final SVNURL parseURIEncoded = SVNURL.parseURIEncoded(svnInformation.getRepositoryUrl());
        this.repository = SVNRepositoryFactory.create(parseURIEncoded);

        if (svnInformation.isAuthentication()) {
            this.repository.setAuthenticationManager(new BasicAuthenticationManager(svnInformation.getUsername(), svnInformation.getPassword()));
        }

        this.root = this.repository.getRepositoryRoot(true);
    }

    /**
     * @return
     */
    public long getLatestRevision() {
        if (this.repository != null) {
            try {
                return this.repository.getLatestRevision();
            } catch (final SVNException e) {
                LOG.warn("Error while trying to get the latest revision from svn repository.", e); //$NON-NLS-1$
            }
        }
        return 0L;
    }

    /**
     * Sometimes the type of a node could not be get directly from the SVNLogEntryPath object. Then we need to connect
     * with the svn repository and ask for the correct type of the node. This method check if the type could be get
     * directly from the SVNLogEntryPath object and if not connect to the svn repository and ask for the type. If both
     * went wrong UNKNOWN is returned as type.
     *
     * @param entry
     *            a SVNLogEntry
     * @param path
     *            a SVNLogEntryPath
     * @return the type of the node
     */
    private SVNNodeKind getNodeKind(final SVNLogEntry entry, final SVNLogEntryPath path) {
        if (path.getKind() == SVNNodeKind.UNKNOWN) {
            try {
                return this.repository.checkPath(path.getPath(), entry.getRevision());
            } catch (final SVNException e) {
                final String msg = MessageFormat.format("Error while trying to resolve the type of the svn node {0}", path.getPath()); //$NON-NLS-1$
                LOG.warn(msg, e);
                return SVNNodeKind.UNKNOWN;
            }
        } else {
            return path.getKind();
        }
    }

    private void initFilter() {
        for (final ISVNFilter filter : this.configuration.getFilter()) {
            this.filter.addFilter(filter);
        }
    }

    /**
     * Fetches the svn log from the configured svn repository.
     *
     * @param startRevision
     * @param endRevision
     * @return the svn log
     * @throws SVNException
     */
    @SuppressWarnings("unchecked")
    private Collection<SVNLogEntry> loadLogEntries(final long startRevision, final long endRevision) throws SVNException {
        if (this.repository != null) {
            return this.repository.log(new String[] { StringUtils.EMPTY }, null, startRevision, endRevision, true, true);
        } else {
            return Collections.emptyList();
        }
    }

    @SuppressWarnings("unchecked")
    /**
     * @param path
     * @param startRevision
     * @param endRevision
     * @return
     * @throws SVNException
     */
    private SVNLogEntry loadFirstLogEntry(final String path, final long startRevision, final long endRevision) throws SVNException {
        if (this.repository != null) {
            final Collection<SVNLogEntry> logEntries = this.repository.log(new String[] { path }, null, startRevision, endRevision, true, true);
            if ((logEntries != null) && !logEntries.isEmpty()) {
                // return first element
                return logEntries.iterator().next();
            }
        }
        return null;
    }

    /**
     * @param revision
     * @param path
     * @return
     * @throws SVNException
     */
    private String readContent(final long revision, final String path) throws SVNException {
        if (this.repository != null) {

            LOG.debug("Reading content of {} at revision {} from svn", path, revision);

            final long sizeMB = getSizeMB(revision, path);
            if (sizeMB > MAX_FILE_SIZE) {
                LOG.warn("Content of {} was not resolved because it exeeds the maximum file size limit of {} its size is {}!", path, MAX_FILE_SIZE, sizeMB);
                return MessageFormat.format("The content of this file was not indexed because it exeeds the maximum file size limit of {0}!", MAX_FILE_SIZE);
            }

            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            this.repository.getFile(path, revision, null, stream);

            final String parseContent = parseContent(path, stream.toByteArray());
            if (parseContent != null) {
                LOG.debug("Resolved content with apache tika parser");
                return parseContent;
            }

            final String parseContentPlain = parseContentPlain(path, stream.toByteArray());
            if (parseContentPlain != null) {
                LOG.debug("Resolved content with plain text interpretation");
                return parseContentPlain;
            }

            LOG.info("Unable to resolve content of {} at revision {} from svn", path, revision);
            return StringConstants.EMPTY;
        }

        return StringConstants.EMPTY;
    }

    /**
     * @param path
     * @param content
     * @return
     */
    private String parseContent(final String path, final byte[] content) {
        try (final ByteArrayInputStream in = new ByteArrayInputStream(content)) {
            final Parser parser = new AutoDetectParser();
            final ContentHandler handler = new BodyContentHandler(IGNORE_WRITE_LIMIT);
            final Metadata metadata = new Metadata();
            final ParseContext context = new ParseContext();
            parser.parse(in, handler, metadata, context);
            parser.getSupportedTypes(context);
            return handler.toString();
        } catch (IOException | SAXException | TikaException ex) {
            final String msg = MessageFormat.format("Error while trying to resolve the content of {0}", path); //$NON-NLS-1$
            LOG.error(msg, ex);
            return null;
        }

    }

    /**
     * @param path
     * @param content
     * @return
     */
    private String parseContentPlain(final String path, final byte[] content) {
        try {
            return IOUtils.toString(content, ENCODING);
        } catch (IOException ex) {
            final String msg = MessageFormat.format("Error while trying to resolve the content of {0}", path); //$NON-NLS-1$
            LOG.error(msg, ex);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readRepository(final IActionQueue queue, final long startRevision, final long endRevision) throws SVNException {

        final Collection<SVNLogEntry> logEntries = loadLogEntries(startRevision, endRevision);

        for (final SVNLogEntry svnLogEntry : logEntries) {
            final Collection<SVNLogEntryPath> values = svnLogEntry.getChangedPaths().values();
            LOG.debug("Start reading of revision {}.", svnLogEntry.getRevision());

            for (final SVNLogEntryPath svnLogEntryPath : values) {
                LOG.debug("Passing {} to FilterManager.", svnLogEntryPath);

                final SVNNodeKind nodeKind = getNodeKind(svnLogEntry, svnLogEntryPath);
                final SVNEntry data = new SVNEntry(this.root, svnLogEntry, svnLogEntryPath, nodeKind);

                if (this.filter.isFiltered(data)) {
                    switch (svnLogEntryPath.getType()) {
                        case SVNLogEntryPath.TYPE_ADDED:
                            addContentToData(data, nodeKind);
                            queue.addAction(new ActionCreate(data));
                            break;

                        case SVNLogEntryPath.TYPE_DELETED:
                            queue.addAction(new ActionDelete(data));
                            break;

                        case SVNLogEntryPath.TYPE_MODIFIED:
                        case SVNLogEntryPath.TYPE_REPLACED:
                            addSVNLogEntryOfCreationToData(data);
                            addContentToData(data, nodeKind);
                            queue.addAction(new ActionUpdate(data));
                            break;

                        default:
                            LOG.warn("No matching action for type {}.", svnLogEntryPath.getType());
                            break;
                    }
                } else {
                    LOG.debug("The element {} did not pass the filter.", svnLogEntryPath);
                }
            }

            LOG.debug("Finished reading of revision {}.", svnLogEntry.getRevision());
        }
    }

    /**
     * @param data
     * @throws SVNException
     */
    private void addSVNLogEntryOfCreationToData(final SVNEntry data) throws SVNException {
        final SVNLogEntry firstLogEntryForReplaced = loadFirstLogEntry(data.getPath(), 0L, data.getRevisionOfCreation());
        data.setSvnLogEntryOfCreation(firstLogEntryForReplaced);
    }

    /**
     * @param data
     * @param nodeKind
     * @throws SVNException
     */
    private void addContentToData(final SVNEntry data, final SVNNodeKind nodeKind) throws SVNException {
        if (SVNNodeKind.FILE.equals(nodeKind)) {
            try {
                final String content = readContent(data.getRevisionOfLastUpdate(), data.getPath());
                data.setContent(content);
            } catch (Exception ex) {
                final String msg = MessageFormat.format("Unable to add content to {0}", data.getPath()); //$NON-NLS-1$
                LOG.error(msg, ex);
            }
        }
    }

    /**
     * Fetch the size of the file specified by the given path in the given revision.
     *
     * @param revision
     *            the revision of the file
     * @param path
     *            the path of the file
     * @return the size of the file in mega byte (MB)
     * @throws SVNException
     */
    private long getSizeMB(final long revision, final String path) throws SVNException {
        final SVNDirEntry dirEntry = this.repository.info(path, revision);
        return dirEntry.getSize() / 1024 / 1024;
    }

}
