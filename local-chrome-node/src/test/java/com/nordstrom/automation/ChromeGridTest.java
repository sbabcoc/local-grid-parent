package com.nordstrom.automation;

import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.plugins.ChromePlugin;
import com.nordstrom.utility.AbstractGridTest;

public class ChromeGridTest extends AbstractGridTest {

    private DriverPlugin plugin = new ChromePlugin();

    @Override
    public DriverPlugin getPlugin() {
        return plugin;
    }
}
