package org.vfs.core.api;

import org.vfs.core.exception.VFSException;
import org.vfs.core.util.VFSLoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

public class DefaultFileManager {

    private static final Logger LOGGER = VFSLoggerFactory.getLogger(DefaultFileManager.class);

    private static FileManager fileManager = createFileManager();

    protected static FileManager createFileManager() {
        FileManagerBuilder fileManagerBuilder;

        LOGGER.info("createFileManager");

        Properties properties = getProperties();

        if (properties != null && !properties.isEmpty()) {
            // construction de la map des propriétés (on enlève ce qui n'est pas de type string)
            ParseConfigFile parseConfigFile = new ParseConfigFile();
            fileManagerBuilder = parseConfigFile.parse(properties);
        } else {
            fileManagerBuilder = new FileManagerBuilder();
        }

        return new FileManager(fileManagerBuilder);
    }

    private static Properties getProperties() {
        Properties properties = null;

        if (properties == null) {
            // lecture du fichier de configuration dans la propriété VFS_CONFIG
            String vfsConfigPath = System.getProperty("VFS_CONFIG");
            if (vfsConfigPath != null && !vfsConfigPath.trim().isEmpty()) {
                Path path = Paths.get(vfsConfigPath);
                if (Files.exists(path)) {
                    try (InputStream in = Files.newInputStream(path)) {
                        properties = new Properties();
                        properties.load(in);
                    } catch (IOException e) {
                        throw new VFSException("Error for reading file '" + vfsConfigPath + "'", e);
                    }
                } else {
                    throw new VFSException("File '" + vfsConfigPath + "' not exists");
                }
            }
        }

        if (properties == null) {
            // lecture du fichier de configuration dans le classpath
            try (InputStream is = DefaultFileManager.class.getResourceAsStream("/vfs.properties")) {
                if (is != null) {
                    properties = new Properties();
                    properties.load(is);
                }
            } catch (FileNotFoundException e) {
                properties = null;
                LOGGER.info("File vfs.properties not found in classpath");
            } catch (IOException e) {
                throw new VFSException("Error in reading file vfs.properties in classpath", e);
            }
        }
        return properties;
    }

    public static FileManager get() {
        return fileManager;
    }

}
