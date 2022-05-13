package com.nordstrom.automation;

import java.io.File;
import java.util.Objects;

import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.plugins.FirefoxPlugin;
import com.nordstrom.automation.selenium.utility.BinaryFinder;
import com.nordstrom.utility.AbstractGridTest;
import com.nordstrom.utility.GridLauncher;

public class FirefoxGridTest extends AbstractGridTest {
    
    private static final String PATH_PROPERTY = "webdriver.gecko.driver";

    private DriverPlugin plugin = new FirefoxPlugin();

    @Override
    public SeleniumGrid launchGrid() {
        File driverPath = Objects.requireNonNull(
                BinaryFinder.findBinary("geckodriver", PATH_PROPERTY, null, null),
                "Executable 'geckodriver' not found");
        System.setProperty(PATH_PROPERTY, driverPath.getAbsolutePath());
        return GridLauncher.launch(plugin);
    }
    
    @Override
    public DriverPlugin getPlugin() {
        return plugin;
    }
    
}
