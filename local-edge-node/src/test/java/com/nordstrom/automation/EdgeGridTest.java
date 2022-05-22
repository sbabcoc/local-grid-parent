package com.nordstrom.automation;

import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.plugins.EdgePlugin;
import com.nordstrom.utility.AbstractGridTest;

public class EdgeGridTest extends AbstractGridTest {

    private DriverPlugin plugin = new EdgePlugin();

    @Override
    public DriverPlugin getPlugin() {
        return plugin;
    }

}
