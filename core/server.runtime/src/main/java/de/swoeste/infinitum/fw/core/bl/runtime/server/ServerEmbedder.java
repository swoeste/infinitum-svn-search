/*
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
package de.swoeste.infinitum.fw.core.bl.runtime.server;

import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.ShutdownHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author swoeste
 */
public class ServerEmbedder {

    private static final Logger LOG                  = LoggerFactory.getLogger(ServerEmbedder.class);

    private static final String TIMEZONE             = "GMT";                                                           //$NON-NLS-1$
    private static final String WAR_LOCATION         = "/war";                                                          //$NON-NLS-1$
    private static final String WEB_DEFAULT_LOCATION = "/de/swoeste/infinitum/fw/core/bl/runtime/server/webdefault.xml"; //$NON-NLS-1$

    private static final String SHUTDOWN_TOKEN       = "infinitum.svn-search.shutdownToken";                            //$NON-NLS-1$

    private final Server        server;
    private final String        host;
    private final int           port;
    private final String        contextPath;
    private final Properties    configuration;

    public ServerEmbedder(final String host, final int port, final String contextPath, final Properties configuration) throws Exception {
        this.host = host.trim();
        this.port = port;
        this.contextPath = contextPath.trim();
        this.configuration = configuration;
        this.server = new Server(new QueuedThreadPool(5, 50));
        configureProgrammatically();
    }

    private Server configureProgrammatically() throws URISyntaxException {
        configureServer();

        final List<Object> handlers = new ArrayList<Object>();

        final String shutdownCookie = System.getProperty(SHUTDOWN_TOKEN);
        if ((shutdownCookie != null) && (!shutdownCookie.isEmpty())) {
            LOG.info("Registering shutdown handler."); //$NON-NLS-1$
            final ShutdownHandler shutdownHandler = new ShutdownHandler(this.server, shutdownCookie);
            shutdownHandler.setExitJvm(true);
            handlers.add(shutdownHandler);
        }

        final WebAppContext context = new WebAppContext(getPath(WAR_LOCATION), this.contextPath);

        context.setDefaultsDescriptor(WEB_DEFAULT_LOCATION);
        handlers.add(context);

        final String filenamePattern = this.configuration.getProperty("infinitum.svn-search.web.jettyRequestLogs"); //$NON-NLS-1$
        if (filenamePattern != null) {
            handlers.add(configureRequestLogHandler(filenamePattern));
        }

        final HandlerCollection handler = new HandlerCollection();
        handler.setHandlers(handlers.toArray(new Handler[handlers.size()]));
        this.server.setHandler(handler);

        return this.server;
    }

    private RequestLogHandler configureRequestLogHandler(final String filenamePattern) {
        final RequestLogHandler requestLogHandler = new RequestLogHandler();
        final NCSARequestLog requestLog = new NCSARequestLog(filenamePattern);
        requestLog.setRetainDays(7);
        requestLog.setAppend(true);
        requestLog.setExtended(true);
        requestLog.setLogTimeZone(TIMEZONE);
        requestLogHandler.setRequestLog(requestLog);
        return requestLogHandler;
    }

    private void configureServer() {
        final ServerConnector connector = new ServerConnector(this.server);
        connector.setHost(this.host);
        connector.setPort(this.port);
        this.server.addConnector(connector);
        this.server.setStopAtShutdown(true);
    }

    private String getPath(final String resourcePath) throws URISyntaxException {
        final URL resource = getClass().getResource(resourcePath);
        if (resource != null) {
            return resource.toURI().toString();
        }
        final String msg = MessageFormat.format("Unable to find resource {0} in classpath.", resourcePath); //$NON-NLS-1$
        throw new IllegalStateException(msg);
    }

    public void start() throws Exception {
        this.server.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    ServerEmbedder.this.server.stop();
                } catch (final Exception ex) {
                    LOG.error("Unable to stop the Jetty server.", ex); //$NON-NLS-1$
                }
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "ServerEmbedder [server=" + this.server + ", host=" + this.host + ", port=" + this.port + ", contextPath=" + this.contextPath + ", configuration=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                + this.configuration + "]"; //$NON-NLS-1$
    }

}
