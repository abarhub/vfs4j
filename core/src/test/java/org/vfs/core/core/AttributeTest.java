package org.vfs.core.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AttributeTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttributeTest.class);
    public static final String PATH1 = "path1";
    public static final String LAST_MODIFIED_TIME_ATTRIBUTE = "lastModifiedTime";

    private FileManager fileManager;

    private Path directory;

    private Attribute attribute;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        //fileManager=new FileManager();

        assertTrue(Files.exists(tempDir));
        Path temp=tempDir.resolve("temp");
        assertFalse(Files.exists(temp));
        Files.createDirectory(temp);
        assertTrue(Files.exists(temp));
        assertTrue(Files.isDirectory(temp));
        directory=temp;
        fileManager=new FileManager();
        fileManager.addPath(PATH1,temp);

        attribute=new Attribute(fileManager);
    }

    @Test
    void setAttribute() throws IOException {
        String filename="fichier1.txt";
        final Path file=directory.resolve(filename);
        Files.createFile(file);
        assertTrue(Files.exists(file));
        final FileTime fileTime = FileTime.from(Instant.EPOCH);

        // methode testée
        PathName res = attribute.setAttribute(getPathName(filename), LAST_MODIFIED_TIME_ATTRIBUTE, fileTime);

        // vérifications
        assertNotNull(res);
        FileTime time= Files.getLastModifiedTime(file);
        assertNotNull(time);
        assertEquals(fileTime, time);
    }

    @Test
    void setLastModifiedTime() throws IOException {
        String filename="fichier2.txt";
        final Path file=directory.resolve(filename);
        Files.createFile(file);
        assertTrue(Files.exists(file));
        final FileTime fileTime = FileTime.from(Instant.EPOCH.plusMillis(1000));

        // methode testée
        PathName res = attribute.setLastModifiedTime(getPathName(filename), fileTime);

        // vérifications
        assertNotNull(res);
        FileTime time= Files.getLastModifiedTime(file);
        assertNotNull(time);
        assertEquals(fileTime, time);
    }

    @Test
    @Disabled("Problème de droit pour modifier les droit d'un fichier")
    void setOwner() throws IOException {
        String filename="fichier3.txt";
        final Path file=directory.resolve(filename);
        Files.createFile(file);
        assertTrue(Files.exists(file));
        LOGGER.info("file={} ({})",file, file.toUri());
        FileSystem fs=FileSystems.getDefault();
        UserPrincipalLookupService lookupService = fs.getUserPrincipalLookupService();
        final String username = "user123";
        UserPrincipal user = lookupService.lookupPrincipalByName(username);

        // methode testée
        PathName res = attribute.setOwner(getPathName(filename), user);

        // vérifications
        assertNotNull(res);
        UserPrincipal owner= Files.getOwner(file);
        assertNotNull(owner);
        assertEquals(owner, user);
    }

    @Test
    @Disabled("Ne fonctionne pas sous windows")
    @Tag("linux")
    void setPosixFilePermissions() throws IOException {
        String filename="fichier4.txt";
        final Path file=directory.resolve(filename);
        Files.createFile(file);
        assertTrue(Files.exists(file));
        Set<PosixFilePermission> permissions=new HashSet<>();
        permissions.add(PosixFilePermission.OWNER_READ);

        // methode testée
        PathName res = attribute.setPosixFilePermissions(getPathName(filename), permissions);

        // vérifications
        assertNotNull(res);
        Set<PosixFilePermission> res2 = Files.getPosixFilePermissions(file);
        assertNotNull(res2);
        assertTrue(res2.contains(PosixFilePermission.OWNER_READ));
    }

    @Test
    void getAttribute() throws IOException {
        String filename="fichier5.txt";
        final Path file=directory.resolve(filename);
        Files.createFile(file);
        assertTrue(Files.exists(file));
        final FileTime fileTime = FileTime.from(Instant.EPOCH.plusMillis(3000));
        Files.setLastModifiedTime(file,fileTime);

        // methode testée
        FileTime res = (FileTime) attribute.getAttribute(getPathName(filename), "lastModifiedTime");

        // vérifications
        assertNotNull(res);
        assertEquals(fileTime, res);
    }

    @Test
    void getFileAttributeView() throws IOException {
        String filename="fichier6.txt";
        final Path file=directory.resolve(filename);
        Files.createFile(file);
        assertTrue(Files.exists(file));

        // methode testée
        AclFileAttributeView res = attribute.getFileAttributeView(getPathName(filename), AclFileAttributeView.class);

        // vérifications
        assertNotNull(res);
        assertNotNull(res.getOwner());
    }

    @Test
    void getLastModifiedTime() throws IOException {
        String filename="fichier7.txt";
        final Path file=directory.resolve(filename);
        Files.createFile(file);
        assertTrue(Files.exists(file));
        final FileTime fileTime = FileTime.from(Instant.EPOCH.plusMillis(4000));
        Files.setLastModifiedTime(file,fileTime);

        // methode testée
        FileTime res = attribute.getLastModifiedTime(getPathName(filename));

        // vérifications
        assertNotNull(res);
        assertEquals(fileTime, res);
    }

    @Test
    void getOwner() throws IOException {
        String filename="fichier8.txt";
        final Path file=directory.resolve(filename);
        Files.createFile(file);
        assertTrue(Files.exists(file));

        // methode testée
        UserPrincipal res = attribute.getOwner(getPathName(filename));

        // vérifications
        assertNotNull(res);
    }

    @Test
    @Disabled("Ne fonctionne pas sous windows")
    @Tag("linux")
    void getPosixFilePermissions() throws IOException {
        String filename="fichier9.txt";
        final Path file=directory.resolve(filename);
        Files.createFile(file);
        assertTrue(Files.exists(file));

        // methode testée
        Set<PosixFilePermission> res = attribute.getPosixFilePermissions(getPathName(filename));

        // vérifications
        assertNotNull(res);
    }

    @Test
    void isExecutable() throws IOException {
        String filename="fichier10.txt";
        final Path file=directory.resolve(filename);
        Files.createFile(file);
        assertTrue(Files.exists(file));

        // methode testée
        boolean res = attribute.isExecutable(getPathName(filename));

        // vérifications
        assertTrue(res);
    }

    @Test
    void isHidden() throws IOException {
        String filename="fichier11.txt";
        final Path file=directory.resolve(filename);
        Files.createFile(file);
        assertTrue(Files.exists(file));

        // methode testée
        boolean res = attribute.isHidden(getPathName(filename));

        // vérifications
        assertFalse(res);
    }

    @Test
    void isReadable() throws IOException {
        String filename="fichier12.txt";
        final Path file=directory.resolve(filename);
        Files.createFile(file);
        assertTrue(Files.exists(file));

        // methode testée
        boolean res = attribute.isReadable(getPathName(filename));

        // vérifications
        assertTrue(res);
    }

    @Test
    void isWritableTrue() throws IOException {

        final String filename = "fichier13.txt";
        final Path file=directory.resolve(filename);
        Files.createFile(file);
        assertTrue(Files.isWritable(file));

        // methode testée
        boolean res=attribute.isWritable(getPathName(filename));

        // vérifications
        assertTrue(res);
    }

    // TODO: trouver un moyen de créer un fichier non writable
    @Test
    @Disabled("TODO: trouver un moyen de créer un fichier non writable")
    void isWritableFalse() throws IOException {
        final String filename = "fichier14.txt";
        final Path file=directory.resolve(filename);
        Files.createFile(file);
        assertFalse(Files.isWritable(file));

        // methode testée
        boolean res=attribute.isWritable(getPathName(filename));

        // vérifications
        assertFalse(res);
    }

    @Test
    void readAttributesStr() throws IOException {
        String filename="fichier15.txt";
        final Path file=directory.resolve(filename);
        Files.createFile(file);
        assertTrue(Files.exists(file));
        final FileTime fileTime = FileTime.from(Instant.EPOCH.plusMillis(6000));
        Files.setLastModifiedTime(file,fileTime);

        // methode testée
        Map<String, Object> res = attribute.readAttributes(getPathName(filename), LAST_MODIFIED_TIME_ATTRIBUTE);

        // vérifications
        assertNotNull(res);
        LOGGER.info("res={}", res);
        assertEquals(fileTime, res.get(LAST_MODIFIED_TIME_ATTRIBUTE));
    }

    @Test
    void readAttributesClass() throws IOException {
        String filename="fichier16.txt";
        final Path file=directory.resolve(filename);
        Files.createFile(file);
        assertTrue(Files.exists(file));
        final FileTime fileTime = FileTime.from(Instant.EPOCH.plusMillis(7000));
        Files.setLastModifiedTime(file,fileTime);

        // methode testée
        BasicFileAttributes res = attribute.readAttributes(getPathName(filename), BasicFileAttributes.class);

        // vérifications
        assertNotNull(res);
        LOGGER.info("res={}", res);
        assertEquals(fileTime, res.lastModifiedTime());
    }

    // methodes utilitaires

    public PathName getPathName(String filename){
        return new PathName(PATH1, filename);
    }

}