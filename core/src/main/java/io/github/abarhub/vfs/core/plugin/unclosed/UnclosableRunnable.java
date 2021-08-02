package io.github.abarhub.vfs.core.plugin.unclosed;

import io.github.abarhub.vfs.core.api.VFS4JPathName;
import io.github.abarhub.vfs.core.plugin.unclosed.open.UnclosedFinalizer;
import io.github.abarhub.vfs.core.plugin.unclosed.open.UnclosedObjectFinalizer;
import io.github.abarhub.vfs.core.util.VFS4JLoggerFactory;
import org.slf4j.Logger;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UnclosableRunnable implements Runnable {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(UnclosableRunnable.class);

    private final ReferenceQueue<UnclosedFinalizer> referenceQueue;
    private boolean stop = false;
    private final List<UnclosedFinalizer> listReference;
    private final UnclosedConfig config;
    private final VFS4JUnclosedPlugins vfs4JUnclosedPlugins;

    public UnclosableRunnable(UnclosedConfig config, VFS4JUnclosedPlugins vfs4JUnclosedPlugins) {
        this.referenceQueue = new ReferenceQueue<>();
        listReference = new CopyOnWriteArrayList<>();
        this.config = config;
        this.vfs4JUnclosedPlugins = vfs4JUnclosedPlugins;
    }

    @Override
    public void run() {
        LOGGER.debug("run ...");

        Reference<?> referenceFromQueue;
        while (!stop) {
            while ((referenceFromQueue = referenceQueue.poll()) != null) {
                if (referenceFromQueue instanceof UnclosedFinalizer) {
                    ((UnclosedFinalizer) referenceFromQueue).finalizeResources();
                    referenceFromQueue.clear();
                    listReference.remove(referenceFromQueue);
                }
            }
        }
        LOGGER.debug("exit");
    }

    public ReferenceQueue<UnclosedFinalizer> getReferenceQueue() {
        return referenceQueue;
    }

    public void stop() {
        this.stop = true;
    }

    public void add(UnclosedObjectFinalizer tmp) {
        listReference.add(tmp.getUnclosedFinalizer());
    }

    public UnclosedFinalizer newUnclosedFinalizer(UnclosedObjectFinalizer object, VFS4JPathName pathName, VFS4JUnclosedOperation operation) {
        return new UnclosedFinalizer(object, this.referenceQueue, pathName, config, operation, vfs4JUnclosedPlugins);
    }
}
