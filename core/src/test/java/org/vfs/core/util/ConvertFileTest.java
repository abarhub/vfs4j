package org.vfs.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vfs.core.api.PathName;
import org.vfs.core.config.VFS4JConfig;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConvertFileTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertFileTest.class);

    @Test
    @DisplayName("getRealFile sur un fichier")
    public void getRealFile_test1() {
        LOGGER.info("getRealFile_test1");
        VFS4JConfig vfs4JConfig;
        vfs4JConfig = new VFS4JConfig();
        vfs4JConfig.addPath("nom", Paths.get("/tmp/test"), false);
        ConvertFile convertFile = new ConvertFile(vfs4JConfig);

        // methode testée
        Path path = convertFile.getRealFile(new PathName("nom", "fichier.txt"));

        // vérifications
        assertNotNull(path);
        assertEquals(Paths.get("/tmp/test/fichier.txt"), path);
    }

    @Test
    @DisplayName("getRealFile sur un fichier dans un répertoire")
    public void getRealFile_test2() {
        LOGGER.info("getRealFile_test2");
        VFS4JConfig vfs4JConfig;
        vfs4JConfig = new VFS4JConfig();
        vfs4JConfig.addPath("nom", Paths.get("/tmp/test"), false);
        ConvertFile convertFile = new ConvertFile(vfs4JConfig);

        // methode testée
        Path path = convertFile.getRealFile(new PathName("nom", "doc/fichier2.txt"));

        // vérifications
        assertNotNull(path);
        assertEquals(Paths.get("/tmp/test/doc/fichier2.txt"), path);
    }

    private static Stream<Arguments> getRealFile_test3Parameter() {
        return Stream.of(
                Arguments.of("nom", "/tmp/test", "fichier.txt", "/tmp/test/fichier.txt"),
                Arguments.of("nom2", "/tmp/test", "temp/fichier.txt", "/tmp/test/temp/fichier.txt"),
                Arguments.of("nom2", "/tmp/test2", "temp/monfichier.txt", "/tmp/test2/temp/monfichier.txt"),
                Arguments.of("nom3", "/tmp/test3", "temp/../monfichier3.txt", "/tmp/test3/monfichier3.txt"),
                Arguments.of("nom4", "/tmp", "../monfichier4.txt", "/tmp/monfichier4.txt")
        );
    }

    @ParameterizedTest
    @MethodSource("getRealFile_test3Parameter")
    @DisplayName("getRealFile sur un fichier dans un répertoire")
    public void getRealFile_test3(final String nameRef, final String pathRoot, final String path, final String pathRef) {
        LOGGER.info("getRealFile_test3({},{},{},{})", nameRef, pathRoot, path, pathRef);
        VFS4JConfig vfs4JConfig;
        vfs4JConfig = new VFS4JConfig();
        vfs4JConfig.addPath(nameRef, Paths.get(pathRoot), false);
        ConvertFile convertFile = new ConvertFile(vfs4JConfig);

        // methode testée
        Path pathRes = convertFile.getRealFile(new PathName(nameRef, path));

        // vérifications
        LOGGER.info("pathRes={}", pathRes);
        assertNotNull(pathRes);
        assertEquals(Paths.get(pathRef), pathRes);
    }

    private static Stream<Arguments> convertFromRealPath_test() {
        return Stream.of(
                Arguments.of(Optional.of(createPathName("nom", "bbb/ccc")), "/tmp/aaa/bbb/ccc",
                        createVFSConfig("nom", "/tmp/aaa")),
                Arguments.of(Optional.empty(), "/tmp/aaa2/bbb/ccc",
                        createVFSConfig("nom", "/tmp/aaa")),
                Arguments.of(Optional.of(createPathName("nom", "bbb/ccc")), "/tmp/aaa/bbb/ccc",
                        createVFSConfig("nom", "/tmp/aaa", "nom2", "/tmp/aaa2")),
                Arguments.of(Optional.of(createPathName("nom2", "bbb/ccc")), "/tmp/aaa2/bbb/ccc",
                        createVFSConfig("nom", "/tmp/aaa", "nom2", "/tmp/aaa2")),
                Arguments.of(Optional.of(createPathName("nom3", "ccc")), "/tmp/aaa/bbb/ccc",
                        createVFSConfig("nom", "/tmp/aaa", "nom2", "/tmp/aaa2", "nom3", "/tmp/aaa/bbb")),
                Arguments.of(Optional.empty(), "/tmp/aaaa/bbb/ccc",
                        createVFSConfig("nom", "/tmp/aaa", "nom2", "/tmp/aaa2")),
                Arguments.of(Optional.of(createPathName("nom", "aaa/bbb/ccc")), "/tmp/aaa/bbb/ccc",
                        createVFSConfig("nom", "/tmp")),
                Arguments.of(Optional.empty(), "/tmp/aaa",
                        createVFSConfig("nom", "/tmp/aaa/bbb")),
                Arguments.of(Optional.of(createPathName("nom", "")), "/tmp/aaa",
                        createVFSConfig("nom", "/tmp/aaa")),
                Arguments.of(Optional.of(createPathName("nom", "bbb/ccc")), "/tmp/aaa/ddd/../bbb/ccc",
                        createVFSConfig("nom", "/tmp/aaa")),
                Arguments.of(Optional.of(createPathName("nom", "bbb/ccc")), "/tmp/ddd/../aaa/bbb/ccc",
                        createVFSConfig("nom", "/tmp/aaa")),
                Arguments.of(Optional.of(createPathName("nom", "bbb/ccc")), "/tmp/aaa/./bbb/ccc",
                        createVFSConfig("nom", "/tmp/aaa")),
                Arguments.of(Optional.of(createPathName("nom", "bbb/ccc")), "/tmp/./aaa/bbb/ccc",
                        createVFSConfig("nom", "/tmp/aaa")),
                Arguments.of(Optional.empty(), "/tmp/aaa/../../ggg",
                        createVFSConfig("nom", "/tmp/aaa/bbb")),
                Arguments.of(Optional.of(createPathName("nom", "ggg")), "/tmp/aaa/bbb//ggg",
                        createVFSConfig("nom", "/tmp/aaa/bbb")),
                Arguments.of(Optional.of(createPathName("nom", "ggg/hhh")), "/tmp/aaa/bbb/ggg//hhh",
                        createVFSConfig("nom", "/tmp/aaa/bbb")),
                Arguments.of(Optional.of(createPathName("nom", "ggg")), "/tmp/aaa//bbb/ggg",
                        createVFSConfig("nom", "/tmp/aaa/bbb"))
        );
    }

    @ParameterizedTest
    @MethodSource("convertFromRealPath_test")
    @DisplayName("convertFromRealPath pour vérifier la convertion d'un chemin reel vers un PathName")
    public void convertFromRealPath(Optional<PathName> pathRef, String path, VFS4JConfig vfs4JConfig) {
        LOGGER.info("convertFromRealPath({},{},{})", pathRef, path, vfs4JConfig);
        ConvertFile convertFile = new ConvertFile(vfs4JConfig);

        // methode testée
        Optional<PathName> pathRes = convertFile.convertFromRealPath(Paths.get(path));

        // vérifications
        LOGGER.info("pathRes={}", pathRes);
        assertEquals(pathRef, pathRes);
    }

    // methodes utilitaires

    private static VFS4JConfig createVFSConfig(String... parameters) {
        VFS4JConfig vfs4JConfig;
        vfs4JConfig = new VFS4JConfig();
        if (parameters != null && parameters.length > 0) {
            assertEquals(0, parameters.length % 2);
            for (int i = 0; i < parameters.length; i += 2) {
                String nameRef = parameters[i];
                String pathRoot = parameters[i + 1];
                assertNotNull(nameRef);
                assertNotNull(pathRoot);
                vfs4JConfig.addPath(nameRef, Paths.get(pathRoot), false);
            }
        }
        return vfs4JConfig;
    }

    private static PathName createPathName(String name, String path) {
        assertNotNull(name);
        assertNotNull(path);
        return new PathName(name, path);
    }

}