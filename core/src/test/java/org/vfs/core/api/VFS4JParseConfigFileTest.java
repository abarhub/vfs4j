package org.vfs.core.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.vfs.core.config.VFS4JClasspathParameter;
import org.vfs.core.config.VFS4JConfig;
import org.vfs.core.config.VFS4JPathMode;
import org.vfs.core.config.VFS4JPathParameter;
import org.vfs.core.exception.VFS4JException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class VFS4JParseConfigFileTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Parse un fichier avec des paths")
    void parseWithPath() {
        final String dir1 = "dir1";
        final String path1 = tempDir.resolve("dir1").toString();
        final String dir2 = "dir2";
        final String path2 = tempDir.resolve("dir2").toString();
        Properties properties = new Properties();
        properties.put("vfs.paths.dir1.path", path1);
        properties.put("vfs.paths.dir2.path", path2);
        VFS4JParseConfigFile parseConfigFile = new VFS4JParseConfigFile();

        // methode testée
        VFS4JFileManagerBuilder res = parseConfigFile.parse(properties);

        // vérifications
        assertNotNull(res);
        VFS4JConfig res2 = res.build();
        assertNotNull(res2);
        List<String> liste = new ArrayList<>();
        liste.add(dir1);
        liste.add(dir2);
        assertEquals(new HashSet<>(liste), new HashSet<>(res2.getNames()));
        VFS4JPathParameter pathParameter = (VFS4JPathParameter) res2.getPath(dir1);
        assertNotNull(pathParameter);
        assertEquals(Paths.get(path1), pathParameter.getPath());
        assertFalse(pathParameter.isReadonly());
        assertEquals(VFS4JPathMode.STANDARD, pathParameter.getMode());
        pathParameter = (VFS4JPathParameter) res2.getPath(dir2);
        assertNotNull(pathParameter);
        assertEquals(Paths.get(path2), pathParameter.getPath());
        assertFalse(pathParameter.isReadonly());
        assertEquals(VFS4JPathMode.STANDARD, pathParameter.getMode());
    }

    @Test
    @DisplayName("Parse un fichier avec des temporary")
    void parseWithTemporary() {
        final String dir1 = "dir1";
        final String path1 = tempDir.resolve("dir1").toString();
        final String dir2 = "dir2";
        Properties properties = new Properties();
        properties.put("vfs.paths.dir1.path", path1);
        properties.put("vfs.paths.dir2.mode", VFS4JPathMode.TEMPORARY.getName());
        VFS4JParseConfigFile parseConfigFile = new VFS4JParseConfigFile();

        // methode testée
        VFS4JFileManagerBuilder res = parseConfigFile.parse(properties);

        // vérifications
        assertNotNull(res);
        VFS4JConfig res2 = res.build();
        assertNotNull(res2);
        List<String> liste = new ArrayList<>();
        liste.add(dir1);
        liste.add(dir2);
        assertEquals(new HashSet<>(liste), new HashSet<>(res2.getNames()));
        VFS4JPathParameter pathParameter = (VFS4JPathParameter) res2.getPath(dir1);
        assertNotNull(pathParameter);
        assertEquals(Paths.get(path1), pathParameter.getPath());
        assertFalse(pathParameter.isReadonly());
        assertEquals(VFS4JPathMode.STANDARD, pathParameter.getMode());
        pathParameter = (VFS4JPathParameter) res2.getPath(dir2);
        assertNotNull(pathParameter);
        assertNotNull(pathParameter.getPath());
        assertFalse(pathParameter.getPath().toString().isEmpty());
        assertFalse(pathParameter.getPath().toString().trim().isEmpty());
        assertTrue(Files.exists(pathParameter.getPath()));
        assertFalse(pathParameter.isReadonly());
        assertEquals(VFS4JPathMode.TEMPORARY, pathParameter.getMode());
    }

    @Test
    @DisplayName("Parse un fichier avec des temporary")
    void parseWithTemporary2() {
        final String dir1 = "dir01";
        final String path1 = tempDir.resolve("dir01").toString();
        final String dir2 = "dir02";
        Properties properties = new Properties();
        properties.put("vfs.paths.dir01.path", path1);
        properties.put("vfs.paths.dir01.mode", VFS4JPathMode.STANDARD.getName());
        properties.put("vfs.paths.dir02.mode", VFS4JPathMode.TEMPORARY.getName());
        VFS4JParseConfigFile parseConfigFile = new VFS4JParseConfigFile();

        // methode testée
        VFS4JFileManagerBuilder res = parseConfigFile.parse(properties);

        // vérifications
        assertNotNull(res);
        VFS4JConfig res2 = res.build();
        assertNotNull(res2);
        List<String> liste = new ArrayList<>();
        liste.add(dir1);
        liste.add(dir2);
        assertEquals(new HashSet<>(liste), new HashSet<>(res2.getNames()));
        VFS4JPathParameter pathParameter = (VFS4JPathParameter) res2.getPath(dir1);
        assertNotNull(pathParameter);
        assertEquals(Paths.get(path1), pathParameter.getPath());
        assertFalse(pathParameter.isReadonly());
        assertEquals(VFS4JPathMode.STANDARD, pathParameter.getMode());
        pathParameter = (VFS4JPathParameter) res2.getPath(dir2);
        assertNotNull(pathParameter);
        assertNotNull(pathParameter.getPath());
        assertFalse(pathParameter.getPath().toString().isEmpty());
        assertFalse(pathParameter.getPath().toString().trim().isEmpty());
        assertTrue(Files.exists(pathParameter.getPath()));
        assertFalse(pathParameter.isReadonly());
        assertEquals(VFS4JPathMode.TEMPORARY, pathParameter.getMode());
    }

    @Test
    @DisplayName("Parse un fichier avec un répertoire en readonly")
    void parseWithReadOnly() {
        final String dir1 = "dir1";
        final String path1 = tempDir.resolve("dir1").toString();
        final String dir2 = "dir2";
        final String path2 = tempDir.resolve("dir2").toString();
        Properties properties = new Properties();
        properties.put("vfs.paths." + dir1 + ".path", path1);
        properties.put("vfs.paths." + dir1 + ".readonly", "true");
        properties.put("vfs.paths." + dir2 + ".path", path2);
        properties.put("vfs.paths." + dir2 + ".readonly", "false");
        VFS4JParseConfigFile parseConfigFile = new VFS4JParseConfigFile();

        // methode testée
        VFS4JFileManagerBuilder res = parseConfigFile.parse(properties);

        // vérifications
        assertNotNull(res);
        VFS4JConfig res2 = res.build();
        assertNotNull(res2);
        List<String> liste = new ArrayList<>();
        liste.add(dir1);
        liste.add(dir2);
        assertEquals(new HashSet<>(liste), new HashSet<>(res2.getNames()));
        VFS4JPathParameter pathParameter = (VFS4JPathParameter) res2.getPath(dir1);
        assertNotNull(pathParameter);
        assertEquals(Paths.get(path1), pathParameter.getPath());
        assertTrue(pathParameter.isReadonly());
        assertEquals(VFS4JPathMode.STANDARD, pathParameter.getMode());
        pathParameter = (VFS4JPathParameter) res2.getPath(dir2);
        assertNotNull(pathParameter);
        assertEquals(Paths.get(path2), pathParameter.getPath());
        assertFalse(pathParameter.isReadonly());
        assertEquals(VFS4JPathMode.STANDARD, pathParameter.getMode());
    }

    @Test
    @DisplayName("Parse un fichier avec un classpath")
    void parseWithClasspath() {
        final String dir1 = "dir01";
        Properties properties = new Properties();
        properties.put("vfs.paths.dir01.readonly", "true");
        properties.put("vfs.paths.dir01.mode", VFS4JPathMode.CLASSPATH.getName());
        VFS4JParseConfigFile parseConfigFile = new VFS4JParseConfigFile();

        // methode testée
        VFS4JFileManagerBuilder res = parseConfigFile.parse(properties);

        // vérifications
        assertNotNull(res);
        VFS4JConfig res2 = res.build();
        assertNotNull(res2);
        List<String> liste = new ArrayList<>();
        liste.add(dir1);
        assertEquals(new HashSet<>(liste), new HashSet<>(res2.getNames()));
        VFS4JClasspathParameter pathParameter = (VFS4JClasspathParameter) res2.getPath(dir1);
        assertNotNull(pathParameter);
        assertEquals("", pathParameter.getPath());
        assertTrue(pathParameter.isReadonly());
        assertEquals(VFS4JPathMode.CLASSPATH, pathParameter.getMode());
    }

    @Test
    @DisplayName("Parse un fichier avec un classpath et un path")
    void parseWithClasspathAndPath() {
        final String dir1 = "dir01";
        Properties properties = new Properties();
        properties.put("vfs.paths.dir01.path", "/test1/");
        properties.put("vfs.paths.dir01.readonly", "true");
        properties.put("vfs.paths.dir01.mode", VFS4JPathMode.CLASSPATH.getName());
        VFS4JParseConfigFile parseConfigFile = new VFS4JParseConfigFile();

        // methode testée
        VFS4JFileManagerBuilder res = parseConfigFile.parse(properties);

        // vérifications
        assertNotNull(res);
        VFS4JConfig res2 = res.build();
        assertNotNull(res2);
        List<String> liste = new ArrayList<>();
        liste.add(dir1);
        assertEquals(new HashSet<>(liste), new HashSet<>(res2.getNames()));
        VFS4JClasspathParameter pathParameter = (VFS4JClasspathParameter) res2.getPath(dir1);
        assertNotNull(pathParameter);
        assertEquals("/test1/", pathParameter.getPath());
        assertTrue(pathParameter.isReadonly());
        assertEquals(VFS4JPathMode.CLASSPATH, pathParameter.getMode());
    }

    @Test
    @DisplayName("Parse un fichier avec un classpath et readonly=false")
    void parseWithClasspathAndReadonlyFalse() {
        Properties properties = new Properties();
        properties.put("vfs.paths.dir01.readonly", "false");
        properties.put("vfs.paths.dir01.mode", VFS4JPathMode.CLASSPATH.getName());
        VFS4JParseConfigFile parseConfigFile = new VFS4JParseConfigFile();

        // methode testée
        VFS4JException exception = assertThrows(VFS4JException.class,
                () -> parseConfigFile.parse(properties));

        // vérifications
        assertNotNull(exception);
        assertEquals("Path for 'dir01' with classpatch mode and readonly to false", exception.getMessage());
    }
}