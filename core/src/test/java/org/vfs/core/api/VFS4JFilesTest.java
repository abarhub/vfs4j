package org.vfs.core.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vfs.core.api.operation.*;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.attribute.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void getAttribute() throws IOException {
        LOGGER.info("getAttribute");
        PathName pathName = getPathName();

        final String s = "abc";

        when(fileManager.getAttribute().getAttribute(pathName, "attr1", LinkOption.NOFOLLOW_LINKS)).thenReturn(s);

        // methode testée
        Object res = VFS4JFiles.getAttribute(pathName, "attr1", LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(s, res);
        verify(fileManager.getAttribute()).getAttribute(pathName, "attr1", LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void getFileAttributeView() throws IOException {
        LOGGER.info("getFileAttributeView");
        PathName pathName = getPathName();

        final BasicFileAttributeView basicFileAttributeView = mock(BasicFileAttributeView.class);

        when(fileManager.getAttribute().getFileAttributeView(pathName, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS)).thenReturn(basicFileAttributeView);

        // methode testée
        BasicFileAttributeView res = VFS4JFiles.getFileAttributeView(pathName, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(basicFileAttributeView, res);
        verify(fileManager.getAttribute()).getFileAttributeView(pathName, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void getLastModifiedTime() throws IOException {
        LOGGER.info("getLastModifiedTime");
        PathName pathName = getPathName();

        final FileTime fileTime = FileTime.from(1L, TimeUnit.SECONDS);

        when(fileManager.getAttribute().getLastModifiedTime(pathName, LinkOption.NOFOLLOW_LINKS)).thenReturn(fileTime);

        // methode testée
        FileTime res = VFS4JFiles.getLastModifiedTime(pathName, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(fileTime, res);
        verify(fileManager.getAttribute()).getLastModifiedTime(pathName, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void getOwner() throws IOException {
        LOGGER.info("getOwner");
        PathName pathName = getPathName();

        final UserPrincipal userPrincipal = mock(UserPrincipal.class);

        when(fileManager.getAttribute().getOwner(pathName, LinkOption.NOFOLLOW_LINKS)).thenReturn(userPrincipal);

        // methode testée
        UserPrincipal res = VFS4JFiles.getOwner(pathName, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(userPrincipal, res);
        verify(fileManager.getAttribute()).getOwner(pathName, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void getPosixFilePermissions() throws IOException {
        LOGGER.info("getPosixFilePermissions");
        PathName pathName = getPathName();

        final Set<PosixFilePermission> posixFilePermissions = new HashSet<>();
        posixFilePermissions.add(PosixFilePermission.OWNER_READ);

        when(fileManager.getAttribute().getPosixFilePermissions(pathName, LinkOption.NOFOLLOW_LINKS)).thenReturn(posixFilePermissions);

        // methode testée
        Set<PosixFilePermission> res = VFS4JFiles.getPosixFilePermissions(pathName, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(posixFilePermissions, res);
        verify(fileManager.getAttribute()).getPosixFilePermissions(pathName, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void isExecutable() throws IOException {
        LOGGER.info("isExecutable");
        PathName pathName = getPathName();

        when(fileManager.getAttribute().isExecutable(pathName)).thenReturn(true);

        // methode testée
        boolean res = VFS4JFiles.isExecutable(pathName);

        // vérification
        assertEquals(true, res);
        verify(fileManager.getAttribute()).isExecutable(pathName);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void isReadable() throws IOException {
        LOGGER.info("isReadable");
        PathName pathName = getPathName();

        when(fileManager.getAttribute().isReadable(pathName)).thenReturn(true);

        // methode testée
        boolean res = VFS4JFiles.isReadable(pathName);

        // vérification
        assertEquals(true, res);
        verify(fileManager.getAttribute()).isReadable(pathName);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void isHidden() throws IOException {
        LOGGER.info("isHidden");
        PathName pathName = getPathName();

        when(fileManager.getAttribute().isHidden(pathName)).thenReturn(true);

        // methode testée
        boolean res = VFS4JFiles.isHidden(pathName);

        // vérification
        assertEquals(true, res);
        verify(fileManager.getAttribute()).isHidden(pathName);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void isWritable() {
        LOGGER.info("isWritable");
        PathName pathName = getPathName();

        when(fileManager.getAttribute().isWritable(pathName)).thenReturn(true);

        // methode testée
        boolean res = VFS4JFiles.isWritable(pathName);

        // vérification
        assertEquals(true, res);
        verify(fileManager.getAttribute()).isWritable(pathName);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void readAttributes() throws IOException {
        LOGGER.info("readAttributes");
        PathName pathName = getPathName();

        final Map<String, Object> map = new HashMap<>();
        map.put("abc", 123);

        when(fileManager.getAttribute().readAttributes(pathName, "attr1", LinkOption.NOFOLLOW_LINKS)).thenReturn(map);

        // methode testée
        Map<String, Object> res = VFS4JFiles.readAttributes(pathName, "attr1", LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(map, res);
        verify(fileManager.getAttribute()).readAttributes(pathName, "attr1", LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void readAttributes2() throws IOException {
        LOGGER.info("readAttributes2");
        PathName pathName = getPathName();

        final BasicFileAttributes basicFileAttributes = mock(BasicFileAttributes.class);

        when(fileManager.getAttribute().readAttributes(pathName, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS)).thenReturn(basicFileAttributes);

        // methode testée
        BasicFileAttributes res = VFS4JFiles.readAttributes(pathName, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(basicFileAttributes, res);
        verify(fileManager.getAttribute()).readAttributes(pathName, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void createFile() throws IOException {
        LOGGER.info("createFile");
        PathName pathName = getPathName();

        final FileAttribute fileAttribute = mock(FileAttribute.class);

        // methode testée
        VFS4JFiles.createFile(pathName, fileAttribute);

        // vérification
        verify(fileManager.getCommand()).createFile(pathName, fileAttribute);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void createDirectory() throws IOException {
        LOGGER.info("createDirectory");
        PathName pathName = getPathName();

        final FileAttribute fileAttribute = mock(FileAttribute.class);

        // methode testée
        VFS4JFiles.createDirectory(pathName, fileAttribute);

        // vérification
        verify(fileManager.getCommand()).createDirectory(pathName, fileAttribute);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void createDirectories() throws IOException {
        LOGGER.info("createDirectories");
        PathName pathName = getPathName();

        final FileAttribute fileAttribute = mock(FileAttribute.class);

        // methode testée
        VFS4JFiles.createDirectories(pathName, fileAttribute);

        // vérification
        verify(fileManager.getCommand()).createDirectories(pathName, fileAttribute);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void delete() throws IOException {
        LOGGER.info("delete");
        PathName pathName = getPathName();

        // methode testée
        VFS4JFiles.delete(pathName);

        // vérification
        verify(fileManager.getCommand()).delete(pathName);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void deleteIfExists() throws IOException {
        LOGGER.info("deleteIfExists");
        PathName pathName = getPathName();

        when(fileManager.getCommand().deleteIfExists(pathName)).thenReturn(true);

        // methode testée
        boolean res = VFS4JFiles.deleteIfExists(pathName);

        // vérification
        assertTrue(res);
        verify(fileManager.getCommand()).deleteIfExists(pathName);
        verifyNoMoreInteractions(fileManager.getAttribute());
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