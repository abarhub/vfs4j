package io.github.abarhub.vfs.core.api.operation;

import io.github.abarhub.vfs.core.api.VFS4JFileManager;
import io.github.abarhub.vfs.core.api.VFS4JPathName;
import io.github.abarhub.vfs.core.api.VFS4JPaths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class VFS4JSimpleQueryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VFS4JSimpleQueryTest.class);
    public static final String PATH1 = "path1";

    private VFS4JFileManager fileManager;

    private Path directory;

    private VFS4JSimpleQuery simpleQuery;

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

        simpleQuery = new VFS4JSimpleQuery(fileManager);
    }

    @Test
    void existsTrue() throws IOException {

        final String filename = "fichier.txt";
        final Path file = directory.resolve(filename);
        Files.createFile(file);
        assertTrue(Files.exists(file));

        // methode testée
        boolean res = simpleQuery.exists(getPathName(filename));

        // vérifications
        assertTrue(res);
    }

    @Test
    void existsNot() throws IOException {

        final String filename = "fichier.txt";
        final Path file = directory.resolve(filename);
        assertFalse(Files.exists(file));

        // methode testée
        boolean res = simpleQuery.exists(getPathName(filename));

        // vérifications
        assertFalse(res);
    }

    @Test
    void isDirectoryTrue() throws IOException {

        final String filename = "directory";
        final Path file = directory.resolve(filename);
        Files.createDirectory(file);
        assertTrue(Files.isDirectory(file));

        // methode testée
        boolean res = simpleQuery.isDirectory(getPathName(filename));

        // vérifications
        assertTrue(res);
    }

    @Test
    void isDirectoryFalse() throws IOException {

        final String filename = "file.txt";
        final Path file = directory.resolve(filename);
        Files.createFile(file);
        assertFalse(Files.isDirectory(file));

        // methode testée
        boolean res = simpleQuery.isDirectory(getPathName(filename));

        // vérifications
        assertFalse(res);
    }

    @Test
    void isRegularFileTrue() throws IOException {

        final String filename = "file.txt";
        final Path file = directory.resolve(filename);
        Files.createFile(file);
        assertTrue(Files.isRegularFile(file));

        // methode testée
        boolean res = simpleQuery.isRegularFile(getPathName(filename));

        // vérifications
        assertTrue(res);
    }

    @Test
    void isRegularFileFalse() throws IOException {

        final String filename = "direct2";
        final Path file = directory.resolve(filename);
        Files.createDirectory(file);
        assertFalse(Files.isRegularFile(file));

        // methode testée
        boolean res = simpleQuery.isRegularFile(getPathName(filename));

        // vérifications
        assertFalse(res);
    }

    private static Stream<Arguments> provideIsSameFile() {
        return Stream.of(
                Arguments.of("file.txt", "file.txt", true),
                Arguments.of("file.txt", "file2.txt", false)
                // TODO: ne fonctionne pas sur travis-ci
                //,Arguments.of("file.txt", "test/../file.txt", true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideIsSameFile")
    void isSameFile(final String filename, final String filename2, final boolean resultat) throws IOException {
        final Path file = directory.resolve(filename);
        final Path file2 = directory.resolve(filename2);
        if (Files.notExists(file)) {
            Files.createFile(file);
        }
        if (Files.notExists(file2)) {
            Files.createFile(file2);
        }
        assertEquals(resultat, Files.isSameFile(file, file2));

        // methode testée
        boolean res = simpleQuery.isSameFile(getPathName(filename), getPathName(filename2));

        // vérifications
        assertEquals(resultat, res);
    }

    @Test
    @Disabled("Les liens symboliques ne fonctionnent sous windows que si on est superadmin => test désactivé")
    @Tag("symbolicLink")
    void isSymbolicLinkTrue() throws IOException {
        final String filename = "fichier1.txt";
        final String filename2 = "file2.txt";
        final Path file = directory.resolve(filename);
        final Path file2 = directory.resolve(filename2);
        Files.createFile(file2);
        Files.createSymbolicLink(file, file2);
        assertTrue(Files.isSymbolicLink(file));

        // methode testée
        boolean res = simpleQuery.isSymbolicLink(getPathName(filename));

        // vérifications
        assertTrue(res);
    }

    @Test
    @Tag("symbolicLink")
    void isSymbolicLinkFalse() throws IOException {

        final String filename = "fichier1.txt";
        final Path file = directory.resolve(filename);
        Files.createFile(file);
        assertFalse(Files.isSymbolicLink(file));

        // methode testée
        boolean res = simpleQuery.isSymbolicLink(getPathName(filename));

        // vérifications
        assertFalse(res);
    }

    @Test
    void lines() throws IOException {

        final String filename = "file.txt";
        final Path file = directory.resolve(filename);
        final List<String> lignes = liste("aaa", "bbb", "ccc", "aébc");
        Files.write(file, lignes, StandardCharsets.UTF_8);

        // methode testée
        try (Stream<String> res = simpleQuery.lines(getPathName(filename))) {

            // vérifications
            assertNotNull(res);
            List<String> listeResultat = res.collect(Collectors.toList());
            assertEquals(lignes, listeResultat);
        }
    }

    @Test
    void linesCs() throws IOException {

        final String filename = "file2.txt";
        final Path file = directory.resolve(filename);
        final List<String> lignes = liste("aaa", "bbéb", "ccc");
        Files.write(file, lignes, StandardCharsets.UTF_8);

        // methode testée
        try (Stream<String> res = simpleQuery.lines(getPathName(filename), StandardCharsets.UTF_8)) {

            // vérifications
            assertNotNull(res);
            List<String> listeResultat = res.collect(Collectors.toList());
            assertEquals(lignes, listeResultat);
        }
    }

    @Test
    void notExistsTrue() throws IOException {

        final String filename = "fichier.txt";
        final Path file = directory.resolve(filename);
        assertTrue(Files.notExists(file));

        // methode testée
        boolean res = simpleQuery.notExists(getPathName(filename));

        // vérifications
        assertTrue(res);
    }

    @Test
    void notExistsFalse() throws IOException {

        final String filename = "fichier.txt";
        final Path file = directory.resolve(filename);
        Files.createFile(file);
        assertFalse(Files.notExists(file));

        // methode testée
        boolean res = simpleQuery.notExists(getPathName(filename));

        // vérifications
        assertFalse(res);
    }

    @Test
    void readAllBytes() throws IOException {

        final String filename = "file2.txt";
        final Path file = directory.resolve(filename);
        final String contenu = "abc";
        Files.write(file, contenu.getBytes(StandardCharsets.UTF_8));

        // methode testée
        byte[] res = simpleQuery.readAllBytes(getPathName(filename));

        // vérifications
        assertArrayEquals(contenu.getBytes(StandardCharsets.UTF_8), res);
    }

    @Test
    void readAllLines() throws IOException {

        final String filename = "file3.txt";
        final Path file = directory.resolve(filename);
        final List<String> lignes = liste("aaa", "bbéb", "ccc");
        Files.write(file, lignes, StandardCharsets.UTF_8);

        // methode testée
        List<String> res = simpleQuery.readAllLines(getPathName(filename));

        // vérifications
        assertEquals(lignes, res);
    }

    @Test
    void readAllLinesCS() throws IOException {

        final String filename = "file3.txt";
        final Path file = directory.resolve(filename);
        final List<String> lignes = liste("aaa", "bbéb", "ccc");
        Files.write(file, lignes, StandardCharsets.UTF_8);

        // methode testée
        List<String> res = simpleQuery.readAllLines(getPathName(filename), StandardCharsets.UTF_8);

        // vérifications
        assertEquals(lignes, res);
    }

    @Test
    void size() throws IOException {

        final String filename = "file2.txt";
        final Path file = directory.resolve(filename);
        final String contenu = "abc123852";
        Files.write(file, contenu.getBytes(StandardCharsets.UTF_8));

        // methode testée
        long res = simpleQuery.size(getPathName(filename));

        // vérifications
        assertEquals(contenu.length(), res);
    }


    // methodes utilitaires

    public VFS4JPathName getPathName(String filename) {
        return VFS4JPaths.get(PATH1, filename);
    }

    public List<String> liste(String... str) {
        List<String> list = new ArrayList<>();
        if (str != null) {
            list.addAll(Arrays.asList(str));
        }
        return list;
    }
}