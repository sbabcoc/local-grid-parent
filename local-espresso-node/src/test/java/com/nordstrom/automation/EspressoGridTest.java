package com.nordstrom.automation;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.annotations.InitialPage;
import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.examples.AndroidPage;
import com.nordstrom.automation.selenium.examples.ExamplePage;
import com.nordstrom.automation.selenium.junit.JUnitBase;
import com.nordstrom.automation.selenium.plugins.EspressoPlugin;
import com.nordstrom.utility.GridLauncher;

@InitialPage(AndroidPage.class)
public class EspressoGridTest extends JUnitBase {
    
    private static SeleniumGrid seleniumGrid = null;
    private final DriverPlugin plugin = new EspressoPlugin();

    @Before
    public void beforeTest() {
        launchSeleniumGrid();
        ExamplePage.setHubAsTarget();
    }

    @Test
    public void testSearchActivity() {
        AndroidPage page = getInitialPage();
        page.submitSearchQuery("Hello world!");
        assertEquals("Hello world!", page.getSearchResult());
    }

    private void launchSeleniumGrid() {
        if (seleniumGrid == null) {
            seleniumGrid = launchGrid();
        }
    }
    
    private SeleniumGrid launchGrid() {
        return GridLauncher.launch(getPlugin());
    }
    
    private DriverPlugin getPlugin() {
        return plugin;
    }
    
}
