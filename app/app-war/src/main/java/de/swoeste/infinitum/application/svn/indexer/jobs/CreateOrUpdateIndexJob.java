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
package de.swoeste.infinitum.application.svn.indexer.jobs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.swoeste.infinitum.application.common.conf.ApplicationProperties;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.ISVNIndexFacade;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexContext;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNInformation;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.filter.ExcludeNameFilter;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.filter.ExcludePathFilter;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.filter.ISVNFilter;

/**
 * @author swoeste
 */
public class CreateOrUpdateIndexJob {

    private static final Logger LOG = LoggerFactory.getLogger(CreateOrUpdateIndexJob.class);

    private ISVNIndexFacade     svnIndexFacade;

    public void setSvnIndexFacade(final ISVNIndexFacade svnIndexFacade) {
        this.svnIndexFacade = svnIndexFacade;
    }

    public void execute() {
        LOG.info("Executing: CreateOrUpdateIndexJob"); //$NON-NLS-1$

        // svn connection configuration
        final String repository = ApplicationProperties.getSVNIndexRepository();
        final String username = ApplicationProperties.getSVNIndexUsername();
        final String password = ApplicationProperties.getSVNIndexPassword();
        final SVNInformation svn = createSVNInformation(repository, username, password);

        // svn index context
        final String indexPath = ApplicationProperties.getSVNIndexPath();
        final int batchSize = ApplicationProperties.getSVNIndexBatchSize();
        final SVNIndexContext context = new SVNIndexContext(svn, indexPath, batchSize);

        // svn index context - filter
        final List<String> svnIndexExcludeFiles = ApplicationProperties.getSVNIndexExcludeFiles();
        context.addFilter(createExcludeNameFilter(svnIndexExcludeFiles));
        final List<String> svnIndexExcludePaths = ApplicationProperties.getSVNIndexExcludePaths();
        context.addFilter(createExcludePathFilter(svnIndexExcludePaths));

        this.svnIndexFacade.createOrUpdateIndex(context);
    }

    private SVNInformation createSVNInformation(final String repository, final String username, final String password) {
        if (!StringUtils.isBlank(username)) {
            LOG.debug("Connection credentials provided, will try authenticated access."); //$NON-NLS-1$
            return new SVNInformation(repository, username, password);
        } else {
            LOG.debug("No authentication information provided, will try anonymous access."); //$NON-NLS-1$
            return new SVNInformation(repository);
        }
    }

    private List<ISVNFilter> createExcludeNameFilter(final List<String> svnIndexExcludeFiles) {
        final List<ISVNFilter> filter = new ArrayList<>();
        for (String file : svnIndexExcludeFiles) {
            filter.add(new ExcludeNameFilter(file));
        }
        return filter;
    }

    private List<ISVNFilter> createExcludePathFilter(final List<String> svnIndexExcludePaths) {
        final List<ISVNFilter> filter = new ArrayList<>();
        for (String path : svnIndexExcludePaths) {
            filter.add(new ExcludePathFilter(path));
        }
        return filter;
    }

}
