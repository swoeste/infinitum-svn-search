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
package de.swoeste.infinitum.fw.core.bl.svn.indexer.util;

import java.io.File;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.io.Files;

import de.swoeste.infinitum.common.utils.properties.SortedProperties;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.SVNConstants;

public class SVNPropertiesUtilTest {

    private File temp;

    @BeforeTest
    public void init() throws IOException {
        this.temp = Files.createTempDir();
    }

    @Test
    public void openPropertiesExpectDefault() {
        final SortedProperties properties = SVNPropertiesUtil.openProperties(this.temp.getAbsolutePath());
        Assert.assertEquals(properties.size(), 1);
        Assert.assertEquals(properties.get(SVNConstants.PROPERTY_LAST_REVISION), SVNConstants.PROPERTY_LAST_REVISION_DEFAULT);
    }

    @Test(dependsOnMethods = { "openPropertiesExpectDefault" })
    public void storeProperties() throws IOException {
        final SortedProperties properties = new SortedProperties();
        properties.put("Test Key 1", "Test Value 1"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("Test Key 2", "Test Value 2"); //$NON-NLS-1$ //$NON-NLS-2$
        SVNPropertiesUtil.storeProperties(this.temp.getAbsolutePath(), properties);
    }

    @Test(dependsOnMethods = { "storeProperties" })
    public void openPropertiesExpectTest() {
        final SortedProperties properties = SVNPropertiesUtil.openProperties(this.temp.getAbsolutePath());
        Assert.assertEquals(properties.size(), 2);
        Assert.assertEquals(properties.get("Test Key 1"), "Test Value 1"); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals(properties.get("Test Key 2"), "Test Value 2"); //$NON-NLS-1$ //$NON-NLS-2$
    }

}
