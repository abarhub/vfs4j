package io.github.abarhub.vfs.core.exemples;

import io.github.abarhub.vfs.core.api.VFS4JDefaultFileManager;
import io.github.abarhub.vfs.core.api.VFS4JFiles;
import io.github.abarhub.vfs.core.api.VFS4JPathName;
import io.github.abarhub.vfs.core.api.operation.VFS4JOpen;
import io.github.abarhub.vfs.core.config.VFS4JConfig;
import io.github.abarhub.vfs.core.plugin.unclosed.UnclosableRunnable;
import io.github.abarhub.vfs.core.plugin.unclosed.UnclosedConfig;
import io.github.abarhub.vfs.core.plugin.unclosed.VFS4JUnclosedOperation;
import io.github.abarhub.vfs.core.plugin.unclosed.VFS4JUnclosedPlugins;
import io.github.abarhub.vfs.core.plugin.unclosed.open.UnclosedFinalizer;
import io.github.abarhub.vfs.core.plugin.unclosed.open.UnclosedObjectFinalizer;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.ref.ReferenceQueue;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ExempleUnclosedTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExempleUnclosedTest.class);

    @BeforeEach
    public void init(TestInfo testInfo) {
        LOGGER.info("start {}", testInfo.getTestMethod());
    }

    @AfterEach
    public void terminate(TestInfo testInfo) {
        LOGGER.info("end {}", testInfo.getTestMethod());
    }

    @Test
    @DisplayName("Test simple de liberation d'un objet")
    public void exemple1() throws Exception {

        ReferenceQueue<UnclosedFinalizer> referenceQueue;
        List<Long> listObject = new CopyOnWriteArrayList<>();
        List<Long> listObjectNotClosed = new CopyOnWriteArrayList<>();

        UnclosedFinalizer.reinit();

        VFS4JUnclosedPlugins vfs4JUnclosedPlugins = getVfs4JUnclosedPlugins(listObject, listObjectNotClosed);

        vfs4JUnclosedPlugins.init("test1", new HashMap<>(), new VFS4JConfig());

        UnclosableRunnable unclosableRunnable = vfs4JUnclosedPlugins.getUnclosableRunnable();
        assertNotNull(unclosableRunnable);
        referenceQueue = unclosableRunnable.getReferenceQueue();
        assertNotNull(referenceQueue);

        LOGGER.debug("queue: {}", unclosableRunnable.getReferenceQueue());

        Thread thread = vfs4JUnclosedPlugins.getThread();
        assertNotNull(thread);
        assertTrue(thread.isAlive());

        VFS4JOpen test = new VFS4JOpenTest();

        Optional<VFS4JOpen> test2 = vfs4JUnclosedPlugins.getOpen(test);

        assertTrue(test2.isPresent());
        VFS4JOpen test3 = test2.get();
        VFS4JPathName filename = new VFS4JPathName("rep01", "fichier01.txt");
        InputStream res = test3.newInputStream(filename);
        assertNotNull(res);

        res = null;
        System.gc();

        sleep(1000);

        assertEquals(1, listObject.size());
        assertEquals(1, listObject.get(0));
        assertEquals(1, listObjectNotClosed.size());
        assertEquals(1, listObjectNotClosed.get(0));

        stopThread(thread, unclosableRunnable);

        assertFalse(thread.isAlive());
    }

    @Test
    @DisplayName("Test simple de non liberation d'un objet car fermé")
    public void exemple2() throws Exception {

        ReferenceQueue<UnclosedFinalizer> referenceQueue;
        List<Long> listObject = new CopyOnWriteArrayList<>();
        List<Long> listObjectNotClosed = new CopyOnWriteArrayList<>();

        UnclosedFinalizer.reinit();

        VFS4JUnclosedPlugins vfs4JUnclosedPlugins = getVfs4JUnclosedPlugins(listObject, listObjectNotClosed);

        vfs4JUnclosedPlugins.init("test1", new HashMap<>(), new VFS4JConfig());

        UnclosableRunnable unclosableRunnable = vfs4JUnclosedPlugins.getUnclosableRunnable();
        assertNotNull(unclosableRunnable);
        referenceQueue = unclosableRunnable.getReferenceQueue();
        assertNotNull(referenceQueue);

        LOGGER.debug("queue: {}", unclosableRunnable.getReferenceQueue());

        Thread thread = vfs4JUnclosedPlugins.getThread();
        assertNotNull(thread);
        assertTrue(thread.isAlive());

        VFS4JOpen test = new VFS4JOpenTest();

        Optional<VFS4JOpen> test2 = vfs4JUnclosedPlugins.getOpen(test);

        assertTrue(test2.isPresent());
        VFS4JOpen test3 = test2.get();
        VFS4JPathName filename = new VFS4JPathName("rep01", "fichier01.txt");
        InputStream res = test3.newInputStream(filename);
        assertNotNull(res);
        res.close();

        res = null;
        System.gc();

        sleep(1000);

        assertEquals(1, listObject.size());
        assertEquals(1, listObject.get(0));
        assertEquals(0, listObjectNotClosed.size());

        stopThread(thread, unclosableRunnable);

        assertFalse(thread.isAlive());
    }

    @Test
    @DisplayName("Test simple de liberation d'un objet et un autre objet fermé")
    public void exemple3() throws Exception {

        ReferenceQueue<UnclosedFinalizer> referenceQueue;
        List<Long> listObject = new CopyOnWriteArrayList<>();
        List<Long> listObjectNotClosed = new CopyOnWriteArrayList<>();

        UnclosedFinalizer.reinit();

        VFS4JUnclosedPlugins vfs4JUnclosedPlugins = getVfs4JUnclosedPlugins(listObject, listObjectNotClosed);

        vfs4JUnclosedPlugins.init("test1", new HashMap<>(), new VFS4JConfig());

        UnclosableRunnable unclosableRunnable = vfs4JUnclosedPlugins.getUnclosableRunnable();
        assertNotNull(unclosableRunnable);
        referenceQueue = unclosableRunnable.getReferenceQueue();
        assertNotNull(referenceQueue);

        LOGGER.debug("queue: {}", unclosableRunnable.getReferenceQueue());

        Thread thread = vfs4JUnclosedPlugins.getThread();
        assertNotNull(thread);
        assertTrue(thread.isAlive());

        VFS4JOpen test = new VFS4JOpenTest();

        Optional<VFS4JOpen> test2 = vfs4JUnclosedPlugins.getOpen(test);

        assertTrue(test2.isPresent());
        VFS4JOpen test3 = test2.get();
        VFS4JPathName filename = new VFS4JPathName("rep01", "fichier01.txt");
        InputStream res = test3.newInputStream(filename);
        assertNotNull(res);
        InputStream res2 = test3.newInputStream(filename);
        assertNotNull(res2);
        res2.close();
        InputStream res3 = test3.newInputStream(filename);
        assertNotNull(res3);

        res = null;
        res2 = null;
        res3 = null;
        System.gc();

        sleep(1000);

        assertEquals(3, listObject.size());
        assertIterableEquals(set(1L, 2L, 3L), set(listObject));
        assertEquals(2, listObjectNotClosed.size());
        assertIterableEquals(set(1L, 3L), set(listObjectNotClosed));

        stopThread(thread, unclosableRunnable);

        assertFalse(thread.isAlive());
    }

    // methodes utilitaires

    private void reinitConfig(VFS4JConfig vfs4JConfig) {
        VFS4JDefaultFileManager.get().setConfig(vfs4JConfig);
        VFS4JFiles.reinit();
    }

    private void sleep(long temps) {

        try {
            LOGGER.debug("wait");
            Thread.sleep(temps);
        } catch (InterruptedException e) {
            LOGGER.debug("interrupt");
        }
    }

    private void stopThread(Thread thread, UnclosableRunnable unclosableRunnable) {
        unclosableRunnable.stop();
        thread.interrupt();

        for (int i = 0; i < 100; i++) {
            sleep(10);
            if (!thread.isAlive()) {
                break;
            }
        }
    }

    private <T> Set<T> set(T... param) {
        return set(Arrays.asList(param));
    }

    private <T> Set<T> set(List<T> param) {
        Set<T> set = new HashSet<>();
        if (param != null && param.size() > 0) {
            for (T n : param) {
                set.add(n);
            }
        }
        return set;
    }

    public class VFS4JOpenTest implements VFS4JOpen {
        @Override
        public InputStream newInputStream(VFS4JPathName pathName, OpenOption... options) throws IOException {
            return new ByteArrayInputStream("abc".getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public OutputStream newOutputStream(VFS4JPathName pathName, OpenOption... options) throws IOException {
            return null;
        }

        @Override
        public Reader newReader(VFS4JPathName pathName) throws IOException {
            return null;
        }

        @Override
        public FileWriter newWriter(VFS4JPathName pathName, boolean append) throws IOException {
            return null;
        }

        @Override
        public SeekableByteChannel newByteChannel(VFS4JPathName pathName, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
            return null;
        }

        @Override
        public DirectoryStream<VFS4JPathName> newDirectoryStream(VFS4JPathName pathName, DirectoryStream.Filter<? super VFS4JPathName> filter) throws IOException {
            return null;
        }
    }

    private VFS4JUnclosedPlugins getVfs4JUnclosedPlugins(List<Long> listObject, List<Long> listObjectNotClosed) {
        UnclosedConfig config = new UnclosedConfig();
        UnclosableRunnable tab[] = new UnclosableRunnable[1];

        VFS4JUnclosedPlugins vfs4JUnclosedPlugins = new VFS4JUnclosedPlugins() {
            @Override
            protected UnclosableRunnable newUnclosableRunnable(UnclosedConfig unclosedConfig) {
                return tab[0];
            }
        };
        UnclosableRunnable unclosableRunnable = new UnclosableRunnable(config, vfs4JUnclosedPlugins) {
            @Override
            public UnclosedFinalizer newUnclosedFinalizer(UnclosedObjectFinalizer unclosedInputStream, VFS4JPathName pathName, VFS4JUnclosedOperation operation) {
                return new UnclosedFinalizer(unclosedInputStream, getReferenceQueue(), pathName, config, operation, vfs4JUnclosedPlugins) {
                    @Override
                    public void finalizeResources() {
                        super.finalizeResources();
                        listObject.add(getNo());
                        if (!isClosed()) {
                            listObjectNotClosed.add(getNo());
                        }
                    }
                };
            }
        };
        tab[0] = unclosableRunnable;

        return vfs4JUnclosedPlugins;
    }
}
