package de.swoeste.infinitum.fw.core.bl.svn.indexer.filter;

import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNElement;

/**
 * @author swoeste
 */
public class ExcludePathFilter extends AbstractFilter {

    /**
     * Constructor for a new ExcludePathFilter.
     * 
     * @param expression
     */
    public ExcludePathFilter(final String expression) {
        super(expression);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isFiltered(final ISVNElement data) {
        return !data.getAbsolutePath().matches(getExpression());
    }
}
