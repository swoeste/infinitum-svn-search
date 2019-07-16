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
package de.swoeste.infinitum.application.svn.indexer.beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.swoeste.infinitum.application.svn.indexer.Pages;
import de.swoeste.infinitum.application.svn.indexer.conf.ApplicationProperties;
import de.swoeste.infinitum.application.svn.indexer.model.SearchResultTO;
import de.swoeste.infinitum.application.svn.indexer.util.Paginator;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.ISVNIndexFacade;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexSearch;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexSearchResult;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexSearchResultEntry;

/**
 * @author swoeste
 */
// ALIAS 4 JSF
@Service(value = "svnIndexerControllerBean")
public class ControllerBean {

    private static final Logger       LOG = LoggerFactory.getLogger(ControllerBean.class);

    @Autowired
    private ISVNIndexFacade           svnIndexFacade;

    private String                    searchText;

    private Paginator<SearchResultTO> searchResult;

    private boolean isSearchTextEmpty() {
        return (this.searchText == null) || this.searchText.isEmpty();
    }

    public void validate() {
        if (isSearchTextEmpty()) {
            try {
                LOG.trace("Redirecting to '{}' because 'searchText' is null or empty.", Pages.SVNINDEXER_SEARCH); //$NON-NLS-1$
                FacesContext.getCurrentInstance().getExternalContext().redirect(Pages.SVNINDEXER_SEARCH);
            } catch (IOException ex) {
                LOG.warn("Unable to redirect.", ex); //$NON-NLS-1$
            }
        }
    }

    public void search(final ActionEvent event) {
        if (!isSearchTextEmpty()) {
            try {
                LOG.debug("Searching for '{}'.", this.searchText); //$NON-NLS-1$
                final String svnIndexPath = ApplicationProperties.getSVNIndexPath();
                final SVNIndexSearch search = new SVNIndexSearch(svnIndexPath, this.searchText, ApplicationProperties.getSVNIndexMaxSearchResults());
                final ISVNIndexSearchResult result = this.svnIndexFacade.searchIndex(search);

                List<SearchResultTO> doSomething = doSomething(result);

                this.searchResult = new Paginator<>(doSomething, ApplicationProperties.getSVNIndexSearchResultsPerPage());
            } catch (Exception ex) {
                LOG.warn("Unable to search for '{}'.", this.searchText, ex); //$NON-NLS-1$
                this.searchResult = new Paginator<>(Collections.<SearchResultTO> emptyList(), ApplicationProperties.getSVNIndexSearchResultsPerPage());
            }
        }
    }

    /**
     * @param result
     * @return
     */
    private List<SearchResultTO> doSomething(final ISVNIndexSearchResult result) {
        final List<SearchResultTO> output = new ArrayList<>();
        for (ISVNIndexSearchResultEntry entry : result.getEntries()) {
            output.add(new SearchResultTO(entry));
        }
        return output;
    }

    public Paginator<SearchResultTO> getSearchResult() {
        return this.searchResult;
    }

    public String getSearchText() {
        LOG.trace("Get 'searchText' : '{}' ", this.searchText); //$NON-NLS-1$
        return this.searchText;
    }

    public void setSearchText(final String searchText) {
        LOG.trace("Set 'searchText' from '{}' to '{}' ", this.searchText, searchText); //$NON-NLS-1$
        this.searchText = searchText;
    }

}
