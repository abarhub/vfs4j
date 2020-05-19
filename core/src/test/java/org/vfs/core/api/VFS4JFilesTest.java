package org.vfs.core.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vfs.core.api.operation.*;

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
        verifyNoMoreInteractions(fileManager.getCommand());
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
        verifyNoMoreInteractions(fileManager.getCommand());
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
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void delete() throws IOException {
        LOGGER.info("delete");
        PathName pathName = getPathName();

        // methode testée
        VFS4JFiles.delete(pathName);

        // vérification
        verify(fileManager.getCommand()).delete(pathName);
        verifyNoMoreInteractions(fileManager.getCommand());
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
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void createLink() throws IOException {
        LOGGER.info("createLink");
        PathName pathName = getPathName();
        PathName pathName2 = getPathName2();
        PathName pathName3 = getPathName3();

        when(fileManager.getCommand().createLink(pathName, pathName2)).thenReturn(pathName3);

        // methode testée
        PathName res = VFS4JFiles.createLink(pathName, pathName2);

        // vérification
        assertEquals(pathName3, res);
        verify(fileManager.getCommand()).createLink(pathName, pathName2);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void createSymbolicLink() throws IOException {
        LOGGER.info("createSymbolicLink");
        PathName pathName = getPathName();
        PathName pathName2 = getPathName2();
        PathName pathName3 = getPathName3();

        final FileAttribute fileAttribute = mock(FileAttribute.class);

        when(fileManager.getCommand().createSymbolicLink(pathName, pathName2, fileAttribute)).thenReturn(pathName3);

        // methode testée
        PathName res = VFS4JFiles.createSymbolicLink(pathName, pathName2, fileAttribute);

        // vérification
        assertEquals(pathName3, res);
        verify(fileManager.getCommand()).createSymbolicLink(pathName, pathName2, fileAttribute);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void copy() throws IOException {
        LOGGER.info("copy");
        PathName pathName2 = getPathName2();

        final InputStream inputStream = new ByteArrayInputStream(getBytesArrays());

        final long len = 5;

        when(fileManager.getCommand().copy(inputStream, pathName2, StandardCopyOption.COPY_ATTRIBUTES)).thenReturn(len);

        // methode testée
        long res = VFS4JFiles.copy(inputStream, pathName2, StandardCopyOption.COPY_ATTRIBUTES);

        // vérification
        assertEquals(len, res);
        verify(fileManager.getCommand()).copy(inputStream, pathName2, StandardCopyOption.COPY_ATTRIBUTES);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void copy2() throws IOException {
        LOGGER.info("copy2");
        PathName pathName = getPathName();

        final OutputStream outputStream = new ByteArrayOutputStream();

        final long len = 10;

        when(fileManager.getCommand().copy(pathName, outputStream)).thenReturn(len);

        // methode testée
        long res = VFS4JFiles.copy(pathName, outputStream);

        // vérification
        assertEquals(len, res);
        verify(fileManager.getCommand()).copy(pathName, outputStream);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void copy3() throws IOException {
        LOGGER.info("copy3");
        PathName pathName = getPathName();
        PathName pathName2 = getPathName2();
        PathName pathName3 = getPathName3();

        when(fileManager.getCommand().copy(pathName, pathName2, StandardCopyOption.COPY_ATTRIBUTES)).thenReturn(pathName3);

        // methode testée
        PathName res = VFS4JFiles.copy(pathName, pathName2, StandardCopyOption.COPY_ATTRIBUTES);

        // vérification
        assertEquals(pathName3, res);
        verify(fileManager.getCommand()).copy(pathName, pathName2, StandardCopyOption.COPY_ATTRIBUTES);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void move() throws IOException {
        LOGGER.info("move");
        PathName pathName = getPathName();
        PathName pathName2 = getPathName2();
        PathName pathName3 = getPathName3();

        when(fileManager.getCommand().move(pathName, pathName2, StandardCopyOption.COPY_ATTRIBUTES)).thenReturn(pathName3);

        // methode testée
        PathName res = VFS4JFiles.move(pathName, pathName2, StandardCopyOption.COPY_ATTRIBUTES);

        // vérification
        assertEquals(pathName3, res);
        verify(fileManager.getCommand()).move(pathName, pathName2, StandardCopyOption.COPY_ATTRIBUTES);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void write() throws IOException {
        LOGGER.info("write");
        PathName pathName = getPathName();
        PathName pathName3 = getPathName3();

        byte[] buf = getBytesArrays();

        when(fileManager.getCommand().write(pathName, buf, StandardOpenOption.READ)).thenReturn(pathName3);

        // methode testée
        PathName res = VFS4JFiles.write(pathName, buf, StandardOpenOption.READ);

        // vérification
        assertEquals(pathName3, res);
        verify(fileManager.getCommand()).write(pathName, buf, StandardOpenOption.READ);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void write2() throws IOException {
        LOGGER.info("write2");
        PathName pathName = getPathName();
        PathName pathName3 = getPathName3();

        List<String> liste = getListStrings();

        when(fileManager.getCommand().write(pathName, liste, StandardCharsets.UTF_8, StandardOpenOption.READ)).thenReturn(pathName3);

        // methode testée
        PathName res = VFS4JFiles.write(pathName, liste, StandardCharsets.UTF_8, StandardOpenOption.READ);

        // vérification
        assertEquals(pathName3, res);
        verify(fileManager.getCommand()).write(pathName, liste, StandardCharsets.UTF_8, StandardOpenOption.READ);
        verifyNoMoreInteractions(fileManager.getCommand());
    }

    @Test
    void newInputStream() throws IOException {
        LOGGER.info("newInputStream");
        PathName pathName = getPathName();

        InputStream inputStream = new ByteArrayInputStream(getBytesArrays());

        when(fileManager.getOpen().newInputStream(pathName, StandardOpenOption.READ)).thenReturn(inputStream);

        // methode testée
        InputStream res = VFS4JFiles.newInputStream(pathName, StandardOpenOption.READ);

        // vérification
        assertEquals(inputStream, res);
        verify(fileManager.getOpen()).newInputStream(pathName, StandardOpenOption.READ);
        verifyNoMoreInteractions(fileManager.getOpen());
    }

    @Test
    void newOutputStream() throws IOException {
        LOGGER.info("newOutputStream");
        PathName pathName = getPathName();

        OutputStream outputStream = new ByteArrayOutputStream();

        when(fileManager.getOpen().newOutputStream(pathName, StandardOpenOption.READ)).thenReturn(outputStream);

        // methode testée
        OutputStream res = VFS4JFiles.newOutputStream(pathName, StandardOpenOption.READ);

        // vérification
        assertEquals(outputStream, res);
        verify(fileManager.getOpen()).newOutputStream(pathName, StandardOpenOption.READ);
        verifyNoMoreInteractions(fileManager.getOpen());
    }

    @Test
    void newReader() throws IOException {
        LOGGER.info("newReader");
        PathName pathName = getPathName();

        FileReader fileReader = mock(FileReader.class);

        when(fileManager.getOpen().newReader(pathName)).thenReturn(fileReader);

        // methode testée
        FileReader res = VFS4JFiles.newReader(pathName);

        // vérification
        assertEquals(fileReader, res);
        verify(fileManager.getOpen()).newReader(pathName);
        verifyNoMoreInteractions(fileManager.getOpen());
    }

    @Test
    void newWriter() throws IOException {
        LOGGER.info("newWriter");
        PathName pathName = getPathName();

        FileWriter fileWriter = mock(FileWriter.class);
        final boolean append = true;

        when(fileManager.getOpen().newWriter(pathName, append)).thenReturn(fileWriter);

        // methode testée
        FileWriter res = VFS4JFiles.newWriter(pathName, append);

        // vérification
        assertEquals(fileWriter, res);
        verify(fileManager.getOpen()).newWriter(pathName, append);
        verifyNoMoreInteractions(fileManager.getOpen());
    }

    @Test
    void newByteChannel() throws IOException {
        LOGGER.info("newByteChannel");
        PathName pathName = getPathName();

        FileAttribute fileAttribute = mock(FileAttribute.class);
        Set<OpenOption> set = new HashSet<>();
        set.add(StandardOpenOption.READ);
        SeekableByteChannel seekableByteChannel = mock(SeekableByteChannel.class);

        when(fileManager.getOpen().newByteChannel(pathName, set, fileAttribute)).thenReturn(seekableByteChannel);

        // methode testée
        SeekableByteChannel res = VFS4JFiles.newByteChannel(pathName, set, fileAttribute);

        // vérification
        assertEquals(seekableByteChannel, res);
        verify(fileManager.getOpen()).newByteChannel(pathName, set, fileAttribute);
        verifyNoMoreInteractions(fileManager.getOpen());
    }

    @Test
    void newDirectoryStream() throws IOException {
        LOGGER.info("newDirectoryStream");
        PathName pathName = getPathName();

        DirectoryStream.Filter<PathName> filter = mock(DirectoryStream.Filter.class);
        DirectoryStream<PathName> directoryStream = mock(DirectoryStream.class);

        when(fileManager.getOpen().newDirectoryStream(pathName, filter)).thenReturn(directoryStream);

        // methode testée
        DirectoryStream<PathName> res = VFS4JFiles.newDirectoryStream(pathName, filter);

        // vérification
        assertEquals(directoryStream, res);
        verify(fileManager.getOpen()).newDirectoryStream(pathName, filter);
        verifyNoMoreInteractions(fileManager.getOpen());
    }

    @Test
    void exists() {
        LOGGER.info("exists");
        PathName pathName = getPathName();

        final boolean isExists = true;

        when(fileManager.getQuery().exists(pathName, LinkOption.NOFOLLOW_LINKS)).thenReturn(isExists);

        // methode testée
        boolean res = VFS4JFiles.exists(pathName, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(isExists, res);
        verify(fileManager.getQuery()).exists(pathName, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void isDirectory() {
        LOGGER.info("isDirectory");
        PathName pathName = getPathName();

        final boolean isExists = true;

        when(fileManager.getQuery().isDirectory(pathName, LinkOption.NOFOLLOW_LINKS)).thenReturn(isExists);

        // methode testée
        boolean res = VFS4JFiles.isDirectory(pathName, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(isExists, res);
        verify(fileManager.getQuery()).isDirectory(pathName, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void isRegularFile() {
        LOGGER.info("isRegularFile");
        PathName pathName = getPathName();

        final boolean isExists = true;

        when(fileManager.getQuery().isRegularFile(pathName, LinkOption.NOFOLLOW_LINKS)).thenReturn(isExists);

        // methode testée
        boolean res = VFS4JFiles.isRegularFile(pathName, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(isExists, res);
        verify(fileManager.getQuery()).isRegularFile(pathName, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void isSameFile() throws IOException {
        LOGGER.info("isSameFile");
        PathName pathName = getPathName();
        PathName pathName2 = getPathName2();

        final boolean isExists = true;

        when(fileManager.getQuery().isSameFile(pathName, pathName2)).thenReturn(isExists);

        // methode testée
        boolean res = VFS4JFiles.isSameFile(pathName, pathName2);

        // vérification
        assertEquals(isExists, res);
        verify(fileManager.getQuery()).isSameFile(pathName, pathName2);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void isSymbolicLink() {
        LOGGER.info("isSymbolicLink");
        PathName pathName = getPathName();

        final boolean isExists = true;

        when(fileManager.getQuery().isSymbolicLink(pathName)).thenReturn(isExists);

        // methode testée
        boolean res = VFS4JFiles.isSymbolicLink(pathName);

        // vérification
        assertEquals(isExists, res);
        verify(fileManager.getQuery()).isSymbolicLink(pathName);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void lines() throws IOException {
        LOGGER.info("lines");
        PathName pathName = getPathName();

        final Stream<String> stream = Stream.of("abc");

        when(fileManager.getQuery().lines(pathName)).thenReturn(stream);

        // methode testée
        Stream<String> res = VFS4JFiles.lines(pathName);

        // vérification
        assertEquals(stream, res);
        verify(fileManager.getQuery()).lines(pathName);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void lines2() throws IOException {
        LOGGER.info("lines2");
        PathName pathName = getPathName();

        final Stream<String> stream = Stream.of("abc2");

        when(fileManager.getQuery().lines(pathName, StandardCharsets.UTF_8)).thenReturn(stream);

        // methode testée
        Stream<String> res = VFS4JFiles.lines(pathName, StandardCharsets.UTF_8);

        // vérification
        assertEquals(stream, res);
        verify(fileManager.getQuery()).lines(pathName, StandardCharsets.UTF_8);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void notExists() {
        LOGGER.info("notExists");
        PathName pathName = getPathName();

        final boolean notExists = true;

        when(fileManager.getQuery().notExists(pathName, LinkOption.NOFOLLOW_LINKS)).thenReturn(notExists);

        // methode testée
        boolean res = VFS4JFiles.notExists(pathName, LinkOption.NOFOLLOW_LINKS);

        // vérification
        assertEquals(notExists, res);
        verify(fileManager.getQuery()).notExists(pathName, LinkOption.NOFOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void readAllBytes() throws IOException {
        LOGGER.info("readAllBytes");
        PathName pathName = getPathName();

        final byte[] buf = getBytesArrays();

        when(fileManager.getQuery().readAllBytes(pathName)).thenReturn(buf);

        // methode testée
        byte[] res = VFS4JFiles.readAllBytes(pathName);

        // vérification
        assertArrayEquals(buf, res);
        verify(fileManager.getQuery()).readAllBytes(pathName);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void readAllLines() throws IOException {
        LOGGER.info("readAllLines");
        PathName pathName = getPathName();

        final List<String> list = getListStrings();

        when(fileManager.getQuery().readAllLines(pathName)).thenReturn(list);

        // methode testée
        List<String> res = VFS4JFiles.readAllLines(pathName);

        // vérification
        assertEquals(list, res);
        verify(fileManager.getQuery()).readAllLines(pathName);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void readAllLines2() throws IOException {
        LOGGER.info("readAllLines2");
        PathName pathName = getPathName();

        final List<String> list = getListStrings();

        when(fileManager.getQuery().readAllLines(pathName, StandardCharsets.UTF_8)).thenReturn(list);

        // methode testée
        List<String> res = VFS4JFiles.readAllLines(pathName, StandardCharsets.UTF_8);

        // vérification
        assertEquals(list, res);
        verify(fileManager.getQuery()).readAllLines(pathName, StandardCharsets.UTF_8);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void size() throws IOException {
        LOGGER.info("size");
        PathName pathName = getPathName();

        final long len = 50;

        when(fileManager.getQuery().size(pathName)).thenReturn(len);

        // methode testée
        long res = VFS4JFiles.size(pathName);

        // vérification
        assertEquals(len, res);
        verify(fileManager.getQuery()).size(pathName);
        verifyNoMoreInteractions(fileManager.getQuery());
    }

    @Test
    void list() throws IOException {
        LOGGER.info("list");
        PathName pathName = getPathName();

        final Stream<PathName> stream = getStreamPathName();

        when(fileManager.getSearch().list(pathName)).thenReturn(stream);

        // methode testée
        Stream<PathName> res = VFS4JFiles.list(pathName);

        // vérification
        assertEquals(stream, res);
        verify(fileManager.getSearch()).list(pathName);
        verifyNoMoreInteractions(fileManager.getSearch());
    }

    @Test
    void walk() throws IOException {
        LOGGER.info("walk");
        PathName pathName = getPathName();

        final Stream<PathName> stream = getStreamPathName();
        final int maxPathDepth = 5;

        when(fileManager.getSearch().walk(pathName, maxPathDepth, FileVisitOption.FOLLOW_LINKS)).thenReturn(stream);

        // methode testée
        Stream<PathName> res = VFS4JFiles.walk(pathName, maxPathDepth, FileVisitOption.FOLLOW_LINKS);

        // vérification
        assertEquals(stream, res);
        verify(fileManager.getSearch()).walk(pathName, maxPathDepth, FileVisitOption.FOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getSearch());
    }

    @Test
    void walk2() throws IOException {
        LOGGER.info("walk2");
        PathName pathName = getPathName();

        final Stream<PathName> stream = getStreamPathName();

        when(fileManager.getSearch().walk(pathName, FileVisitOption.FOLLOW_LINKS)).thenReturn(stream);

        // methode testée
        Stream<PathName> res = VFS4JFiles.walk(pathName, FileVisitOption.FOLLOW_LINKS);

        // vérification
        assertEquals(stream, res);
        verify(fileManager.getSearch()).walk(pathName, FileVisitOption.FOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getSearch());
    }

    @Test
    void find() throws IOException {
        LOGGER.info("find");
        PathName pathName = getPathName();

        final Stream<PathName> stream = getStreamPathName();
        final int maxPathDepth = 5;
        final BiPredicate<PathName, BasicFileAttributes> func = (x, y) -> true;

        when(fileManager.getSearch().find(pathName, maxPathDepth, func, FileVisitOption.FOLLOW_LINKS)).thenReturn(stream);

        // methode testée
        Stream<PathName> res = VFS4JFiles.find(pathName, maxPathDepth, func, FileVisitOption.FOLLOW_LINKS);

        // vérification
        assertEquals(stream, res);
        verify(fileManager.getSearch()).find(pathName, maxPathDepth, func, FileVisitOption.FOLLOW_LINKS);
        verifyNoMoreInteractions(fileManager.getSearch());
    }

    // methodes utilitaires

    private PathName getPathName() {
        return new PathName("aaa", "/tmp");
    }

    private PathName getPathName2() {
        return new PathName("bbb", "/tmp2");
    }

    private PathName getPathName3() {
        return new PathName("ccc", "/tmp3");
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

    private Stream<PathName> getStreamPathName() {
        return Stream.of(new PathName("aaa", "/tmp"), new PathName("bbb", "/usr"));
    }
}