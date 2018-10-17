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
     */
    @Override
    protected void doInternalAction(final IndexWriter writer, final Analyzer analyzer) throws IOException {

        // Handle Files
        if (getData().isFile()) {
            ActionDelete.LOG.info("Execute 'DELETE' action for FILE: {}", getData().getAbsolutePath()); //$NON-NLS-1$
            writer.deleteDocuments(new Term(SVNIndexFields.ID.name(), getData().getAbsolutePath()));
        }

        // Handle Directories
        if (getData().isFolder()) {
            ActionDelete.LOG.info("Execute 'DELETE' action for FOLDER: {}", getData().getAbsolutePath()); //$NON-NLS-1$
            writer.deleteDocuments(new PrefixQuery(new Term(SVNIndexFields.ID.name(), getData().getAbsolutePath())));
        }

    }

}
