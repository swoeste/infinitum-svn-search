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
package de.swoeste.infinitum.fw.core.bl.svn.indexer.model;

import java.text.MessageFormat;

/**
 * @author swoeste
 */
public class SVNIndexSearchResultEntry implements ISVNIndexSearchResultEntry {

    private final SVNIndexEntry indexItem;

    private float               score;
    private String              excerpt;

    public SVNIndexSearchResultEntry(final SVNIndexEntry indexItem) {
        this.indexItem = indexItem;
    }

    /**
     * @return the score
     */
    @Override
    public float getScore() {
        return this.score;
    }

    /**
     * @param score
     *            the score to set
     */
    public void setScore(final float score) {
        this.score = score;
    }

    /**
     * @return the excerpt
     */
    @Override
    public String getExcerpt() {
        return this.excerpt;
    }

    /**
     * @param excerpt
     *            the excerpt to set
     */
    public void setExcerpt(final String excerpt) {
        this.excerpt = excerpt;
    }

    /**
     * @return the indexItem
     */
    @Override
    public SVNIndexEntry getIndexEntry() {
        return this.indexItem;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return MessageFormat.format("SVNIndexSearchResultEntry [indexItem={0}, score={1}, excerpt={2}]", this.indexItem, this.score, this.excerpt); //$NON-NLS-1$
    }

}
