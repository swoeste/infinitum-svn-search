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
package de.swoeste.infinitum.fw.core.bl.svn.indexer;

import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexConfiguration;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexSearch;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexEntry;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexSearchResult;

/**
 * @author swoeste
 */
public interface ISVNIndexFacade {

    /**
     * Create a new or update an existing index based on the given configuration. This method might be called multiple
     * times but is only executing if it is not already running.
     *
     * @param configuration
     *            a configuration
     */
    void createOrUpdateIndex(final SVNIndexConfiguration configuration);

    /**
     * Search the index with the given search criteria.
     *
     * @param search
     *            a search
     * @return a list of {@link ISVNIndexEntry} which match the search criteria, may be empty but never null.
     */
    ISVNIndexSearchResult searchIndex(final SVNIndexSearch search);

}
