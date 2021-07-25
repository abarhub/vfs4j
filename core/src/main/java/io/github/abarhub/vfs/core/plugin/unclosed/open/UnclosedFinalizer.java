package io.github.abarhub.vfs.core.plugin.unclosed.open;

import io.github.abarhub.vfs.core.plugin.audit.VFS4JAuditPluginsFactory;
import io.github.abarhub.vfs.core.util.VFS4JLoggerFactory;
import org.slf4j.Logger;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.time.Duration;
import java.time.Instant;

public class UnclosedFinalizer extends PhantomReference<Object> {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(VFS4JAuditPluginsFactory.class);
    private static long nextNo = 1;

    private boolean closed;
    private final Exception exception;
    private final Instant start;
    private final long no;

    public UnclosedFinalizer(Object referent, ReferenceQueue<? super UnclosedFinalizer> q) {
        super(referent, (ReferenceQueue<? super Object>) q);
        exception = new Exception();
        start = Instant.now();
        no = nextNo++;
    }

    public void finalizeResources() {
        if (!this.closed) {
            LOGGER.debug("finalizer for {}", no);
        }
    }

    public void closed() {
        this.closed = true;
        LOGGER.debug("closed for no {} ({})", no, Duration.between(start, Instant.now()));
    }

    public long getNo() {
        return no;
    }

    public boolean isClosed() {
        return closed;
    }

    public static void reinit() {
        nextNo = 1;
    }
}
