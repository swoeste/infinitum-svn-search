package de.swoeste.infinitum.fw.core.bl.svn.indexer.action;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.swoeste.infinitum.common.utils.DateUtils;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexFields;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.SVNEntry;

/**
 * @author swoeste
 */
public class ActionCreate extends AbstractAction {

    private static final Logger LOG = LoggerFactory.getLogger(ActionCreate.class);

    /**
     * Constructor for a new ActionCreate.
     * 
     * @param root
     * @param entry
     * @param entryPath
     */
    public ActionCreate(final SVNEntry data) {
        super(data);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IOException
     */
    @Override
    public void doInternalAction(final IndexWriter writer, final Analyzer analyzer) throws IOException {
        try {

            if (getData().isFile()) {

                LOG.info("Execute 'CREATE' action for FILE: {}", getData().getAbsolutePath()); //$NON-NLS-1$

                // write all gathered information to the lucene index
                final Document document = new Document();
                document.add(new StringField(SVNIndexFields.ID.name(), getData().getAbsolutePath(), Store.YES));
                document.add(new StringField(SVNIndexFields.FILE_NAME.name(), getData().getNodeName(), Store.YES));
                document.add(new StringField(SVNIndexFields.FILE_TYPE.name(), getData().getFileType(), Store.YES));
                document.add(new TextField(SVNIndexFields.FILE_CONTENT.name(), getData().getContent(), Store.YES));

                document.add(new LongField(SVNIndexFields.CREATED_REVISION.name(), getData().getRevisionOfCreation(), Store.YES));
                document.add(new StringField(SVNIndexFields.CREATED_AUTHOR.name(), getData().getAuthorOfCreation(), Store.YES));
                document.add(new StringField(SVNIndexFields.CREATED_DATE.name(), DateUtils.format(getData().getDateOfCreation()), Store.YES));

                document.add(new StringField(SVNIndexFields.UPDATED.name(), Boolean.FALSE.toString(), Store.YES));

                writer.addDocument(document, analyzer);
            }

        } catch (final IllegalArgumentException ex) {
            final String msg = MessageFormat.format("Error while trying to execute 'CREATE' action for FILE: ''{0}''.", getData().getAbsolutePath()); //$NON-NLS-1$
            LOG.warn(msg, ex);
        }
    }
}
