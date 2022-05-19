package com.nordstrom.utility;

import com.nordstrom.automation.selenium.core.SeleniumGrid;

public class GridHubTest {

    public static SeleniumGrid launchGrid() {
        return GridLauncher.launch(null);
    }

}
