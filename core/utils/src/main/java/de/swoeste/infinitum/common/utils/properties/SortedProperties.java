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
