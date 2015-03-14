package de.swoeste.infinitum.fw.core.bl.svn.indexer.action;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.PrefixQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexFields;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.SVNEntry;

/**
 * @author swoeste
 */
public class ActionDelete extends AbstractAction {

    private static final Logger LOG = LoggerFactory.getLogger(ActionDelete.class);

    /**
     * Constructor for a new ActionDelete.
     * 
     * @param data
     */
    public ActionDelete(final SVNEntry data) {
        super(data);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IOException
     */
    @Override
    public void doInternalAction(final IndexWriter writer, final Analyzer analyzer) throws IOException {

        // Handle Files
        if (getData().isFile()) {
            ActionDelete.LOG.info("Execute 'DELETE' action for FILE: {}", getData().getAbsolutePath()); //$NON-NLS-1$
            writer.deleteDocuments(new Term(SVNIndexFields.ID.name(), getData().getAbsolutePath()));
            return;
        }

        // Handle Directories
        if (getData().isFolder()) {
            ActionDelete.LOG.info("Execute 'DELETE' action for FOLDER: {}", getData().getAbsolutePath()); //$NON-NLS-1$
            writer.deleteDocuments(new PrefixQuery(new Term(SVNIndexFields.ID.name(), getData().getAbsolutePath())));
            return;
        }

    }

}