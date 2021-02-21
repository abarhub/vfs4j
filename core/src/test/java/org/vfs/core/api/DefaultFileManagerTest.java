package org.vfs.core.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.vfs.core.config.PathParameter;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DefaultFileManagerTest {

    public static final String PROPERTIES_CONFIG = "VFS_CONFIG";

    private Path directory;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        directory = tempDir;
    }

    @Test
    void createFileManager() throws IOException {
        Path path01 = directory.resolve("path1");
        Path path02 = directory.resolve("path2");
        final String dir1 = "dir1";
        final String path1 = path01.toString();
        final String dir2 = "dir2";
        final String path2 = path02.toString();
        Files.createDirectory(path01);
        Files.createDirectory(path02);
        Properties properties = new Properties();
        properties.put("vfs.paths." + dir1 + ".path", path1);
        properties.put("vfs.paths." + dir2 + ".path", path2);
        Path configFile = directory.resolve("vfs.properties");
        try (Writer writer = Files.newBufferedWriter(configFile)) {
            properties.store(writer, "");
        }
        System.setProperty(PROPERTIES_CONFIG, configFile.toString());

        // methode testée
        FileManager fileManager = DefaultFileManager.createFileManager();

        // vérifications
        assertNotNull(fileManager);
        PathParameter pathParameter = (PathParameter) fileManager.getPath(dir1);
        assertNotNull(pathParameter);
        assertEquals(path01, pathParameter.getPath());
        pathParameter = (PathParameter) fileManager.getPath(dir2);
        assertNotNull(pathParameter);
        assertEquals(path02, pathParameter.getPath());
    }

    @Test
    void createFileManager2() throws IOException {
        Path path01 = directory.resolve("path_dir1");
        Path path02 = directory.resolve("path_dir2");
        final String dir1 = "directory1";
        final String path1 = path01.toString();
        final String dir2 = "directory2";
        final String path2 = path02.toString();
        Files.createDirectory(path01);
        Files.createDirectory(path02);
        Properties properties = new Properties();
        properties.put("vfs.paths." + dir1 + ".path", path1);
        properties.put("vfs.paths." + dir2 + ".path", path2);
        Path configFile = directory.resolve("vfs.properties");
        try (Writer writer = Files.newBufferedWriter(configFile)) {
            properties.store(writer, "");
        }
        System.setProperty(PROPERTIES_CONFIG, configFile.toString());

        // methode testée
        FileManager fileManager = DefaultFileManager.createFileManager();

        // vérifications
        assertNotNull(fileManager);
        PathParameter pathParameter = (PathParameter) fileManager.getPath(dir1);
        assertNotNull(pathParameter);
        assertEquals(path01, pathParameter.getPath());
        pathParameter = (PathParameter) fileManager.getPath(dir2);
        assertNotNull(pathParameter);
        assertEquals(path02, pathParameter.getPath());
    }
}