package com.nordstrom.automation;

import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.plugins.FirefoxPlugin;
import com.nordstrom.utility.AbstractGridTest;

public class FirefoxGridTest extends AbstractGridTest {
    
    private DriverPlugin plugin = new FirefoxPlugin();

    @Override
    public DriverPlugin getPlugin() {
        return plugin;
    }
    
}
