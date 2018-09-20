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
package de.swoeste.infinitum.fw.core.bl.svn.indexer.config;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.swoeste.infinitum.fw.core.bl.svn.indexer.filter.ISVNFilter;

/**
 * @author swoeste
 */
public class SVNIndexConfiguration {

    // TODO rename to SVNIndexContext ? - Because it better reflects its usage.

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
        this.filter = new HashSet<>();
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
        return MessageFormat.format("SVNIndexConfiguration [svnInformation={0}, filter={1}, rootPath={2}, startRevision={3}, batchSize={4}]",  //$NON-NLS-1$
                this.svnInformation, this.filter, this.rootPath, this.startRevision, this.batchSize);
    }

}
