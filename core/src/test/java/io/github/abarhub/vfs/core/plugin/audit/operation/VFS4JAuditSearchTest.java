package io.github.abarhub.vfs.core.plugin.audit.operation;

import io.github.abarhub.vfs.core.api.VFS4JPathName;
import io.github.abarhub.vfs.core.api.operation.VFS4JSearch;
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
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VFS4JAuditSearchTest implements VFS4JLogAudit {

    private static final Logger LOGGER = LoggerFactory.getLogger(VFS4JAuditCommandTest.class);

    public static final String PATH1 = "path1";

    private final VFS4JSearch search = mock(VFS4JSearch.class);

    private VFS4JAuditSearch auditSearch;

    private final List<LogMessage> listLog = new ArrayList<>();

    @BeforeEach
    void setUp() {
        LOGGER.info("setUp");
        VFS4JAuditPlugins vfs4JAuditPlugins = new VFS4JAuditPlugins();
        vfs4JAuditPlugins.addListener(this);
        Map<String, String> config = defautConfig();
        VFS4JConfig config2 = new VFS4JConfig();
        vfs4JAuditPlugins.init("exemple1", config, config2);
        auditSearch = new VFS4JAuditSearch(vfs4JAuditPlugins, search);
    }

    @Nested
    class TestList {

        @Test
        void listOK() throws IOException {
            VFS4JPathName pathName = getPathName("abc.txt");
            Stream<VFS4JPathName> stream = Stream.of(getPathName("abc2.txt"));
            when(search.list(pathName)).thenReturn(stream);

            // methode testée
            Stream<VFS4JPathName> res = auditSearch.list(pathName);

            // vérifications
            assertNotNull(res);
            assertEquals(stream, res);
            verify(search).list(pathName);
            verifyNoMoreInteractions(search);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("list for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(pathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void listDisabled() throws IOException {
            VFS4JPathName pathName = getPathName("abc.jpg");
            Stream<VFS4JPathName> stream = Stream.of(getPathName("abc2.txt"));
            when(search.list(pathName)).thenReturn(stream);

            // methode testée
            Stream<VFS4JPathName> res = auditSearch.list(pathName);

            // vérifications
            assertNotNull(res);
            assertEquals(stream, res);
            verify(search).list(pathName);
            verifyNoMoreInteractions(search);
            assertEquals(0, listLog.size());
        }

        @Test
        void listException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(search.list(VFS4JPathName)).thenThrow(new IOException("Error list"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditSearch.list(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error list", exception.getMessage());
            verify(search).list(VFS4JPathName);
            verifyNoMoreInteractions(search);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for list for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestWalk {

        @Test
        void walkOK() throws IOException {
            VFS4JPathName pathName = getPathName("abc.txt");
            Stream<VFS4JPathName> stream = Stream.of(getPathName("abc2.txt"));
            when(search.walk(pathName)).thenReturn(stream);

            // methode testée
            Stream<VFS4JPathName> res = auditSearch.walk(pathName);

            // vérifications
            assertNotNull(res);
            assertEquals(stream, res);
            verify(search).walk(pathName);
            verifyNoMoreInteractions(search);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("walk for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(pathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void walkDisabled() throws IOException {
            VFS4JPathName pathName = getPathName("abc.jpg");
            Stream<VFS4JPathName> stream = Stream.of(getPathName("abc2.txt"));
            when(search.walk(pathName)).thenReturn(stream);

            // methode testée
            Stream<VFS4JPathName> res = auditSearch.walk(pathName);

            // vérifications
            assertNotNull(res);
            assertEquals(stream, res);
            verify(search).walk(pathName);
            verifyNoMoreInteractions(search);
            assertEquals(0, listLog.size());
        }

        @Test
        void walkException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(search.walk(VFS4JPathName)).thenThrow(new IOException("Error walk"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditSearch.walk(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error walk", exception.getMessage());
            verify(search).walk(VFS4JPathName);
            verifyNoMoreInteractions(search);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for walk for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestWalkMaxDepth {

        @Test
        void walkOK() throws IOException {
            VFS4JPathName pathName = getPathName("abc.txt");
            final int maxDepth = 10;
            Stream<VFS4JPathName> stream = Stream.of(getPathName("abc2.txt"));
            when(search.walk(pathName, maxDepth)).thenReturn(stream);

            // methode testée
            Stream<VFS4JPathName> res = auditSearch.walk(pathName, maxDepth);

            // vérifications
            assertNotNull(res);
            assertEquals(stream, res);
            verify(search).walk(pathName, maxDepth);
            verifyNoMoreInteractions(search);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("walk for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(pathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void walkDisabled() throws IOException {
            VFS4JPathName pathName = getPathName("abc.jpg");
            final int maxDepth = 10;
            Stream<VFS4JPathName> stream = Stream.of(getPathName("abc2.txt"));
            when(search.walk(pathName, maxDepth)).thenReturn(stream);

            // methode testée
            Stream<VFS4JPathName> res = auditSearch.walk(pathName, maxDepth);

            // vérifications
            assertNotNull(res);
            assertEquals(stream, res);
            verify(search).walk(pathName, maxDepth);
            verifyNoMoreInteractions(search);
            assertEquals(0, listLog.size());
        }

        @Test
        void walkException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            final int maxDepth = 10;
            when(search.walk(VFS4JPathName, maxDepth)).thenThrow(new IOException("Error walk"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditSearch.walk(VFS4JPathName, maxDepth));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error walk", exception.getMessage());
            verify(search).walk(VFS4JPathName, maxDepth);
            verifyNoMoreInteractions(search);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for walk for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestFind {

        @Test
        void findOK() throws IOException {
            VFS4JPathName pathName = getPathName("abc.txt");
            final int maxDepth = 10;
            BiPredicate<VFS4JPathName, BasicFileAttributes> filter = getFilter();
            Stream<VFS4JPathName> stream = Stream.of(getPathName("abc2.txt"));
            when(search.find(pathName, maxDepth, filter)).thenReturn(stream);

            // methode testée
            Stream<VFS4JPathName> res = auditSearch.find(pathName, maxDepth, filter);

            // vérifications
            assertNotNull(res);
            assertEquals(stream, res);
            verify(search).find(pathName, maxDepth, filter);
            verifyNoMoreInteractions(search);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("find for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(pathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void findDisabled() throws IOException {
            VFS4JPathName pathName = getPathName("abc.jpg");
            final int maxDepth = 10;
            BiPredicate<VFS4JPathName, BasicFileAttributes> filter = getFilter();
            Stream<VFS4JPathName> stream = Stream.of(getPathName("abc2.txt"));
            when(search.find(pathName, maxDepth, filter)).thenReturn(stream);

            // methode testée
            Stream<VFS4JPathName> res = auditSearch.find(pathName, maxDepth, filter);

            // vérifications
            assertNotNull(res);
            assertEquals(stream, res);
            verify(search).find(pathName, maxDepth, filter);
            verifyNoMoreInteractions(search);
            assertEquals(0, listLog.size());
        }

        @Test
        void findException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            final int maxDepth = 10;
            BiPredicate<VFS4JPathName, BasicFileAttributes> filter = getFilter();
            when(search.find(VFS4JPathName, maxDepth, filter)).thenThrow(new IOException("Error find"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditSearch.find(VFS4JPathName, maxDepth, filter));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error find", exception.getMessage());
            verify(search).find(VFS4JPathName, maxDepth, filter);
            verifyNoMoreInteractions(search);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for find for file {}", logMessage.getMessage());
            assertEquals(Collections.singletonList(VFS4JPathName), logMessage.getParameters());
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

    private BiPredicate<VFS4JPathName, BasicFileAttributes> getFilter() {
        return (x, y) -> true;
    }

}