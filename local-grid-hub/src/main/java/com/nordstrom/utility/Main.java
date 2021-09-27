package com.nordstrom.utility;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeoutException;

import org.apache.commons.configuration2.ex.ConfigurationException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.nordstrom.automation.selenium.SeleniumConfig;
import com.nordstrom.automation.selenium.AbstractSeleniumConfig.SeleniumSettings;
import com.nordstrom.automation.selenium.core.GridUtility;
import com.nordstrom.automation.selenium.core.SeleniumGrid;

public class Main {
    public static void main(String... args)
            throws InterruptedException, ConfigurationException, IOException, TimeoutException {
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
            String hostStr = "http://" + GridUtility.getLocalHost() + ":" + hubPort;
            hubUrl = new URL(hostStr);
        }

        boolean isActive = GridUtility.isHubActive(hubUrl);

        if (opts.doShutdown() && isActive) {
            SeleniumGrid.create(config, hubUrl).shutdown(true);
        } else {
            if (!isActive) {
                SeleniumGrid grid = SeleniumGrid.create(config, hubUrl);
                hubUrl = grid.getHubServer().getUrl();
            }
            parser.getConsole().println(hubUrl.toString());
        }
    }
}
