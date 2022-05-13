package com.nordstrom.automation;

import java.io.File;
import java.util.Objects;

import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.plugins.OperaPlugin;
import com.nordstrom.automation.selenium.utility.BinaryFinder;
import com.nordstrom.utility.AbstractGridTest;
import com.nordstrom.utility.GridLauncher;

public class OperaGridTest extends AbstractGridTest {
    
    private static final String PATH_PROPERTY = "webdriver.opera.driver";

    private DriverPlugin plugin = new OperaPlugin();

    @Override
    public SeleniumGrid launchGrid() {
        File driverPath = Objects.requireNonNull(
                BinaryFinder.findBinary("operadriver", PATH_PROPERTY, null, null),
                "Executable 'operadriver' not found");
        System.setProperty(PATH_PROPERTY, driverPath.getAbsolutePath());
        return GridLauncher.launch(plugin);
    }

    @Override
    public DriverPlugin getPlugin() {
        return plugin;
    }
    
}
