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
package de.swoeste.infinitum.fw.core.bl.svn.indexer.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.swoeste.infinitum.fw.core.bl.svn.indexer.filter.ISVNFilter;

/**
 * @author swoeste
 */
public class SVNIndexConfiguration {

    private final SVNInformation  svnInformation;
    private final Set<ISVNFilter> filter;

    private String                rootPath;
    private long                  startRevision;
    private int                   batchSize;

    public SVNIndexConfiguration(final SVNInformation svn, final String indexPath) {
        this(svn, indexPath, 0, 5000);
    }

    public SVNIndexConfiguration(final SVNInformation svnInformation, final String indexPath, final long startRevision, final int batchSize) {
        this.svnInformation = svnInformation;
        this.filter = new HashSet<ISVNFilter>();
        this.startRevision = startRevision;
        this.rootPath = indexPath;
        this.batchSize = batchSize;
    }

    public void addFilter(final ISVNFilter filter) {
        this.filter.add(filter);
    }

    public Set<ISVNFilter> getFilter() {
        return Collections.unmodifiableSet(this.filter);
    }

    /**
     * @return the rootPath
     */
    public String getRootPath() {
        return this.rootPath;
    }

    /**
     * @param rootPath
     *            the rootPath to set
     */
    public void setRootPath(final String rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * @return the startRevision
     */
    public long getStartRevision() {
        return this.startRevision;
    }

    /**
     * @param startRevision
     *            the startRevision to set
     */
    public void setStartRevision(final long startRevision) {
        this.startRevision = startRevision;
    }

    /**
     * @return the svnInformation
     */
    public SVNInformation getSvnInformation() {
        return this.svnInformation;
    }

    /**
     * @return the batchSize
     */
    public int getBatchSize() {
        return this.batchSize;
    }

    /**
     * @param batchSize
     *            the batchSize to set
     */
    public void setBatchSize(final int batchSize) {
        this.batchSize = batchSize;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "SVNIndexConfiguration [svnInformation=" + this.svnInformation + ", filter=" + this.filter + ", rootPath=" + this.rootPath + ", startRevision=" + this.startRevision
                + ", batchSize=" + this.batchSize + "]";
    }

}
