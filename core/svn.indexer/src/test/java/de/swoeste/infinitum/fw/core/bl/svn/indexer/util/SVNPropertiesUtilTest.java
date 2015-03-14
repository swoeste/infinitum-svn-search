package de.swoeste.infinitum.fw.core.bl.svn.indexer.util;

import java.io.File;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.io.Files;

import de.swoeste.infinitum.common.utils.properties.SortedProperties;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.SVNConstants;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.util.SVNPropertiesUtil;

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
