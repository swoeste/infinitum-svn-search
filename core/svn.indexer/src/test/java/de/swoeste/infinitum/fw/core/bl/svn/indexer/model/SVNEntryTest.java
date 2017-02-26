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
package de.swoeste.infinitum.fw.core.bl.svn.indexer.model;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;

/**
 * @author swoeste
 */
public class SVNEntryTest {

    private static final String PROTOCOL  = "http";          //$NON-NLS-1$
    private static final String HOST      = "svn.swoeste.de";	//$NON-NLS-1$
    private static final String HOST_PATH = "svn";           //$NON-NLS-1$

    private final ISVNElement createSVNIndexItemData(final String path, final SVNNodeKind svnNodeKind) throws Exception {
        final SVNURL svnURL = createTestSVNURL();
        final SVNLogEntryPath svnLogEntryPath = createTestSVNLogEntryPath(path);
        return new SVNEntry(svnURL, null, svnLogEntryPath, svnNodeKind);
    }

    @DataProvider(name = "testSVNIndexItemDataDataProvider")
    public Object[][] createTestSVNIndexItemDataDataProvider() {
        return new Object[][] { //
        // Check file without type
                { "some/path/file", SVNNodeKind.FILE, "file", "", true },  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                // Check file without type and a dot in path
                { "some.path/file", SVNNodeKind.FILE, "file", "", true },  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                // Check file with type
                { "some/path/file.type", SVNNodeKind.FILE, "file.type", "type", true },  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                // Check file with type and a dot in path
                { "some.path/file.type", SVNNodeKind.FILE, "file.type", "type", true },  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                // Check folder
                { "some/path/folder", SVNNodeKind.DIR, "folder", "", false },  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                // Check folder with dot in name
                { "some/path/fol.der", SVNNodeKind.DIR, "fol.der", "", false }  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        };
    }

    @Test(dataProvider = "testSVNIndexItemDataDataProvider")
    public void testSVNIndexItemData(final String path, final SVNNodeKind svnNodeKind, final String expectedName, final String expectedType, final boolean isFile) throws Exception {
        final ISVNElement entry = createSVNIndexItemData(path, svnNodeKind);
        Assert.assertEquals("http://svn.swoeste.de:3690/svn/" + path, entry.getAbsolutePath()); //$NON-NLS-1$
        Assert.assertEquals(expectedType, entry.getFileType());
        Assert.assertEquals(expectedName, entry.getNodeName());
        Assert.assertEquals(isFile, entry.isFile());
        Assert.assertEquals(!isFile, entry.isFolder());
    }

    private final SVNLogEntryPath createTestSVNLogEntryPath(final String path) throws SVNException {
        return new SVNLogEntryPath(path, 'A', null, -1, SVNNodeKind.UNKNOWN);
    }

    private final SVNURL createTestSVNURL() throws SVNException {
        return SVNURL.create(PROTOCOL, null, HOST, 3690, HOST_PATH, false);
    }

}
