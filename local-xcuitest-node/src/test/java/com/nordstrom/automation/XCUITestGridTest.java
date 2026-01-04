package com.nordstrom.automation;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.annotations.InitialPage;
import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.examples.ExamplePage;
import com.nordstrom.automation.selenium.examples.IOSApplicationEchoScreenView;
import com.nordstrom.automation.selenium.examples.IOSApplicationMainView;
import com.nordstrom.automation.selenium.junit.JUnitBase;
import com.nordstrom.automation.selenium.plugins.XCUITestPlugin;
import com.nordstrom.common.file.OSInfo;
import com.nordstrom.common.file.OSInfo.OSType;
import com.nordstrom.utility.GridLauncher;

@InitialPage(IOSApplicationMainView.class)
public class XCUITestGridTest extends JUnitBase {

    private static SeleniumGrid seleniumGrid = null;
    private final DriverPlugin plugin = new XCUITestPlugin();

    @Before
    public void beforeTest() {
        Assume.assumeTrue(OSInfo.getDefault().getType() == OSType.MACINTOSH);
        launchSeleniumGrid();
        ExamplePage.setHubAsTarget();
    }

    @Test
    public void testEditing() {
        IOSApplicationMainView mainView = getInitialPage();
        IOSApplicationEchoScreenView echoScreen = mainView.openEchoScreen();
        UUID uuid = UUID.randomUUID();
        echoScreen.setSavedMessage(uuid.toString());
        assertEquals(echoScreen.getSavedMessage(), uuid.toString());
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
