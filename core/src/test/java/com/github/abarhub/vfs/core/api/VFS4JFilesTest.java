package com.github.abarhub.vfs.core.api;

import com.github.abarhub.vfs.core.api.operation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VFS4JFilesTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VFS4JFileManagerTest.class);

    private VFS4JFileManager fileManager;
    private VFS4JCommand command;
    private VFS4JQuery query;
    private VFS4JAttribute attribute;
    private VFS4JOpen open;
    private VFS4JSearch search;

    @BeforeEach
    void setUp() {
        fileManager = Mockito.mock(VFS4JFileManager.class);
        command = mock(VFS4JCommand.class);
        when(fileManager.getCommand()).thenReturn(command);
        query = mock(VFS4JQuery.class);
        when(fileManager.getQuery()).thenReturn(query);
        attribute = mock(VFS4JAttribute.class);
        when(fileManager.getAttribute()).thenReturn(attribute);
        open = mock(VFS4JOpen.class);
        when(fileManager.getOpen()).thenReturn(open);
        search = mock(VFS4JSearch.class);
        when(fileManager.getSearch()).thenReturn(search);

        VFS4JFiles.setFileManager(fileManager);
    }

    // TODO: finir les TU

    @Test
    void setAttribute() throws IOException {
        LOGGER.info("setAttribute");
        VFS4JPathName VFS4JPathName = getPathName();
        VFS4JPathName VFS4JPathName2 = getPathName();

        when(fileManager.getAttribute().setAttribute(VFS4JPathName, "time", 1L, LinkOption.NOFOLLOW_LINKS)).thenReturn(VFS4JPathName2);

        // methode testée
        VFS4JPathName res = VFS4JFiles.setAttribute(VFS4JPathName, "time", 1L, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(VFS4JPathName2, res);
        verify(fileManager.getAttribute()).setAttribute(VFS4JPathName, "time", 1L, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void setLastModifiedTime() throws IOException {
        LOGGER.info("setLastModifiedTime");
        VFS4JPathName VFS4JPathName = getPathName();
        VFS4JPathName VFS4JPathName2 = getPathName();

        FileTime fileTime = FileTime.from(1, TimeUnit.SECONDS);

        when(fileManager.getAttribute().setLastModifiedTime(VFS4JPathName, fileTime)).thenReturn(VFS4JPathName2);

        // methode testée
        VFS4JPathName res = VFS4JFiles.setLastModifiedTime(VFS4JPathName, fileTime);

        // vérification
        assertEquals(VFS4JPathName2, res);
        verify(fileManager.getAttribute()).setLastModifiedTime(VFS4JPathName, fileTime);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void setOwner() throws IOException {
        LOGGER.info("setOwner");
        VFS4JPathName VFS4JPathName = getPathName();
        VFS4JPathName VFS4JPathName2 = getPathName();

        UserPrincipal user = mock(UserPrincipal.class);

        when(fileManager.getAttribute().setOwner(VFS4JPathName, user)).thenReturn(VFS4JPathName2);

        // methode testée
        VFS4JPathName res = VFS4JFiles.setOwner(VFS4JPathName, user);

        // vérification
        assertEquals(VFS4JPathName2, res);
        verify(fileManager.getAttribute()).setOwner(VFS4JPathName, user);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void setPosixFilePermissions() throws IOException {
        LOGGER.info("setPosixFilePermissions");
        VFS4JPathName VFS4JPathName = getPathName();
        VFS4JPathName VFS4JPathName2 = getPathName();

        Set<PosixFilePermission> permissionSet = new HashSet<>();
        permissionSet.add(PosixFilePermission.OWNER_READ);
        permissionSet.add(PosixFilePermission.GROUP_READ);

        when(fileManager.getAttribute().setPosixFilePermissions(VFS4JPathName, permissionSet)).thenReturn(VFS4JPathName2);

        // methode testée
        VFS4JPathName res = VFS4JFiles.setPosixFilePermissions(VFS4JPathName, permissionSet);

        // vérification
        assertEquals(VFS4JPathName2, res);
        verify(fileManager.getAttribute()).setPosixFilePermissions(VFS4JPathName, permissionSet);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void getAttribute() throws IOException {
        LOGGER.info("getAttribute");
        VFS4JPathName VFS4JPathName = getPathName();

        final String s = "abc";

        when(fileManager.getAttribute().getAttribute(VFS4JPathName, "attr1", LinkOption.NOFOLLOW_LINKS)).thenReturn(s);

        // methode testée
        Object res = VFS4JFiles.getAttribute(VFS4JPathName, "attr1", LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(s, res);
        verify(fileManager.getAttribute()).getAttribute(VFS4JPathName, "attr1", LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void getFileAttributeView() throws IOException {
        LOGGER.info("getFileAttributeView");
        VFS4JPathName VFS4JPathName = getPathName();

        final BasicFileAttributeView basicFileAttributeView = mock(BasicFileAttributeView.class);

        when(fileManager.getAttribute().getFileAttributeView(VFS4JPathName, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS)).thenReturn(basicFileAttributeView);

        // methode testée
        BasicFileAttributeView res = VFS4JFiles.getFileAttributeView(VFS4JPathName, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(basicFileAttributeView, res);
        verify(fileManager.getAttribute()).getFileAttributeView(VFS4JPathName, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void getLastModifiedTime() throws IOException {
        LOGGER.info("getLastModifiedTime");
        VFS4JPathName VFS4JPathName = getPathName();

        final FileTime fileTime = FileTime.from(1L, TimeUnit.SECONDS);

        when(fileManager.getAttribute().getLastModifiedTime(VFS4JPathName, LinkOption.NOFOLLOW_LINKS)).thenReturn(fileTime);

        // methode testée
        FileTime res = VFS4JFiles.getLastModifiedTime(VFS4JPathName, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(fileTime, res);
        verify(fileManager.getAttribute()).getLastModifiedTime(VFS4JPathName, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void getOwner() throws IOException {
        LOGGER.info("getOwner");
        VFS4JPathName VFS4JPathName = getPathName();

        final UserPrincipal userPrincipal = mock(UserPrincipal.class);

        when(fileManager.getAttribute().getOwner(VFS4JPathName, LinkOption.NOFOLLOW_LINKS)).thenReturn(userPrincipal);

        // methode testée
        UserPrincipal res = VFS4JFiles.getOwner(VFS4JPathName, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(userPrincipal, res);
        verify(fileManager.getAttribute()).getOwner(VFS4JPathName, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void getPosixFilePermissions() throws IOException {
        LOGGER.info("getPosixFilePermissions");
        VFS4JPathName VFS4JPathName = getPathName();

        final Set<PosixFilePermission> posixFilePermissions = new HashSet<>();
        posixFilePermissions.add(PosixFilePermission.OWNER_READ);

        when(fileManager.getAttribute().getPosixFilePermissions(VFS4JPathName, LinkOption.NOFOLLOW_LINKS)).thenReturn(posixFilePermissions);

        // methode testée
        Set<PosixFilePermission> res = VFS4JFiles.getPosixFilePermissions(VFS4JPathName, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(posixFilePermissions, res);
        verify(fileManager.getAttribute()).getPosixFilePermissions(VFS4JPathName, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void isExecutable() throws IOException {
        LOGGER.info("isExecutable");
        VFS4JPathName VFS4JPathName = getPathName();

        when(fileManager.getAttribute().isExecutable(VFS4JPathName)).thenReturn(true);

        // methode testée
        boolean res = VFS4JFiles.isExecutable(VFS4JPathName);

        // vérification
        assertEquals(true, res);
        verify(fileManager.getAttribute()).isExecutable(VFS4JPathName);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void isReadable() throws IOException {
        LOGGER.info("isReadable");
        VFS4JPathName VFS4JPathName = getPathName();

        when(fileManager.getAttribute().isReadable(VFS4JPathName)).thenReturn(true);

        // methode testée
        boolean res = VFS4JFiles.isReadable(VFS4JPathName);

        // vérification
        assertEquals(true, res);
        verify(fileManager.getAttribute()).isReadable(VFS4JPathName);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void isHidden() throws IOException {
        LOGGER.info("isHidden");
        VFS4JPathName VFS4JPathName = getPathName();

        when(fileManager.getAttribute().isHidden(VFS4JPathName)).thenReturn(true);

        // methode testée
        boolean res = VFS4JFiles.isHidden(VFS4JPathName);

        // vérification
        assertEquals(true, res);
        verify(fileManager.getAttribute()).isHidden(VFS4JPathName);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void isWritable() throws IOException {
        LOGGER.info("isWritable");
        VFS4JPathName VFS4JPathName = getPathName();

        when(fileManager.getAttribute().isWritable(VFS4JPathName)).thenReturn(true);

        // methode testée
        boolean res = VFS4JFiles.isWritable(VFS4JPathName);

        // vérification
        assertEquals(true, res);
        verify(fileManager.getAttribute()).isWritable(VFS4JPathName);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void readAttributes() throws IOException {
        LOGGER.info("readAttributes");
        VFS4JPathName VFS4JPathName = getPathName();

        final Map<String, Object> map = new HashMap<>();
        map.put("abc", 123);

        when(fileManager.getAttribute().readAttributes(VFS4JPathName, "attr1", LinkOption.NOFOLLOW_LINKS)).thenReturn(map);

        // methode testée
        Map<String, Object> res = VFS4JFiles.readAttributes(VFS4JPathName, "attr1", LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(map, res);
        verify(fileManager.getAttribute()).readAttributes(VFS4JPathName, "attr1", LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void readAttributes2() throws IOException {
        LOGGER.info("readAttributes2");
        VFS4JPathName VFS4JPathName = getPathName();

        final BasicFileAttributes basicFileAttributes = mock(BasicFileAttributes.class);

        when(fileManager.getAttribute().readAttributes(VFS4JPathName, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS)).thenReturn(basicFileAttributes);

        // methode testée
        BasicFileAttributes res = VFS4JFiles.readAttributes(VFS4JPathName, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(basicFileAttributes, res);
        verify(fileManager.getAttribute()).readAttributes(VFS4JPathName, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getAttribute());
    }

    @Test
    void createFile() throws IOException {
        LOGGER.info("createFile");
        VFS4JPathName VFS4JPathName = getPathName();

        final FileAttribute fileAttribute = mock(FileAttribute.class);

        // methode testée
        VFS4JFiles.createFile(VFS4JPathName, fileAttribute);

        // vérification
        verify(fileManager.getCommand()).createFile(VFS4JPathName, fileAttribute);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void createDirectory() throws IOException {
        LOGGER.info("createDirectory");
        VFS4JPathName VFS4JPathName = getPathName();

        final FileAttribute fileAttribute = mock(FileAttribute.class);

        // methode testée
        VFS4JFiles.createDirectory(VFS4JPathName, fileAttribute);

        // vérification
        verify(fileManager.getCommand()).createDirectory(VFS4JPathName, fileAttribute);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void createDirectories() throws IOException {
        LOGGER.info("createDirectories");
        VFS4JPathName VFS4JPathName = getPathName();

        final FileAttribute fileAttribute = mock(FileAttribute.class);

        // methode testée
        VFS4JFiles.createDirectories(VFS4JPathName, fileAttribute);

        // vérification
        verify(fileManager.getCommand()).createDirectories(VFS4JPathName, fileAttribute);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void delete() throws IOException {
        LOGGER.info("delete");
        VFS4JPathName VFS4JPathName = getPathName();

        // methode testée
        VFS4JFiles.delete(VFS4JPathName);

        // vérification
        verify(fileManager.getCommand()).delete(VFS4JPathName);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void deleteIfExists() throws IOException {
        LOGGER.info("deleteIfExists");
        VFS4JPathName VFS4JPathName = getPathName();

        when(fileManager.getCommand().deleteIfExists(VFS4JPathName)).thenReturn(true);

        // methode testée
        boolean res = VFS4JFiles.deleteIfExists(VFS4JPathName);

        // vérification
        assertTrue(res);
        verify(fileManager.getCommand()).deleteIfExists(VFS4JPathName);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void createLink() throws IOException {
        LOGGER.info("createLink");
        VFS4JPathName VFS4JPathName = getPathName();
        VFS4JPathName VFS4JPathName2 = getPathName2();
        VFS4JPathName VFS4JPathName3 = getPathName3();

        when(fileManager.getCommand().createLink(VFS4JPathName, VFS4JPathName2)).thenReturn(VFS4JPathName3);

        // methode testée
        VFS4JPathName res = VFS4JFiles.createLink(VFS4JPathName, VFS4JPathName2);

        // vérification
        assertEquals(VFS4JPathName3, res);
        verify(fileManager.getCommand()).createLink(VFS4JPathName, VFS4JPathName2);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void createSymbolicLink() throws IOException {
        LOGGER.info("createSymbolicLink");
        VFS4JPathName VFS4JPathName = getPathName();
        VFS4JPathName VFS4JPathName2 = getPathName2();
        VFS4JPathName VFS4JPathName3 = getPathName3();

        final FileAttribute fileAttribute = mock(FileAttribute.class);

        when(fileManager.getCommand().createSymbolicLink(VFS4JPathName, VFS4JPathName2, fileAttribute)).thenReturn(VFS4JPathName3);

        // methode testée
        VFS4JPathName res = VFS4JFiles.createSymbolicLink(VFS4JPathName, VFS4JPathName2, fileAttribute);

        // vérification
        assertEquals(VFS4JPathName3, res);
        verify(fileManager.getCommand()).createSymbolicLink(VFS4JPathName, VFS4JPathName2, fileAttribute);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void copy() throws IOException {
        LOGGER.info("copy");
        VFS4JPathName VFS4JPathName2 = getPathName2();

        final InputStream inputStream = new ByteArrayInputStream(getBytesArrays());

        final long len = 5;

        when(fileManager.getCommand().copy(inputStream, VFS4JPathName2, StandardCopyOption.COPY_ATTRIBUTES)).thenReturn(len);

        // methode testée
        long res = VFS4JFiles.copy(inputStream, VFS4JPathName2, StandardCopyOption.COPY_ATTRIBUTES);

        // vérification
        assertEquals(len, res);
        verify(fileManager.getCommand()).copy(inputStream, VFS4JPathName2, StandardCopyOption.COPY_ATTRIBUTES);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void copy2() throws IOException {
        LOGGER.info("copy2");
        VFS4JPathName VFS4JPathName = getPathName();

        final OutputStream outputStream = new ByteArrayOutputStream();

        final long len = 10;

        when(fileManager.getCommand().copy(VFS4JPathName, outputStream)).thenReturn(len);

        // methode testée
        long res = VFS4JFiles.copy(VFS4JPathName, outputStream);

        // vérification
        assertEquals(len, res);
        verify(fileManager.getCommand()).copy(VFS4JPathName, outputStream);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void copy3() throws IOException {
        LOGGER.info("copy3");
        VFS4JPathName VFS4JPathName = getPathName();
        VFS4JPathName VFS4JPathName2 = getPathName2();
        VFS4JPathName VFS4JPathName3 = getPathName3();

        when(fileManager.getCommand().copy(VFS4JPathName, VFS4JPathName2, StandardCopyOption.COPY_ATTRIBUTES)).thenReturn(VFS4JPathName3);

        // methode testée
        VFS4JPathName res = VFS4JFiles.copy(VFS4JPathName, VFS4JPathName2, StandardCopyOption.COPY_ATTRIBUTES);

        // vérification
        assertEquals(VFS4JPathName3, res);
        verify(fileManager.getCommand()).copy(VFS4JPathName, VFS4JPathName2, StandardCopyOption.COPY_ATTRIBUTES);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void move() throws IOException {
        LOGGER.info("move");
        VFS4JPathName VFS4JPathName = getPathName();
        VFS4JPathName VFS4JPathName2 = getPathName2();
        VFS4JPathName VFS4JPathName3 = getPathName3();

        when(fileManager.getCommand().move(VFS4JPathName, VFS4JPathName2, StandardCopyOption.COPY_ATTRIBUTES)).thenReturn(VFS4JPathName3);

        // methode testée
        VFS4JPathName res = VFS4JFiles.move(VFS4JPathName, VFS4JPathName2, StandardCopyOption.COPY_ATTRIBUTES);

        // vérification
        assertEquals(VFS4JPathName3, res);
        verify(fileManager.getCommand()).move(VFS4JPathName, VFS4JPathName2, StandardCopyOption.COPY_ATTRIBUTES);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void write() throws IOException {
        LOGGER.info("write");
        VFS4JPathName VFS4JPathName = getPathName();
        VFS4JPathName VFS4JPathName3 = getPathName3();

        byte[] buf = getBytesArrays();

        when(fileManager.getCommand().write(VFS4JPathName, buf, StandardOpenOption.READ)).thenReturn(VFS4JPathName3);

        // methode testée
        VFS4JPathName res = VFS4JFiles.write(VFS4JPathName, buf, StandardOpenOption.READ);

        // vérification
        assertEquals(VFS4JPathName3, res);
        verify(fileManager.getCommand()).write(VFS4JPathName, buf, StandardOpenOption.READ);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void write2() throws IOException {
        LOGGER.info("write2");
        VFS4JPathName VFS4JPathName = getPathName();
        VFS4JPathName VFS4JPathName3 = getPathName3();

        List<String> liste = getListStrings();

        when(fileManager.getCommand().write(VFS4JPathName, liste, StandardCharsets.UTF_8, StandardOpenOption.READ)).thenReturn(VFS4JPathName3);

        // methode testée
        VFS4JPathName res = VFS4JFiles.write(VFS4JPathName, liste, StandardCharsets.UTF_8, StandardOpenOption.READ);

        // vérification
        assertEquals(VFS4JPathName3, res);
        verify(fileManager.getCommand()).write(VFS4JPathName, liste, StandardCharsets.UTF_8, StandardOpenOption.READ);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void newInputStream() throws IOException {
        LOGGER.info("newInputStream");
        VFS4JPathName VFS4JPathName = getPathName();

        InputStream inputStream = new ByteArrayInputStream(getBytesArrays());

        when(fileManager.getOpen().newInputStream(VFS4JPathName, StandardOpenOption.READ)).thenReturn(inputStream);

        // methode testée
        InputStream res = VFS4JFiles.newInputStream(VFS4JPathName, StandardOpenOption.READ);

        // vérification
        assertEquals(inputStream, res);
        verify(fileManager.getOpen()).newInputStream(VFS4JPathName, StandardOpenOption.READ);
        verifyNoMoreInteractions(fileManager.getOpen());
    }

    @Test
    void newOutputStream() throws IOException {
        LOGGER.info("newOutputStream");
        VFS4JPathName VFS4JPathName = getPathName();

        OutputStream outputStream = new ByteArrayOutputStream();

        when(fileManager.getOpen().newOutputStream(VFS4JPathName, StandardOpenOption.READ)).thenReturn(outputStream);

        // methode testée
        OutputStream res = VFS4JFiles.newOutputStream(VFS4JPathName, StandardOpenOption.READ);

        // vérification
        assertEquals(outputStream, res);
        verify(fileManager.getOpen()).newOutputStream(VFS4JPathName, StandardOpenOption.READ);
        verifyNoMoreInteractions(fileManager.getOpen());
    }

    @Test
    void newReader() throws IOException {
        LOGGER.info("newReader");
        VFS4JPathName VFS4JPathName = getPathName();

        FileReader fileReader = mock(FileReader.class);

        when(fileManager.getOpen().newReader(VFS4JPathName)).thenReturn(fileReader);

        // methode testée
        FileReader res = VFS4JFiles.newReader(VFS4JPathName);

        // vérification
        assertEquals(fileReader, res);
        verify(fileManager.getOpen()).newReader(VFS4JPathName);
        verifyNoMoreInteractions(fileManager.getOpen());
    }

    @Test
    void newWriter() throws IOException {
        LOGGER.info("newWriter");
        VFS4JPathName VFS4JPathName = getPathName();

        FileWriter fileWriter = mock(FileWriter.class);
        final boolean append = true;

        when(fileManager.getOpen().newWriter(VFS4JPathName, append)).thenReturn(fileWriter);

        // methode testée
        FileWriter res = VFS4JFiles.newWriter(VFS4JPathName, append);

        // vérification
        assertEquals(fileWriter, res);
        verify(fileManager.getOpen()).newWriter(VFS4JPathName, append);
        verifyNoMoreInteractions(fileManager.getOpen());
    }

    @Test
    void newByteChannel() throws IOException {
        LOGGER.info("newByteChannel");
        VFS4JPathName VFS4JPathName = getPathName();

        FileAttribute fileAttribute = mock(FileAttribute.class);
        Set<OpenOption> set = new HashSet<>();
        set.add(StandardOpenOption.READ);
        SeekableByteChannel seekableByteChannel = mock(SeekableByteChannel.class);

        when(fileManager.getOpen().newByteChannel(VFS4JPathName, set, fileAttribute)).thenReturn(seekableByteChannel);

        // methode testée
        SeekableByteChannel res = VFS4JFiles.newByteChannel(VFS4JPathName, set, fileAttribute);

        // vérification
        assertEquals(seekableByteChannel, res);
        verify(fileManager.getOpen()).newByteChannel(VFS4JPathName, set, fileAttribute);
        verifyNoMoreInteractions(fileManager.getOpen());
    }

    @Test
    void newDirectoryStream() throws IOException {
        LOGGER.info("newDirectoryStream");
        VFS4JPathName VFS4JPathName = getPathName();

        DirectoryStream.Filter<VFS4JPathName> filter = mock(DirectoryStream.Filter.class);
        DirectoryStream<VFS4JPathName> directoryStream = mock(DirectoryStream.class);

        when(fileManager.getOpen().newDirectoryStream(VFS4JPathName, filter)).thenReturn(directoryStream);

        // methode testée
        DirectoryStream<VFS4JPathName> res = VFS4JFiles.newDirectoryStream(VFS4JPathName, filter);

        // vérification
        assertEquals(directoryStream, res);
        verify(fileManager.getOpen()).newDirectoryStream(VFS4JPathName, filter);
        verifyNoMoreInteractions(fileManager.getOpen());
    }

    @Test
    void exists() {
        LOGGER.info("exists");
        VFS4JPathName VFS4JPathName = getPathName();

        final boolean isExists = true;

        when(fileManager.getQuery().exists(VFS4JPathName, LinkOption.NOFOLLOW_LINKS)).thenReturn(isExists);

        // methode testée
        boolean res = VFS4JFiles.exists(VFS4JPathName, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(isExists, res);
        verify(fileManager.getQuery()).exists(VFS4JPathName, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void isDirectory() {
        LOGGER.info("isDirectory");
        VFS4JPathName VFS4JPathName = getPathName();

        final boolean isExists = true;

        when(fileManager.getQuery().isDirectory(VFS4JPathName, LinkOption.NOFOLLOW_LINKS)).thenReturn(isExists);

        // methode testée
        boolean res = VFS4JFiles.isDirectory(VFS4JPathName, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(isExists, res);
        verify(fileManager.getQuery()).isDirectory(VFS4JPathName, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void isRegularFile() {
        LOGGER.info("isRegularFile");
        VFS4JPathName VFS4JPathName = getPathName();

        final boolean isExists = true;

        when(fileManager.getQuery().isRegularFile(VFS4JPathName, LinkOption.NOFOLLOW_LINKS)).thenReturn(isExists);

        // methode testée
        boolean res = VFS4JFiles.isRegularFile(VFS4JPathName, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(isExists, res);
        verify(fileManager.getQuery()).isRegularFile(VFS4JPathName, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void isSameFile() throws IOException {
        LOGGER.info("isSameFile");
        VFS4JPathName VFS4JPathName = getPathName();
        VFS4JPathName VFS4JPathName2 = getPathName2();

        final boolean isExists = true;

        when(fileManager.getQuery().isSameFile(VFS4JPathName, VFS4JPathName2)).thenReturn(isExists);

        // methode testée
        boolean res = VFS4JFiles.isSameFile(VFS4JPathName, VFS4JPathName2);

        // vérification
        assertEquals(isExists, res);
        verify(fileManager.getQuery()).isSameFile(VFS4JPathName, VFS4JPathName2);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void isSymbolicLink() {
        LOGGER.info("isSymbolicLink");
        VFS4JPathName VFS4JPathName = getPathName();

        final boolean isExists = true;

        when(fileManager.getQuery().isSymbolicLink(VFS4JPathName)).thenReturn(isExists);

        // methode testée
        boolean res = VFS4JFiles.isSymbolicLink(VFS4JPathName);

        // vérification
        assertEquals(isExists, res);
        verify(fileManager.getQuery()).isSymbolicLink(VFS4JPathName);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void lines() throws IOException {
        LOGGER.info("lines");
        VFS4JPathName VFS4JPathName = getPathName();

        final Stream<String> stream = Stream.of("abc");

        when(fileManager.getQuery().lines(VFS4JPathName)).thenReturn(stream);

        // methode testée
        Stream<String> res = VFS4JFiles.lines(VFS4JPathName);

        // vérification
        assertEquals(stream, res);
        verify(fileManager.getQuery()).lines(VFS4JPathName);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void lines2() throws IOException {
        LOGGER.info("lines2");
        VFS4JPathName VFS4JPathName = getPathName();

        final Stream<String> stream = Stream.of("abc2");

        when(fileManager.getQuery().lines(VFS4JPathName, StandardCharsets.UTF_8)).thenReturn(stream);

        // methode testée
        Stream<String> res = VFS4JFiles.lines(VFS4JPathName, StandardCharsets.UTF_8);

        // vérification
        assertEquals(stream, res);
        verify(fileManager.getQuery()).lines(VFS4JPathName, StandardCharsets.UTF_8);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void notExists() {
        LOGGER.info("notExists");
        VFS4JPathName VFS4JPathName = getPathName();

        final boolean notExists = true;

        when(fileManager.getQuery().notExists(VFS4JPathName, LinkOption.NOFOLLOW_LINKS)).thenReturn(notExists);

        // methode testée
        boolean res = VFS4JFiles.notExists(VFS4JPathName, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(notExists, res);
        verify(fileManager.getQuery()).notExists(VFS4JPathName, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void readAllBytes() throws IOException {
        LOGGER.info("readAllBytes");
        VFS4JPathName VFS4JPathName = getPathName();

        final byte[] buf = getBytesArrays();

        when(fileManager.getQuery().readAllBytes(VFS4JPathName)).thenReturn(buf);

        // methode testée
        byte[] res = VFS4JFiles.readAllBytes(VFS4JPathName);

        // vérification
        assertArrayEquals(buf, res);
        verify(fileManager.getQuery()).readAllBytes(VFS4JPathName);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void readAllLines() throws IOException {
        LOGGER.info("readAllLines");
        VFS4JPathName VFS4JPathName = getPathName();

        final List<String> list = getListStrings();

        when(fileManager.getQuery().readAllLines(VFS4JPathName)).thenReturn(list);

        // methode testée
        List<String> res = VFS4JFiles.readAllLines(VFS4JPathName);

        // vérification
        assertEquals(list, res);
        verify(fileManager.getQuery()).readAllLines(VFS4JPathName);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void readAllLines2() throws IOException {
        LOGGER.info("readAllLines2");
        VFS4JPathName VFS4JPathName = getPathName();

        final List<String> list = getListStrings();

        when(fileManager.getQuery().readAllLines(VFS4JPathName, StandardCharsets.UTF_8)).thenReturn(list);

        // methode testée
        List<String> res = VFS4JFiles.readAllLines(VFS4JPathName, StandardCharsets.UTF_8);

        // vérification
        assertEquals(list, res);
        verify(fileManager.getQuery()).readAllLines(VFS4JPathName, StandardCharsets.UTF_8);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void size() throws IOException {
        LOGGER.info("size");
        VFS4JPathName VFS4JPathName = getPathName();

        final long len = 50;

        when(fileManager.getQuery().size(VFS4JPathName)).thenReturn(len);

        // methode testée
        long res = VFS4JFiles.size(VFS4JPathName);

        // vérification
        assertEquals(len, res);
        verify(fileManager.getQuery()).size(VFS4JPathName);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void list() throws IOException {
        LOGGER.info("list");
        VFS4JPathName VFS4JPathName = getPathName();

        final Stream<VFS4JPathName> stream = getStreamPathName();

        when(fileManager.getSearch().list(VFS4JPathName)).thenReturn(stream);

        // methode testée
        Stream<VFS4JPathName> res = VFS4JFiles.list(VFS4JPathName);

        // vérification
        assertEquals(stream, res);
        verify(fileManager.getSearch()).list(VFS4JPathName);
        verifyNoMoreInteractions(fileManager.getSearch());
    }

    @Test
    void walk() throws IOException {
        LOGGER.info("walk");
        VFS4JPathName VFS4JPathName = getPathName();

        final Stream<VFS4JPathName> stream = getStreamPathName();
        final int maxPathDepth = 5;

        when(fileManager.getSearch().walk(VFS4JPathName, maxPathDepth, FileVisitOption.FOLLOW_LINKS)).thenReturn(stream);

        // methode testée
        Stream<VFS4JPathName> res = VFS4JFiles.walk(VFS4JPathName, maxPathDepth, FileVisitOption.FOLLOW_LINKS);

        // vérification
        assertEquals(stream, res);
        verify(fileManager.getSearch()).walk(VFS4JPathName, maxPathDepth, FileVisitOption.FOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getSearch());
    }

    @Test
    void walk2() throws IOException {
        LOGGER.info("walk2");
        VFS4JPathName VFS4JPathName = getPathName();

        final Stream<VFS4JPathName> stream = getStreamPathName();

        when(fileManager.getSearch().walk(VFS4JPathName, FileVisitOption.FOLLOW_LINKS)).thenReturn(stream);

        // methode testée
        Stream<VFS4JPathName> res = VFS4JFiles.walk(VFS4JPathName, FileVisitOption.FOLLOW_LINKS);

        // vérification
        assertEquals(stream, res);
        verify(fileManager.getSearch()).walk(VFS4JPathName, FileVisitOption.FOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getSearch());
    }

    @Test
    void find() throws IOException {
        LOGGER.info("find");
        VFS4JPathName VFS4JPathName = getPathName();

        final Stream<VFS4JPathName> stream = getStreamPathName();
        final int maxPathDepth = 5;
        final BiPredicate<VFS4JPathName, BasicFileAttributes> func = (x, y) -> true;

        when(fileManager.getSearch().find(VFS4JPathName, maxPathDepth, func, FileVisitOption.FOLLOW_LINKS)).thenReturn(stream);

        // methode testée
        Stream<VFS4JPathName> res = VFS4JFiles.find(VFS4JPathName, maxPathDepth, func, FileVisitOption.FOLLOW_LINKS);

        // vérification
        assertEquals(stream, res);
        verify(fileManager.getSearch()).find(VFS4JPathName, maxPathDepth, func, FileVisitOption.FOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getSearch());
    }

    // methodes utilitaires

    private VFS4JPathName getPathName() {
        return new VFS4JPathName("aaa", "/tmp");
    }

    private VFS4JPathName getPathName2() {
        return new VFS4JPathName("bbb", "/tmp2");
    }

    private VFS4JPathName getPathName3() {
        return new VFS4JPathName("ccc", "/tmp3");
    }

    private byte[] getBytesArrays() {
        return "test".getBytes(StandardCharsets.UTF_8);
    }

    private List<String> getListStrings() {
        final List<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        return list;
    }

    private Stream<VFS4JPathName> getStreamPathName() {
        return Stream.of(new VFS4JPathName("aaa", "/tmp"), new VFS4JPathName("bbb", "/usr"));
    }
}