
[![Maven Central](https://img.shields.io/maven-central/v/com.nordstrom.ui-tools/local-grid-parent.svg)](https://central.sonatype.com/search?q=com.nordstrom.ui-tools+local-grid-parent&core=gav)

# local-grid-parent

Built on [Selenium Foundation](https://github.com/sbabcoc/Selenium-Foundation), this project produces a collection of modules that make launching **Selenium Grid** collections more manageable and modular.

## Implementation Strategy

Unlike other projects with similar objectives, `local-grid-parent` simplifies the process of launching **Selenium Grid** collections by leveraging the power of **Apache Maven** to marshal the dependencies required by the specified grid configuration. Instead of lumping everything together in a massive "uber-JAR", the submodules defined in this project declare the dependencies of hub and node servers, including a bit of glue to configure and launch these servers.

This approach yields several benefits:
* To install, just download the `local-grid-hub` JAR and run it:
  * `java -jar local-grid-hub-1.9.4.jar`
  * **NOTE**: The `maven-central` badge above links to the latest release.
* Because all dependencies are managed individually, remediation of defects and vulnerabilities is easy.
* Your installation gets the dependencies it needs, without getting bulked up with unused extras.

The task of launching the grid servers is performed by the **Maven Exec** plugin, which executes the Java command line application implemented in the **Main** class of the `local-grid-hub` module. The Maven project definition (POM) file for this module defines several profiles (one for each supported browser), and it's these profiles that activate the dependency declarations requires by their respective grid node servers.

## System Requirements

* As indicated above, `local-grid-parent` relies on **Apache Maven** to manage dependencies and execute the Java command line application that launches the specified grid collection. This project was developed with version _3.8.4_.
* To run pre-built `local-grid-parent` modules, you'll need a Java 8+ runtime environment.
* For each of the desktop browsers for which you'll be serving sessions, you'll need to install the corresponding driver executable. If the directory in which these executables are stored is on the PATH, the corresponding System properties will be set automatically.
* If you want to explore the code and build it locally, you'll need a `git` client to clone the repository and a Java 8+ development kit to build the project.

## Requirements for Appium

Unlike the other drivers supported by `local-grid-parent` which are implemented in Java, the "engines" provided by [Appium](https://appium.io) are implemented in NodeJS. To launch a **Selenium Grid** collection that includes Appium nodes, you'll need the following additional tools:
* Platform-specific Node Version Manager: The installation page for `npm` (below) provides links to recommended version managers.
* [NodeJS (node)](https://nodejs.org): Currently, I'm running version 17.5.0
* [Node Package Manager (npm)](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm): Currently, I'm running version 8.13.2
* [Node Process Manager (pm2)](https://pm2.io/): Currently, I'm running version 5.2.0
* [Appium](https://appium.io): Currently, I'm running version 1.22.3

Typically, these tools must be on the system file path. However, you can provide specific paths for each of these via **Selenium Foundation** settings:
* **NPM_BINARY_PATH**: If unspecified, the `PATH` is searched
* **NODE_BINARY_PATH**: If unspecified, the `NODE_BINARY_PATH` environment variable is checked; if this is undefined, the `PATH` is searched
* **PM2_BINARY_PATH**: If unspecified, the `PATH` is searched
* **APPIUM_BINARY_PATH**: If unspecified, the `APPIUM_BINARY_PATH` environment variable is checked; if this is undefined, the `PATH` is searched

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

## Augment an Active Grid

In addition to its ability to launch a Selenium Grid collection, `local-grid-parent` enables you to add nodes to an existing active Grid. This can either extend the set of supported browsers or provide additional sessions of browsers that are already supported. For example:

```bash
mvn exec:java -Pchrome # launch a grid providing Chrome sessions
mvn exec:java -Popera # attach a node providing Opera sessions
mvn exec:java -Pchrome # attach a second node providing Chrome
```

## Supported Profiles

| Profile | Plugin |
|:--:|--|
| `chrome` | com.nordstrom.automation.selenium.plugins.ChromePlugin |
| `edge` | com.nordstrom.automation.selenium.plugins.EdgePlugin |
| `espresso` | com.nordstrom.automation.selenium.plugins.EspressoPlugin |
| `firefox` | com.nordstrom.automation.selenium.plugins.FirefoxPlugin |
| `htmlunit` | com.nordstrom.automation.selenium.plugins.HtmlUnitPlugin |
| `mac2` | com.nordstrom.automation.selenium.plugins.Mac2Plugin |
| `opera` | com.nordstrom.automation.selenium.plugins.OperaPlugin |
| `phantomjs` | com.nordstrom.automation.selenium.plugins.PhantomJsPlugin |
| `safari` | com.nordstrom.automation.selenium.plugins.SafariPlugin |
| `uiautomator2` | com.nordstrom.automation.selenium.plugins.UiAutomator2Plugin |
| `windows` | com.nordstrom.automation.selenium.plugins.WindowsPlugin |
| `xcuitest` | com.nordstrom.automation.selenium.plugins.XCUITestPlugin |

## Shut Down a Local Grid

To shut down an active local grid instance, specify the `-shutdown` argument:

```bash
mvn exec:java -Dexec.args="-shutdown"
```
**NOTE**: Appium local grid nodes will be shut down by this command, but the `pm2` process manager will remain active. This is due to its meager resource consumption and the possibility that it may be managing other processes.

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

## Running the **local-grid-parent** Unit Tests

#### From command line...

The easiest way to run the unit tests is from the command line. From the root folder of each `local-grid-parent` node module (e.g. - `local-espresso-node`):

```bash
mvn test -DskipTests=false
```

> When running the unit tests, be sure that you don't have a `settings.properties` file in your user "home" folder, as this will conflict with the settings provided with each node module project and may cause the tests to fail.

#### From Eclipse IDE...

If you wish to run the unit tests from within Eclipse, you'll need to create a **run configuration** for the desired test class or method that activates the **JUnit Foundation** java agent. On the `Arguments` tab:

| VM arguments: |
|:---|
| -javaagent:${env_var:M2_REPO}/repository/com/nordstrom/tools/junit-foundation/17.0.3/junit-foundation-17.0.3.jar |

The value assigned to the `javaagent` argument above assumes that you've defined an **M2_REPO** environment variable that specifies the path to your Maven `.m2` folder and that you have **JUnit Foundation** version `17.0.3` installed. This should be the case if you've built and installed the `local-grid-parent` project. The Java agent of **JUnit Foundation** creates an augmented version of JUnit 4 that provides the test lifecycle notifications that enable **Selenium Foundation** to manage the local Grid instance and driver sessions used by the tests themselves.
 
Note that the `local-grid-parent` Maven project defines the `javaagent` command line argument in the configuration for the **Surefire** plugin, which is why you don't need to specify this in any form when running from the command line.

## Notes

The ports used by the node servers that supply browser sessions are auto-selected via the [PortProber.findFreePort()](https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/net/PortProber.html#findFreePort--) method of the **`selenium-remote-driver`** library.

Specification of a browser profile implicitly adds the corresponding `plugins` option. If you launch a local grid with no specified profiles, the hub runs as a servlet container.

Unless disabled with the `noRedirect` option, **`local-grid-hub`** redirects the output of the hub and node servers to log files in a `logs` folder under the current working directory. Each log file contains the output from a single launch of its associated server. Log file names are auto-incremented to avoid overwriting or appending to the output of previous launches.

* `grid-hub*.log` for hub server output
* `grid-node*.log` for node server output

The default output folder can be overridden with the `logsFolder` option, specifying either absolute or relative path. If a relative path is specified, or the default ("logs") is accepted, logs are written to a sub-folder of the current working directory, which can be overridden with the `workingDir` option.

> Written with [StackEdit](https://stackedit.io/).
