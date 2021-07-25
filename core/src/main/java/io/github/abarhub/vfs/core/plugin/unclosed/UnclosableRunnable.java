package io.github.abarhub.vfs.core.plugin.unclosed;

import io.github.abarhub.vfs.core.plugin.unclosed.open.UnclosedFinalizer;
import io.github.abarhub.vfs.core.plugin.unclosed.open.UnclosedInputStream;
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

    public UnclosableRunnable() {
        this.referenceQueue = new ReferenceQueue<>();
        listReference = new CopyOnWriteArrayList<>();
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

    public void add(UnclosedInputStream tmp) {
        listReference.add(tmp.getUnclosedFinalizer());
    }

    public UnclosedFinalizer newUnclosedFinalizer(UnclosedInputStream unclosedInputStream) {
        return new UnclosedFinalizer(unclosedInputStream, this.referenceQueue);
    }
}
