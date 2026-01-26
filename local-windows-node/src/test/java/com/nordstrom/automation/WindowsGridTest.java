package com.nordstrom.automation;

import static org.junit.Assert.assertEquals;

import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.annotations.InitialPage;
import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.examples.ExamplePage;
import com.nordstrom.automation.selenium.examples.NotepadApplication;
import com.nordstrom.automation.selenium.junit.JUnitBase;
import com.nordstrom.automation.selenium.plugins.WindowsPlugin;
import com.nordstrom.common.file.OSInfo;
import com.nordstrom.common.file.OSInfo.OSType;
import com.nordstrom.utility.GridLauncher;

@InitialPage(NotepadApplication.class)
public class WindowsGridTest extends JUnitBase {
    
    private static SeleniumGrid seleniumGrid = null;
    private final DriverPlugin plugin = new WindowsPlugin();

    @BeforeClass
    public static void beforeClass() {
        Assume.assumeTrue(OSInfo.getDefault().getType() == OSType.WINDOWS);
    }

    @Before
    public void beforeTest() {
        launchSeleniumGrid();
        ExamplePage.setHubAsTarget();
    }

    @Test
    public void testEditing() {
        NotepadApplication page = getInitialPage();
        page.modifyDocument("Hello world!");
        assertEquals(page.getDocumentContent(), "Hello world!");
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
