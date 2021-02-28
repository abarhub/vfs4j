package org.vfs.core.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VFS4JFileManagerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VFS4JFileManagerTest.class);

    @Test
    void createFile_test1(@TempDir Path tempDir) throws IOException {

        LOGGER.info("createFile_test1");

        assertTrue(Files.exists(tempDir));
        Path temp = tempDir.resolve("temp");
        assertFalse(Files.exists(temp));
        Files.createDirectory(temp);
        assertTrue(Files.exists(temp));
        VFS4JFileManager fileManager = new VFS4JFileManager();
        fileManager.getConfig().addPath("path1", temp);

        final String pathRef = "test.txt";
        Path p = temp.resolve(pathRef);

        assertFalse(Files.exists(p));

        LOGGER.info("Le fichier {} n'existe pas", p);

        // methode testée
        fileManager.getCommand().createFile(new VFS4JPathName("path1", pathRef));

        // vérifications
        assertTrue(Files.exists(p));

        LOGGER.info("Le fichier {} existe", p);
    }

    @Test
    void createDirectory_test1(@TempDir Path tempDir) throws IOException {

        LOGGER.info("createDirectory_test1");

        assertTrue(Files.exists(tempDir));
        Path temp = tempDir.resolve("temp");
        assertFalse(Files.exists(temp));
        Files.createDirectory(temp);
        assertTrue(Files.exists(temp));
        VFS4JFileManager fileManager = new VFS4JFileManager();
        fileManager.getConfig().addPath("path1", temp);

        final String pathRef = "mydirectory";
        Path p = temp.resolve(pathRef);

        assertFalse(Files.exists(p));

        LOGGER.info("Le repertoire {} n'existe pas", p);

        // methode testée
        fileManager.getCommand().createDirectory(new VFS4JPathName("path1", pathRef));

        // vérifications
        assertTrue(Files.exists(p));

        LOGGER.info("Le repertoire {} existe", p);
    }

    @Test
    void createDirectories_test1(@TempDir Path tempDir) throws IOException {

        LOGGER.info("createDirectories_test1");

        assertTrue(Files.exists(tempDir));
        Path temp = tempDir.resolve("temp");
        assertFalse(Files.exists(temp));
        Files.createDirectory(temp);
        assertTrue(Files.exists(temp));
        VFS4JFileManager fileManager = new VFS4JFileManager();
        fileManager.getConfig().addPath("path1", temp);

        final String pathRef = "mydirectory/mysubdir";
        Path p = temp.resolve(pathRef);

        assertFalse(Files.exists(p));

        LOGGER.info("Le repertoire {} n'existe pas", p);

        // methode testée
        fileManager.getCommand().createDirectories(new VFS4JPathName("path1", pathRef));

        // vérifications
        assertTrue(Files.exists(p));

        LOGGER.info("Le repertoire {} existe", p);
    }

    @Test
    void delete_test1(@TempDir Path tempDir) throws IOException {

        LOGGER.info("delete_test1");

        assertTrue(Files.exists(tempDir));
        Path temp = tempDir.resolve("temp");
        assertFalse(Files.exists(temp));
        Files.createDirectory(temp);
        assertTrue(Files.exists(temp));
        VFS4JFileManager fileManager = new VFS4JFileManager();
        fileManager.getConfig().addPath("path1", temp);

        final String pathRef = "mydir5";
        Path p = temp.resolve(pathRef);
        Files.createDirectories(p);

        assertTrue(Files.exists(p));

        LOGGER.info("Le repertoire {} existe", p);

        // methode testée
        fileManager.getCommand().delete(new VFS4JPathName("path1", pathRef));

        // vérifications
        assertFalse(Files.exists(p));

        LOGGER.info("Le repertoire {} n'existe pas", p);
    }

    @Test
    void deleteIfExists_test1(@TempDir Path tempDir) throws IOException {

        LOGGER.info("deleteIfExists_test1");

        assertTrue(Files.exists(tempDir));
        Path temp = tempDir.resolve("temp");
        assertFalse(Files.exists(temp));
        Files.createDirectory(temp);
        assertTrue(Files.exists(temp));
        VFS4JFileManager fileManager = new VFS4JFileManager();
        fileManager.getConfig().addPath("path1", temp);

        final String pathRef = "mydir6";
        Path p = temp.resolve(pathRef);
        Files.createDirectories(p);

        assertTrue(Files.exists(p));

        LOGGER.info("Le repertoire {} existe", p);

        // methode testée
        fileManager.getCommand().deleteIfExists(new VFS4JPathName("path1", pathRef));

        // vérifications
        assertFalse(Files.exists(p));

        LOGGER.info("Le repertoire {} n'existe pas", p);
    }
}