package org.vfs.core.exemples;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vfs.core.api.*;
import org.vfs.core.config.VFS4JConfig;
import org.vfs.core.config.VFS4JPathMode;
import org.vfs.core.exception.VFS4JWriteException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("ces TU ne fonctionnent pas avec Maven")
public class Exemple1Test {

    private static final Logger LOGGER = LoggerFactory.getLogger(Exemple1Test.class);

    @Test
    public void exemple1() throws IOException {

        LOGGER.info("exemple1 ...");

        Path temp = Files.createTempDirectory("junit_test_vfs4j");

        Path path1 = temp.resolve("rep01");
        Files.createDirectories(path1);

        Path path2 = temp.resolve("rep02");
        Files.createDirectories(path2);

        Properties properties = new Properties();

        properties.setProperty("vfs.paths.rep01.path", path1.toString());
        properties.setProperty("vfs.paths.rep01.mode", VFS4JPathMode.STANDARD.getName());
        properties.setProperty("vfs.paths.rep02.path", path2.toString());
        properties.setProperty("vfs.paths.rep02.mode", VFS4JPathMode.STANDARD.getName());
        properties.setProperty("vfs.paths.temp.mode", VFS4JPathMode.TEMPORARY.getName());

        ParseConfigFile parseConfigFile = new ParseConfigFile();
        FileManagerBuilder fileManagerBuilder = parseConfigFile.parse(properties);

        DefaultFileManager.get().setConfig(fileManagerBuilder.build());

        VFS4JConfig config = DefaultFileManager.get().getConfig();

        assertNotNull(config.getPath("rep01"));
        assertNotNull(config.getPath("rep02"));
        assertNotNull(config.getPath("temp"));

        Path pathTemp = config.getPath("temp").getPath();


        Path file1 = path1.resolve("fichier01.txt");
        Path file2 = path2.resolve("fichier02.txt");
        Path file3 = pathTemp.resolve("fichier03.txt");

        Files.deleteIfExists(file1);
        Files.deleteIfExists(file2);
        Files.deleteIfExists(file3);

        assertTrue(Files.exists(path1));
        assertTrue(Files.exists(path2));
        assertTrue(Files.exists(pathTemp));

        assertFalse(Files.exists(file1));
        assertFalse(Files.exists(file2));
        assertFalse(Files.exists(file3));

        // methodes testées
        VFS4JFiles.createFile(new PathName("rep01", "fichier01.txt"));
        VFS4JFiles.createFile(new PathName("rep02", "fichier02.txt"));
        VFS4JFiles.createFile(new PathName("temp", "fichier03.txt"));

        // vérifications
        assertTrue(Files.exists(file1));
        assertTrue(Files.exists(file2));
        assertTrue(Files.exists(file3));

        LOGGER.info("exemple1 OK");
    }

    @Test
    public void exemple2() throws IOException {

        LOGGER.info("exemple2 ...");

        Path temp = Files.createTempDirectory("junit_test_vfs4j");

        Path path1 = temp.resolve("rep01");
        Files.createDirectories(path1);

        Path path2 = temp.resolve("rep02");
        Files.createDirectories(path2);

        Properties properties = new Properties();

        properties.setProperty("vfs.paths.rep01.path", path1.toString());
        properties.setProperty("vfs.paths.rep01.readonly", "false");
        properties.setProperty("vfs.paths.rep02.path", path2.toString());
        properties.setProperty("vfs.paths.rep02.readonly", "true");

        ParseConfigFile parseConfigFile = new ParseConfigFile();
        FileManagerBuilder fileManagerBuilder = parseConfigFile.parse(properties);

        DefaultFileManager.get().setConfig(fileManagerBuilder.build());

        VFS4JConfig config = DefaultFileManager.get().getConfig();

        assertNotNull(config.getPath("rep01"));
        assertFalse(config.getPath("rep01").isReadonly());
        assertNotNull(config.getPath("rep02"));
        assertTrue(config.getPath("rep02").isReadonly());

        Path file1 = path1.resolve("fichier01.txt");
        Path file2 = path2.resolve("fichier02.txt");

        Files.deleteIfExists(file1);
        Files.deleteIfExists(file2);

        assertTrue(Files.exists(path1));
        assertTrue(Files.exists(path2));

        assertFalse(Files.exists(file1));
        assertFalse(Files.exists(file2));

        // methodes testées
        VFS4JFiles.createFile(new PathName("rep01", "fichier01.txt"));

        VFS4JWriteException exception = assertThrows(VFS4JWriteException.class, () -> VFS4JFiles.createFile(new PathName("rep02", "fichier02.txt")));

        // vérifications
        assertEquals("write operation forbidden for createFile on rep02", exception.getMessage());

        assertTrue(Files.exists(file1));
        assertFalse(Files.exists(file2));

        LOGGER.info("exemple2 OK");
    }
}
