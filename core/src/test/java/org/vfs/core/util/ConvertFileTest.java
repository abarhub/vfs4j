package org.vfs.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vfs.core.api.PathName;
import org.vfs.core.config.VFS4JConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConvertFileTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertFileTest.class);

    @TempDir
    public Path tempDir;

    @Test
    @DisplayName("getRealFile sur un fichier")
    public void getRealFile_test1() throws IOException {
        LOGGER.info("getRealFile_test1");
        VFS4JConfig vfs4JConfig;
        vfs4JConfig = new VFS4JConfig();
        vfs4JConfig.addPath("nom", createTempPath("/tmp/test"), false);
        ConvertFile convertFile = new ConvertFile(vfs4JConfig);

        // methode testée
        Path path = convertFile.getRealFile(new PathName("nom", "fichier.txt"));

        // vérifications
        assertNotNull(path);
        assertEquals(concatPath(tempDir, "/tmp/test/fichier.txt"), path);
    }

    @Test
    @DisplayName("getRealFile sur un fichier dans un répertoire")
    public void getRealFile_test2() throws IOException {
        LOGGER.info("getRealFile_test2");
        VFS4JConfig vfs4JConfig;
        vfs4JConfig = new VFS4JConfig();
        vfs4JConfig.addPath("nom", createTempPath("/tmp/test"), false);
        ConvertFile convertFile = new ConvertFile(vfs4JConfig);

        // methode testée
        Path path = convertFile.getRealFile(new PathName("nom", "doc/fichier2.txt"));

        // vérifications
        assertNotNull(path);
        assertEquals(concatPath(tempDir, "/tmp/test/doc/fichier2.txt"), path);
    }

    private static Stream<Arguments> getRealFile_test3Parameter() throws IOException {
        Path tempDir = Files.createTempDirectory("junit5_vfs4j");
        return Stream.of(
                Arguments.of("nom", "/tmp/test", "fichier.txt", "/tmp/test/fichier.txt", tempDir),
                Arguments.of("nom2", "/tmp/test", "temp/fichier.txt", "/tmp/test/temp/fichier.txt", tempDir),
                Arguments.of("nom2", "/tmp/test2", "temp/monfichier.txt", "/tmp/test2/temp/monfichier.txt", tempDir),
                Arguments.of("nom3", "/tmp/test3", "temp/../monfichier3.txt", "/tmp/test3/monfichier3.txt", tempDir),
                Arguments.of("nom4", "/tmp", "../monfichier4.txt", "/tmp/monfichier4.txt", tempDir)
        );
    }

    @ParameterizedTest
    @MethodSource("getRealFile_test3Parameter")
    @DisplayName("getRealFile sur un fichier dans un répertoire")
    public void getRealFile_test3(final String nameRef, final String pathRoot,
                                  final String path, final String pathRef,
                                  final Path tempDir) throws IOException {
        LOGGER.info("getRealFile_test3({},{},{},{})", nameRef, pathRoot, path, pathRef);
        VFS4JConfig vfs4JConfig;
        vfs4JConfig = new VFS4JConfig();
        vfs4JConfig.addPath(nameRef, createPath(concatPath(tempDir, pathRoot)), false);
        ConvertFile convertFile = new ConvertFile(vfs4JConfig);

        // methode testée
        Path pathRes = convertFile.getRealFile(new PathName(nameRef, path));

        // vérifications
        LOGGER.info("pathRes={}", pathRes);
        assertNotNull(pathRes);
        assertEquals(concatPath(tempDir, pathRef), pathRes);
    }

    private static Stream<Arguments> convertFromRealPath_test() throws IOException {
        Path tempDir = Files.createTempDirectory("junit5_vfs4j");
        LOGGER.info("tempDir={}", tempDir);
        return Stream.of(
                Arguments.of(Optional.of(createPathName("nom", "bbb/ccc")), "/tmp/aaa/bbb/ccc",
                        createVFSConfig(tempDir, "nom", "/tmp/aaa"), tempDir),
                Arguments.of(Optional.empty(), "/tmp/aaa2/bbb/ccc",
                        createVFSConfig(tempDir, "nom", "/tmp/aaa"), tempDir),
                Arguments.of(Optional.of(createPathName("nom", "bbb/ccc")), "/tmp/aaa/bbb/ccc",
                        createVFSConfig(tempDir, "nom", "/tmp/aaa", "nom2", "/tmp/aaa2"), tempDir),
                Arguments.of(Optional.of(createPathName("nom2", "bbb/ccc")), "/tmp/aaa2/bbb/ccc",
                        createVFSConfig(tempDir, "nom", "/tmp/aaa", "nom2", "/tmp/aaa2"), tempDir),
                Arguments.of(Optional.of(createPathName("nom3", "ccc")), "/tmp/aaa/bbb/ccc",
                        createVFSConfig(tempDir, "nom", "/tmp/aaa", "nom2", "/tmp/aaa2",
                                "nom3", "/tmp/aaa/bbb"), tempDir),
                Arguments.of(Optional.empty(), "/tmp/aaaa/bbb/ccc",
                        createVFSConfig(tempDir, "nom", "/tmp/aaa", "nom2", "/tmp/aaa2"), tempDir),
                Arguments.of(Optional.of(createPathName("nom", "aaa/bbb/ccc")), "/tmp/aaa/bbb/ccc",
                        createVFSConfig(tempDir, "nom", "/tmp"), tempDir),
                Arguments.of(Optional.empty(), "/tmp/aaa",
                        createVFSConfig(tempDir, "nom", "/tmp/aaa/bbb"), tempDir),
                Arguments.of(Optional.of(createPathName("nom", "")), "/tmp/aaa",
                        createVFSConfig(tempDir, "nom", "/tmp/aaa"), tempDir),
                Arguments.of(Optional.of(createPathName("nom", "bbb/ccc")), "/tmp/aaa/ddd/../bbb/ccc",
                        createVFSConfig(tempDir, "nom", "/tmp/aaa"), tempDir),
                Arguments.of(Optional.of(createPathName("nom", "bbb/ccc")), "/tmp/ddd/../aaa/bbb/ccc",
                        createVFSConfig(tempDir, "nom", "/tmp/aaa"), tempDir),
                Arguments.of(Optional.of(createPathName("nom", "bbb/ccc")), "/tmp/aaa/./bbb/ccc",
                        createVFSConfig(tempDir, "nom", "/tmp/aaa"), tempDir),
                Arguments.of(Optional.of(createPathName("nom", "bbb/ccc")), "/tmp/./aaa/bbb/ccc",
                        createVFSConfig(tempDir, "nom", "/tmp/aaa"), tempDir),
                Arguments.of(Optional.empty(), "/tmp/aaa/../../ggg",
                        createVFSConfig(tempDir, "nom", "/tmp/aaa/bbb"), tempDir),
                Arguments.of(Optional.of(createPathName("nom", "ggg")), "/tmp/aaa/bbb//ggg",
                        createVFSConfig(tempDir, "nom", "/tmp/aaa/bbb"), tempDir),
                Arguments.of(Optional.of(createPathName("nom", "ggg/hhh")), "/tmp/aaa/bbb/ggg//hhh",
                        createVFSConfig(tempDir, "nom", "/tmp/aaa/bbb"), tempDir),
                Arguments.of(Optional.of(createPathName("nom", "ggg")), "/tmp/aaa//bbb/ggg",
                        createVFSConfig(tempDir, "nom", "/tmp/aaa/bbb"), tempDir)
        );
    }

    @ParameterizedTest
    @MethodSource("convertFromRealPath_test")
    @DisplayName("convertFromRealPath pour vérifier la convertion d'un chemin reel vers un PathName")
    public void convertFromRealPath(Optional<PathName> pathRef, String path,
                                    VFS4JConfig vfs4JConfig, Path tempDir) {
        LOGGER.info("convertFromRealPath({},{},{})", pathRef, path, vfs4JConfig);
        ConvertFile convertFile = new ConvertFile(vfs4JConfig);

        // methode testée
        Optional<PathName> pathRes = convertFile.convertFromRealPath(concatPath(tempDir, path));

        // vérifications
        LOGGER.info("pathRes={}", pathRes);
        assertEquals(pathRef, pathRes);
    }

    // methodes utilitaires

    private static VFS4JConfig createVFSConfig(Path pathRoot0, String... parameters) throws IOException {
        VFS4JConfig vfs4JConfig;
        vfs4JConfig = new VFS4JConfig();
        if (parameters != null && parameters.length > 0) {
            assertEquals(0, parameters.length % 2);
            for (int i = 0; i < parameters.length; i += 2) {
                String nameRef = parameters[i];
                String pathRoot = parameters[i + 1];
                assertNotNull(nameRef);
                assertNotNull(pathRoot);
                vfs4JConfig.addPath(nameRef, createPath(concatPath(pathRoot0, pathRoot)), false);
            }
        }
        return vfs4JConfig;
    }

    private static PathName createPathName(String name, String path) {
        assertNotNull(name);
        assertNotNull(path);
        return new PathName(name, path);
    }

    private static Path createPath(Path path) throws IOException {
        if (Files.notExists(path)) {
            LOGGER.debug("create path {}", path);
            return Files.createDirectories(path);
        } else {
            LOGGER.debug("path already exists {}", path);
            return path;
        }
    }

    private static Path concatPath(Path path, String s) {
        if (s.startsWith("/")) {
            return path.resolve(s.substring(1));
        } else {
            return path.resolve(s);
        }
    }

    private Path createTempPath(String path) throws IOException {
        return Files.createDirectories(concatPath(tempDir, path));
    }
}