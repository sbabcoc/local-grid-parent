package com.nordstrom.automation;

import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.plugins.PhantomJsPlugin;
import com.nordstrom.utility.AbstractGridTest;

public class PhantomJsGridTest extends AbstractGridTest {

    private DriverPlugin plugin = new PhantomJsPlugin();

    @Override
    public DriverPlugin getPlugin() {
        return plugin;
    }
}
