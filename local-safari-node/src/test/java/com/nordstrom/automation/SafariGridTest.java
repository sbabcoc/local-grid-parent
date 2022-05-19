package com.nordstrom.automation;

import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.plugins.SafariPlugin;
import com.nordstrom.utility.AbstractGridTest;

public class SafariGridTest extends AbstractGridTest {

    private DriverPlugin plugin = new SafariPlugin();

    @Override
    public DriverPlugin getPlugin() {
        return plugin;
    }
}
