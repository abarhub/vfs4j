package org.vfs4j.example.simple;

import com.google.common.base.Verify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vfs.core.api.DefaultFileManager;
import org.vfs.core.api.FileManager;
import org.vfs.core.api.PathName;
import org.vfs.core.api.VFS4JFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        FileManager fileManager = DefaultFileManager.get();
        fileManager.getCommand().createFile(new PathName("dir1", "fichier1.txt"));
        fileManager.getCommand().createFile(new PathName("dir2", "fichier2.txt"));

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

        VFS4JFiles.createFile(new PathName("dir1", "fichier3.txt"));
        VFS4JFiles.createFile(new PathName("dir2", "fichier4.txt"));

        Verify.verify(Files.exists(file1));
        Verify.verify(Files.exists(file2));

        LOGGER.info("exemple2 OK");
    }
}
