package com.nordstrom.automation;

import java.util.Objects;

import org.openqa.selenium.os.CommandLine;

import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.plugins.EdgePlugin;
import com.nordstrom.utility.AbstractGridTest;
import com.nordstrom.utility.GridLauncher;

public class EdgeGridTest extends AbstractGridTest {
    
    @SuppressWarnings("deprecation")
    public static SeleniumGrid launchGrid() {
        String driverPath =
                Objects.requireNonNull(CommandLine.find("msedgedriver"), "Executable 'msedgedriver' not found");
        System.setProperty("webdriver.edge.driver", driverPath);
        return GridLauncher.launch(new EdgePlugin());
    }

}
