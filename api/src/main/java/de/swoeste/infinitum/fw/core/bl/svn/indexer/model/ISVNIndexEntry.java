/*
 *Copyright (C) 2017 Sebastian Woeste
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General License for more details.
 * 
 * You should have received a copy of the GNU General License along with this
 * program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.swoeste.infinitum.fw.core.bl.svn.indexer.model;

import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexFields;

/**
 * @author swoeste
 */
public interface ISVNIndexEntry {

    /**
     * Get an attribute from this item.
     * 
     * @param field
     *            the attribute name
     * @return the attribute value
     */
    String getAttribute(final SVNIndexFields field);

    /**
     * The author who created this item.
     * 
     * @return the author
     */
    String getAuthorOfCreation();

    /**
     * The author who made the last change on this item.
     * 
     * @return the author
     */
    String getAuthorOfLastUpdate();

    /**
     * The date this item was created.
     * 
     * @return the date
     */
    String getDateOfCreation();

    /**
     * The date this item was last updated.
     * 
     * @return the date
     */
    String getDateOfLastUpdate();

    /**
     * The file content of this item.
     * 
     * @return the file content
     */
    String getFileContent();

    /**
     * The file name of this item.
     * 
     * @return the file name
     */
    String getFileName();

    /**
     * The file type of this item.
     * 
     * @return the file type
     */
    String getFileType();

    /**
     * The identifier of this item.
     * 
     * @return the identifier
     */
    String getId();

    /**
     * The svn revision this item was created.
     * 
     * @return the revision
     */
    String getRevisionOfCreation();

    /**
     * The svn revision this item was last updated.
     * 
     * @return the revision
     */
    String getRevisionOfLastUpdate();

    /**
     * Check if this item was updated or if it is just committed once.
     * 
     * @return true if it was updated, false if not
     */
    boolean isUpdated();

}
