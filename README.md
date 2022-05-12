[![Maven Central](https://img.shields.io/maven-central/v/com.nordstrom.ui-tools/local-grid-parent.svg)](https://search.maven.org/search?q=g:com.nordstrom.ui-tools%20AND%20a:local-grid-parent&core=gav)

# local-grid-parent

This project produces a collection of "uber-JARs" that make standing up Selenium Grid collections more manageable and modular.

## Launch Local Grid Hub

In one step, **`local-grid-utility`** launches a Selenium Grid hub and a single node that supplies `PhantomJS` sessions:

```bash
java -jar local-grid-utility.jar
```

The driver for the **`PhantomJS`** browser requires access to a compatible binary. By default, **`local-grid-utility`** will search for the **`PhantomJS`** binary on the file search path. To provide a specific path from which to load the binary, set the `phantomjs.binary.path` system property:

```bash
java -Dphantomjs.binary.path=C:\tools\drivers\phantomjs.exe -jar local-grid-utility.jar
```

### Example

The following is the output from launching a local Grid collection:

> $ java -jar local-grid-utility.jar  
> 15:21:04.063 [main] INFO  org.eclipse.jetty.util.log - Logging initialized @5864ms  
> http://192.168.0.16:4444/wd/hub

Note that the last line of the output provides the URL from which to request remote driver sessions.

## Shut Down Local Grid

To shut down the local Grid collection, specify the `-shutdown` option:

```bash
java -jar local-grid-utility.jar -shutdown
```

This command shuts down the Grid hub and all attached nodes.

## Command Line Options

* `-port` : specify port for local hub server (default = 4444)
* `-shutdown` : shut down active local Grid collection
* `-workingDir` : working directory for servers
* `-logsFolder` : server output logs folder (default = "logs")
* `-noRedirect` : disable server output redirection (default = `false`)

## Notes

The port used by the node server that supplies **`PhantomJS`** sessions is auto-selected via the [PortProber.findFreePort()](https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/net/PortProber.html#findFreePort--) method of the **`selenium-remote-driver`** library.

Unless disabled with the `noRedirect` option, **`local-grid-utility`** redirects the output of the hub and node servers to log files in a `logs` folder under the current working directory. Each log file contains the output from a single launch of its associated server. Log file names are auto-incremented to avoid overwriting or appending to the output of previous launches.

* `grid-hub*.log` for hub server output
* `grid-node*.log` for node server output

The default output folder can be overridden with the `logsFolder` option, specifying either absolute or relative path. If a relative path is specified, or the default ("logs") is accepted, logs are written to a sub-folder of the current working directory, which can be overridden with the `workingDir` option.
