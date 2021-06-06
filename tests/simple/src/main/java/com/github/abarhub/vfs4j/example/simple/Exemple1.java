package com.github.abarhub.vfs4j.example.simple;

import com.github.abarhub.vfs.core.api.*;
import com.google.common.base.Verify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.abarhub.vfs.core.config.VFS4JConfig;
import com.github.abarhub.vfs.core.config.VFS4JPathMode;
import com.github.abarhub.vfs.core.config.VFS4JPathParameter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Exemple1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(Exemple1.class);

    public void exemple1() throws IOException {

        LOGGER.info("exemple1 ...");

        Path path1 = Paths.get("./target/test/dir1");
        Files.createDirectories(path1);
        Path path2 = Paths.get("./target/test/dir2");
        Files.createDirectories(path2);
        Path file1 = path1.resolve("fichier1.txt");
        Path file2 = path2.resolve("fichier2.txt");

        Files.deleteIfExists(file1);
        Files.deleteIfExists(file2);

        Verify.verify(Files.exists(path1));
        Verify.verify(Files.exists(path2));

        Verify.verify(!Files.exists(file1));
        Verify.verify(!Files.exists(file2));

        VFS4JFileManager fileManager = VFS4JDefaultFileManager.get();
        fileManager.getCommand().createFile(new VFS4JPathName("dir1", "fichier1.txt"));
        fileManager.getCommand().createFile(new VFS4JPathName("dir2", "fichier2.txt"));

        Verify.verify(Files.exists(file1));
        Verify.verify(Files.exists(file2));

        LOGGER.info("exemple1 OK");
    }

    public void exemple2() throws IOException {

        LOGGER.info("exemple2 ...");

        Path path1 = Paths.get("./target/test/dir1");
        Files.createDirectories(path1);
        Path path2 = Paths.get("./target/test/dir2");
        Files.createDirectories(path2);
        Path file1 = path1.resolve("fichier3.txt");
        Path file2 = path2.resolve("fichier4.txt");

        Files.deleteIfExists(file1);
        Files.deleteIfExists(file2);

        Verify.verify(Files.exists(path1));
        Verify.verify(Files.exists(path2));

        Verify.verify(!Files.exists(file1));
        Verify.verify(!Files.exists(file2));

        VFS4JFiles.createFile(new VFS4JPathName("dir1", "fichier3.txt"));
        VFS4JFiles.createFile(new VFS4JPathName("dir2", "fichier4.txt"));

        Verify.verify(Files.exists(file1));
        Verify.verify(Files.exists(file2));

        LOGGER.info("exemple2 OK");
    }

    public void exemple3() throws IOException {

        LOGGER.info("exemple3 ...");

        Path temp = Files.createTempDirectory("junit_test_vfs4j");

        Path path1 = temp.resolve("rep01");
        Files.createDirectories(path1);

        Path path2 = temp.resolve("rep02");
        Files.createDirectories(path2);

        Properties properties = new Properties();

        properties.setProperty("vfs.paths.rep01.path", path1.toString());
        properties.setProperty("vfs.paths.rep01.mode", VFS4JPathMode.STANDARD.getName());
        properties.setProperty("vfs.paths.rep02.path", path2.toString());
        properties.setProperty("vfs.paths.rep02.mode", VFS4JPathMode.STANDARD.getName());
        properties.setProperty("vfs.paths.temp.mode", VFS4JPathMode.TEMPORARY.getName());

        VFS4JParseConfigFile parseConfigFile = new VFS4JParseConfigFile();
        VFS4JFileManagerBuilder fileManagerBuilder = parseConfigFile.parse(properties);

        VFS4JDefaultFileManager.get().setConfig(fileManagerBuilder.build());

        VFS4JConfig config = VFS4JDefaultFileManager.get().getConfig();

        Verify.verifyNotNull(config.getPath("rep01"));
        Verify.verifyNotNull(config.getPath("rep02"));
        Verify.verifyNotNull(config.getPath("temp"));

        VFS4JPathParameter parameter = (VFS4JPathParameter) config.getPath("temp");
        Path pathTemp = parameter.getPath();


        Path file1 = path1.resolve("fichier01.txt");
        Path file2 = path2.resolve("fichier02.txt");
        Path file3 = pathTemp.resolve("fichier03.txt");

        Files.deleteIfExists(file1);
        Files.deleteIfExists(file2);
        Files.deleteIfExists(file3);

        Verify.verify(Files.exists(path1));
        Verify.verify(Files.exists(path2));
        Verify.verify(Files.exists(pathTemp));

        Verify.verify(!Files.exists(file1));
        Verify.verify(!Files.exists(file2));
        Verify.verify(!Files.exists(file3));

        VFS4JFiles.createFile(new VFS4JPathName("rep01", "fichier01.txt"));
        VFS4JFiles.createFile(new VFS4JPathName("rep02", "fichier02.txt"));
        VFS4JFiles.createFile(new VFS4JPathName("temp", "fichier03.txt"));

        Verify.verify(Files.exists(file1));
        Verify.verify(Files.exists(file2));
        Verify.verify(Files.exists(file3));

        LOGGER.info("exemple3 OK");
    }
}
