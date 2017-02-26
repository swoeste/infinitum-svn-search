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
        return MessageFormat.format("SVNIndexSearch [rootPath={0}, queryString={1}, maxResults={2}]", this.rootPath, this.queryString, this.maxResults); //$NON-NLS-1$
    }

}
