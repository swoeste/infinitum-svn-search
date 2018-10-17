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
package de.swoeste.infinitum.fw.core.bl.svn.indexer.model;

import java.util.EnumMap;
import java.util.Map;

import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexFields;

/**
 * @author swoeste
 */
public class SVNIndexEntry implements ISVNIndexEntry {

    private final Map<SVNIndexFields, String> indexedAttributes;

    public SVNIndexEntry() {
        this.indexedAttributes = new EnumMap<>(SVNIndexFields.class);
    }

    /**
     * Add an attribute to this item.
     *
     * @param key
     *            the attribute
     * @param value
     *            the value of the attribute
     */
    public void addAttribute(final SVNIndexFields field, final String value) {
        this.indexedAttributes.put(field, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAttribute(final SVNIndexFields field) {
        return this.indexedAttributes.get(field);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthorOfCreation() {
        return getAttribute(SVNIndexFields.CREATED_AUTHOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthorOfLastUpdate() {
        return getAttribute(SVNIndexFields.UPDATE_AUTHOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDateOfCreation() {
        return getAttribute(SVNIndexFields.CREATED_DATE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDateOfLastUpdate() {
        return getAttribute(SVNIndexFields.UPDATE_DATE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileContent() {
        return getAttribute(SVNIndexFields.FILE_CONTENT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileName() {
        return getAttribute(SVNIndexFields.FILE_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileType() {
        return getAttribute(SVNIndexFields.FILE_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return getAttribute(SVNIndexFields.ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRevisionOfCreation() {
        return getAttribute(SVNIndexFields.CREATED_REVISION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRevisionOfLastUpdate() {
        return getAttribute(SVNIndexFields.UPDATE_REVISION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUpdated() {
        return Boolean.valueOf(getAttribute(SVNIndexFields.UPDATED));
    }
}
