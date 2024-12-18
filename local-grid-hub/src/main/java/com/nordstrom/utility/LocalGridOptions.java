package com.nordstrom.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;
import com.nordstrom.automation.selenium.AbstractSeleniumConfig.SeleniumSettings;
import com.nordstrom.automation.selenium.exceptions.GridServerLaunchFailedException;

public class LocalGridOptions {

    @Parameter(names = "-plugins", description = "Path-delimited list of fully-qualified node plugin classes")
    private List<String> plugins = new ArrayList<>();

    @Parameter(names = "-port", description = "Port for local hub server")
    private Integer port;

    @Parameter(names = "-gridServlets", description = "Comma-delimited list of fully-qualified servlet classes to install")
    private String gridServlets;

    @Parameter(names = "-shutdown", description = "Shutdown active local Grid")
    private boolean shutdown = false;

    @Parameter(names = "-workingDir", description = "Working directory for servers", converter = PathConverter.class)
    private Path workingDir;

    @Parameter(names = "-logsFolder", description = "Server output logs folder", converter = PathConverter.class)
    private Path logsFolder;

    @Parameter(names = "-noRedirect", description = "Disable server output redirection")
    private Boolean noRedirect;

    @Parameter(names = { "-help", "-?" }, description = "Display supported options and exit", help = true)
    private boolean help = false;

    public Integer getPort() {
        return port;
    }

    public List<String> getPlugins() {
        return plugins;
    }

    public boolean doShutdown() {
        return shutdown;
    }

    public Path getWorkingDir() {
        return workingDir;
    }

    public Path getLogsFolder() {
        return logsFolder;
    }

    public boolean noRedirect() {
        return (noRedirect != null) ? noRedirect : false;
    }

    public void setHelp() {
        help = true;
    }

    public boolean isHelp() {
        return help;
    }

    public void injectSettings() {
        String workingFolder = System.getProperty("user.dir");
        
        if ( ! plugins.isEmpty()) {
            System.setProperty(SeleniumSettings.GRID_PLUGINS.key(), String.join(File.pathSeparator, plugins));
        }
        
        if (port != null) {
            System.setProperty(SeleniumSettings.HUB_PORT.key(), port.toString());
        }
        if (gridServlets != null) {
            System.setProperty(SeleniumSettings.GRID_SERVLETS.key(), gridServlets);
        }
        if (workingDir != null) {
            workingFolder = workingDir.toString();
            System.setProperty(SeleniumSettings.GRID_WORKING_DIR.key(), workingFolder);
        }
        if (logsFolder != null) {
            System.setProperty(SeleniumSettings.GRID_LOGS_FOLDER.key(), logsFolder.toString());
        }
        if (noRedirect != null) {
            System.setProperty(SeleniumSettings.GRID_NO_REDIRECT.key(), noRedirect.toString());
        }
        Path workingPath = Paths.get(workingFolder);
        try {
            if (!workingPath.toFile().exists()) {
                Files.createDirectories(workingPath);
            }
        } catch (IOException e) {
            throw new GridServerLaunchFailedException("parent", e);
        }
    }
}
