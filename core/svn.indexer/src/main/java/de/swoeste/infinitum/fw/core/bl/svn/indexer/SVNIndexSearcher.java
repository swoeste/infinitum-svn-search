/*
 * Copyright (C) 2017 Sebastian Woeste
 *
 * Licensed to Sebastian Woeste under one or more contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership. I license this file to You under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package de.swoeste.infinitum.fw.core.bl.svn.indexer;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexFields;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexSearch;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.exception.SVNIndexException;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.ext.analyzer.SVNIndexAnalyzer;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.ext.formatter.NoneHilightingFormatter;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexSearchResult;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.SVNIndexEntry;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.SVNIndexSearchResult;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.SVNIndexSearchResultEntry;

/**
 * @author swoeste
 */
public class SVNIndexSearcher {

    private static final Logger LOG = LoggerFactory.getLogger(SVNIndexSearcher.class);

    private final File          location;

    private IndexReader         reader;
    private IndexSearcher       searcher;
    private Analyzer            analyzer;

    public SVNIndexSearcher(final File location) {
        this.location = location;
    }

    private Query createQuery(final String query) {
        try {
            final StandardQueryParser parser2 = new StandardQueryParser(this.analyzer);
            parser2.setAllowLeadingWildcard(false);
            return parser2.parse(query, SVNIndexFields.FILE_CONTENT.toString());
        } catch (QueryNodeException e) {
            final String msg = MessageFormat.format("Unable to parse query: ''{0}''", query); //$NON-NLS-1$
            throw new SVNIndexException(msg, e);
        }
    }

    private String createExcerpt(final Query query, final String text) {
        try {
            final QueryScorer queryScorer = new QueryScorer(query);
            final Formatter formatter = new NoneHilightingFormatter();
            final Highlighter highlighter = new Highlighter(formatter, queryScorer);
            highlighter.setTextFragmenter(new SimpleSpanFragmenter(queryScorer, 250));
            highlighter.setMaxDocCharsToAnalyze(Integer.MAX_VALUE);
            return highlighter.getBestFragment(this.analyzer, SVNIndexFields.FILE_CONTENT.name(), text);

        } catch (InvalidTokenOffsetsException ex) {
            LOG.error("Unable to create excerpt for content, excerpt will not be available!", ex); //$NON-NLS-1$
        } catch (IOException ex) {
            LOG.error("Unable to create excerpt for content, excerpt will not be available!", ex); //$NON-NLS-1$
        }

        return StringUtils.EMPTY;
    }

    public ISVNIndexSearchResult search(final SVNIndexSearch search) {

        try {
            setup();

            final SVNIndexSearchResult searchResult = new SVNIndexSearchResult(search.getMaxResults());

            final Query query = createQuery(search.getQueryString());
            final TopDocs topDocs = this.searcher.search(query, search.getMaxResults());

            int index = 0;

            while ((index < topDocs.totalHits) && (index < search.getMaxResults())) {
                final ScoreDoc scoreDoc = topDocs.scoreDocs[index];
                final Document doc = this.searcher.doc(scoreDoc.doc);

                final SVNIndexEntry item = new SVNIndexEntry();
                for (final SVNIndexFields svnIndexFields : SVNIndexFields.values()) {
                    item.addAttribute(svnIndexFields, doc.get(svnIndexFields.toString()));
                }

                final float score = scoreDoc.score;
                final String excerpt = createExcerpt(query, item.getFileContent());

                final SVNIndexSearchResultEntry entry = new SVNIndexSearchResultEntry(item);
                entry.setScore(score);
                entry.setExcerpt(excerpt);

                searchResult.add(entry);

                index = index + 1;
            }

            return searchResult;

        } catch (final IOException e) {
            final String msg = MessageFormat.format("Unable to execute search for query: ''{0}''", search.getQueryString()); //$NON-NLS-1$
            throw new SVNIndexException(msg, e);
        } finally {
            teardown();
        }
    }

    private void setup() throws IOException {
        this.reader = DirectoryReader.open(FSDirectory.open(this.location));
        this.searcher = new IndexSearcher(this.reader);
        this.analyzer = new SVNIndexAnalyzer(SVNConstants.LUCENE_VERSION);
    }

    private void teardown() {
        IOUtils.closeQuietly(this.reader);
        IOUtils.closeQuietly(this.analyzer);
    }
}
