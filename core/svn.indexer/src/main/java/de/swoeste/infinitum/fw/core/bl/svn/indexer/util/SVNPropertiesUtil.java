/*
 * Copyright (C) 2012 Sebastian Woeste
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
package de.swoeste.infinitum.fw.core.bl.svn.indexer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.swoeste.infinitum.common.utils.constants.StringConstants;
import de.swoeste.infinitum.common.utils.properties.SortedProperties;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.SVNConstants;

/**
 * @author swoeste
 */
public final class SVNPropertiesUtil {

    private static final Logger LOG = LoggerFactory.getLogger(SVNPropertiesUtil.class);

    private SVNPropertiesUtil() {
        // hidden
    }

    private static SortedProperties getDefaultProperties() {
        final SortedProperties properties = new SortedProperties();
        properties.put(SVNConstants.PROPERTY_LAST_REVISION, SVNConstants.PROPERTY_LAST_REVISION_DEFAULT);
        return properties;
    }

    private static File getPropertiesFile(final String rootPath) {
        return new File(rootPath + SVNConstants.PROPERTIES_PATH);
    }

    private static boolean hasProperties(final String rootPath) {
        return getPropertiesFile(rootPath).exists();
    }

    public static SortedProperties openProperties(final String rootPath) {
        if (SVNPropertiesUtil.hasProperties(rootPath)) {
            try {
                return readProperties(rootPath);
            } catch (final IOException ex) {
                LOG.warn("Error while trying to open properties! Default values will be used instead.", ex); //$NON-NLS-1$
                return getDefaultProperties();
            }
        } else {
            return getDefaultProperties();
        }
    }

    private static SortedProperties readProperties(final String rootPath) throws IOException {
        final File propertiesFile = getPropertiesFile(rootPath);
        final SortedProperties defaultProperties = getDefaultProperties();
        final SortedProperties properties = new SortedProperties(defaultProperties);

        try (final FileInputStream inputStream = new FileInputStream(propertiesFile);
                final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StringConstants.ENCODING_UTF_8);) {

            properties.load(inputStreamReader);
            return properties;
        }

    }

    public static void storeProperties(final String rootPath, final SortedProperties properties) throws IOException {
        final File propertiesFile = getPropertiesFile(rootPath);

        try (final FileOutputStream outputStream = new FileOutputStream(propertiesFile);
                final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StringConstants.ENCODING_UTF_8)) {

            properties.store(outputStreamWriter, "Created by SVNIndexer"); //$NON-NLS-1$
        }
    }

}
