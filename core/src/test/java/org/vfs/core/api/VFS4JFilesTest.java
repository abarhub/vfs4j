package org.vfs.core.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vfs.core.api.operation.*;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.LinkOption;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VFS4JFilesTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileManagerTest.class);

    private FileManager fileManager;
    private Command command;
    private Query query;
    private Attribute attribute;
    private Open open;
    private Search search;

    @BeforeEach
    void setUp() {
        fileManager = Mockito.mock(FileManager.class);
        command = mock(Command.class);
        when(fileManager.getCommand()).thenReturn(command);
        query = mock(Query.class);
        when(fileManager.getQuery()).thenReturn(query);
        attribute = mock(Attribute.class);
        when(fileManager.getAttribute()).thenReturn(attribute);
        open = mock(Open.class);
        when(fileManager.getOpen()).thenReturn(open);
        search = mock(Search.class);
        when(fileManager.getSearch()).thenReturn(search);

        VFS4JFiles.setFileManager(fileManager);
    }

    // TODO: finir les TU

    @Test
    void setAttribute() throws IOException {
        LOGGER.info("setAttribute");
        PathName pathName = getPathName();
        PathName pathName2 = getPathName();

        when(fileManager.getAttribute().setAttribute(pathName, "time", 1L, LinkOption.NOFOLLOW_LINKS)).thenReturn(pathName2);

        // methode testée
        PathName res = VFS4JFiles.setAttribute(pathName, "time", 1L, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(pathName2, res);
        verify(fileManager.getAttribute()).setAttribute(pathName, "time", 1L, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void setLastModifiedTime() throws IOException {
        LOGGER.info("setLastModifiedTime");
        PathName pathName = getPathName();
        PathName pathName2 = getPathName();

        FileTime fileTime = FileTime.from(1, TimeUnit.SECONDS);

        when(fileManager.getAttribute().setLastModifiedTime(pathName, fileTime)).thenReturn(pathName2);

        // methode testée
        PathName res = VFS4JFiles.setLastModifiedTime(pathName, fileTime);

        // vérification
        assertEquals(pathName2, res);
        verify(fileManager.getAttribute()).setLastModifiedTime(pathName, fileTime);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void setOwner() throws IOException {
        LOGGER.info("setOwner");
        PathName pathName = getPathName();
        PathName pathName2 = getPathName();

        UserPrincipal user = mock(UserPrincipal.class);

        when(fileManager.getAttribute().setOwner(pathName, user)).thenReturn(pathName2);

        // methode testée
        PathName res = VFS4JFiles.setOwner(pathName, user);

        // vérification
        assertEquals(pathName2, res);
        verify(fileManager.getAttribute()).setOwner(pathName, user);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void setPosixFilePermissions() throws IOException {
        LOGGER.info("setPosixFilePermissions");
        PathName pathName = getPathName();
        PathName pathName2 = getPathName();

        Set<PosixFilePermission> permissionSet = new HashSet<>();
        permissionSet.add(PosixFilePermission.OWNER_READ);
        permissionSet.add(PosixFilePermission.GROUP_READ);

        when(fileManager.getAttribute().setPosixFilePermissions(pathName, permissionSet)).thenReturn(pathName2);

        // methode testée
        PathName res = VFS4JFiles.setPosixFilePermissions(pathName, permissionSet);

        // vérification
        assertEquals(pathName2, res);
        verify(fileManager.getAttribute()).setPosixFilePermissions(pathName, permissionSet);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void getAttribute() {
    }

    @Test
    void getFileAttributeView() {
    }

    @Test
    void getLastModifiedTime() {
    }

    @Test
    void getOwner() {
    }

    @Test
    void getPosixFilePermissions() {
    }

    @Test
    void isExecutable() {
    }

    @Test
    void isReadable() {
    }

    @Test
    void isHidden() {
    }

    @Test
    void isWritable() {
    }

    @Test
    void readAttributes() {
    }

    @Test
    void testReadAttributes() {
    }

    @Test
    void createFile() {
    }

    @Test
    void createDirectory() {
    }

    @Test
    void createDirectories() {
    }

    @Test
    void delete() {
    }

    @Test
    void deleteIfExists() {
    }

    @Test
    void createLink() {
    }

    @Test
    void createSymbolicLink() {
    }

    @Test
    void copy() {
    }

    @Test
    void testCopy() {
    }

    @Test
    void testCopy1() {
    }

    @Test
    void move() {
    }

    @Test
    void write() {
    }

    @Test
    void testWrite() {
    }

    @Test
    void newInputStream() {
    }

    @Test
    void newOutputStream() {
    }

    @Test
    void newReader() {
    }

    @Test
    void newWriter() {
    }

    @Test
    void newByteChannel() {
    }

    @Test
    void newDirectoryStream() {
    }

    @Test
    void exists() {
    }

    @Test
    void isDirectory() {
    }

    @Test
    void isRegularFile() {
    }

    @Test
    void isSameFile() {
    }

    @Test
    void isSymbolicLink() {
    }

    @Test
    void lines() {
    }

    @Test
    void testLines() {
    }

    @Test
    void notExists() {
    }

    @Test
    void readAllBytes() {
    }

    @Test
    void readAllLines() {
    }

    @Test
    void testReadAllLines() {
    }

    @Test
    void size() {
    }

    @Test
    void list() {
    }

    @Test
    void walk() {
    }

    @Test
    void testWalk() {
    }

    @Test
    void find() {
    }

    // methodes utilitaires

    private PathName getPathName() {
        return new PathName("aaa", "/tmp");
    }

    private PathName getPathName2() {
        return new PathName("bbb", "/tmp2");
    }
}