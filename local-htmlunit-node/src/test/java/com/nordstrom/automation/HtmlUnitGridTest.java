package com.nordstrom.automation;

import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.plugins.HtmlUnitPlugin;
import com.nordstrom.utility.AbstractGridTest;
import com.nordstrom.utility.GridLauncher;

public class HtmlUnitGridTest extends AbstractGridTest {
    
    public static SeleniumGrid launchGrid() {
        return GridLauncher.launch(new HtmlUnitPlugin());
    }

}
