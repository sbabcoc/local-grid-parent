package com.nordstrom.automation;

import java.util.Objects;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.os.CommandLine;

import com.nordstrom.automation.selenium.annotations.InitialPage;
import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.examples.ExamplePage;
import com.nordstrom.automation.selenium.plugins.EdgePlugin;
import com.nordstrom.utility.AbstractGridTest;
import com.nordstrom.utility.GridLauncher;

@InitialPage(ExamplePage.class)
public class EdgeGridTest extends AbstractGridTest {
    
    private static SeleniumGrid seleniumGrid = null;
    
    @BeforeClass
    @SuppressWarnings("deprecation")
    public static void launch() {
        String driverPath =
                Objects.requireNonNull(CommandLine.find("msedgedriver"), "Executable 'msedgedriver' not found");
        System.setProperty("webdriver.edge.driver", driverPath);
        seleniumGrid = GridLauncher.start(new EdgePlugin());
    }
    
    public static SeleniumGrid getSeleniumGrid() {
        return seleniumGrid;
    }
    
    @AfterClass
    public static void finish() throws InterruptedException {
        try {
            seleniumGrid.shutdown(true);
        } finally {
            seleniumGrid = null;
        }
    }

}
