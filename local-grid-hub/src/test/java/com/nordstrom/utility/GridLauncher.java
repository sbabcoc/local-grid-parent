package com.nordstrom.utility;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.os.CommandLine;

import com.google.common.collect.ObjectArrays;
import com.google.common.io.Resources;
import com.nordstrom.automation.selenium.AbstractSeleniumConfig.SeleniumSettings;
import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.SeleniumConfig;
import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.common.file.PathUtils;
import com.nordstrom.common.jar.JarUtils;

/**
 * This class implements a launcher for a local <b>Selenium Grid</b> instance.
 */
public class GridLauncher {
    
    private static final String[] DEPENDENCY_CONTEXTS = {
            Main.class.getName(),
            "com.nordstrom.automation.settings.SettingsCore",
            "org.apache.commons.configuration2.Configuration",
            "org.slf4j.MDC",
            "ch.qos.logback.core.Context",
            "org.apache.commons.logging.Log",
            "org.apache.commons.beanutils.DynaBean",
            "org.apache.http.HttpRequest",
            "org.apache.http.client.HttpClient",
            "org.apache.commons.text.Builder"
            };

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
     * </ul>
     * 
     * @param driverPlugins {@link DriverPlugin} that provides grid node configuration
     * @return {@link SeleniumGrid} object that represents the new grid instance
     */
    public static SeleniumGrid launch(DriverPlugin... driverPlugins) {
        List<String> argsList = new ArrayList<>();
        List<String> plugins = new ArrayList<>();
        
        SeleniumConfig config = SeleniumConfig.getConfig();
        String[] dependencyContexts = 
                ObjectArrays.concat(DEPENDENCY_CONTEXTS, config.getDependencyContexts(), String.class);
        
        for (DriverPlugin driverPlugin : driverPlugins) {
            String capabilities = driverPlugin.getCapabilities(config);
            dependencyContexts = 
                    ObjectArrays.concat(dependencyContexts, driverPlugin.getDependencyContexts(), String.class);
            
            plugins.add(driverPlugin.getClass().getName());
            
            // propagate Java System properties
            for (String name : driverPlugin.getPropertyNames(capabilities)) {
                String value = System.getProperty(name);
                if (value != null) {
                    argsList.add("-D" + name + "=" + value);
                }
            }
        }
        
        // get dependency context paths
        List<String> contextPaths = JarUtils.getContextPaths(dependencyContexts);
        // extract classpath specification
        String classPath = contextPaths.remove(0);
        // for each specified Java agent...
        // ... specify a 'javaagent' argument
        argsList.addAll(contextPaths);
        
        try {
            // find settings file for Selenium Foundation
            URL settings = Resources.getResource("settings.properties");
            // get path to folder that contains this file
            Path folder = Paths.get(settings.toURI()).getParent();
            // prepend resource folder path to class path
            classPath = folder.toString() + File.pathSeparator + classPath;
        } catch (Exception e) {
            // don't die trying
        }
        
        // specify Java class path
        argsList.add("-cp");
        argsList.add(classPath);        
        argsList.add(Main.class.getName());
        argsList.add("-port");
        argsList.add(Integer.toString(PortProber.findFreePort()));
        
        if ( ! plugins.isEmpty()) {
            argsList.add("-plugins");
            argsList.add(String.join(File.pathSeparator, plugins));
        }
        
        String executable = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        CommandLine process = new CommandLine(executable, argsList.toArray(new String[0]));
        process.setEnvironmentVariable("PATH", PathUtils.getSystemPath());
        process.execute();
        
        String[] lines = process.getStdOut().split("[\\n\\r]+");
        String strUri = lines[lines.length - 1];
        
        URI hubUri;
        try {
            hubUri = new URI(strUri);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Launch failed" + getDetails(lines));
        }
        
        synchronized(SeleniumGrid.class) {
            System.setProperty(SeleniumSettings.HUB_HOST.key(), hubUri.toString());
            System.setProperty(SeleniumSettings.HUB_PORT.key(), Integer.toString(hubUri.getPort()));
            return config.getSeleniumGrid();
        }
    }
    
    private static String getDetails(String[] lines) {
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("Exception")) {
                StringBuilder details = new StringBuilder(":");
                for (; i < lines.length; i++) {
                    details.append('\n').append(lines[i]);
                }
                return details.toString();
            }
        }
        return "";
    }

}
