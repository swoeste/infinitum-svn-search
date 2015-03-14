/*
 * Copyright (C) 2013 Sebastian Woeste
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
package de.swoeste.infinitum.application.svn.indexer.model;

import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexFields;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexSearchResultEntry;

/**
 * @author swoeste
 */
public class SearchResultTO {

    private final ISVNIndexSearchResultEntry searchResult;

    private boolean                          showExcerpt;

    public SearchResultTO(final ISVNIndexSearchResultEntry searchResult) {
        this.searchResult = searchResult;
        this.showExcerpt = true;
    }

    public void switchShowExcerpt() {
        this.showExcerpt = !this.showExcerpt;
    }

    /**
     * @return
     * @see de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexSearchResultEntry#getScore()
     */
    public float getScore() {
        return this.searchResult.getScore();
    }

    /**
     * @return
     * @see de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexSearchResultEntry#getExcerpt()
     */
    public String getExcerpt() {
        return this.searchResult.getExcerpt();
    }

    /**
     * @param field
     * @return
     * @see de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexEntry#getAttribute(de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexFields)
     */
    public String getAttribute(final SVNIndexFields field) {
        return this.searchResult.getIndexEntry().getAttribute(field);
    }

    /**
     * @return
     * @see de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexEntry#getAuthorOfCreation()
     */
    public String getAuthorOfCreation() {
        return this.searchResult.getIndexEntry().getAuthorOfCreation();
    }

    /**
     * @return
     * @see de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexEntry#getAuthorOfLastUpdate()
     */
    public String getAuthorOfLastUpdate() {
        return this.searchResult.getIndexEntry().getAuthorOfLastUpdate();
    }

    /**
     * @return
     * @see de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexEntry#getDateOfCreation()
     */
    public String getDateOfCreation() {
        return this.searchResult.getIndexEntry().getDateOfCreation();
    }

    /**
     * @return
     * @see de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexEntry#getDateOfLastUpdate()
     */
    public String getDateOfLastUpdate() {
        return this.searchResult.getIndexEntry().getDateOfLastUpdate();
    }

    /**
     * @return
     * @see de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexEntry#getFileContent()
     */
    public String getFileContent() {
        return this.searchResult.getIndexEntry().getFileContent();
    }

    /**
     * @return
     * @see de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexEntry#getFileName()
     */
    public String getFileName() {
        return this.searchResult.getIndexEntry().getFileName();
    }

    /**
     * @return
     * @see de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexEntry#getFileType()
     */
    public String getFileType() {
        return this.searchResult.getIndexEntry().getFileType();
    }

    /**
     * @return
     * @see de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexEntry#getId()
     */
    public String getId() {
        return this.searchResult.getIndexEntry().getId();
    }

    /**
     * @return
     * @see de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexEntry#getRevisionOfCreation()
     */
    public String getRevisionOfCreation() {
        return this.searchResult.getIndexEntry().getRevisionOfCreation();
    }

    /**
     * @return
     * @see de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexEntry#getRevisionOfLastUpdate()
     */
    public String getRevisionOfLastUpdate() {
        return this.searchResult.getIndexEntry().getRevisionOfLastUpdate();
    }

    /**
     * @return
     * @see de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexEntry#isUpdated()
     */
    public boolean isUpdated() {
        return this.searchResult.getIndexEntry().isUpdated();
    }

    /**
     * @return the showExcerpt
     */
    public boolean isShowExcerpt() {
        return this.showExcerpt;
    }

    /**
     * @param showExcerpt
     *            the showExcerpt to set
     */
    public void setShowExcerpt(final boolean showExcerpt) {
        this.showExcerpt = showExcerpt;
    }

    /**
     * @return the searchResult
     */
    public ISVNIndexSearchResultEntry getSearchResult() {
        return this.searchResult;
    }

}
