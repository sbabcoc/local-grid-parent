package com.nordstrom.utility;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.configuration2.ex.ConfigurationException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.nordstrom.automation.selenium.SeleniumConfig;
import com.nordstrom.automation.selenium.AbstractSeleniumConfig.SeleniumSettings;
import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.core.GridUtility;
import com.nordstrom.automation.selenium.core.LocalSeleniumGrid;
import com.nordstrom.automation.selenium.core.LocalSeleniumGrid.LocalGridServer;
import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.core.GridServer;

public class Main {
    public static void main(String... args) throws ConfigurationException, IOException, InterruptedException,
            TimeoutException, InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        
        LocalGridOptions opts = new LocalGridOptions();
        JCommander parser = new JCommander(opts);
        try {
            parser.parse(args);
        } catch (ParameterException e) {
            parser.getConsole().println(e.getMessage());
            opts.setHelp();
        }

        if (opts.isHelp()) {
            parser.setProgramName("local-grid-hub");
            parser.usage();
            return;
        }

        opts.injectSettings();

        SeleniumConfig config = new SeleniumConfig();

        URL hubUrl = config.getHubUrl();
        if (hubUrl == null) {
            int hubPort = config.getInt(SeleniumSettings.HUB_PORT.key());
            String hostStr = "http://" + GridUtility.getLocalHost() + ":" + hubPort + "/wd/hub";
            hubUrl = new URL(hostStr);
        }

        boolean isActive = GridServer.isHubActive(hubUrl);

        if (opts.doShutdown()) {
            if (isActive) {
                parser.getConsole().println("Shutting down active grid at: " + hubUrl.toString());
                SeleniumGrid.create(config, hubUrl).shutdown(true);
            }
            return;
        } else if (isActive) {
            parser.getConsole().println("Adding local nodes to grid at: " + hubUrl.toString());
            SeleniumGrid grid = SeleniumGrid.create(config, hubUrl);
            GridServer hubServer = grid.getHubServer();
            List<GridServer> nodeServers = new ArrayList<>();
            
            for (String pluginName : opts.getPlugins()) {
                Object plugin = Class.forName(pluginName).getConstructor().newInstance();
                nodeServers.add(DriverPlugin.class.cast(plugin).create(config, hubUrl));
            }
            
            for (GridServer nodeServer : nodeServers) {
                ((LocalGridServer) nodeServer).start();
            }
            
            LocalSeleniumGrid.awaitGridReady(hubServer, nodeServers);
            SeleniumGrid.create(config, hubUrl);
        } else {
            parser.getConsole().println("Creating new local grid at: " + hubUrl.toString());
            LocalSeleniumGrid grid = (LocalSeleniumGrid) SeleniumGrid.create(config, hubUrl);
            hubUrl = grid.getHubServer().getUrl();
            grid.activate();
        }
        parser.getConsole().println(hubUrl.toString());
    }

}
