/*
 *Copyright (C) 2017 Sebastian Woeste
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
