package io.github.abarhub.vfs.core.api;

import io.github.abarhub.vfs.core.api.path.VFS4JPaths;
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
        fileManager.getCommand().createFile(VFS4JPaths.get("path1", pathRef));

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
        fileManager.getCommand().createDirectory(VFS4JPaths.get("path1", pathRef));

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
        fileManager.getCommand().createDirectories(VFS4JPaths.get("path1", pathRef));

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
        fileManager.getCommand().delete(VFS4JPaths.get("path1", pathRef));

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
        fileManager.getCommand().deleteIfExists(VFS4JPaths.get("path1", pathRef));

        // vérifications
        assertFalse(Files.exists(p));

        LOGGER.info("Le repertoire {} n'existe pas", p);
    }

    @Test
    void matcher_test1(@TempDir Path tempDir) throws IOException {

        LOGGER.info("matcher_test1");

        assertTrue(Files.exists(tempDir));
        Path temp = tempDir.resolve("temp");
        assertFalse(Files.exists(temp));
        Files.createDirectory(temp);
        assertTrue(Files.exists(temp));
        VFS4JFileManager fileManager = new VFS4JFileManager();
        fileManager.getConfig().addPath("path1", temp);

        final String pathRef = "myfile1.txt";
        Path p = temp.resolve(pathRef);
        Files.createDirectories(p);

        assertTrue(Files.exists(p));

        LOGGER.info("Le fichier {} existe", p);

        // methode testée
        boolean resultat = fileManager.matcher("glob:**/*.txt").matches(VFS4JPaths.get("path1", pathRef));

        // vérifications
        assertTrue(resultat);

        LOGGER.info("Le fichier {} match", p);
    }
}