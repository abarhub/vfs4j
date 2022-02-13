package io.github.abarhub.vfs.core.plugin.unclosed;

import io.github.abarhub.vfs.core.api.path.VFS4JPathName;
import io.github.abarhub.vfs.core.api.path.VFS4JPaths;
import io.github.abarhub.vfs.core.plugin.unclosed.open.UnclosedFinalizer;
import io.github.abarhub.vfs.core.plugin.unclosed.open.UnclosedObjectFinalizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UnclosableRunnableTest {

    private UnclosedConfig config;
    private VFS4JUnclosedPlugins vfs4JUnclosedPlugins;

    private UnclosableRunnable unclosableRunnable;

    @BeforeEach
    void setUp() {
        config = new UnclosedConfig();
        vfs4JUnclosedPlugins = new VFS4JUnclosedPlugins();

        unclosableRunnable = new UnclosableRunnable(config, vfs4JUnclosedPlugins);
    }

    @Test
    void run() throws ExecutionException, InterruptedException, TimeoutException {

        // méthode testée
        Future<?> result = Executors.newSingleThreadExecutor().submit(unclosableRunnable);

        Thread.sleep(200);
        unclosableRunnable.stop();

        // vérifications
        Object res = result.get(2000, TimeUnit.MILLISECONDS);
        assertNull(res);
    }

    @Test
    void getReferenceQueue() {
        // méthode testée
        ReferenceQueue<UnclosedFinalizer> result = unclosableRunnable.getReferenceQueue();

        // vérifications
        assertNotNull(result);
        assertNull(result.poll());
    }

    @Test
    void stop() {
        assertFalse(unclosableRunnable.isStop());

        // méthode testée
        unclosableRunnable.stop();

        // vérifications
        assertTrue(unclosableRunnable.isStop());
    }

    @Test
    void newUnclosedFinalizer() {
        VFS4JPathName path = VFS4JPaths.get("dir", "test.txt");
        UnclosedObjectFinalizer unclosedObjectFinalizer = mock(UnclosedObjectFinalizer.class);
        UnclosedFinalizer unclosedFinalizer = mock(UnclosedFinalizer.class);
        when(unclosedObjectFinalizer.getUnclosedFinalizer()).thenReturn(unclosedFinalizer);

        // méthode testée
        UnclosedFinalizer result = unclosableRunnable.newUnclosedFinalizer(unclosedObjectFinalizer, path, VFS4JUnclosedOperation.NEW_INPUT_STREAM);

        // vérifications
        assertNotNull(result);
    }
}