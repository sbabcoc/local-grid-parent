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
import com.nordstrom.automation.selenium.plugins.UiAutomator2Plugin;
import com.nordstrom.utility.GridLauncher;

@InitialPage(AndroidPage.class)
public class UiAutomator2GridTest extends JUnitBase {
    
    private static SeleniumGrid seleniumGrid = null;
    private DriverPlugin plugin = new UiAutomator2Plugin();

    @Before
    public void beforeTest() {
        launchSeleniumGrid();
        ExamplePage.setHubAsTarget();
    }

    @Test
    public void testSearchActivity() {
        AndroidPage page = getInitialPage();
        page.submitSearchQuery("Hello world!");
        assertEquals(page.getSearchResult(), "Hello world!");
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
