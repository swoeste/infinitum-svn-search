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
package de.swoeste.infinitum.fw.core.bl.svn.indexer;

import org.apache.lucene.util.Version;

/**
 * @author swoeste
 */
@SuppressWarnings("nls")
public final class SVNConstants {

    public static final Version LUCENE_VERSION                 = Version.LUCENE_4_9;

    public static final String  PROPERTIES_PATH                = "/index.properties";

    public static final String  PROPERTY_LAST_REVISION         = "last.revision";

    public static final String  PROPERTY_LAST_REVISION_DEFAULT = "0";

    public static final String  INDEX_PATH                     = "/index";

    private SVNConstants() {
        // hidden
    }

}
