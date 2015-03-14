package de.swoeste.infinitum.fw.core.bl.svn.indexer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.io.Files;

import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexConfiguration;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexFields;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNIndexSearch;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.config.SVNInformation;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.filter.ExcludeNameFilter;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.filter.ExcludePathFilter;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexSearchResult;
import de.swoeste.infinitum.fw.core.bl.svn.indexer.model.ISVNIndexSearchResultEntry;

public class SVNIndexFacadeTest {

    private static final Logger LOG = LoggerFactory.getLogger(SVNIndexFacadeTest.class);

    private File                index;

    @BeforeTest
    private final void init() throws IOException {
        this.index = Files.createTempDir();
    }

    @Test
    public void createOrUpdateIndex() throws URISyntaxException {
        final SVNIndexFacadeBean facade = new SVNIndexFacadeBean();
        final SVNInformation svn = new SVNInformation(getRepositoryLocation());
        final SVNIndexConfiguration configuration = new SVNIndexConfiguration(svn, this.index.getAbsolutePath());
        configuration.addFilter(new ExcludeNameFilter(".*.ignore"));
        configuration.addFilter(new ExcludePathFilter(".*/IGNORE/.*"));
        facade.createOrUpdateIndex(configuration);
    }

    @Test(dependsOnMethods = { "createOrUpdateIndex" })
    public void searchIndex() throws URISyntaxException {
        final SVNIndexFacadeBean facade = new SVNIndexFacadeBean();
        final SVNIndexSearch search = new SVNIndexSearch(this.index.getAbsolutePath(), "It should simulate a text file.", 10);
        final ISVNIndexSearchResult searchResult = facade.searchIndex(search);

        // check size
        Assert.assertEquals(searchResult.getEntries().size(), 7);

        for (final ISVNIndexSearchResultEntry resultEntry : searchResult.getEntries()) {
            LOG.info(resultEntry.getIndexEntry().getId());

            // check score
            Assert.assertTrue(resultEntry.getScore() > 0);

            // check if exclude filter works
            Assert.assertFalse(resultEntry.getIndexEntry().getAttribute(SVNIndexFields.ID).contains("IGNORE"));
            Assert.assertFalse(resultEntry.getIndexEntry().getAttribute(SVNIndexFields.FILE_TYPE).equals("ignore"));

            // check if the file content matches search
            if (resultEntry.getScore() >= 0.05) {
                Assert.assertTrue(resultEntry.getIndexEntry().getAttribute(SVNIndexFields.FILE_CONTENT).contains("It should simulate"));
            }
        }
    }

    private static final String getRepositoryLocation() throws URISyntaxException {
        final URL url = Thread.currentThread().getContextClassLoader().getResource("svn-repository/svn-repository.location"); //$NON-NLS-1$
        final File location = new File(url.toURI()).getParentFile();
        return "file:///" + location.getAbsolutePath();
    }

}
