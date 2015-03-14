/*
 * Copyright (C) 2012 Sebastian Woeste
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
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
