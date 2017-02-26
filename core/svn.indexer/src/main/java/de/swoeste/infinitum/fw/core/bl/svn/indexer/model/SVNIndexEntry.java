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

import java.util.HashMap;
import java.util.Map;

import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexFields;

/**
 * @author swoeste
 */
public class SVNIndexEntry implements ISVNIndexEntry {

    private final Map<SVNIndexFields, String> indexedAttributes;

    public SVNIndexEntry() {
        this.indexedAttributes = new HashMap<>();
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
