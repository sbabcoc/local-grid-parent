package com.nordstrom.automation;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.nordstrom.automation.selenium.annotations.InitialPage;
import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.examples.ExamplePage;
import com.nordstrom.automation.selenium.plugins.ChromePlugin;
import com.nordstrom.utility.AbstractGridTest;
import com.nordstrom.utility.GridLauncher;

@InitialPage(ExamplePage.class)
public class ChromeGridTest extends AbstractGridTest {
    
    private static SeleniumGrid seleniumGrid = null;
    
    @BeforeClass
    public static void launch() {
        seleniumGrid = GridLauncher.start(new ChromePlugin());
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
