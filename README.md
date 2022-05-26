[![Maven Central](https://img.shields.io/maven-central/v/com.nordstrom.ui-tools/local-grid-hub.svg)](https://search.maven.org/search?q=g:com.nordstrom.ui-tools%20AND%20a:local-grid-hub&core=gav)

# local-grid-parent

Built on [Selenium Foundation](https://github.com/sbabcoc/Selenium-Foundation), this project produces a collection of modules that make launching **Selenium Grid** collections more manageable and modular.

## Implementation Strategy

Unlike other projects with similar objectives, `local-grid-parent` simplifies the process of launching **Selenium Grid** collections by leveraging the power of **Apache Maven** to marshal the dependencies required by the specified grid configuration. Instead of lumping everything together in a massive "uber-JAR", the submodules defined in this project declare the dependencies of hub and node servers, including a bit of glue to configure and launch these servers,

This approach yields several benefits:
* To install, just download the `local-grid-hub` JAR and run it:
  * `java -jar local-grid-hub-1.1.0.jar`
  * **NOTE**: The `maven-central` badge above links to the latest release.
* Because all dependencies are managed individually, remediation of defects and vulnerabilities is easy.
* Your installation gets the dependencies it needs, without getting bulked up with unused extras.

The task of launching the grid servers is performed by the **Maven Exec** plugin, which executes the Java command line application implemented in the **Main** class of the `local-grid-hub` module. The Maven project definition (POM) file for this module defines several profiles (one for each supported browser), and it's these profiles that activate the dependency declarations requires by their respective grid node servers.

## System Requirements

* As indicated above, `local-grid-parent` relies on **Apache Maven** to manage dependencies and execute the Java command line application that launches the specified grid collection. This project was developed with version _3.8.4_.
* To run pre-built `local-grid-parent` modules, you'll need a Java 8+ runtime environment.
* For each of the desktop browsers for which you'll be serving sessions, you'll need to install the corresponding driver executable. If the directory in which these executables are stored is on the PATH, the corresponding System properties will be set automatically.
* If you want to explore the code and build it locally, you'll need a `git` client to clone the repository and a Java 8+ development kit to build the project.

## Launch a Local Grid

In one step, you can launch a Selenium Grid hub and a single node that supplies **HtmlUnit** browser sessions. From the directory that contains the `local-grid-hub` JAR and its extracted POM file:

```bash
mvn exec:java -Phtmlunit
```

Note the `-Phtmlunit` option on the preceding command. This specifies the inclusion of the **htmlunit** profile, which activates the dependencies required by this "headless" browser. It also adds a parameter to the internal command line that specifies activation of a node that provides **HtmlUnit** browser sessions.

To launch a grid that provides multiple browser types, specify multiple plugin profiles. The grid collection from this command  provides both **Chrome** and **Firefox** sessions:

```bash
mvn exec:java -Pchrome -Pfirefox
```

## Supported Profiles

| Profile | Plugin |
|--|--|
| `chrome` | com.nordstrom.automation.selenium.plugins.ChromePlugin |
| `edge` | com.nordstrom.automation.selenium.plugins.EdgePlugin |
| `firefox` | com.nordstrom.automation.selenium.plugins.FirefoxPlugin |
| `htmlunit` | com.nordstrom.automation.selenium.plugins.HtmlUnitPlugin |
| `opera` | com.nordstrom.automation.selenium.plugins.OperaPlugin |
| `phantomjs` | com.nordstrom.automation.selenium.plugins.PhantomJsPlugin
| `safari` | com.nordstrom.automation.selenium.plugins.SafariPlugin

## Shut Down a Local Grid

To shut down an active local grid instance, specify the `-shutdown` argument:

```bash
mvn exec:java -Dexec.args="-shutdown"
```

## Command Line Options

As show above, `local-grid-hub` accepts command line options via the `exec.args` property. Here's the list of supported options: 

* `-port` : specify port for local hub server (default = 4445)
* `-plugins` : path-delimited list of fully-qualified node plugin classes
* `-hubServlets` : comma-delimited list of fully-qualified servlet classes to install on the hub server
* `-nodeServlet` : comma-delimited list of fully-qualified servlet classes to install on the node servers
* `-workingDir` : working directory for servers
* `-logsFolder` : server output logs folder (default = "logs")
* `-noRedirect` : disable server output redirection (default = `false`)
* `-shutdown` : shut down active local Grid collection

For example, to add support for the hub status API, specify the corresponding servlets:

```bash
mvn exec:java -Dexec.args="-hubServlets org.openqa.grid.web.servlet.HubStatusServlet"
```

Navigating to the hub status path (`/grid/admin/HubStatusServlet`) on the grid will yield a result like this:

```
{
  "browserTimeout": 0,
  "capabilityMatcher": "com.nordstrom.automation.selenium.utility.RevisedCapabilityMatcher",
  "cleanUpCycle": 5000,
  "custom": {
  },
  "debug": false,
  "host": "192.168.254.20",
  "newSessionRequestCount": 0,
  "newSessionWaitTimeout": -1,
  "port": 4445,
  "registry": "org.openqa.grid.internal.DefaultGridRegistry",
  "role": "hub",
  "servlets": [
    "com.nordstrom.automation.selenium.servlet.ExamplePageServlet$FrameA_Servlet",
    "com.nordstrom.automation.selenium.servlet.ExamplePageServlet$FrameB_Servlet",
    "com.nordstrom.automation.selenium.servlet.ExamplePageServlet$FrameC_Servlet",
    "com.nordstrom.automation.selenium.servlet.ExamplePageServlet$FrameD_Servlet",
    "com.nordstrom.automation.selenium.servlet.ExamplePageServlet",
    "org.openqa.grid.web.servlet.HubStatusServlet"
  ],
  "slotCounts": {
    "free": 0,
    "total": 0
  },
  "success": true,
  "throwOnCapabilityNotPresent": true,
  "timeout": 300000,
  "withoutServlets": [
  ]
}
```

Notice that the hub configuration includes the **Selenium Foundation** example page servlets. These are available at path `/grid/admin/ExamplePageServlet` to provide a target for basic functionality testing, but you can omit them from your configuration with the `selenium.grid.examples` property:

```bash
mvn exec:java -Dselenium.grid.examples=false
```

## Selenium Settings

Because `local-grid-parent` is built on **Selenium Foundation**, all of the settings supported by this library are available for your grid configuration. The settings can be specified individually on the command line as demonstrated by the previous example, or you can specify them collectively in the corresponding settings files (e.g. - `settings.properties`). For details, check out the [Configuring Project Settings](https://github.com/sbabcoc/Selenium-Foundation/blob/master/docs/ConfiguringProjectSettings.md#introduction) page of the **Selenium Foundation** project.

## Notes

The ports used by the node servers that supply browser sessions are auto-selected via the [PortProber.findFreePort()](https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/net/PortProber.html#findFreePort--) method of the **`selenium-remote-driver`** library.

Specification of a browser profile implicitly adds the corresponding `plugins` option. If you launch a local grid with no specified profiles, the hub runs as a servlet container.

Unless disabled with the `noRedirect` option, **`local-grid-hub`** redirects the output of the hub and node servers to log files in a `logs` folder under the current working directory. Each log file contains the output from a single launch of its associated server. Log file names are auto-incremented to avoid overwriting or appending to the output of previous launches.

* `grid-hub*.log` for hub server output
* `grid-node*.log` for node server output

The default output folder can be overridden with the `logsFolder` option, specifying either absolute or relative path. If a relative path is specified, or the default ("logs") is accepted, logs are written to a sub-folder of the current working directory, which can be overridden with the `workingDir` option.

> Written with [StackEdit](https://stackedit.io/).
