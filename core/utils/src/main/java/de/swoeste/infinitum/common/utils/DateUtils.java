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
package de.swoeste.infinitum.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author swoeste
 */
public final class DateUtils {

    private DateUtils() {
        // hidden
    }

    // TODO make a localized one of this

    public static String format(final Date date) {
        final SimpleDateFormat format = new SimpleDateFormat();
        return format.format(date);
    }

}
