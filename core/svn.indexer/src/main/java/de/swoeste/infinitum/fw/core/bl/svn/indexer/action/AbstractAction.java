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
package de.swoeste.infinitum.fw.core.bl.svn.indexer.action;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;

import de.swoeste.infinitum.fw.core.bl.svn.indexer.exception.SVNIndexException;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNElement;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.SVNEntry;

/**
 * Actions are implementing the command pattern. The command executor just
 * invokes the doAction method.
 *
 * @author swoeste
 */
public abstract class AbstractAction {

    private final SVNEntry data;

    /**
     * Constructor for a new AbstractAction.
     *
     * @param data
     *            the data this action should handle
     */
    public AbstractAction(final SVNEntry data) {
        this.data = data;
    }

    /**
     * Get the ISVNIndexItemData this action should handle.
     *
     * @return the data this action should handle
     */
    protected ISVNElement getData() {
        return this.data;
    }

    /**
     * This method will be called from the AbstractAction, the implemented
     * action should update the lucene index during execution. Any occurring
     * CorruptIndexException or IOException will be handled by the calling
     * method.
     *
     * @param writer
     *            a pre-configured IndexWriter. You could directly access the
     *            lucene index from it. <b>Do not close it after use, it is
     *            handled from the invoking class!<b/>
     * @param analyzer
     */
    protected abstract void doInternalAction(IndexWriter writer, Analyzer analyzer) throws CorruptIndexException, IOException, IllegalArgumentException;

    /**
     * This method will be called from the SVNIndexWriterJob, the implemented
     * action should update the lucene index during execution.
     *
     * @param writer
     *            a pre-configured IndexWriter. You could directly access the
     *            lucene index from it. <b>Do not close it after use, it is
     *            handled from the invoking class!<b/>
     * @param analyzer
     */
    public void doAction(final IndexWriter writer, final Analyzer analyzer) {
        try {
            doInternalAction(writer, analyzer);
        } catch (final CorruptIndexException ex) {
            throw new SVNIndexException("The lucene index seems to be corrupt.", ex); //$NON-NLS-1$
        } catch (final IOException ex) {
            throw new SVNIndexException("Error while trying to update lucene index.", ex); //$NON-NLS-1$
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "AbstractAction [data=" + this.data + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }

}
