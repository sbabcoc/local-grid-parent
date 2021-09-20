package com.nordstrom.automation;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.os.CommandLine;
import com.google.common.collect.ObjectArrays;
import com.nordstrom.automation.selenium.AbstractSeleniumConfig.SeleniumSettings;
import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.SeleniumConfig;
import com.nordstrom.automation.selenium.annotations.InitialPage;
import com.nordstrom.automation.selenium.core.DriverManager;
import com.nordstrom.automation.selenium.examples.ExamplePage;
import com.nordstrom.automation.selenium.junit.JUnitBase;
import com.nordstrom.automation.selenium.model.Page;
import com.nordstrom.automation.selenium.plugins.PhantomJsPlugin;
import com.nordstrom.common.jar.JarUtils;
import com.nordstrom.utility.LocalGridOptions;
import com.nordstrom.utility.Main;

@InitialPage(ExamplePage.class)
public class GridTest extends JUnitBase {
    
    private static final DriverPlugin driverPlugin;
    private static final URI hubUri;
    private static final URI targetUri;
    
    static {
        driverPlugin = new PhantomJsPlugin();
        
        String[] dependencyContexts =
                ObjectArrays.concat(LocalGridOptions.class.getName(), driverPlugin.getDependencyContexts());
        
        List<String> argsList = new ArrayList<>();
        
        // get dependency context paths
        List<String> contextPaths = JarUtils.getContextPaths(dependencyContexts);
        // extract classpath specification
        String classPath = contextPaths.remove(0);
        // for each specified Java agent...
        for (String agentSpec : contextPaths) {
            // ... specify a 'javaagent' argument
            argsList.add(agentSpec);
        }
        
        // specify Java class path
        argsList.add("-cp");
        argsList.add(classPath);        
        argsList.add(Main.class.getName());
        argsList.add("-port");
        argsList.add(Integer.toString(PortProber.findFreePort()));
        argsList.add("-plugins");
        argsList.add(driverPlugin.getClass().getName());
        
        String executable = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        CommandLine process = new CommandLine(executable, argsList.toArray(new String[0]));
        process.execute();
        
        String[] lines = process.getStdOut().split("[\\n\\r]+");
        hubUri = URI.create(lines[lines.length - 1]);
        
        System.setProperty(SeleniumSettings.HUB_HOST.key(), hubUri.toString());
        System.setProperty(SeleniumSettings.HUB_PORT.key(), Integer.toString(hubUri.getPort()));
        System.setProperty(SeleniumSettings.BROWSER_NAME.key(), driverPlugin.getBrowserName());
        
        targetUri = ExamplePage.setHubAsTarget();
    }
    
    @Before
    public void beforeTest() {
        DriverManager.injectDriver(this, null);
    }

    @Test
    public void test() {
        InitialPage initialPage = this.getClass().getAnnotation(InitialPage.class);
        ExamplePage page = Page.openInitialPage(initialPage, getDriver(), targetUri);
        assertEquals(ExamplePage.TITLE, page.getTitle());
    }
    
    @After
    public void afterTest() throws InterruptedException {
        DriverManager.closeDriver(this);
        SeleniumConfig.getConfig().getSeleniumGrid().shutdown(true);
    }

}
