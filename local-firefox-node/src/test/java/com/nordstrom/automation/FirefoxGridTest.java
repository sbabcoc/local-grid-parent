package com.nordstrom.automation;

import java.io.File;
import java.util.Objects;

import org.junit.Ignore;
import org.junit.Test;

import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.plugins.FirefoxPlugin;
import com.nordstrom.automation.selenium.utility.BinaryFinder;
import com.nordstrom.utility.AbstractGridTest;
import com.nordstrom.utility.GridLauncher;

public class FirefoxGridTest extends AbstractGridTest {
    
    private static final String PATH_PROPERTY = "webdriver.gecko.driver";
    
    @Test
    @Ignore
    @Override
    public void testShadowRootByLocator() { }

    @Test
    @Ignore
    @Override
    public void testShadowRootByElement() { }
    
    @Test
    @Ignore
    @Override
    public void testShadowRootList() { }
    
    @Test
    @Ignore
    @Override
    public void testShadowRootMap() { }
    
    public static SeleniumGrid launchGrid() {
        File driverPath = Objects.requireNonNull(
                BinaryFinder.findBinary("geckodriver", PATH_PROPERTY, null, null),
                "Executable 'geckodriver' not found");
        System.setProperty(PATH_PROPERTY, driverPath.getAbsolutePath());
        return GridLauncher.launch(new FirefoxPlugin());
    }
    
}
