package com.nordstrom.automation;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Keys;

import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.annotations.InitialPage;
import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.examples.ExamplePage;
import com.nordstrom.automation.selenium.examples.WindowsPage;
import com.nordstrom.automation.selenium.junit.JUnitBase;
import com.nordstrom.automation.selenium.plugins.WindowsPlugin;
import com.nordstrom.utility.GridLauncher;

@InitialPage(WindowsPage.class)
public class WindowsGridTest extends JUnitBase {
    
    private static SeleniumGrid seleniumGrid = null;
    private DriverPlugin plugin = new WindowsPlugin();

    @Before
    public void beforeTest() {
        launchSeleniumGrid();
        ExamplePage.setHubAsTarget();
    }

    @Test
    public void testEditing() {
        WindowsPage page = getInitialPage();
        page.modifyDocument("Hello world!");
        assertEquals(page.getDocument(), "Hello world!");
        page.modifyDocument(Keys.CONTROL + "z");
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
