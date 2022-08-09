package com.nordstrom.automation;

import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.plugins.OperaPlugin;
import com.nordstrom.utility.AbstractGridTest;

public class OperaGridTest extends AbstractGridTest {
    
    private final DriverPlugin plugin = new OperaPlugin();

    @Override
    public DriverPlugin getPlugin() {
        return plugin;
    }
    
}
