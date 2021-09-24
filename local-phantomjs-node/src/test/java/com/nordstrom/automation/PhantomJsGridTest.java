package com.nordstrom.automation;

import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.plugins.PhantomJsPlugin;
import com.nordstrom.utility.AbstractGridTest;
import com.nordstrom.utility.GridLauncher;

public class PhantomJsGridTest extends AbstractGridTest {
    
    public static SeleniumGrid launchGrid() {
        return GridLauncher.launch(new PhantomJsPlugin());
    }

}
