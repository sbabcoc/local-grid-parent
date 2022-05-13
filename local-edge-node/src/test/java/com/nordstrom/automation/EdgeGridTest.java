package com.nordstrom.automation;

import java.io.File;
import java.util.Objects;

import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.plugins.EdgePlugin;
import com.nordstrom.automation.selenium.utility.BinaryFinder;
import com.nordstrom.utility.AbstractGridTest;
import com.nordstrom.utility.GridLauncher;

public class EdgeGridTest extends AbstractGridTest {

    private static final String PATH_PROPERTY = "webdriver.edge.driver";
    
    private DriverPlugin plugin = new EdgePlugin();

    @Override
    public SeleniumGrid launchGrid() {
        File driverPath = Objects.requireNonNull(
                BinaryFinder.findBinary("msedgedriver", PATH_PROPERTY, null, null),
                "Executable 'msedgedriver' not found");
        System.setProperty(PATH_PROPERTY, driverPath.getAbsolutePath());
        return GridLauncher.launch(plugin);
    }

    @Override
    public DriverPlugin getPlugin() {
        return plugin;
    }

}
