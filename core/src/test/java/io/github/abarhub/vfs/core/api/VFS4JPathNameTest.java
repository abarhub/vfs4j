package io.github.abarhub.vfs.core.api;

import io.github.abarhub.vfs.core.exception.VFS4JInvalideParameterException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class VFS4JPathNameTest {

    @Test
    void testConstructeurOK() {
        // methode testée
        VFS4JPathName VFS4JPathName = new VFS4JPathName("aaa", "/test/test1");

        // verifications
        assertEquals("aaa", VFS4JPathName.getName());
        assertEquals("/test/test1", VFS4JPathName.getPath());
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
        VFS4JPathName VFS4JPathName = new VFS4JPathName(name, path);

        // verifications
        assertEquals(name, VFS4JPathName.getName());
        assertEquals(path, VFS4JPathName.getPath());
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
            VFS4JPathName VFS4JPathName = new VFS4JPathName(name, path);

            fail("Erreur");

        } catch (VFS4JInvalideParameterException e) {
            assertEquals(messageError, e.getMessage());
        }
    }

    private static Stream<Arguments> provideTestGetParent() {
        return Stream.of(
                Arguments.of("path1", "/aaa/bbb/ccc", "/aaa/bbb"),
                Arguments.of("path1", "/aaa/bbb", "/aaa"),
                Arguments.of("path1", "/aaa", ""),
                Arguments.of("path1", "/", ""),
                Arguments.of("path1", "", ""),
                Arguments.of("path1", "/aaa/bbb/ccc/", "/aaa/bbb/ccc"),
                Arguments.of("path1", "/aaa/bbb/ccc/..", "/aaa/bbb/ccc")
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestGetParent")
    void testGetParent(final String name, final String path, String pathParent) {

        VFS4JPathName VFS4JPathName = new VFS4JPathName(name, path);

        // methode testée
        VFS4JPathName res = VFS4JPathName.getParent();

        // vérifications
        assertNotNull(res);
        assertEquals(name, res.getName());
        assertEquals(pathParent, res.getPath());
    }

    private static Stream<Arguments> provideTestResolve() {
        return Stream.of(
                Arguments.of("path1", "/aaa/bbb/ccc", "ddd", "/aaa/bbb/ccc/ddd"),
                Arguments.of("path1", "/aaa/bbb/ccc", "/ddd", "/aaa/bbb/ccc/ddd"),
                Arguments.of("path1", "/aaa/bbb/ccc/", "ddd", "/aaa/bbb/ccc/ddd"),
                Arguments.of("path1", "/aaa/bbb/ccc", "ddd/", "/aaa/bbb/ccc/ddd/"),
                Arguments.of("path1", "aaa/bbb/ccc", "ddd", "aaa/bbb/ccc/ddd"),
                Arguments.of("path1", "/aaa/bbb/ccc", "", "/aaa/bbb/ccc"),
                Arguments.of("path1", "", "/aaa/bbb/ccc", "/aaa/bbb/ccc"),
                Arguments.of("path1", "", "", ""),
                Arguments.of("path1", "/", "", "/"),
                Arguments.of("path1", "", "/", "/")
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestResolve")
    void testResolve(final String name, final String path, final String pathToAppend, final String pathResult) {

        VFS4JPathName VFS4JPathName = new VFS4JPathName(name, path);

        // methode testée
        VFS4JPathName res = VFS4JPathName.resolve(pathToAppend);

        // vérifications
        assertNotNull(res);
        assertEquals(name, res.getName());
        assertEquals(pathResult, res.getPath());
    }

    private static Stream<Arguments> provideTestGetNameCount() {
        return Stream.of(
                Arguments.of("path1", "/aaa/bbb/ccc", 3),
                Arguments.of("path1", "/aaa/bbb/ccc/", 3),
                Arguments.of("path1", "/aaa/bbb/ccc", 3),
                Arguments.of("path1", "/aaa/bbb/", 2),
                Arguments.of("path1", "aaa/bbb", 2),
                Arguments.of("path1", "/", 1),
                Arguments.of("path1", "", 1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestGetNameCount")
    void testGetNameCount(final String name, final String path, final int nb) {

        VFS4JPathName VFS4JPathName = new VFS4JPathName(name, path);

        // methode testée
        int res = VFS4JPathName.getNameCount();

        // vérifications
        assertEquals(nb, res);
    }

    private static Stream<Arguments> provideTestGetNameOK() {
        return Stream.of(
                Arguments.of("path1", "/aaa/bbb/ccc", 0, "aaa"),
                Arguments.of("path1", "/aaa/bbb/ccc", 1, "bbb"),
                Arguments.of("path1", "/aaa/bbb/ccc", 2, "ccc"),
                Arguments.of("path1", "/aaa/bbb/ccc/", 2, "ccc"),
                Arguments.of("path1", "aaa/bbb/ccc", 0, "aaa"),
                Arguments.of("path1", "aaa/bbb/ccc", 1, "bbb"),
                Arguments.of("path1", "aaa/bbb/ccc", 2, "ccc"),
                Arguments.of("path1", "aaa//bbb/ccc/", 1, "")
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestGetNameOK")
    void testGetNameOK(final String name, final String path, final int nb, final String pathResult) {

        VFS4JPathName VFS4JPathName = new VFS4JPathName(name, path);

        // methode testée
        String res = VFS4JPathName.getName(nb);

        // vérifications
        assertNotNull(res);
        assertEquals(pathResult, res);
    }

    private static Stream<Arguments> provideTestGetNameKO() {
        return Stream.of(
                Arguments.of("path1", "/aaa/bbb/ccc", -1),
                Arguments.of("path1", "/aaa/bbb/ccc", 3)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestGetNameKO")
    void testGetNameKO(final String name, final String path, final int nb) {

        VFS4JPathName VFS4JPathName = new VFS4JPathName(name, path);

        // methode testée
        VFS4JInvalideParameterException res = assertThrows(VFS4JInvalideParameterException.class, () -> VFS4JPathName.getName(nb));

        // vérifications
        assertNotNull(res);
        assertEquals("No is invalid", res.getMessage());
    }

    private static Stream<Arguments> provideTestNormalize() {
        return Stream.of(
                Arguments.of("path1", "/aaa/bbb/ccc", "/aaa/bbb/ccc"),
                Arguments.of("path1", "aaa/bbb/ccc", "aaa/bbb/ccc"),
                Arguments.of("path1", "aaa/bbb/ccc/", "aaa/bbb/ccc/"),
                Arguments.of("path1", "//aaa/bbb/ccc", "//aaa/bbb/ccc"),
                Arguments.of("path1", "aaa/bbb/ccc//", "aaa/bbb/ccc//"),
                Arguments.of("path1", "aaa/bbb/./ccc", "aaa/bbb/ccc"),
                Arguments.of("path1", "aaa/bbb/../ccc", "aaa/ccc"),
                Arguments.of("path1", "aaa/./bbb/./ccc", "aaa/bbb/ccc"),
                Arguments.of("path1", "aaa/../bbb/../ccc", "ccc"),
                Arguments.of("path1", "aaa/../..", ""),
                Arguments.of("path1", "..", ""),
                Arguments.of("path1", ".", ""),
                Arguments.of("path1", "././.", ""),
                Arguments.of("path1", "../../..", ""),
                Arguments.of("path1", "aaa//bbb/ccc/", "aaa/bbb/ccc/")
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestNormalize")
    void testNormalize(final String name, final String path, final String pathResult) {

        VFS4JPathName VFS4JPathName = new VFS4JPathName(name, path);

        // methode testée
        VFS4JPathName res = VFS4JPathName.normalize();

        // vérifications
        assertNotNull(res);
        assertEquals(name, res.getName());
        assertEquals(pathResult, res.getPath());
    }

    private static Stream<Arguments> provideTestRelativizeOK() {
        return Stream.of(
                Arguments.of("path1", "aaa/bbb", "aaa/bbb/ccc", "ccc"),
                Arguments.of("path1", "aaa/bbb/", "aaa/bbb/ccc", "ccc"),
                Arguments.of("path1", "aaa", "aaa/bbb/ccc", "bbb/ccc"),
                Arguments.of("path1", "", "aaa/bbb/ccc", "aaa/bbb/ccc")
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestRelativizeOK")
    void testRelativizeOK(final String name, final String path, final String pathRelativize, final String pathResult) {

        VFS4JPathName VFS4JPathName = new VFS4JPathName(name, path);

        // methode testée
        String res = VFS4JPathName.relativize(pathRelativize);

        // vérifications
        assertNotNull(res);
        assertEquals(pathResult, res);
    }

    private static Stream<Arguments> provideTestRelativizeKO() {
        return Stream.of(
                Arguments.of("path1", "aaa/bbb", "aaa2/bbb/ccc", "Path parameter must start with path"),
                Arguments.of("path1", "aaa/bbb/", null, "Path must not be empty"),
                Arguments.of("path1", "aaa/bbb/", "", "Path must not be empty")
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestRelativizeKO")
    void testRelativizeKO(final String name, final String path, final String pathRelativize, final String messageError) {

        VFS4JPathName VFS4JPathName = new VFS4JPathName(name, path);

        // methode testée
        VFS4JInvalideParameterException res = assertThrows(VFS4JInvalideParameterException.class,
                () -> VFS4JPathName.relativize(pathRelativize));

        // vérifications
        assertNotNull(res);
        assertEquals(messageError, res.getMessage());
    }

    private static Stream<Arguments> provideTestStartsWithOK() {
        return Stream.of(
                Arguments.of("path1", "aaa/bbb/ccc", "aaa", true),
                Arguments.of("path1", "aaa/bbb/ccc", "aaa2", false),
                Arguments.of("path1", "aaa/bbb/ccc", "", true),
                Arguments.of("path1", "aaa/bbb/ccc", "aaa/bbb", true),
                Arguments.of("path1", "aaa/bbb/ccc", "aaa/bbb/", true),
                Arguments.of("path1", "aaa/bbb/ccc", "aaa/bbb/ccc", true),
                Arguments.of("path1", "aaa/bbb/ccc", "aaa/bbb2/ccc", false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestStartsWithOK")
    void testStartsWithOK(final String name, final String path, final String pathToTest, final boolean result) {

        VFS4JPathName VFS4JPathName = new VFS4JPathName(name, path);

        // methode testée
        boolean res = VFS4JPathName.startsWith(pathToTest);

        // vérifications
        assertNotNull(res);
        assertEquals(result, res);
    }

    private static Stream<Arguments> provideTestStartsWithKO() {
        return Stream.of(
                Arguments.of("path1", "aaa/bbb", null, "Path must not be null")
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestStartsWithKO")
    void testStartsWithKO(final String name, final String path, final String pathToTest, final String messageError) {

        VFS4JPathName VFS4JPathName = new VFS4JPathName(name, path);

        // methode testée
        VFS4JInvalideParameterException res = assertThrows(VFS4JInvalideParameterException.class,
                () -> VFS4JPathName.startsWith(pathToTest));

        // vérifications
        assertNotNull(res);
        assertEquals(messageError, res.getMessage());
    }
    // .................


    private static Stream<Arguments> provideTestEndsWithOK() {
        return Stream.of(
                Arguments.of("path1", "aaa/bbb/ccc", "ccc", true),
                Arguments.of("path1", "aaa/bbb/ccc", "ccc2", false),
                Arguments.of("path1", "aaa/bbb/ccc", "", true),
                Arguments.of("path1", "aaa/bbb/ccc", "bbb/ccc", true),
                Arguments.of("path1", "aaa/bbb/ccc", "/bbb/ccc", true),
                Arguments.of("path1", "aaa/bbb/ccc", "aaa/bbb/ccc", true),
                Arguments.of("path1", "aaa/bbb/ccc", "aaa/bbb2/ccc", false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestEndsWithOK")
    void testEndsWithOK(final String name, final String path, final String pathToTest, final boolean result) {

        VFS4JPathName VFS4JPathName = new VFS4JPathName(name, path);

        // methode testée
        boolean res = VFS4JPathName.endsWith(pathToTest);

        // vérifications
        assertNotNull(res);
        assertEquals(result, res);
    }

    private static Stream<Arguments> provideTestEndsWithKO() {
        return Stream.of(
                Arguments.of("path1", "aaa/bbb", null, "Path must not be null")
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestEndsWithKO")
    void testEndsWithKO(final String name, final String path, final String pathToTest, final String messageError) {

        VFS4JPathName VFS4JPathName = new VFS4JPathName(name, path);

        // methode testée
        VFS4JInvalideParameterException res = assertThrows(VFS4JInvalideParameterException.class,
                () -> VFS4JPathName.endsWith(pathToTest));

        // vérifications
        assertNotNull(res);
        assertEquals(messageError, res.getMessage());
    }
}