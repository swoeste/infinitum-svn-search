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
package de.swoeste.infinitum.application.svn.indexer.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.swoeste.infinitum.application.common.conf.ApplicationProperties;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.ISVNIndexFacade;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexConfiguration;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNInformation;

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

        // FIXME: support for username / password authentication!
        final String repository = ApplicationProperties.getSVNIndexRepository();
        final SVNInformation svn = new SVNInformation(repository);

        final String indexPath = ApplicationProperties.getSVNIndexPath();
        final SVNIndexConfiguration configuration = new SVNIndexConfiguration(svn, indexPath);

        this.svnIndexFacade.createOrUpdateIndex(configuration);
    }

}
