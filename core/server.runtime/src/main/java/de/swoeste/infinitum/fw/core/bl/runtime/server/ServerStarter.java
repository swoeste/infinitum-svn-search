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
package de.swoeste.infinitum.fw.core.bl.runtime.server;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

/**
 * @author swoeste
 */
public class ServerStarter {

    private static final String DEFAULT_WEB_HOST    = "0.0.0.0"; //$NON-NLS-1$ // NOSONAR - This is used as default value
    private static final String DEFAULT_WEB_PORT    = "9000";   //$NON-NLS-1$
    private static final String DEFAULT_WEB_CONTEXT = "/";      //$NON-NLS-1$

    private ServerStarter() {
        // hidden default constructor
    }

    private static void canCreateTemporaryFiles() {
        File file = null;
        try {
            file = File.createTempFile("write-to-tmp-check", "tmp"); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (final IOException ex) {
            throw new IllegalStateException("Unable to create file in temporary directory, please check existence of it and permissions: " + FileUtils.getTempDirectoryPath(), ex); //$NON-NLS-1$
        } finally {
            FileUtils.deleteQuietly(file);
        }
    }

    private static void configureHome() throws URISyntaxException {
        final URL configurationResource = getConfigurationResource();
        if (configurationResource != null) {
            final File confFile = new File(configurationResource.toURI());
            System.setProperty("INFINITUM_HOME", confFile.getParentFile().getParentFile().getAbsolutePath()); //$NON-NLS-1$
        }
    }

    private static Properties getConfiguration() throws IOException {
        final Properties properties = new Properties();
        final URL configurationResource = getConfigurationResource();
        if (configurationResource != null) {
            properties.load(configurationResource.openStream());
        }
        return properties;
    }

    private static URL getConfigurationResource() {
        return ServerStarter.class.getResource("/conf/infinitum.properties"); //$NON-NLS-1$
    }

    public static void main(final String[] args) throws Exception {
        canCreateTemporaryFiles();
        configureHome();

        final Properties configuration = getConfiguration();
        final String host = configuration.getProperty("infinitum.web.host", DEFAULT_WEB_HOST); //$NON-NLS-1$
        final int port = Integer.parseInt(configuration.getProperty("infinitum.web.port", DEFAULT_WEB_PORT)); //$NON-NLS-1$
        final String context = configuration.getProperty("infinitum.web.context", DEFAULT_WEB_CONTEXT); //$NON-NLS-1$
        final ServerEmbedder jetty = new ServerEmbedder(host, port, context, configuration);

        jetty.start();
        Thread.currentThread().join();
    }

}
