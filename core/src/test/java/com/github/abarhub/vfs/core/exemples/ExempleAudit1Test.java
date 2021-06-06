package com.github.abarhub.vfs.core.exemples;

import com.github.abarhub.vfs.core.api.*;
import com.github.abarhub.vfs.core.config.VFS4JConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import com.github.abarhub.vfs.core.plugin.audit.VFS4JAuditPlugins;
import com.github.abarhub.vfs.core.plugin.common.VFS4JPlugins;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class ExempleAudit1Test {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExempleAudit1Test.class);

    @Test
    @DisplayName("Test simple de création d'un fichier")
    public void exemple1() throws IOException {

        LOGGER.info("exemple1 ...");

        Path temp = Files.createTempDirectory("junit_test_vfs4j");

        Path path1 = temp.resolve("rep01");
        Files.createDirectories(path1);

        Properties properties = new Properties();

        properties.setProperty("vfs.paths.rep01.path", path1.toString());
        properties.setProperty("vfs.paths.rep01.readonly", "false");
        properties.setProperty("vfs.plugins.plugins1.class", "com.github.abarhub.vfs.core.plugin.audit.VFS4JAuditPluginsFactory");

        VFS4JParseConfigFile parseConfigFile = new VFS4JParseConfigFile();
        VFS4JFileManagerBuilder fileManagerBuilder = parseConfigFile.parse(properties);

        reinitConfig(fileManagerBuilder.build());

        VFS4JConfig config = VFS4JDefaultFileManager.get().getConfig();

        assertNotNull(config.getPath("rep01"));
        assertFalse(config.getPath("rep01").isReadonly());

        Path file1 = path1.resolve("fichier01.txt");

        Files.deleteIfExists(file1);

        assertTrue(Files.exists(path1));

        assertFalse(Files.exists(file1));


        VFS4JPlugins plugins = config.getPlugins("plugins1");
        assertNotNull(plugins);
        VFS4JAuditPlugins audit = (VFS4JAuditPlugins) plugins;
        assertNotNull(audit);
        List<String> listeAudit = new ArrayList<>();
        audit.addListener((x, y, z, e, t) -> listeAudit.add(x + ";" + y + ";" + MessageFormatter.arrayFormat(z, t).getMessage()));

        // methodes testées
        VFS4JFiles.createFile(new VFS4JPathName("rep01", "fichier01.txt"));

        assertTrue(Files.exists(file1));

        assertEquals(1, listeAudit.size());
        assertEquals("INFO;false;createFile for file rep01:fichier01.txt", listeAudit.get(0));

        LOGGER.info("exemple1 OK");
    }

    @Test
    @DisplayName("Test avec filtrage")
    public void exemple2() throws IOException {

        LOGGER.info("exemple2 ...");

        Path temp = Files.createTempDirectory("junit_test_vfs4j");

        Path path1 = temp.resolve("rep01");
        Files.createDirectories(path1);

        Properties properties = new Properties();

        properties.setProperty("vfs.paths.rep01.path", path1.toString());
        properties.setProperty("vfs.paths.rep01.readonly", "false");
        properties.setProperty("vfs.plugins.plugins1.class", "com.github.abarhub.vfs.core.plugin.audit.VFS4JAuditPluginsFactory");
        properties.setProperty("vfs.plugins.plugins1.operations", "COMMAND");
        properties.setProperty("vfs.plugins.plugins1.filterPath", "*.txt");

        VFS4JParseConfigFile parseConfigFile = new VFS4JParseConfigFile();
        VFS4JFileManagerBuilder fileManagerBuilder = parseConfigFile.parse(properties);

        reinitConfig(fileManagerBuilder.build());

        VFS4JConfig config = VFS4JDefaultFileManager.get().getConfig();

        assertNotNull(config.getPath("rep01"));
        assertFalse(config.getPath("rep01").isReadonly());

        Path file1 = path1.resolve("fichier01.txt");
        Path file2 = path1.resolve("fichier02.jpg");
        Path file3 = path1.resolve("fichier03.txt");

        Files.deleteIfExists(file1);
        Files.deleteIfExists(file2);
        Files.deleteIfExists(file3);

        assertTrue(Files.exists(path1));

        assertFalse(Files.exists(file1));
        assertFalse(Files.exists(file2));
        assertFalse(Files.exists(file3));

        VFS4JPlugins plugins = config.getPlugins("plugins1");
        assertNotNull(plugins);
        VFS4JAuditPlugins audit = (VFS4JAuditPlugins) plugins;
        assertNotNull(audit);
        List<String> listeAudit = new ArrayList<>();
        audit.addListener((x, y, z, e, t) -> listeAudit.add(x + ";" + y + ";" + MessageFormatter.arrayFormat(z, t).getMessage()));

        // methodes testées
        VFS4JFiles.createFile(new VFS4JPathName("rep01", "fichier01.txt"));
        VFS4JFiles.createFile(new VFS4JPathName("rep01", "fichier02.jpg"));
        VFS4JFiles.createFile(new VFS4JPathName("rep01", "fichier03.txt"));

        // vérifications


        assertTrue(Files.exists(file1));
        assertTrue(Files.exists(file2));
        assertTrue(Files.exists(file3));

        assertEquals(2, listeAudit.size());
        assertEquals("INFO;false;createFile for file rep01:fichier01.txt", listeAudit.get(0));
        assertEquals("INFO;false;createFile for file rep01:fichier03.txt", listeAudit.get(1));

        LOGGER.info("exemple2 OK");
    }

    @Test
    @DisplayName("Test avec une erreur")
    public void exemple3() throws IOException {

        LOGGER.info("exemple3 ...");

        Path temp = Files.createTempDirectory("junit_test_vfs4j");

        Path path1 = temp.resolve("rep03");
        Files.createDirectories(path1);

        Properties properties = new Properties();

        properties.setProperty("vfs.paths.rep03.path", path1.toString());
        properties.setProperty("vfs.paths.rep03.readonly", "false");
        properties.setProperty("vfs.plugins.plugins3.class", "com.github.abarhub.vfs.core.plugin.audit.VFS4JAuditPluginsFactory");

        VFS4JParseConfigFile parseConfigFile = new VFS4JParseConfigFile();
        VFS4JFileManagerBuilder fileManagerBuilder = parseConfigFile.parse(properties);

        reinitConfig(fileManagerBuilder.build());

        VFS4JConfig config = VFS4JDefaultFileManager.get().getConfig();

        assertNotNull(config.getPath("rep03"));
        assertFalse(config.getPath("rep03").isReadonly());

        Path file1 = path1.resolve("fichierErreur01.txt");

        Files.deleteIfExists(file1);

        Files.createFile(file1);

        assertTrue(Files.exists(path1));

        assertTrue(Files.exists(file1));


        VFS4JPlugins plugins = config.getPlugins("plugins3");
        assertNotNull(plugins);
        VFS4JAuditPlugins audit = (VFS4JAuditPlugins) plugins;
        assertNotNull(audit);
        List<String> listeAudit = new ArrayList<>();
        audit.addListener((x, y, z, e, t) -> listeAudit.add(x + ";" + y + ";" + MessageFormatter.arrayFormat(z, t).getMessage()));

        // methodes testées
        FileAlreadyExistsException res = assertThrows(FileAlreadyExistsException.class, () -> VFS4JFiles.createFile(new VFS4JPathName("rep03", "fichierErreur01.txt")));

        assertNotNull(res);
        assertTrue(Files.exists(file1));

        assertEquals(1, listeAudit.size());
        assertEquals("INFO;true;Error for createFile for file rep03:fichierErreur01.txt", listeAudit.get(0));

        LOGGER.info("exemple3 OK");
    }


    // methodes utilitaires

    private void reinitConfig(VFS4JConfig vfs4JConfig) {
        VFS4JDefaultFileManager.get().setConfig(vfs4JConfig);
        VFS4JFiles.reinit();
    }
}
