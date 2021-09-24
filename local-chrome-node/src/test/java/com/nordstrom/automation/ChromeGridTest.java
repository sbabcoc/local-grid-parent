package com.nordstrom.automation;

import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.plugins.ChromePlugin;
import com.nordstrom.utility.AbstractGridTest;
import com.nordstrom.utility.GridLauncher;

public class ChromeGridTest extends AbstractGridTest {
    
    public static SeleniumGrid launchGrid() {
        return GridLauncher.launch(new ChromePlugin());
    }

}
