package de.swoeste.infinitum.fw.core.bl.svn.indexer.filter;

import de.swoeste.infinitum.fw.core.bl.svn.indexer.filter.ISVNFilter;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNElement;

/**
 * Used for Command-Pattern.
 * 
 * @author swoeste
 */
public abstract class AbstractFilter implements ISVNFilter {

    private final String expression;

    /**
     * Constructor for a new AbstractFilter.
     */
    public AbstractFilter(final String expression) {
        this.expression = expression;
    }

    /**
     * Returns the expression set for this AbstractFilter.
     * 
     * @return the expression
     */
    public String getExpression() {
        return this.expression;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean isFiltered(ISVNElement data);

}
