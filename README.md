[![Maven Central](https://img.shields.io/maven-central/v/com.nordstrom.ui-tools/local-grid-parent.svg)](https://search.maven.org/search?q=g:com.nordstrom.ui-tools%20AND%20a:local-grid-parent&core=gav)

# local-grid-parent

This project produces a collection of modules that make launching **Selenium Grid** collections more manageable and modular.

## Implementation Strategy

Unlike other projects with similar objectives, `local-grid-parent` simplifies the process of launching **Selenium Grid** collections by leveraging the power of **Apache Maven** to marshal the dependencies required by the specified grid configuration. Instead of lumping everything together in a massive "uber-JAR", the submodules defined in this project declare the dependencies of hub and node servers, including a bit of glue to configure and launch these servers,

This approach yields several benefits:
* Basic installation of `local-grid-parent` is performed by cloning the project repository.
* Because all dependencies are managed individually, remediation of defects and vulnerabilities is easy.
* Your installation gets the dependencies it needs, without getting bulked up with unused extras.

The task of launching the grid servers is performed by the **Maven Exec** plugin, which executes the Java command line application implemented in the **Main** class of the `local-grid-hub` module. The Maven project definition (POM) file for this module defines several profiles (one for each supported browser), and it's these profiles that activate the dependency declarations requires by their respective grid node servers.

## System Requirements

* As indicated above, `local-grid-parent` relies on **Apache Maven** to manage dependencies and execute the Java command line application that launches the specified grid collection. This project was developed with version _3.8.4_.
* You'll need a Java development kit - JDK 8 at a minimum, but I've been running with Oracle JDK _11.0.13_. You can probably run with a JRE if you don't perform any operations that require compilation.
* To clone the project repository, you'll need a `git` client. On my Windows 10 development machine, I use release _2.35.1.windows.2_.
* For each of the desktop browsers for which you'll be serving sessions, you'll need to install the corresponding driver executable. If the directory in which these executables are stored is on the PATH, the corresponding System properties will be set automatically.

## Launch a Local Grid

In one step, you can launch a Selenium Grid hub and a single node that supplies **HtmlUnit** browser sessions. From the `local-grid-hub` directory:

```bash
mvn exec:java -Phtmlunit
```

Note the `-Phtmlunit` option on the preceding command. This specifies the inclusion of the **htmlunit** profile, which activates the dependencies required by this "headless" browser. It also adds an option to the command line that specifies attachment a node to the grid hub that provides **HtmlUnit** browser sessions.

## Supported Profiles

| Profile | Plugin |
|--|--|--|
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

* `-port` : specify port for local hub server (default = 4445)
* `-plugins` : path-delimited list of fully-qualified node plugin classes
* `-hubServlets` : comma-delimited list of fully-qualified servlet classes to install on the hub server
* `-nodeServlet` : comma-delimited list of fully-qualified servlet classes to install on the node servers
* `-workingDir` : working directory for servers
* `-logsFolder` : server output logs folder (default = "logs")
* `-noRedirect` : disable server output redirection (default = `false`)
* `-shutdown` : shut down active local Grid collection

## Notes

The ports used by the node servers that supply browser sessions are auto-selected via the [PortProber.findFreePort()](https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/net/PortProber.html#findFreePort--) method of the **`selenium-remote-driver`** library.

Specification of a browser profile implicitly adds the corresponding `plugins` option. If you launch a local grid with no specified profiles, the hub runs as a servlet container.

Unless disabled with the `noRedirect` option, **`local-grid-hub`** redirects the output of the hub and node servers to log files in a `logs` folder under the current working directory. Each log file contains the output from a single launch of its associated server. Log file names are auto-incremented to avoid overwriting or appending to the output of previous launches.

* `grid-hub*.log` for hub server output
* `grid-node*.log` for node server output

The default output folder can be overridden with the `logsFolder` option, specifying either absolute or relative path. If a relative path is specified, or the default ("logs") is accepted, logs are written to a sub-folder of the current working directory, which can be overridden with the `workingDir` option.

> Written with [StackEdit](https://stackedit.io/).
