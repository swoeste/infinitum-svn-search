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
package de.swoeste.infinitum.common.utils.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * @author swoeste
 */
public class SortedProperties extends ExtendedProperties {

    private static final long serialVersionUID = -1246466856419039790L;

    /**
     * Constructor for a new SortedProperties.
     */
    public SortedProperties() {
        super();
    }

    /**
     * Constructor for a new SortedProperties.
     *
     * @param defaults
     */
    public SortedProperties(final Properties defaults) {
        super(defaults);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Enumeration keys() {
        final Enumeration<Object> keys = super.keys();
        final List keyList = new ArrayList();

        while (keys.hasMoreElements()) {
            keyList.add(keys.nextElement());
        }

        Collections.sort(keyList);
        return Collections.enumeration(keyList);
    }

}
