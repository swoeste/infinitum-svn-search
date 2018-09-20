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
