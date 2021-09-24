package com.nordstrom.automation;

import java.util.Objects;

import org.junit.Ignore;
import org.openqa.selenium.os.CommandLine;

import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.plugins.FirefoxPlugin;
import com.nordstrom.utility.AbstractGridTest;
import com.nordstrom.utility.GridLauncher;

@Ignore
public class FirefoxGridTest extends AbstractGridTest {
    
    @SuppressWarnings("deprecation")
    public static SeleniumGrid launchGrid() {
        String driverPath =
                Objects.requireNonNull(CommandLine.find("geckodriver"), "Executable 'geckodriver' not found");
        System.setProperty("webdriver.gecko.driver", driverPath);
        return GridLauncher.launch(new FirefoxPlugin());
    }

}
