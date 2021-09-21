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

public class GridLauncher {
    
    public static SeleniumGrid start(DriverPlugin driverPlugin) {
        String[] dependencyContexts =
                ObjectArrays.concat(LocalGridOptions.class.getName(), driverPlugin.getDependencyContexts());
        
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
