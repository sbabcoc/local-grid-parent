package com.nordstrom.automation;

import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.plugins.HtmlUnitPlugin;
import com.nordstrom.utility.AbstractGridTest;

public class HtmlUnitGridTest extends AbstractGridTest {

    private DriverPlugin plugin = new HtmlUnitPlugin();

    @Override
    public DriverPlugin getPlugin() {
        return plugin;
    }
}
