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
package de.swoeste.infinitum.fw.core.bl.svn.indexer.manager;

import java.util.HashSet;
import java.util.Set;

import de.swoeste.infinitum.fw.core.bl.svn.indexer.filter.ISVNFilter;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNElement;

/**
 * @author swoeste
 */
public class FilterManager {

    private final Set<ISVNFilter> filters = new HashSet<>();

    /**
     * Add a new Filter to this FilterExecutor.
     *
     * @param filter
     */
    public void addFilter(final ISVNFilter filter) {
        this.filters.add(filter);
    }

    /**
     * Check if the given svn element should be rejected from being indexed.
     *
     * @param data
     * @return true if the given svn element is rejected, false if not
     */

    public boolean isRejected(final ISVNElement data) {
        for (final ISVNFilter filter : this.filters) {
            if (filter.isRejected(data)) {
                return true;
            }
        }

        return false;
    }

}
