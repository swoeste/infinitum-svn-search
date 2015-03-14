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
     * Create a new or update an existing index based on the given
     * configuration. This method might be called multiple times but is only
     * executing if it is not already running.
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
     * @return a list of {@link ISVNIndexEntry} which match the search
     *         criteria, may be empty but never null.
     */
    ISVNIndexSearchResult searchIndex(final SVNIndexSearch search);

}
