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

import java.util.Properties;

/**
 * @author swoeste
 */
public class ExtendedProperties extends Properties {

    private static final long serialVersionUID = -7012915736994878517L;

    /**
     * Constructor for a new ExtendedProperties.
     */
    public ExtendedProperties() {
        super();
    }

    /**
     * Constructor for a new ExtendedProperties.
     *
     * @param defaults
     */
    public ExtendedProperties(final Properties defaults) {
        super(defaults);
    }

    public long getLongValue(final String key) {
        return Long.valueOf(getProperty(key));
    }

}
