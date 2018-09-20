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
package de.swoeste.infinitum.application.common.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author swoeste
 */
public class ApplicationProperties {

    private static final Logger     LOG             = LoggerFactory.getLogger(ApplicationProperties.class);

    private static final Properties PROPERTIES      = loadProperties();

    private static final String     PROPERTIES_FILE = "infinitum.properties";                              //$NON-NLS-1$

    private ApplicationProperties() {
        // hidden default constructor
    }

    public static final String getSVNIndexPath() {
        return PROPERTIES.getProperty("svn.index.path"); //$NON-NLS-1$
    }

    public static final String getSVNIndexRepository() {
        return PROPERTIES.getProperty("svn.index.repository"); //$NON-NLS-1$
    }

    public static final int getSVNIndexMaxSearchResults() {
        final String property = PROPERTIES.getProperty("svn.index.maxSearchResults"); //$NON-NLS-1$
        return Integer.valueOf(property);
    }

    public static final int getSVNIndexSearchResultsPerPage() {
        final String property = PROPERTIES.getProperty("svn.index.searchResultsPerPage"); //$NON-NLS-1$
        return Integer.valueOf(property);
    }

    private static final Properties loadProperties() {
        final Properties properties = new Properties();

        try (final InputStream inputStream = getResourceAsStream(PROPERTIES_FILE)) {
            properties.load(inputStream);
        } catch (final IOException ex) {
            LOG.error("Error while trying to load properties.", ex); //$NON-NLS-1$
        }

        return properties;
    }

    private static InputStream getResourceAsStream(final String name) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
    }

}
