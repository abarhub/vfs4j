package io.github.abarhub.vfs.core.api.path;

import io.github.abarhub.vfs.core.api.VFS4JFileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VFS4JDefaultPathMatcherTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VFS4JDefaultPathMatcherTest.class);

    private VFS4JFileManager fileManager = mock(VFS4JFileManager.class);

    private VFS4JDefaultPathMatcher matcher;

    @BeforeEach
    void setUp() {
    }

    @Test
    void matchesOK() {
        VFS4JPathName pathName = VFS4JPaths.get("dir1", "test1.txt");
        Path path = Paths.get("/tmp/test1.txt");
        when(fileManager.getRealFile(eq(pathName))).thenReturn(path);
        matcher = new VFS4JDefaultPathMatcher("glob:**/*.txt", fileManager);

        // méthode testée
        boolean res = matcher.matches(pathName);

        // vérification
        assertTrue(res);
    }

    @Test
    void matchesKO() {
        VFS4JPathName pathName = VFS4JPaths.get("dir1", "test1.txt");
        Path path = Paths.get("/tmp/test1.txt");
        when(fileManager.getRealFile(eq(pathName))).thenReturn(path);
        matcher = new VFS4JDefaultPathMatcher("glob:**/*.doc", fileManager);

        // méthode testée
        boolean res = matcher.matches(pathName);

        // vérification
        assertFalse(res);
    }

    @Test
    void testZipOK() throws Exception {
        LOGGER.debug("testZipOK");
        URI uri = getClass().getClassLoader().getResource("zip/test1.zip").toURI();
        Path pathZip = Paths.get(uri).toAbsolutePath();
        LOGGER.debug("uri={}", uri);
        LOGGER.debug("pathZip={}", pathZip);
        VFS4JPathName pathName = VFS4JPaths.get("dir1", "doc1.txt");
        try (FileSystem zipfs = FileSystems.newFileSystem(pathZip, null)) {
            Path path = zipfs.getPath("/doc1.txt");
            when(fileManager.getRealFile(eq(pathName))).thenReturn(path);
            matcher = new VFS4JDefaultPathMatcher("glob:**/*.txt", fileManager);

            // méthode testée
            boolean res = matcher.matches(pathName);

            // vérification
            assertTrue(res);
        }
    }

    @Test
    void testZip2OK() throws Exception {
        LOGGER.debug("testZip2OK");
        URI uri = getClass().getClassLoader().getResource("zip/test1.zip").toURI();
        Path pathZip = Paths.get(uri).toAbsolutePath();
        LOGGER.debug("uri={}", uri);
        LOGGER.debug("pathZip={}", pathZip);
        VFS4JPathName pathName = VFS4JPaths.get("dir1", "dir1/doc4.txt");
        try (FileSystem zipfs = FileSystems.newFileSystem(pathZip, null)) {
            Path path = zipfs.getPath("/dir1/doc4.txt");
            when(fileManager.getRealFile(eq(pathName))).thenReturn(path);
            matcher = new VFS4JDefaultPathMatcher("glob:**/*.txt", fileManager);

            // méthode testée
            boolean res = matcher.matches(pathName);

            // vérification
            assertTrue(res);
        }
    }

    @Test
    void testZipKO() throws Exception {
        LOGGER.debug("testZipKO");
        URI uri = getClass().getClassLoader().getResource("zip/test1.zip").toURI();
        Path pathZip = Paths.get(uri).toAbsolutePath();
        LOGGER.debug("uri={}", uri);
        LOGGER.debug("pathZip={}", pathZip);
        VFS4JPathName pathName = VFS4JPaths.get("dir1", "dir1/doc4.htm");
        try (FileSystem zipfs = FileSystems.newFileSystem(pathZip, null)) {
            Path path = zipfs.getPath("/dir1/doc4.htm");
            when(fileManager.getRealFile(eq(pathName))).thenReturn(path);
            matcher = new VFS4JDefaultPathMatcher("glob:**/*.txt", fileManager);

            // méthode testée
            boolean res = matcher.matches(pathName);

            // vérification
            assertFalse(res);
        }
    }

}