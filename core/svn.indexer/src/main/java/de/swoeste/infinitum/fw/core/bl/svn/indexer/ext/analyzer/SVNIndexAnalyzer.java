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
package de.swoeste.infinitum.fw.core.bl.svn.indexer.ext.analyzer;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.AnalyzerWrapper;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexFields;

/**
 * @author swoeste
 */
public class SVNIndexAnalyzer extends AnalyzerWrapper {

    private final Version               version;
    private final Analyzer              defaultAnalyzer;
    private final Map<String, Analyzer> fieldAnalyzers;

    public SVNIndexAnalyzer(final Version version) {
        super(PER_FIELD_REUSE_STRATEGY);
        this.version = version;
        this.defaultAnalyzer = new StandardAnalyzer(version);
        this.fieldAnalyzers = initAnalyzer(version);
    }

    /** {@inheritDoc} */
    @Override
    protected Analyzer getWrappedAnalyzer(final String fieldName) {
        final Analyzer analyzer = this.fieldAnalyzers.get(fieldName);
        return (analyzer != null) ? analyzer : this.defaultAnalyzer;
    }

    /**
     * @param version
     * @return
     */
    private static final Map<String, Analyzer> initAnalyzer(final Version version) {
        final Map<String, Analyzer> map = new HashMap<>();
        map.put(SVNIndexFields.CREATED_AUTHOR.name(), new KeywordAnalyzer());
        map.put(SVNIndexFields.CREATED_DATE.name(), new KeywordAnalyzer());
        map.put(SVNIndexFields.CREATED_REVISION.name(), new KeywordAnalyzer());
        map.put(SVNIndexFields.FILE_CONTENT.name(), new StandardAnalyzer(version));
        map.put(SVNIndexFields.FILE_NAME.name(), new KeywordAnalyzer());
        map.put(SVNIndexFields.FILE_TYPE.name(), new KeywordAnalyzer());
        map.put(SVNIndexFields.ID.name(), new KeywordAnalyzer());
        map.put(SVNIndexFields.UPDATE_AUTHOR.name(), new KeywordAnalyzer());
        map.put(SVNIndexFields.UPDATE_DATE.name(), new KeywordAnalyzer());
        map.put(SVNIndexFields.UPDATE_REVISION.name(), new KeywordAnalyzer());
        map.put(SVNIndexFields.UPDATED.name(), new KeywordAnalyzer());
        return map;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "SVNIndexAnalyzer [version=" + this.version + ", defaultAnalyzer=" + this.defaultAnalyzer + ", fieldAnalyzers=" + this.fieldAnalyzers + "]";
    }

}
