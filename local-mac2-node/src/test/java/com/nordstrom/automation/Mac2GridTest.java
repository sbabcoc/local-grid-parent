package com.nordstrom.automation;

import static org.junit.Assert.assertEquals;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.annotations.InitialPage;
import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.examples.ExamplePage;
import com.nordstrom.automation.selenium.examples.MacPage;
import com.nordstrom.automation.selenium.junit.JUnitBase;
import com.nordstrom.automation.selenium.plugins.Mac2Plugin;
import com.nordstrom.common.file.OSInfo;
import com.nordstrom.common.file.OSInfo.OSType;
import com.nordstrom.utility.GridLauncher;

@InitialPage(MacPage.class)
public class Mac2GridTest extends JUnitBase {

    private static SeleniumGrid seleniumGrid = null;
    private final DriverPlugin plugin = new Mac2Plugin();

    @Before
    public void beforeTest() {
        Assume.assumeTrue(OSInfo.getDefault().getType() == OSType.MACINTOSH);
        launchSeleniumGrid();
        ExamplePage.setHubAsTarget();
    }

    @Test
    public void testEditing() {
        MacPage page = getInitialPage();
        page.modifyDocument("Hello world!");
        assertEquals("Hello world!", page.accessDocument());
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
