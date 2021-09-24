package com.nordstrom.utility;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.os.CommandLine;

import com.google.common.collect.ObjectArrays;
import com.nordstrom.automation.selenium.AbstractSeleniumConfig.SeleniumSettings;
import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.SeleniumConfig;
import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.common.jar.JarUtils;

/**
 * This class implements a launcher for a local <b>Selenium Grid</b> instance.
 */
public class GridLauncher {
    
    private GridLauncher() {
        throw new AssertionError("GridLauncher is a static constants class that cannot be instantiated");
    }

    /**
     * Launch a local <b>Selenium Grid</b> instance that provides sessions of the indicate browser.
     * <p>
     * <b>NOTE</b>: The new grid instance is injected into the current <b>Selenium Foundation</b> configuration. <br>
     * The following {@link SeleniumSettings properties} are directly updated by this method:
     * <ul>
     *     <li>{@link SeleniumSettings#HUB_HOST HUB_HOST}: URL for the Selenium Grid endpoint</li>
     *     <li>{@link SeleniumSettings#HUB_PORT HUB_PORT}: port of the local Selenium Grid hub</li>
     *     <li>{@link SeleniumSettings#BROWSER_NAME BROWSER_NAME}: browser name for new sessions</li>
     * </ul>
     * 
     * @param driverPlugin {@link DriverPlugin} that provides grid node configuration
     * @return {@link SeleniumGrid} object that represents the new grid instance
     */
    public static SeleniumGrid launch(DriverPlugin driverPlugin) {
        String[] dependencyContexts =
                ObjectArrays.concat(Main.class.getName(), driverPlugin.getDependencyContexts());
        
        List<String> argsList = new ArrayList<>();
        
        // propagate Java System properties
        for (String name : driverPlugin.getPropertyNames()) {
            String value = System.getProperty(name);
            if (value != null) {
                argsList.add("-D" + name + "=" + value);
            }
        }
        
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
        URI hubUri = URI.create(lines[lines.length - 1]);
        
        synchronized(SeleniumGrid.class) {
            System.setProperty(SeleniumSettings.HUB_HOST.key(), hubUri.toString());
            System.setProperty(SeleniumSettings.HUB_PORT.key(), Integer.toString(hubUri.getPort()));
            System.setProperty(SeleniumSettings.BROWSER_NAME.key(), driverPlugin.getBrowserName());
            return SeleniumConfig.getConfig().getSeleniumGrid();
        }
    }

}
