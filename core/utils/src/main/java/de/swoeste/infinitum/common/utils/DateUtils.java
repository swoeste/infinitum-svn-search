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
