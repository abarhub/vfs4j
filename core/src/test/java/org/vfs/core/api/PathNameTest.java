package org.vfs.core.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.vfs.core.exception.VFS4JInvalideParameterException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class PathNameTest {

    @Test
    void testConstructeurOK() {
        // methode testée
        PathName pathName = new PathName("aaa", "/test/test1");

        // verifications
        assertEquals("aaa", pathName.getName());
        assertEquals("/test/test1", pathName.getPath());
    }

    private static Stream<Arguments> provideTestConstructeurParametersOK() {
        return Stream.of(
                Arguments.of("aaa", "/test2"),
                Arguments.of("bbb", "/test3"),
                Arguments.of("ccc", "test4"),
                Arguments.of("ccc", "test5/test05/ddd"),
                Arguments.of("ddd", ""),
                Arguments.of("eee2", "/test5"),
                Arguments.of("f", "/test6")
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestConstructeurParametersOK")
    void testConstructeurParametersOK(final String name, final String path) {
        // methode testée
        PathName pathName = new PathName(name, path);

        // verifications
        assertEquals(name, pathName.getName());
        assertEquals(path, pathName.getPath());
    }

    private static Stream<Arguments> provideTestConstructeurParametersKO() {
        return Stream.of(
                Arguments.of(null, "/test", "Name is empty"),
                Arguments.of("", "/test", "Name is empty"),
                Arguments.of("aaa", null, "Path is null"),
                Arguments.of("AAA", "/test", "Name is invalide"),
                Arguments.of("123", "/test", "Name is invalide"),
                Arguments.of(" aaa", "/test", "Name is invalide"),
                Arguments.of("a aa", "/test", "Name is invalide"),
                Arguments.of("aaa ", "/test", "Name is invalide"),
                Arguments.of("aAa", "/test", "Name is invalide")
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestConstructeurParametersKO")
    void testConstructeurParametersKO(final String name, final String path, String messageError) {
        try {
            // methode testée
            PathName pathName = new PathName(name, path);

            fail("Erreur");

        } catch (VFS4JInvalideParameterException e) {
            assertEquals(messageError, e.getMessage());
        }
    }
}