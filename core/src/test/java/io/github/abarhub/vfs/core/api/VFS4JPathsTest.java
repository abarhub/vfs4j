package io.github.abarhub.vfs.core.api;

import io.github.abarhub.vfs.core.exception.VFS4JInvalideParameterException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class VFS4JPathsTest {

    @Test
    void getPathEmptyOK() {
        final String name = "name1";
        final String pathRef = "";

        // methode testée
        VFS4JPathName res = VFS4JPaths.get(name);

        // vérifications
        assertNotNull(res);
        assertEquals(name, res.getName());
        assertEquals(pathRef, res.getPath());
    }

    private static Stream<Arguments> provideGetWithPathOK() {
        return Stream.of(
                Arguments.of("name2", "abc", new String[]{"abc"}),
                Arguments.of("name2", "/abc", new String[]{"/abc"}),
                Arguments.of("name2", "/abc/def", new String[]{"/abc/def"}),
                Arguments.of("name2", "/abc/def", new String[]{"/abc", "def"}),
                Arguments.of("name2", "/abc/def/ghi", new String[]{"/abc", "def", "ghi"}),
                Arguments.of("name3", "", new String[]{""}),
                Arguments.of("name4", "/", new String[]{"/"}),
                Arguments.of("name4", "/test", new String[]{"/test"}),
                Arguments.of("name4", "\\test2\\zz\\gg", new String[]{"\\test2\\zz\\gg"}),
                Arguments.of("name4", "\\test3/tt\\gg", new String[]{"\\test3", "tt\\gg"})
        );
    }

    @ParameterizedTest
    @MethodSource("provideGetWithPathOK")
    void getWithPathOK(String name, String pathRef, String[] path) {

        // methode testée
        VFS4JPathName res = VFS4JPaths.get(name, path);

        // vérifications
        assertNotNull(res);
        assertEquals(name, res.getName());
        assertEquals(pathRef, res.getPath());
    }

    @Test
    void getPathNameEmptyKO() {
        final String name = "";

        // methode testée
        VFS4JInvalideParameterException res = assertThrows(VFS4JInvalideParameterException.class,
                () -> VFS4JPaths.get(name));

        // vérifications
        assertNotNull(res);
        assertEquals("Name is empty", res.getMessage());
    }

    @Test
    void getPathNameNullKO() {
        final String name = null;

        // methode testée
        VFS4JInvalideParameterException res = assertThrows(VFS4JInvalideParameterException.class,
                () -> VFS4JPaths.get(name));

        // vérifications
        assertNotNull(res);
        assertEquals("Name is empty", res.getMessage());
    }

}