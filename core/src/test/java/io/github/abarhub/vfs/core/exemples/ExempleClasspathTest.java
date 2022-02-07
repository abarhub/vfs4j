package io.github.abarhub.vfs.core.exemples;

import io.github.abarhub.vfs.core.api.*;
import io.github.abarhub.vfs.core.config.VFS4JConfig;
import io.github.abarhub.vfs.core.config.VFS4JPathMode;
import io.github.abarhub.vfs.core.exception.VFS4JException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class ExempleClasspathTest {


    private static final Logger LOGGER = LoggerFactory.getLogger(ExempleClasspathTest.class);

    @Test
    @DisplayName("Test simple de lecture d'un fichier")
    public void exemple1() throws IOException {

        LOGGER.info("exemple1 ...");

        Properties properties = new Properties();

        properties.setProperty("vfs.paths.rep01.readonly", "true");
        properties.setProperty("vfs.paths.rep01.mode", VFS4JPathMode.CLASSPATH.getName());

        VFS4JParseConfigFile parseConfigFile = new VFS4JParseConfigFile();
        VFS4JFileManagerBuilder fileManagerBuilder = parseConfigFile.parse(properties);

        reinitConfig(fileManagerBuilder.build());

        VFS4JConfig config = VFS4JDefaultFileManager.get().getConfig();

        assertNotNull(config.getPath("rep01"));
        assertTrue(config.getPath("rep01").isReadonly());

        try {
            // methodes testées
            List<String> res = VFS4JFiles.readAllLines(VFS4JPaths.get("rep01", "/test_classpath01.txt"));

        assertNotNull(res);

        assertEquals(1, res.size());
        assertEquals("aaaa BBBBB CCCCC NNNNNNNNN", res.get(0));

        LOGGER.info("exemple1 OK");

        } catch(Exception e){
            LOGGER.error("Erreur exemple1()", e);
            throw e;
        }
    }

    @Test
    @DisplayName("Test simple de lecture d'un fichier dans un répertoire")
    public void exemple2() throws IOException {

        LOGGER.info("exemple2 ...");

        Properties properties = new Properties();

        properties.setProperty("vfs.paths.rep01.readonly", "true");
        properties.setProperty("vfs.paths.rep01.mode", VFS4JPathMode.CLASSPATH.getName());

        VFS4JParseConfigFile parseConfigFile = new VFS4JParseConfigFile();
        VFS4JFileManagerBuilder fileManagerBuilder = parseConfigFile.parse(properties);

        reinitConfig(fileManagerBuilder.build());

        VFS4JConfig config = VFS4JDefaultFileManager.get().getConfig();

        assertNotNull(config.getPath("rep01"));
        assertTrue(config.getPath("rep01").isReadonly());

        // methodes testées
        List<String> res = VFS4JFiles.readAllLines(VFS4JPaths.get("rep01", "/test_classpath/fichier01.txt"));

        assertNotNull(res);

        assertEquals(3, res.size());
        assertEquals("abc", res.get(0));
        assertEquals("aaa", res.get(1));
        assertEquals("ddddddd", res.get(2));

        LOGGER.info("exemple2 OK");
    }

    @Test
    @DisplayName("Test simple de configuration avec readonly à false")
    public void exemple3() throws IOException {

        LOGGER.info("exemple3 ...");

        Properties properties = new Properties();

        properties.setProperty("vfs.paths.rep01.readonly", "false");
        properties.setProperty("vfs.paths.rep01.mode", VFS4JPathMode.CLASSPATH.getName());

        VFS4JParseConfigFile parseConfigFile = new VFS4JParseConfigFile();

        // methode testée
        VFS4JException exception = assertThrows(VFS4JException.class, () -> parseConfigFile.parse(properties));

        assertNotNull(exception);
        assertEquals("Path for 'rep01' with classpatch mode and readonly to false",
                exception.getMessage());

        LOGGER.info("exemple3 OK");
    }

    @Test
    @DisplayName("Test simple de lecture d'un fichier, avec un path dans la configuration")
    public void exemple4() throws IOException {

        LOGGER.info("exemple4 ...");

        Properties properties = new Properties();

        properties.setProperty("vfs.paths.rep01.path", "/test_classpath/");
        properties.setProperty("vfs.paths.rep01.readonly", "true");
        properties.setProperty("vfs.paths.rep01.mode", VFS4JPathMode.CLASSPATH.getName());

        VFS4JParseConfigFile parseConfigFile = new VFS4JParseConfigFile();
        VFS4JFileManagerBuilder fileManagerBuilder = parseConfigFile.parse(properties);

        reinitConfig(fileManagerBuilder.build());

        VFS4JConfig config = VFS4JDefaultFileManager.get().getConfig();

        assertNotNull(config.getPath("rep01"));
        assertTrue(config.getPath("rep01").isReadonly());

        // methodes testées
        List<String> res = VFS4JFiles.readAllLines(VFS4JPaths.get("rep01", "fichier01.txt"));

        assertNotNull(res);

        assertEquals(3, res.size());
        assertEquals("abc", res.get(0));
        assertEquals("aaa", res.get(1));
        assertEquals("ddddddd", res.get(2));

        LOGGER.info("exemple4 OK");
    }


    // methodes utilitaires

    private void reinitConfig(VFS4JConfig vfs4JConfig) {
        VFS4JDefaultFileManager.get().setConfig(vfs4JConfig);
        VFS4JFiles.reinit();
    }
}
