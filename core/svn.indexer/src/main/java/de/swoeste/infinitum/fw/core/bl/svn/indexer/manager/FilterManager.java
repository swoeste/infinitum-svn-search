package de.swoeste.infinitum.fw.core.bl.svn.indexer.manager;

import java.util.HashSet;
import java.util.Set;

import de.swoeste.infinitum.fw.core.bl.svn.indexer.filter.ISVNFilter;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNElement;

/**
 * @author swoeste
 */
public class FilterManager {

    private final Set<ISVNFilter> filter = new HashSet<>();

    /**
     * Add a new Filter to this FilterExecutor.
     * 
     * @param filter
     */
    public void addFilter(final ISVNFilter filter) {
        this.filter.add(filter);
    }

    /**
     * For each item fetched from the svn repository this method is called to
     * check if the current item should be filtered or not.
     * 
     * @param data
     * @return true if the given item passes the filter, false if not
     */

    public boolean isFiltered(final ISVNElement data) {
        for (final ISVNFilter filter : this.filter) {
            if (!filter.isFiltered(data)) {
                return false;
            }
        }

        // by default all items pass the filter
        return true;
    }

}
