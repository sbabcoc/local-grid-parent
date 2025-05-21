package com.nordstrom.utility;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.nordstrom.automation.selenium.AbstractSeleniumConfig.SeleniumSettings;
import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.SeleniumConfig;
import com.nordstrom.automation.selenium.core.SeleniumGrid;

/**
 * This class implements a launcher for a local <b>Selenium Grid</b> instance.
 */
public class GridLauncher {
    
    private GridLauncher() {
        throw new AssertionError("GridLauncher is a static constants class that cannot be instantiated");
    }

    /**
     * Launch a local <b>Selenium Grid</b> instance that provides sessions of the indicate browser.
     * <p>
     * <b>NOTE</b>: The new grid instance is injected into the current <b>Selenium Foundation</b> configuration. <br>
     * The following {@link SeleniumSettings properties} are directly updated by this method:
     * <ul>
     *     <li>{@link SeleniumSettings#HUB_HOST HUB_HOST}: URL for the Selenium Grid endpoint</li>
     *     <li>{@link SeleniumSettings#HUB_PORT HUB_PORT}: port of the local Selenium Grid hub</li>
     * </ul>
     * 
     * @param driverPlugins {@link DriverPlugin} that provides grid node configuration
     * @return {@link SeleniumGrid} object that represents the new grid instance
     */
    public static SeleniumGrid launch(DriverPlugin... driverPlugins) {
        SeleniumConfig config = SeleniumConfig.getConfig();
        String plugins = Arrays.stream(driverPlugins).map(p -> p.getClass().getName()).collect(Collectors.joining(File.pathSeparator));
        System.setProperty(SeleniumSettings.GRID_PLUGINS.key(), plugins);
        
        synchronized(SeleniumGrid.class) {
            return config.getSeleniumGrid();
        }
    }
}
