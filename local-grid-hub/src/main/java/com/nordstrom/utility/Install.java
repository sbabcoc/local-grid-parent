package com.nordstrom.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;

/**
 * This class implements the installer for {@code local-grid-hub}. To run the installer:
 * <ul>
 *     <li>Download the {@code local-grid-hub} JAR</li>
 *     <li>Run the JAR:<br>
 *     {@code $ java -jar local-grid-hub-<version>.jar}</li>
 * </ul>
 * This command will extract the Maven POM from the {@code local-grid-hub} JAR, which provides the configurations
 * required by the main command line interface to launch, augment, and shut down local Selenium Grid collections.
 */
public class Install {

    private static final String POM_PATH = "META-INF/maven/com.nordstrom.ui-tools/local-grid-hub/pom.xml";
    
    /**
     * This is the main entry point for the {@code local-grid-hub} installer.
     * 
     * @param args (unused) command line arguments
     * @throws FileNotFoundException The {@code local-grid-hub} POM file was not found.
     * @throws IOException An error occurred trying to create the POM file.
     */
    public static void main(String... args) throws FileNotFoundException, IOException {        
        File pomFile = Paths.get("pom.xml").toFile();
        if (pomFile.createNewFile()) {
            try(InputStream ris = Install.class.getClassLoader().getResourceAsStream(POM_PATH);
                OutputStream fos = new FileOutputStream(pomFile)) {
                copy(ris, fos);
            }
            System.out.println("POM file extracted to: " + pomFile.getAbsolutePath());
        } else {
            System.out.println("POM already exists at: " + pomFile.getAbsolutePath());
        }
    }
    
    private static void copy(InputStream source, OutputStream target) throws IOException {
        byte[] buf = new byte[8192];
        int length;
        while ((length = source.read(buf)) > 0) {
            target.write(buf, 0, length);
        }
    }
}
