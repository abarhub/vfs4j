package com.github.abarhub.vfs.core.api;

import com.github.abarhub.vfs.core.exception.VFS4JException;
import org.slf4j.Logger;
import com.github.abarhub.vfs.core.util.VFS4JLoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class VFS4JDefaultFileManager {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(VFS4JDefaultFileManager.class);

    private static VFS4JFileManager fileManager = createFileManager();

    private VFS4JDefaultFileManager() {
    }

    protected static VFS4JFileManager createFileManager() {
        VFS4JFileManagerBuilder fileManagerBuilder;

        LOGGER.info("Start initialization");

        Properties properties = getProperties();

        if (properties != null && !properties.isEmpty()) {
            // construction de la map des propriétés (on enlève ce qui n'est pas de type string)
            VFS4JParseConfigFile parseConfigFile = new VFS4JParseConfigFile();
            fileManagerBuilder = parseConfigFile.parse(properties);
        } else {
            fileManagerBuilder = new VFS4JFileManagerBuilder();
        }

        VFS4JFileManager fileManager = new VFS4JFileManager(fileManagerBuilder.build());

        LOGGER.info("End initialization");

        return fileManager;
    }

    private static Properties getProperties() {
        // lecture du fichier de configuration dans la propriété VFS_CONFIG
        Properties properties = getPropertiesFromSystemProperties();

        if (properties == null) {
            // lecture du fichier de configuration dans le classpath
            properties = getPropertiesFromClasspath(properties);
        }

        if (properties == null) {
            LOGGER.info("no VFS4J config file found");
        }
        return properties;
    }

    private static Properties getPropertiesFromSystemProperties() {
        Properties properties = null;
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
        URL url = VFS4JDefaultFileManager.class.getResource("/vfs.properties");
        if (url != null) {
            File file = new File(url.getFile());
            LOGGER.info("VFS4J config file is '{}'", file);
            try (InputStream is = new FileInputStream(file)) {
                properties = new Properties();
                properties.load(is);
            } catch (FileNotFoundException e) {
                properties = null;
                LOGGER.info("File vfs.properties not found in classpath");
            } catch (IOException e) {
                throw new VFS4JException("Error in reading file vfs.properties in classpath", e);
            }
        }
        return properties;
    }

    public static VFS4JFileManager get() {
        LOGGER.debug("get FileManager");
        return fileManager;
    }

}
