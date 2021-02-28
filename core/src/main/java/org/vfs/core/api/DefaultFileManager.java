package org.vfs.core.api;

import org.slf4j.Logger;
import org.vfs.core.exception.VFS4JException;
import org.vfs.core.util.VFS4JLoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class DefaultFileManager {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(DefaultFileManager.class);

    private static FileManager fileManager = createFileManager();

    private DefaultFileManager() {
    }

    protected static FileManager createFileManager() {
        FileManagerBuilder fileManagerBuilder;

        LOGGER.info("Start initialization");

        Properties properties = getProperties();

        if (properties != null && !properties.isEmpty()) {
            // construction de la map des propriétés (on enlève ce qui n'est pas de type string)
            ParseConfigFile parseConfigFile = new ParseConfigFile();
            fileManagerBuilder = parseConfigFile.parse(properties);
        } else {
            fileManagerBuilder = new FileManagerBuilder();
        }

        FileManager fileManager = new FileManager(fileManagerBuilder.build());

        LOGGER.info("End initialization");

        return fileManager;
    }

    private static Properties getProperties() {
        Properties properties = null;

        if (properties == null) {
            // lecture du fichier de configuration dans la propriété VFS_CONFIG
            properties = getPropertiesFromSystemProperties(properties);
        }

        if (properties == null) {
            // lecture du fichier de configuration dans le classpath
            properties = getPropertiesFromClasspath(properties);
        }

        if (properties == null) {
            LOGGER.info("no VFS4J config file found");
        }
        return properties;
    }

    private static Properties getPropertiesFromSystemProperties(Properties properties) {
        String vfsConfigPath = System.getProperty("VFS_CONFIG");
        if (vfsConfigPath != null && !vfsConfigPath.trim().isEmpty()) {
            Path path = Paths.get(vfsConfigPath);
            if (Files.exists(path)) {
                LOGGER.info("VFS4J config file is '{}'", path);
                try (InputStream in = Files.newInputStream(path)) {
                    properties = new Properties();
                    properties.load(in);
                } catch (IOException e) {
                    throw new VFS4JException("Error for reading file '" + vfsConfigPath + "'", e);
                }
            } else {
                throw new VFS4JException("File '" + vfsConfigPath + "' not exists");
            }
        }
        return properties;
    }

    private static Properties getPropertiesFromClasspath(Properties properties) {
        URL url = DefaultFileManager.class.getResource("/vfs.properties");
        if (url != null) {
            File file = new File(url.getFile());
            LOGGER.info("VFS4J config file is '{}'", file);
            try (InputStream is = new FileInputStream(file)) {
                if (is != null) {
                    properties = new Properties();
                    properties.load(is);
                }
            } catch (FileNotFoundException e) {
                properties = null;
                LOGGER.info("File vfs.properties not found in classpath");
            } catch (IOException e) {
                throw new VFS4JException("Error in reading file vfs.properties in classpath", e);
            }
        }
        return properties;
    }

    public static FileManager get() {
        LOGGER.debug("get FileManager");
        return fileManager;
    }

}
