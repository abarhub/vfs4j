package org.vfs.core.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class VFS4JConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VFS4JConfigTest.class);

    @TempDir
    public Path tempDir;

    private static Stream<Arguments> addPath_test1Parameter() throws IOException {
        Path tempDirectory = Files.createTempDirectory("junit5_vfs4j");
        return Stream.of(
                Arguments.of("test1", createPath(concatPath(tempDirectory, "/tmp/aaa/bbb")), false),
                Arguments.of("test2", createPath(concatPath(tempDirectory, "/tmp/aaa/bbb/ccc")), true)
        );
    }

    @ParameterizedTest
    @MethodSource("addPath_test1Parameter")
    void addPath_test1(final String nom, final Path path, final boolean readOnly) {
        LOGGER.info("addPath_test1({}, {}, {})", nom, path, readOnly);
        VFS4JConfig vfs4JConfig = new VFS4JConfig();

        // methode testée
        vfs4JConfig.addPath(nom, path, readOnly);

        // vérifications
        PathParameter pathParameter = vfs4JConfig.getPath(nom);
        assertNotNull(pathParameter);
        assertEquals(path, pathParameter.getPath());
        assertEquals(readOnly, pathParameter.isReadonly());
    }

    @Test
    @DisplayName("getPath sur un nom qui existe (une seule configuration)")
    void getPath_test1() throws IOException {
        LOGGER.info("addPath_test1");
        VFS4JConfig vfs4JConfig = new VFS4JConfig();

        String nom = "name1";
        Path path = createTempPath("/tmp/dddd");
        boolean readOnly = true;

        vfs4JConfig.addPath(nom, path, readOnly);

        // methode testée
        PathParameter pathParameter = vfs4JConfig.getPath(nom);

        // vérifications
        assertNotNull(pathParameter);
        assertEquals(path, pathParameter.getPath());
        assertEquals(readOnly, pathParameter.isReadonly());
    }

    @Test
    @DisplayName("getPath sur un nom qui existe (deux configurations)")
    void getPath_test2() throws IOException {
        LOGGER.info("addPath_test2");
        VFS4JConfig vfs4JConfig = new VFS4JConfig();

        String nom = "name1";
        Path path = createTempPath("/tmp/dddd");
        boolean readOnly = true;

        vfs4JConfig.addPath(nom, path, readOnly);

        final String nom2 = "ggg";
        final Path path2 = createTempPath("/tmp/mmm");
        final boolean readonly2 = false;
        vfs4JConfig.addPath(nom2, path2, readonly2);

        // methode testée
        PathParameter pathParameter = vfs4JConfig.getPath(nom2);

        // vérifications
        assertNotNull(pathParameter);
        assertEquals(path2, pathParameter.getPath());
        assertEquals(readonly2, pathParameter.isReadonly());
    }

    @Test
    @DisplayName("getPath sur un nom qui n'existe pas (une seule configuration)")
    void getPath_test3() throws IOException {
        LOGGER.info("addPath_test3");
        VFS4JConfig vfs4JConfig = new VFS4JConfig();

        String nom = "name01";
        Path path = createTempPath("/tmp/nnn");
        boolean readOnly = true;

        vfs4JConfig.addPath(nom, path, readOnly);

        // methode testée
        PathParameter pathParameter = vfs4JConfig.getPath("name02");

        // vérifications
        assertNull(pathParameter);
    }

    @Test
    @DisplayName("getPath sur un nom qui n'existe pas (aucune configuration)")
    void getPath_test4() {
        LOGGER.info("addPath_test4");
        VFS4JConfig vfs4JConfig = new VFS4JConfig();

        // methode testée
        PathParameter pathParameter = vfs4JConfig.getPath("name03");

        // vérifications
        assertNull(pathParameter);
    }

    private static Stream<Arguments> getPath_test5Parameter() throws IOException {
        Path tempDirectory = Files.createTempDirectory("junit5_vfs4j");
        return Stream.of(
                Arguments.of("name1", true, createPath(concatPath(tempDirectory, "/tmp/ccc1")), true, tempDirectory),
                Arguments.of("name2", true, createPath(concatPath(tempDirectory, "/tmp/ccc2")), false, tempDirectory),
                Arguments.of("name3", true, createPath(concatPath(tempDirectory, "/tmp/ccc3")), true, tempDirectory),
                Arguments.of("name4", true, createPath(concatPath(tempDirectory, "/tmp/ccc4")), false, tempDirectory),
                Arguments.of("name5", true, createPath(concatPath(tempDirectory, "/tmp/ccc5")), true, tempDirectory),
                Arguments.of("name6", true, createPath(concatPath(tempDirectory, "/tmp/ccc6")), false, tempDirectory),
                Arguments.of("name7", true, createPath(concatPath(tempDirectory, "/tmp/ccc7")), true, tempDirectory),
                Arguments.of("name8", true, createPath(concatPath(tempDirectory, "/tmp/ccc8")), false, tempDirectory),
                Arguments.of("name9", true, createPath(concatPath(tempDirectory, "/tmp/ccc9")), true, tempDirectory),
                Arguments.of("name10", true, createPath(concatPath(tempDirectory, "/tmp/ccc10")), false, tempDirectory),
                Arguments.of("name11", false, null, false, tempDirectory),
                Arguments.of("name0", false, null, false, tempDirectory),
                Arguments.of("name", false, null, false, tempDirectory)
        );
    }


    @ParameterizedTest
    @MethodSource("getPath_test5Parameter")
    @DisplayName("getPath sur un nom qui existe (10 configurations)")
    void getPath_test5(final String nameRef, final boolean existRef, final Path pathRef,
                       final boolean readonlyRef, final Path tempDirectory) throws IOException {
        LOGGER.info("getPath_test5");

        assertNotNull(nameRef);

        if (existRef) {
            assertNotNull(pathRef);
        } else {
            assertNull(pathRef);
        }

        VFS4JConfig vfs4JConfig = new VFS4JConfig();

        for (int i = 0; i < 10; i++) {
            int no = i + 1;

            String nom = "name" + no;
            Path path = createPath(concatPath(tempDirectory, "/tmp/ccc" + no));
            boolean readOnly = (no % 2 == 1);

            vfs4JConfig.addPath(nom, path, readOnly);
        }

        // methode testée
        PathParameter pathParameter = vfs4JConfig.getPath(nameRef);

        // vérifications
        if (!existRef) {
            assertNull(pathParameter);
        } else {
            assertNotNull(pathParameter);
            assertEquals(pathRef, pathParameter.getPath());
            assertEquals(readonlyRef, pathParameter.isReadonly());
        }
    }

    // methodes utilitaires

    private static Path createPath(Path path) throws IOException {
        return Files.createDirectories(path);
    }

    private Path createTempPath(String path) throws IOException {
        return Files.createDirectories(concatPath(tempDir, path));
    }

    private static Path concatPath(Path path, String s) {
        if (s.startsWith("/")) {
            return path.resolve(s.substring(1));
        } else {
            return path.resolve(s);
        }
    }
}