package com.github.abarhub.vfs.core.plugin.audit.operation;

import com.github.abarhub.vfs.core.api.VFS4JPathName;
import com.github.abarhub.vfs.core.config.VFS4JConfig;
import com.github.abarhub.vfs.core.plugin.audit.VFS4JAuditLogLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.abarhub.vfs.core.api.operation.VFS4JCommand;
import com.github.abarhub.vfs.core.plugin.audit.VFS4JAuditPlugins;
import com.github.abarhub.vfs.core.plugin.audit.VFS4JLogAudit;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VFS4JAuditCommandTest implements VFS4JLogAudit {

    private static final Logger LOGGER = LoggerFactory.getLogger(VFS4JAuditCommandTest.class);

    public static final String PATH1 = "path1";

    private final VFS4JCommand command = mock(VFS4JCommand.class);

    private VFS4JAuditCommand auditCommand;

    private final List<LogMessage> listLog = new ArrayList<>();

    @BeforeEach
    void setUp() {
        LOGGER.info("setUp");
        VFS4JAuditPlugins vfs4JAuditPlugins = new VFS4JAuditPlugins();
        vfs4JAuditPlugins.addListener(this);
        Map<String, String> config = defautConfig();
        VFS4JConfig config2 = new VFS4JConfig();
        vfs4JAuditPlugins.init("exemple1", config, config2);
        auditCommand = new VFS4JAuditCommand(vfs4JAuditPlugins, command);
    }

    @Nested
    class TestCreateFile {

        @Test
        void createFileOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.txt");
            when(command.createFile(VFS4JPathName)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditCommand.createFile(VFS4JPathName);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(command).createFile(VFS4JPathName);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("createFile for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void createFileDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.jpg");
            when(command.createFile(VFS4JPathName)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditCommand.createFile(VFS4JPathName);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(command).createFile(VFS4JPathName);
            verifyNoMoreInteractions(command);
            assertEquals(0, listLog.size());
        }

        @Test
        void createFileException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(command.createFile(VFS4JPathName)).thenThrow(new IOException("Error createFile"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditCommand.createFile(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error createFile", exception.getMessage());
            verify(command).createFile(VFS4JPathName);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for createFile for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestCreateDirectory {

        @Test
        void createDirectoryOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.txt");
            when(command.createDirectory(VFS4JPathName)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditCommand.createDirectory(VFS4JPathName);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(command).createDirectory(VFS4JPathName);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("createDirectory for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void createFileDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.jpg");
            when(command.createDirectory(VFS4JPathName)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditCommand.createDirectory(VFS4JPathName);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(command).createDirectory(VFS4JPathName);
            verifyNoMoreInteractions(command);
            assertEquals(0, listLog.size());
        }

        @Test
        void createFileException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(command.createDirectory(VFS4JPathName)).thenThrow(new IOException("Error createDirectory"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditCommand.createDirectory(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error createDirectory", exception.getMessage());
            verify(command).createDirectory(VFS4JPathName);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for createDirectory for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestCreateDirectories {

        @Test
        void createDirectoriesOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.txt");
            when(command.createDirectories(VFS4JPathName)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditCommand.createDirectories(VFS4JPathName);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(command).createDirectories(VFS4JPathName);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("createDirectories for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void createDirectoriesDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.jpg");
            when(command.createDirectories(VFS4JPathName)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditCommand.createDirectories(VFS4JPathName);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(command).createDirectories(VFS4JPathName);
            verifyNoMoreInteractions(command);
            assertEquals(0, listLog.size());
        }

        @Test
        void createDirectoriesException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(command.createDirectories(VFS4JPathName)).thenThrow(new IOException("Error createDirectories"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditCommand.createDirectories(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error createDirectories", exception.getMessage());
            verify(command).createDirectories(VFS4JPathName);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for createDirectories for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class Testelete {

        @Test
        void deleteOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");

            // methode testée
            auditCommand.delete(VFS4JPathName);

            // vérifications
            verify(command).delete(VFS4JPathName);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("delete for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void deleteDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");

            // methode testée
            auditCommand.delete(VFS4JPathName);

            // vérifications
            verify(command).delete(VFS4JPathName);
            verifyNoMoreInteractions(command);
            assertEquals(0, listLog.size());
        }

        @Test
        void deleteException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            doThrow(new IOException("Error delete")).when(command).delete(VFS4JPathName);

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditCommand.delete(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error delete", exception.getMessage());
            verify(command).delete(VFS4JPathName);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for delete for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestDeleteIfExists {

        @Test
        void deleteIfExistsOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(command.deleteIfExists(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditCommand.deleteIfExists(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(command).deleteIfExists(VFS4JPathName);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("deleteIfExists for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void deleteIfExistsDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            when(command.deleteIfExists(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditCommand.deleteIfExists(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(command).deleteIfExists(VFS4JPathName);
            verifyNoMoreInteractions(command);
            assertEquals(0, listLog.size());
        }

        @Test
        void deleteIfExistsException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(command.deleteIfExists(VFS4JPathName)).thenThrow(new IOException("Error deleteIfExists"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditCommand.deleteIfExists(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error deleteIfExists", exception.getMessage());
            verify(command).deleteIfExists(VFS4JPathName);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for deleteIfExists for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestCreateLink {

        @Test
        void createLinkOK() throws IOException {
            VFS4JPathName source = getPathName("abc.txt");
            VFS4JPathName target = getPathName("abc3.txt");
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.txt");
            when(command.createLink(source, target)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditCommand.createLink(source, target);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(command).createLink(source, target);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("createLink for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(source), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void createLinkDisabled() throws IOException {
            VFS4JPathName source = getPathName("abc.jpg");
            VFS4JPathName target = getPathName("abc3.jpg");
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.jpg");
            when(command.createLink(source, target)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditCommand.createLink(source, target);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(command).createLink(source, target);
            verifyNoMoreInteractions(command);
            assertEquals(0, listLog.size());
        }

        @Test
        void createLinkException() throws IOException {
            VFS4JPathName source = getPathName("abc.txt");
            VFS4JPathName target = getPathName("abc3.txt");
            when(command.createLink(source, target)).thenThrow(new IOException("Error createLink"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditCommand.createLink(source, target));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error createLink", exception.getMessage());
            verify(command).createLink(source, target);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for createLink for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(source), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestCreateSymbolicLink {

        @Test
        void createSymbolicLinkOK() throws IOException {
            VFS4JPathName source = getPathName("abc.txt");
            VFS4JPathName target = getPathName("abc3.txt");
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.txt");
            when(command.createSymbolicLink(source, target)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditCommand.createSymbolicLink(source, target);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(command).createSymbolicLink(source, target);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("createSymbolicLink for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(source), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void createSymbolicLinkDisabled() throws IOException {
            VFS4JPathName source = getPathName("abc.jpg");
            VFS4JPathName target = getPathName("abc3.jpg");
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.jpg");
            when(command.createSymbolicLink(source, target)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditCommand.createSymbolicLink(source, target);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(command).createSymbolicLink(source, target);
            verifyNoMoreInteractions(command);
            assertEquals(0, listLog.size());
        }

        @Test
        void createSymbolicLinkException() throws IOException {
            VFS4JPathName source = getPathName("abc.txt");
            VFS4JPathName target = getPathName("abc3.txt");
            when(command.createSymbolicLink(source, target)).thenThrow(new IOException("Error createSymbolicLink"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditCommand.createSymbolicLink(source, target));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error createSymbolicLink", exception.getMessage());
            verify(command).createSymbolicLink(source, target);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for createSymbolicLink for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(source), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestCopy {

        @Test
        void copyOK() throws IOException {
            VFS4JPathName source = getPathName("abc.txt");
            VFS4JPathName target = getPathName("abc3.txt");
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.txt");
            when(command.copy(source, target)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditCommand.copy(source, target);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(command).copy(source, target);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("copy from file {} to file {}", logMessage.getMessage());
            assertEquals(Arrays.asList(source, target), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void copyDisabled() throws IOException {
            VFS4JPathName source = getPathName("abc.jpg");
            VFS4JPathName target = getPathName("abc3.jpg");
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.jpg");
            when(command.copy(source, target)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditCommand.copy(source, target);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(command).copy(source, target);
            verifyNoMoreInteractions(command);
            assertEquals(0, listLog.size());
        }

        @Test
        void copyException() throws IOException {
            VFS4JPathName source = getPathName("abc.txt");
            VFS4JPathName target = getPathName("abc3.txt");
            when(command.copy(source, target)).thenThrow(new IOException("Error copy"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditCommand.copy(source, target));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error copy", exception.getMessage());
            verify(command).copy(source, target);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for copy from file {} to file {}", logMessage.getMessage());
            assertEquals(Arrays.asList(source, target), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestCopyInputStreamSource {

        @Test
        void copyOK() throws IOException {
            InputStream input = getInputStream();
            VFS4JPathName target = getPathName("abc3.txt");
            final long len = 50;
            when(command.copy(input, target)).thenReturn(len);

            // methode testée
            long res = auditCommand.copy(input, target);

            // vérifications
            assertEquals(len, res);
            verify(command).copy(input, target);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("copy to file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(target), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void copyDisabled() throws IOException {
            InputStream input = getInputStream();
            VFS4JPathName target = getPathName("abc3.jpg");
            final long len = 50;
            when(command.copy(input, target)).thenReturn(len);

            // methode testée
            long res = auditCommand.copy(input, target);

            // vérifications
            assertEquals(len, res);
            verify(command).copy(input, target);
            verifyNoMoreInteractions(command);
            assertEquals(0, listLog.size());
        }

        @Test
        void copyException() throws IOException {
            InputStream input = getInputStream();
            VFS4JPathName target = getPathName("abc3.txt");
            when(command.copy(input, target)).thenThrow(new IOException("Error copy InputStream"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditCommand.copy(input, target));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error copy InputStream", exception.getMessage());
            verify(command).copy(input, target);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for copy to file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(target), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestCopyOutputStreamTarget {

        @Test
        void copyOK() throws IOException {
            VFS4JPathName source = getPathName("abc.txt");
            OutputStream output = getOutputStream();
            final long len = 30;
            when(command.copy(source, output)).thenReturn(len);

            // methode testée
            long res = auditCommand.copy(source, output);

            // vérifications
            assertEquals(len, res);
            verify(command).copy(source, output);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("copy from file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(source), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void copyDisabled() throws IOException {
            VFS4JPathName source = getPathName("abc.jpg");
            OutputStream output = getOutputStream();
            final long len = 30;
            when(command.copy(source, output)).thenReturn(len);

            // methode testée
            long res = auditCommand.copy(source, output);

            // vérifications
            assertEquals(len, res);
            verify(command).copy(source, output);
            verifyNoMoreInteractions(command);
            assertEquals(0, listLog.size());
        }

        @Test
        void copyException() throws IOException {
            VFS4JPathName source = getPathName("abc.txt");
            OutputStream output = getOutputStream();
            when(command.copy(source, output)).thenThrow(new IOException("Error copy OutputStream"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditCommand.copy(source, output));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error copy OutputStream", exception.getMessage());
            verify(command).copy(source, output);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for copy from file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(source), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestMove {

        @Test
        void moveOK() throws IOException {
            VFS4JPathName source = getPathName("abc.txt");
            VFS4JPathName target = getPathName("abc3.txt");
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.txt");
            when(command.move(source, target)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditCommand.move(source, target);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(command).move(source, target);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("move from file {} to file {}", logMessage.getMessage());
            assertEquals(Arrays.asList(source, target), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void moveDisabled() throws IOException {
            VFS4JPathName source = getPathName("abc.jpg");
            VFS4JPathName target = getPathName("abc3.jpg");
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.jpg");
            when(command.move(source, target)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditCommand.move(source, target);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(command).move(source, target);
            verifyNoMoreInteractions(command);
            assertEquals(0, listLog.size());
        }

        @Test
        void moveException() throws IOException {
            VFS4JPathName source = getPathName("abc.txt");
            VFS4JPathName target = getPathName("abc3.txt");
            when(command.move(source, target)).thenThrow(new IOException("Error move"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditCommand.move(source, target));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error move", exception.getMessage());
            verify(command).move(source, target);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for move from file {} to file {}", logMessage.getMessage());
            assertEquals(Arrays.asList(source, target), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestWriteBytes {

        @Test
        void writeOK() throws IOException {
            VFS4JPathName source = getPathName("abc.txt");
            byte[] buf = getBytes();
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.txt");
            when(command.write(source, buf)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditCommand.write(source, buf);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(command).write(source, buf);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("write for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(source), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void writeDisabled() throws IOException {
            VFS4JPathName source = getPathName("abc.jpg");
            byte[] buf = getBytes();
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.jpg");
            when(command.write(source, buf)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditCommand.write(source, buf);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(command).write(source, buf);
            verifyNoMoreInteractions(command);
            assertEquals(0, listLog.size());
        }

        @Test
        void writeException() throws IOException {
            VFS4JPathName source = getPathName("abc.txt");
            byte[] buf = getBytes();
            when(command.write(source, buf)).thenThrow(new IOException("Error write"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditCommand.write(source, buf));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error write", exception.getMessage());
            verify(command).write(source, buf);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for write for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(source), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestWriteIterable {

        @Test
        void writeOK() throws IOException {
            VFS4JPathName source = getPathName("abc.txt");
            List<String> liste = getList();
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.txt");
            when(command.write(source, liste, StandardCharsets.UTF_8)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditCommand.write(source, liste, StandardCharsets.UTF_8);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(command).write(source, liste, StandardCharsets.UTF_8);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("write for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(source), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void writeDisabled() throws IOException {
            VFS4JPathName source = getPathName("abc.jpg");
            List<String> liste = getList();
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.jpg");
            when(command.write(source, liste, StandardCharsets.UTF_8)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditCommand.write(source, liste, StandardCharsets.UTF_8);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(command).write(source, liste, StandardCharsets.UTF_8);
            verifyNoMoreInteractions(command);
            assertEquals(0, listLog.size());
        }

        @Test
        void writeException() throws IOException {
            VFS4JPathName source = getPathName("abc.txt");
            List<String> liste = getList();
            when(command.write(source, liste, StandardCharsets.UTF_8)).thenThrow(new IOException("Error write"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditCommand.write(source, liste, StandardCharsets.UTF_8));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error write", exception.getMessage());
            verify(command).write(source, liste, StandardCharsets.UTF_8);
            verifyNoMoreInteractions(command);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for write for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(source), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    // méthodes utilitaires

    private static VFS4JPathName getPathName(String filename) {
        return new VFS4JPathName(PATH1, filename);
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

    private ByteArrayInputStream getInputStream() {
        return new ByteArrayInputStream("abc".getBytes(StandardCharsets.UTF_8));
    }

    private ByteArrayOutputStream getOutputStream() {
        return new ByteArrayOutputStream(20);
    }

    private byte[] getBytes() {
        return "abc".getBytes(StandardCharsets.UTF_8);
    }

    private List<String> getList() {
        List<String> liste = new ArrayList<>();
        liste.add("xxx yyyy zzz");
        return liste;
    }
}