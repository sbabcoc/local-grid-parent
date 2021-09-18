package com.nordstrom.automation;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.os.CommandLine;
import com.google.common.collect.ObjectArrays;
import com.nordstrom.automation.selenium.SeleniumConfig;
import com.nordstrom.automation.selenium.AbstractSeleniumConfig.SeleniumSettings;
import com.nordstrom.automation.selenium.annotations.InitialPage;
import com.nordstrom.automation.selenium.core.GridUtility;
import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.examples.ExamplePage;
import com.nordstrom.automation.selenium.interfaces.DriverProvider;
import com.nordstrom.automation.selenium.junit.JUnitBase;
import com.nordstrom.automation.selenium.plugins.PhantomJsPlugin;
import com.nordstrom.common.base.UncheckedThrow;
import com.nordstrom.common.jar.JarUtils;
import com.nordstrom.utility.LocalGridOptions;

@InitialPage(ExamplePage.class)
public class GridTest extends JUnitBase implements DriverProvider {
    
    private static final SeleniumConfig config;
    private static final PhantomJsPlugin driverPlugin;
    private static final SeleniumGrid grid;
    
    static {
        driverPlugin = new PhantomJsPlugin();
        config = SeleniumConfig.getConfig();
        
        String[] dependencyContexts =
                ObjectArrays.concat(LocalGridOptions.class.getName(), driverPlugin.getDependencyContexts());
        
        String[] argsList = new String[] {
                "-cp", JarUtils.getClasspath(dependencyContexts),
                "com.nordstrom.utility.Main",
                "-port", Integer.toString(PortProber.findFreePort()),
                "-plugins", driverPlugin.getClass().getName()
        };
        
        String executable = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        CommandLine process = new CommandLine(executable, argsList);
        process.execute();
        
        try {
            String stdOut = process.getStdOut();
            String[] lines = stdOut.split("[\\n\\r]+");
            String hubHost = lines[lines.length - 1];
            URI hubUri = URI.create(hubHost);
            config.setTargetUri(hubUri);
            URL hubUrl = hubUri.toURL();
            grid = SeleniumGrid.create(config, hubUrl);
            System.setProperty(SeleniumSettings.HUB_HOST.key(), hubUrl.toString());
            System.setProperty(SeleniumSettings.HUB_PORT.key(), Integer.toString(hubUrl.getPort()));
        } catch (IOException | InterruptedException | TimeoutException e) {
            throw UncheckedThrow.throwUnchecked(e);
        }
    }

    @Test
    public void test() {
        ExamplePage page = this.getInitialPage();
        assertEquals(ExamplePage.TITLE, page.getTitle());
    }

    @Override
    public WebDriver provideDriver(Method method) {
        Capabilities capabilities = grid.getPersonality(config, driverPlugin.getBrowserName());
        return GridUtility.getDriver(grid.getHubServer().getUrl(), capabilities);
    }
    
}