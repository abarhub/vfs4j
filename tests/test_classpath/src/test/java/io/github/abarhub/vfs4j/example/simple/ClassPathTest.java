package io.github.abarhub.vfs4j.example.simple;

import com.google.common.base.Verify;
import io.github.abarhub.vfs.core.api.*;
import io.github.abarhub.vfs.core.api.path.VFS4JPathName;
import io.github.abarhub.vfs.core.api.path.VFS4JPaths;
import io.github.abarhub.vfs.core.config.VFS4JConfig;
import io.github.abarhub.vfs.core.config.VFS4JPathMode;
import io.github.abarhub.vfs.core.config.VFS4JPathParameter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class ClassPathTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathTest.class);

    @Test
    public void exemple1() throws IOException {

        LOGGER.info("exemple1 ...");

        Properties properties = new Properties();

        properties.setProperty("vfs.paths.rep01.mode", VFS4JPathMode.CLASSPATH.getName());
        properties.setProperty("vfs.paths.rep01.readonly", "true");
        properties.setProperty("vfs.paths.rep02.path", "/test");
        properties.setProperty("vfs.paths.rep02.mode", VFS4JPathMode.CLASSPATH.getName());
        properties.setProperty("vfs.paths.rep02.readonly", "true");
        properties.setProperty("vfs.paths.temp.mode", VFS4JPathMode.TEMPORARY.getName());

        VFS4JParseConfigFile parseConfigFile = new VFS4JParseConfigFile();
        VFS4JFileManagerBuilder fileManagerBuilder = parseConfigFile.parse(properties);

        VFS4JDefaultFileManager.get().setConfig(fileManagerBuilder.build());

        VFS4JConfig config = VFS4JDefaultFileManager.get().getConfig();

        Verify.verifyNotNull(config.getPath("temp"));

        VFS4JPathParameter parameter = (VFS4JPathParameter) config.getPath("temp");
        Path pathTemp = parameter.getPath();

        Path file3 = pathTemp.resolve("fichier03.txt");

        Files.deleteIfExists(file3);

        assertTrue(Files.exists(pathTemp));

        assertFalse(Files.exists(file3));

        VFS4JPathName pathName = VFS4JPaths.get("temp", "fichier03.txt");
        VFS4JFiles.createFile(pathName);

        assertTrue(Files.exists(file3));

        byte[] buf = "abc".getBytes(StandardCharsets.UTF_8);
        VFS4JFiles.write(pathName, buf);

        byte[] res = VFS4JFiles.readAllBytes(pathName);

        assertArrayEquals(buf, res);

        LOGGER.info("exemple1 OK");
    }

    @Test
    public void exemple2() throws IOException {

        LOGGER.info("exemple2 ...");

        Properties properties = new Properties();

        properties.setProperty("vfs.paths.rep01.mode", VFS4JPathMode.CLASSPATH.getName());
        properties.setProperty("vfs.paths.rep01.readonly", "true");
        properties.setProperty("vfs.paths.rep02.path", "/test");
        properties.setProperty("vfs.paths.rep02.mode", VFS4JPathMode.CLASSPATH.getName());
        properties.setProperty("vfs.paths.rep02.readonly", "true");
        properties.setProperty("vfs.paths.temp.mode", VFS4JPathMode.TEMPORARY.getName());

        VFS4JParseConfigFile parseConfigFile = new VFS4JParseConfigFile();
        VFS4JFileManagerBuilder fileManagerBuilder = parseConfigFile.parse(properties);

        VFS4JDefaultFileManager.get().setConfig(fileManagerBuilder.build());

        VFS4JConfig config = VFS4JDefaultFileManager.get().getConfig();

        Verify.verifyNotNull(config.getPath("temp"));

        VFS4JPathParameter parameter = (VFS4JPathParameter) config.getPath("temp");
        Path pathTemp = parameter.getPath();

        Path file3 = pathTemp.resolve("fichier03.txt");

        Files.deleteIfExists(file3);

        assertTrue(Files.exists(pathTemp));

        assertFalse(Files.exists(file3));

        // ecriture puis lecture du fichier temp:fichier03.txt
        VFS4JPathName pathName = VFS4JPaths.get("temp", "fichier03.txt");
        VFS4JFiles.createFile(pathName);

        assertTrue(Files.exists(file3));

        byte[] buf = "abc".getBytes(StandardCharsets.UTF_8);
        VFS4JFiles.write(pathName, buf);

        byte[] res = VFS4JFiles.readAllBytes(pathName);

        assertArrayEquals(buf, res);

        // lecture du fichier rep01:test/test1.txt
        VFS4JPathName pathName2 = VFS4JPaths.get("rep01", "test/test1.txt");

        List<String> res2 = VFS4JFiles.readAllLines(pathName2, StandardCharsets.UTF_8);

        List<String> ref2 = Arrays.asList("abc aaa", "bbbb ffff");

        assertEquals(ref2, res2);

        // lecture du fichier rep02:test1.txt
        VFS4JPathName pathName3 = VFS4JPaths.get("rep02", "test1.txt");

        List<String> res3 = VFS4JFiles.readAllLines(pathName3, StandardCharsets.UTF_8);

        List<String> ref3 = Arrays.asList("abc aaa", "bbbb ffff");

        assertEquals(ref3, res3);

        // copie de du fichier rep02:test1.txt vers temp:test1.txt
        VFS4JPathName pathName4 = VFS4JPaths.get("temp", "test1.txt");
        VFS4JFiles.copy(pathName3, pathName4);

        assertTrue(Files.exists(pathTemp.resolve("test1.txt")));

        List<String> res4 = VFS4JFiles.readAllLines(pathName3, StandardCharsets.UTF_8);

        assertEquals(ref3, res4);

        LOGGER.info("exemple2 OK");
    }

    @Test
    @Disabled
    public void exemple3() throws IOException {

        LOGGER.info("exemple3 ...");

        Properties properties = new Properties();

        properties.setProperty("vfs.paths.rep01.mode", VFS4JPathMode.CLASSPATH.getName());
        properties.setProperty("vfs.paths.rep01.readonly", "true");
        properties.setProperty("vfs.paths.rep02.path", "/test");
        properties.setProperty("vfs.paths.rep02.mode", VFS4JPathMode.CLASSPATH.getName());
        properties.setProperty("vfs.paths.rep02.readonly", "true");
        properties.setProperty("vfs.paths.temp.mode", VFS4JPathMode.TEMPORARY.getName());

        VFS4JParseConfigFile parseConfigFile = new VFS4JParseConfigFile();
        VFS4JFileManagerBuilder fileManagerBuilder = parseConfigFile.parse(properties);

        VFS4JDefaultFileManager.get().setConfig(fileManagerBuilder.build());

        VFS4JConfig config = VFS4JDefaultFileManager.get().getConfig();

        Verify.verifyNotNull(config.getPath("temp"));

        // lecture du fichier rep01:META-INF/MANIFEST.MF
        //VFS4JPathName pathName4 = new VFS4JPathName("rep01", "META-INF/MANIFEST.MF");
        VFS4JPathName pathName4 = VFS4JPaths.get("rep01", "META-INF/maven.com.google.guava.guava/pom.xml");

        byte[] res4 = VFS4JFiles.readAllBytes(pathName4);

        assertNotNull(res4);

        LOGGER.info("exemple3 OK");
    }


}
