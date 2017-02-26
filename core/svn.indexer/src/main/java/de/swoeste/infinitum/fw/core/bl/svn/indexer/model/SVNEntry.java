/*
 *Copyright (C) 2017 Sebastian Woeste
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.swoeste.infinitum.fw.core.bl.svn.indexer.model;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;

import de.swoeste.infinitum.common.utils.constants.StringConstants;

/**
 * @author swoeste
 */
public class SVNEntry implements ISVNElement {

    private static final Logger   LOG = LoggerFactory.getLogger(SVNEntry.class);

    private final SVNURL          svnUrl;
    private final SVNLogEntry     svnLogEntry;
    private final SVNLogEntryPath svnLogEntryPath;
    private final SVNNodeKind     svnNodeKind;

    private String                content;

    private SVNLogEntry           svnLogEntryOfCreation;

    public SVNEntry(final SVNURL url, final SVNLogEntry logEntry, final SVNLogEntryPath logEntryPath, final SVNNodeKind nodeKind) {
        this.svnUrl = url;
        this.svnLogEntry = logEntry;
        this.svnLogEntryPath = logEntryPath;
        this.svnNodeKind = nodeKind;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(final String content) {
        this.content = content;
    }

    /** {@inheritDoc} */
    @Override
    public String getContent() {
        return this.content;
    }

    /**
     * @param svnLogEntryOfCreation
     *            the svnLogEntryOfCreation to set
     */
    public void setSvnLogEntryOfCreation(final SVNLogEntry svnLogEntryOfCreation) {
        this.svnLogEntryOfCreation = svnLogEntryOfCreation;
    }

    /**
     * @return
     */
    @Override
    public String getPath() {
        return this.svnLogEntryPath.getPath();
    }

    /**
     * @return
     */
    @Override
    public String getAbsolutePath() {
        try {
            return this.svnUrl.appendPath(this.svnLogEntryPath.getPath(), true).toDecodedString();
        } catch (final SVNException ex) {
            LOG.warn("Error while trying to resolve absolute path of svn url.", ex); //$NON-NLS-1$
            return this.svnLogEntryPath.getPath();
        }
    }

    /**
     * @return
     */
    @Override
    public String getFileType() {
        if (isFile()) {
            final String nodeName = getNodeName();
            final int delimiter = nodeName.lastIndexOf(StringConstants.DOT);
            if (delimiter >= 0) {
                return nodeName.substring(delimiter + 1);
            } else {
                return StringConstants.EMPTY;
            }
        } else {
            return StringConstants.EMPTY;
        }
    }

    /**
     * @return
     */
    @Override
    public String getNodeName() {
        if (isFile() || isFolder()) {
            final int delimiter = this.svnLogEntryPath.getPath().lastIndexOf(StringConstants.SLASH);
            if (delimiter >= 0) {
                return this.svnLogEntryPath.getPath().substring(delimiter + 1);
            } else {
                return this.svnLogEntryPath.getPath();
            }
        } else {
            return StringConstants.EMPTY;
        }
    }

    @Override
    public boolean isFile() {
        return this.svnNodeKind == SVNNodeKind.FILE;
    }

    @Override
    public boolean isFolder() {
        return this.svnNodeKind == SVNNodeKind.DIR;
    }

    /** {@inheritDoc} */
    @Override
    public long getRevisionOfLastUpdate() {
        return this.svnLogEntry.getRevision();
    }

    /** {@inheritDoc} */
    @Override
    public String getAuthorOfLastUpdate() {
        return this.svnLogEntry.getAuthor();
    }

    /** {@inheritDoc} */
    @Override
    public Date getDateOfLastUpdate() {
        return this.svnLogEntry.getDate();
    }

    /** {@inheritDoc} */
    @Override
    public long getRevisionOfCreation() {
        if (this.svnLogEntryOfCreation != null) {
            return this.svnLogEntryOfCreation.getRevision();
        } else {
            return this.svnLogEntry.getRevision();
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getAuthorOfCreation() {
        if (this.svnLogEntryOfCreation != null) {
            return this.svnLogEntryOfCreation.getAuthor();
        } else {
            return this.svnLogEntry.getAuthor();
        }
    }

    /** {@inheritDoc} */
    @Override
    public Date getDateOfCreation() {
        if (this.svnLogEntryOfCreation != null) {
            return this.svnLogEntryOfCreation.getDate();
        } else {
            return this.svnLogEntry.getDate();
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "SVNIndexItemData [svnUrl=" + this.svnUrl + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }

}
