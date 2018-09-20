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
