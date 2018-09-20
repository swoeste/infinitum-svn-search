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

/**
 * @author swoeste
 */
public class SVNIndexSearch {

    private final String rootPath;
    private final String queryString;
    private final int    maxResults;

    /**
     * Create a new search criteria.
     *
     * @param rootPath
     *            the path to index
     * @param query
     *            the lucene search query
     * @param maxResults
     *            the maximum amount of returned results
     */
    public SVNIndexSearch(final String rootPath, final String queryString, final int maxResults) {
        this.rootPath = rootPath;
        this.queryString = queryString;
        this.maxResults = maxResults;
    }

    /**
     * @return the rootPath
     */
    public String getRootPath() {
        return this.rootPath;
    }

    /**
     * @return the queryString
     */
    public String getQueryString() {
        return this.queryString;
    }

    /**
     * @return the maxResults
     */
    public int getMaxResults() {
        return this.maxResults;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return MessageFormat.format("SVNIndexSearch [rootPath={0}, queryString={1}, maxResults={2}]", //$NON-NLS-1$
                this.rootPath, this.queryString, this.maxResults);
    }

}
