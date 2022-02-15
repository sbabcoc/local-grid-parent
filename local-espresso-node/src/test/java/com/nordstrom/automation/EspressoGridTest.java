package com.nordstrom.automation;

import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.plugins.EspressoPlugin;
import com.nordstrom.utility.AbstractGridTest;
import com.nordstrom.utility.GridLauncher;

public class EspressoGridTest extends AbstractGridTest {
    
    public static SeleniumGrid launchGrid() {
        return GridLauncher.launch(new EspressoPlugin());
    }

}
