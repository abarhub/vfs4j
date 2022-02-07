package io.github.abarhub.vfs.core.plugin.audit.operation;

import io.github.abarhub.vfs.core.api.VFS4JPathName;
import io.github.abarhub.vfs.core.api.VFS4JPaths;
import io.github.abarhub.vfs.core.api.operation.VFS4JQuery;
import io.github.abarhub.vfs.core.config.VFS4JConfig;
import io.github.abarhub.vfs.core.plugin.audit.VFS4JAuditLogLevel;
import io.github.abarhub.vfs.core.plugin.audit.VFS4JAuditPlugins;
import io.github.abarhub.vfs.core.plugin.audit.VFS4JLogAudit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VFS4JAuditQueryTest implements VFS4JLogAudit {

    private static final Logger LOGGER = LoggerFactory.getLogger(VFS4JAuditCommandTest.class);

    public static final String PATH1 = "path1";

    private final VFS4JQuery query = mock(VFS4JQuery.class);

    private VFS4JAuditQuery auditQuery;

    private final List<LogMessage> listLog = new ArrayList<>();

    @BeforeEach
    void setUp() {
        LOGGER.info("setUp");
        VFS4JAuditPlugins vfs4JAuditPlugins = new VFS4JAuditPlugins();
        vfs4JAuditPlugins.addListener(this);
        Map<String, String> config = defautConfig();
        VFS4JConfig config2 = new VFS4JConfig();
        vfs4JAuditPlugins.init("exemple1", config, config2);
        auditQuery = new VFS4JAuditQuery(vfs4JAuditPlugins, query);
    }

    @Nested
    class TestExists {

        @Test
        void existsOK() {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(query.exists(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditQuery.exists(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(query).exists(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("exists for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void existsDisabled() {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            when(query.exists(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditQuery.exists(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(query).exists(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(0, listLog.size());
        }

        @Test
        void existsException() {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(query.exists(VFS4JPathName)).thenThrow(new RuntimeException("Error exists"));

            // methode testée
            RuntimeException exception = assertThrows(RuntimeException.class, () -> auditQuery.exists(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error exists", exception.getMessage());
            verify(query).exists(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for exists for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestIsDirectory {

        @Test
        void isDirectoryOK() {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(query.isDirectory(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditQuery.isDirectory(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(query).isDirectory(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("isDirectory for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void isDirectoryDisabled() {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            when(query.isDirectory(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditQuery.isDirectory(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(query).isDirectory(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(0, listLog.size());
        }

        @Test
        void isDirectoryException() {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(query.isDirectory(VFS4JPathName)).thenThrow(new RuntimeException("Error isDirectory"));

            // methode testée
            RuntimeException exception = assertThrows(RuntimeException.class, () -> auditQuery.isDirectory(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error isDirectory", exception.getMessage());
            verify(query).isDirectory(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for isDirectory for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestIsRegularFile {

        @Test
        void isRegularFileOK() {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(query.isRegularFile(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditQuery.isRegularFile(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(query).isRegularFile(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("isRegularFile for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void isRegularFileDisabled() {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            when(query.isRegularFile(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditQuery.isRegularFile(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(query).isRegularFile(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(0, listLog.size());
        }

        @Test
        void isRegularFileException() {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(query.isRegularFile(VFS4JPathName)).thenThrow(new RuntimeException("Error isRegularFile"));

            // methode testée
            RuntimeException exception = assertThrows(RuntimeException.class, () -> auditQuery.isRegularFile(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error isRegularFile", exception.getMessage());
            verify(query).isRegularFile(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for isRegularFile for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestIsSameFile {

        @Test
        void isSameFileOK() throws IOException {
            VFS4JPathName file = getPathName("abc.txt");
            VFS4JPathName file2 = getPathName("abc2.txt");
            when(query.isSameFile(file, file2)).thenReturn(true);

            // methode testée
            boolean res = auditQuery.isSameFile(file, file2);

            // vérifications
            assertTrue(res);
            verify(query).isSameFile(file, file2);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("isSameFile for file {} and {}", logMessage.getMessage());
            assertEquals(Arrays.asList(file, file2), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void isSameFileDisabled() throws IOException {
            VFS4JPathName file = getPathName("abc.jpg");
            VFS4JPathName file2 = getPathName("abc2.jpg");
            when(query.isSameFile(file, file2)).thenReturn(true);

            // methode testée
            boolean res = auditQuery.isSameFile(file, file2);

            // vérifications
            assertTrue(res);
            verify(query).isSameFile(file, file2);
            verifyNoMoreInteractions(query);
            assertEquals(0, listLog.size());
        }

        @Test
        void isSameFileException() throws IOException {
            VFS4JPathName file = getPathName("abc.txt");
            VFS4JPathName file2 = getPathName("abc2.txt");
            when(query.isSameFile(file, file2)).thenThrow(new IOException("Error isSameFile"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditQuery.isSameFile(file, file2));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error isSameFile", exception.getMessage());
            verify(query).isSameFile(file, file2);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for isSameFile for file {} and {}", logMessage.getMessage());
            assertEquals(Arrays.asList(file, file2), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestIsSymbolicLink {

        @Test
        void isSymbolicLinkOK() {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(query.isSymbolicLink(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditQuery.isSymbolicLink(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(query).isSymbolicLink(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("isSymbolicLink for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void isSymbolicLinkDisabled() {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            when(query.isSymbolicLink(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditQuery.isSymbolicLink(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(query).isSymbolicLink(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(0, listLog.size());
        }

        @Test
        void isSymbolicLinkException() {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(query.isSymbolicLink(VFS4JPathName)).thenThrow(new RuntimeException("Error isSymbolicLink"));

            // methode testée
            RuntimeException exception = assertThrows(RuntimeException.class, () -> auditQuery.isSymbolicLink(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error isSymbolicLink", exception.getMessage());
            verify(query).isSymbolicLink(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for isSymbolicLink for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestLines {

        @Test
        void linesOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            Stream<String> stream = Stream.of("abc");
            when(query.lines(VFS4JPathName)).thenReturn(stream);

            // methode testée
            Stream<String> res = auditQuery.lines(VFS4JPathName);

            // vérifications
            assertNotNull(res);
            assertEquals(stream, res);
            verify(query).lines(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("lines for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void linesDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            Stream<String> stream = Stream.of("abc");
            when(query.lines(VFS4JPathName)).thenReturn(stream);

            // methode testée
            Stream<String> res = auditQuery.lines(VFS4JPathName);

            // vérifications
            assertNotNull(res);
            assertEquals(stream, res);
            verify(query).lines(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(0, listLog.size());
        }

        @Test
        void linesException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(query.lines(VFS4JPathName)).thenThrow(new IOException("Error lines"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditQuery.lines(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error lines", exception.getMessage());
            verify(query).lines(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for lines for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestLinesWithCharset {

        @Test
        void linesOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            Stream<String> stream = Stream.of("abc");
            when(query.lines(VFS4JPathName, StandardCharsets.UTF_8)).thenReturn(stream);

            // methode testée
            Stream<String> res = auditQuery.lines(VFS4JPathName, StandardCharsets.UTF_8);

            // vérifications
            assertNotNull(res);
            assertEquals(stream, res);
            verify(query).lines(VFS4JPathName, StandardCharsets.UTF_8);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("lines for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void linesDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            Stream<String> stream = Stream.of("abc");
            when(query.lines(VFS4JPathName, StandardCharsets.UTF_8)).thenReturn(stream);

            // methode testée
            Stream<String> res = auditQuery.lines(VFS4JPathName, StandardCharsets.UTF_8);

            // vérifications
            assertNotNull(res);
            assertEquals(stream, res);
            verify(query).lines(VFS4JPathName, StandardCharsets.UTF_8);
            verifyNoMoreInteractions(query);
            assertEquals(0, listLog.size());
        }

        @Test
        void linesException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(query.lines(VFS4JPathName, StandardCharsets.UTF_8)).thenThrow(new IOException("Error lines"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditQuery.lines(VFS4JPathName, StandardCharsets.UTF_8));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error lines", exception.getMessage());
            verify(query).lines(VFS4JPathName, StandardCharsets.UTF_8);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for lines for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestNotExists {

        @Test
        void isSymbolicLinkOK() {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(query.notExists(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditQuery.notExists(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(query).notExists(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("notExists for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void notExistsDisabled() {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            when(query.notExists(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditQuery.notExists(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(query).notExists(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(0, listLog.size());
        }

        @Test
        void notExistsException() {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(query.notExists(VFS4JPathName)).thenThrow(new RuntimeException("Error notExists"));

            // methode testée
            RuntimeException exception = assertThrows(RuntimeException.class, () -> auditQuery.notExists(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error notExists", exception.getMessage());
            verify(query).notExists(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for notExists for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestReadAllBytes {

        @Test
        void readAllBytesOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            byte[] buf = getBytes();
            when(query.readAllBytes(VFS4JPathName)).thenReturn(buf);

            // methode testée
            byte[] res = auditQuery.readAllBytes(VFS4JPathName);

            // vérifications
            assertNotNull(res);
            assertArrayEquals(buf, res);
            verify(query).readAllBytes(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("readAllBytes for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void readAllBytesDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            byte[] buf = getBytes();
            when(query.readAllBytes(VFS4JPathName)).thenReturn(buf);

            // methode testée
            byte[] res = auditQuery.readAllBytes(VFS4JPathName);

            // vérifications
            assertNotNull(res);
            assertArrayEquals(buf, res);
            verify(query).readAllBytes(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(0, listLog.size());
        }

        @Test
        void readAllBytesException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(query.readAllBytes(VFS4JPathName)).thenThrow(new IOException("Error readAllBytes"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditQuery.readAllBytes(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error readAllBytes", exception.getMessage());
            verify(query).readAllBytes(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for readAllBytes for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestReadAllLines {

        @Test
        void readAllLinesOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            List<String> liste = getList();
            when(query.readAllLines(VFS4JPathName)).thenReturn(liste);

            // methode testée
            List<String> res = auditQuery.readAllLines(VFS4JPathName);

            // vérifications
            assertNotNull(res);
            assertEquals(liste, res);
            verify(query).readAllLines(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("readAllLines for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void readAllLinesDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            List<String> liste = getList();
            when(query.readAllLines(VFS4JPathName)).thenReturn(liste);

            // methode testée
            List<String> res = auditQuery.readAllLines(VFS4JPathName);

            // vérifications
            assertNotNull(res);
            assertEquals(liste, res);
            verify(query).readAllLines(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(0, listLog.size());
        }

        @Test
        void readAllLinesException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(query.readAllLines(VFS4JPathName)).thenThrow(new IOException("Error readAllLines"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditQuery.readAllLines(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error readAllLines", exception.getMessage());
            verify(query).readAllLines(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for readAllLines for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestReadAllLinesWithCharset {

        @Test
        void readAllLinesOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            List<String> liste = getList();
            when(query.readAllLines(VFS4JPathName, StandardCharsets.UTF_8)).thenReturn(liste);

            // methode testée
            List<String> res = auditQuery.readAllLines(VFS4JPathName, StandardCharsets.UTF_8);

            // vérifications
            assertNotNull(res);
            assertEquals(liste, res);
            verify(query).readAllLines(VFS4JPathName, StandardCharsets.UTF_8);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("readAllLines for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void readAllLinesDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            List<String> liste = getList();
            when(query.readAllLines(VFS4JPathName, StandardCharsets.UTF_8)).thenReturn(liste);

            // methode testée
            List<String> res = auditQuery.readAllLines(VFS4JPathName, StandardCharsets.UTF_8);

            // vérifications
            assertNotNull(res);
            assertEquals(liste, res);
            verify(query).readAllLines(VFS4JPathName, StandardCharsets.UTF_8);
            verifyNoMoreInteractions(query);
            assertEquals(0, listLog.size());
        }

        @Test
        void readAllLinesException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(query.readAllLines(VFS4JPathName, StandardCharsets.UTF_8)).thenThrow(new IOException("Error readAllLines"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditQuery.readAllLines(VFS4JPathName, StandardCharsets.UTF_8));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error readAllLines", exception.getMessage());
            verify(query).readAllLines(VFS4JPathName, StandardCharsets.UTF_8);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for readAllLines for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestSize {

        @Test
        void sizeOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            long size = 50;
            when(query.size(VFS4JPathName)).thenReturn(size);

            // methode testée
            long res = auditQuery.size(VFS4JPathName);

            // vérifications
            assertEquals(size, res);
            verify(query).size(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("size for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void sizeDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            long size = 50;
            when(query.size(VFS4JPathName)).thenReturn(size);

            // methode testée
            long res = auditQuery.size(VFS4JPathName);

            // vérifications
            assertEquals(size, res);
            verify(query).size(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(0, listLog.size());
        }

        @Test
        void sizeException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(query.size(VFS4JPathName)).thenThrow(new IOException("Error size"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditQuery.size(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error size", exception.getMessage());
            verify(query).size(VFS4JPathName);
            verifyNoMoreInteractions(query);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for size for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    // méthodes utilitaires

    private static VFS4JPathName getPathName(String filename) {
        return VFS4JPaths.get(PATH1, filename);
    }

    @Override
    public void log(VFS4JAuditLogLevel logLevel, boolean error, String message, Exception exception, Object... parameters) {
        LogMessage logMessage;
        if (parameters != null && parameters.length > 0) {
            logMessage = new LogMessage(logLevel, error, message, Arrays.asList(parameters), exception);
        } else {
            logMessage = new LogMessage(logLevel, error, message, new ArrayList<>(), exception);
        }
        listLog.add(logMessage);
    }

    private Map<String, String> defautConfig() {

        Map<String, String> config = new HashMap<>();
        config.put("loglevel", VFS4JAuditLogLevel.INFO.name());
        config.put("filterPath", "*.txt");
        return config;
    }

    private byte[] getBytes() {
        return "abc".getBytes(StandardCharsets.UTF_8);
    }

    private List<String> getList() {
        return Arrays.asList("abc", "aaa");
    }

}