package io.github.abarhub.vfs.core.api.operation;

import io.github.abarhub.vfs.core.api.VFS4JFileManager;
import io.github.abarhub.vfs.core.api.VFS4JPathName;
import io.github.abarhub.vfs.core.api.VFS4JPaths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class VFS4JSimpleSearchTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VFS4JSimpleSearchTest.class);
    public static final String PATH1 = "path1";

    private VFS4JFileManager fileManager;

    private Path directory;

    private VFS4JSimpleSearch simpleSearch;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        fileManager = new VFS4JFileManager();

        assertTrue(Files.exists(tempDir));
        Path temp = tempDir.resolve("temp");
        assertFalse(Files.exists(temp));
        Files.createDirectory(temp);
        assertTrue(Files.exists(temp));
        assertTrue(Files.isDirectory(temp));
        directory = temp;
        fileManager = new VFS4JFileManager();
        fileManager.getConfig().addPath(PATH1, temp);

        simpleSearch = new VFS4JSimpleSearch(fileManager);
    }

    @Test
    void list() throws IOException {

        final String filename = "directory";
        final Path file = directory.resolve(filename);
        final List<String> files = liste("fichier1.txt", "fichier2.pdf", "fichier3.jpg");
        Set<VFS4JPathName> set = new HashSet<>();
        Files.createDirectory(file);
        for (String s : files) {
            Files.createFile(file.resolve(s));
            set.add(getPathName(filename + File.separator + s));
        }

        // methode testée
        try (Stream<VFS4JPathName> res = simpleSearch.list(getPathName(filename))) {

            // vérifications
            assertNotNull(res);
            List<VFS4JPathName> liste = res.collect(Collectors.toList());
            assertEquals(3, liste.size());
            Set<VFS4JPathName> set2 = new HashSet<>(liste);
            assertEquals(set, set2);
        }
    }

    @Test
    void walkMaxDept() throws IOException {

        final String filename = "directory";
        final Path file = directory.resolve(filename);
        final List<String> files = liste("fichier1.txt", "fichier2.pdf", "fichier3.jpg");
        Set<VFS4JPathName> set = new HashSet<>();
        set.add(getPathName(filename));
        Files.createDirectory(file);
        for (String s : files) {
            Files.createFile(file.resolve(s));
            set.add(getPathName(filename + File.separator + s));
        }

        // methode testée
        try (Stream<VFS4JPathName> res = simpleSearch.walk(getPathName(filename), 5)) {

            // vérifications
            assertNotNull(res);
            List<VFS4JPathName> liste = res.collect(Collectors.toList());
            assertEquals(4, liste.size());
            Set<VFS4JPathName> set2 = new HashSet<>(liste);
            assertEquals(set, set2);
        }
    }

    @Test
    void walk() throws IOException {

        final String filename = "directory";
        final Path file = directory.resolve(filename);
        final List<String> files = liste("fichier1.txt", "fichier2.pdf", "fichier3.jpg");
        Set<VFS4JPathName> set = new HashSet<>();
        set.add(getPathName(filename));
        Files.createDirectory(file);
        for (String s : files) {
            Files.createFile(file.resolve(s));
            set.add(getPathName(filename + File.separator + s));
        }

        // methode testée
        try (Stream<VFS4JPathName> res = simpleSearch.walk(getPathName(filename))) {

            // vérifications
            assertNotNull(res);
            List<VFS4JPathName> liste = res.collect(Collectors.toList());
            assertEquals(4, liste.size());
            Set<VFS4JPathName> set2 = new HashSet<>(liste);
            assertEquals(set, set2);
        }
    }

    @Test
    void find() throws IOException {

        final String filename = "directory";
        final Path file = directory.resolve(filename);
        final List<String> files = liste("fichier1.txt", "fichier2.pdf", "fichier3.jpg");
        Set<VFS4JPathName> set = new HashSet<>();
        set.add(getPathName(filename));
        Files.createDirectory(file);
        for (String s : files) {
            Files.createFile(file.resolve(s));
            set.add(getPathName(filename + File.separator + s));
        }

        // methode testée
        try (Stream<VFS4JPathName> res = simpleSearch.find(getPathName(filename), 10, (x, y) -> true)) {

            // vérifications
            assertNotNull(res);
            List<VFS4JPathName> liste = res.collect(Collectors.toList());
            assertEquals(4, liste.size());
            Set<VFS4JPathName> set2 = new HashSet<>(liste);
            assertEquals(set, set2);
        }
    }

    // methodes utilitaires

    public VFS4JPathName getPathName(String filename) {
        return VFS4JPaths.get(PATH1, filename);
    }

    public List<String> liste(String... str) {
        List<String> list = new ArrayList<>();
        if (str != null) {
            for (String s : str) {
                list.add(s);
            }
        }
        return list;
    }
}