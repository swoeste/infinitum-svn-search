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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author swoeste
 */
public class ApplicationProperties {

    private static final Logger     LOG                                  = LoggerFactory.getLogger(ApplicationProperties.class);

    private static final Properties PROPERTIES                           = loadProperties();

    private static final String     PROPERTIES_FILE                      = "infinitum.properties";           //$NON-NLS-1$

    private static final String     PROP_REPOSITORY                      = "svn.index.repository";           //$NON-NLS-1$

    private static final String     PROP_PATH                            = "svn.index.path";                 //$NON-NLS-1$
    private static final String     PROP_PATH_DEFAULT                    = "";                     //$NON-NLS-1$

    private static final String     PROP_USERNAME                        = "svn.index.username";             //$NON-NLS-1$
    private static final String     PROP_USERNAME_DEFAULT                = "";                               //$NON-NLS-1$

    private static final String     PROP_PASSWORD                        = "svn.index.password";             //$NON-NLS-1$
    private static final String     PROP_PASSWORD_DEFAULT                = "";                               //$NON-NLS-1$

    private static final String     PROP_MAX_SEARCH_RESULTS              = "svn.index.maxSearchResults";     //$NON-NLS-1$
    private static final String     PROP_MAX_SEARCH_RESULTS_DEFAULT      = "100";                            //$NON-NLS-1$

    private static final String     PROP_SEARCH_RESULTS_PER_PAGE         = "svn.index.searchResultsPerPage"; //$NON-NLS-1$
    private static final String     PROP_SEARCH_RESULTS_PER_PAGE_DEFAULT = "10";                             //$NON-NLS-1$

    private static final String     PROP_BATCH_SIZE                      = "svn.index.batchSize";         //$NON-NLS-1$
    private static final String     PROP_BATCH_SIZE_DEFAULT              = "5000";                            //$NON-NLS-1$

    private static final String     PROP_EXCLUDE_PATHS                   = "svn.index.excludePaths";         //$NON-NLS-1$

    private static final String     PROP_EXCLUDE_FILES                   = "svn.index.excludeFiles";         //$NON-NLS-1$

    private ApplicationProperties() {
        // hidden default constructor
    }

    public static final String getSVNIndexRepository() {
        return PROPERTIES.getProperty(PROP_REPOSITORY);
    }

    public static final String getSVNIndexPath() {
        return PROPERTIES.getProperty(PROP_PATH, PROP_PATH_DEFAULT);
    }

    public static final String getSVNIndexUsername() {
        return PROPERTIES.getProperty(PROP_USERNAME, PROP_USERNAME_DEFAULT);
    }

    public static final String getSVNIndexPassword() {
        return PROPERTIES.getProperty(PROP_PASSWORD, PROP_PASSWORD_DEFAULT);
    }

    public static final int getSVNIndexMaxSearchResults() {
        return getPropertyAsInteger(PROP_MAX_SEARCH_RESULTS, PROP_MAX_SEARCH_RESULTS_DEFAULT);
    }

    public static final int getSVNIndexSearchResultsPerPage() {
        return getPropertyAsInteger(PROP_SEARCH_RESULTS_PER_PAGE, PROP_SEARCH_RESULTS_PER_PAGE_DEFAULT);
    }

    public static final int getSVNIndexBatchSize() {
        return getPropertyAsInteger(PROP_BATCH_SIZE, PROP_BATCH_SIZE_DEFAULT);
    }

    private static int getPropertyAsInteger(final String property, final String defaultValue) {
        return Integer.valueOf(PROPERTIES.getProperty(property, defaultValue));
    }

    private static List<String> getPropertyAsList(final String property) {
        final List<String> result = new ArrayList<>();

        for (int i = 0; true; i++) {
            final String propertyValue = PROPERTIES.getProperty(property + "." + i); //$NON-NLS-1$

            if (!StringUtils.isBlank(propertyValue)) {
                result.add(propertyValue);
            } else {
                return result;
            }
        }
    }

    public static final List<String> getSVNIndexExcludePaths() {
        return getPropertyAsList(PROP_EXCLUDE_PATHS);
    }

    public static final List<String> getSVNIndexExcludeFiles() {
        return getPropertyAsList(PROP_EXCLUDE_FILES);
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
