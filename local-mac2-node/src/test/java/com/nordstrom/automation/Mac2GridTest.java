package com.nordstrom.automation;

import static org.junit.Assert.assertEquals;

import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.annotations.InitialPage;
import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.examples.ExamplePage;
import com.nordstrom.automation.selenium.examples.TextEditApplication;
import com.nordstrom.automation.selenium.examples.TextEditDocumentWindow;
import com.nordstrom.automation.selenium.examples.TextEditManagementPanel;
import com.nordstrom.automation.selenium.junit.JUnitBase;
import com.nordstrom.automation.selenium.plugins.Mac2Plugin;
import com.nordstrom.common.file.OSInfo;
import com.nordstrom.common.file.OSInfo.OSType;
import com.nordstrom.utility.GridLauncher;

@InitialPage(TextEditApplication.class)
public class Mac2GridTest extends JUnitBase {

    private static SeleniumGrid seleniumGrid = null;
    private final DriverPlugin plugin = new Mac2Plugin();

    @BeforeClass
    public static void beforeClass() {
        Assume.assumeTrue(OSInfo.getDefault().getType() == OSType.MACINTOSH);
    }
    
    @Before
    public void beforeTest() {
        launchSeleniumGrid();
        ExamplePage.setHubAsTarget();
    }

    @Test
    public void testEditing() {
        TextEditApplication application = getInitialPage();
        TextEditManagementPanel managementPanel = application.openManagementPanel();
        TextEditDocumentWindow documentWindow = managementPanel.openNewDocument();
        documentWindow.modifyDocument("Hello world!");
        assertEquals(documentWindow.getDocumentContent(), "Hello world!");
        documentWindow.closeDocumentWithoutSaving();
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
