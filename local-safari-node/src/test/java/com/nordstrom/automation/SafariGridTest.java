package com.nordstrom.automation;

import org.junit.Assume;
import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.plugins.SafariPlugin;
import com.nordstrom.common.file.OSInfo;
import com.nordstrom.common.file.OSInfo.OSType;
import com.nordstrom.utility.AbstractGridTest;

public class SafariGridTest extends AbstractGridTest {

    private final DriverPlugin plugin = new SafariPlugin();

    @Override
    public void beforeTest() {
        Assume.assumeTrue(OSInfo.getDefault().getType() == OSType.MACINTOSH);
        super.beforeTest();
    }
    
    @Override
    public DriverPlugin getPlugin() {
        return plugin;
    }
}
