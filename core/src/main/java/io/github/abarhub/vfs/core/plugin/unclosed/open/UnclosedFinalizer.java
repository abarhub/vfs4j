package io.github.abarhub.vfs.core.plugin.unclosed.open;

import io.github.abarhub.vfs.core.api.VFS4JPathName;
import io.github.abarhub.vfs.core.plugin.audit.VFS4JAuditPluginsFactory;
import io.github.abarhub.vfs.core.plugin.unclosed.UnclosedConfig;
import io.github.abarhub.vfs.core.plugin.unclosed.VFS4JLogUnclosed;
import io.github.abarhub.vfs.core.plugin.unclosed.VFS4JUnclosedOperation;
import io.github.abarhub.vfs.core.plugin.unclosed.VFS4JUnclosedPlugins;
import io.github.abarhub.vfs.core.util.VFS4JLoggerFactory;
import org.slf4j.Logger;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

public class UnclosedFinalizer extends PhantomReference<Object> {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(VFS4JAuditPluginsFactory.class);
    private static long nextNo = 1;

    private boolean closed;
    private final Exception exception;
    private final Instant start;
    private final long no;
    private final VFS4JPathName pathName;
    private final UnclosedConfig config;
    private final VFS4JUnclosedOperation operation;
    private final VFS4JUnclosedPlugins vfs4JUnclosedPlugins;

    public UnclosedFinalizer(Object referent, ReferenceQueue<? super UnclosedFinalizer> q,
                             VFS4JPathName pathName, UnclosedConfig config,
                             VFS4JUnclosedOperation operation, VFS4JUnclosedPlugins vfs4JUnclosedPlugins) {
        super(referent, (ReferenceQueue<? super Object>) q);
        exception = new Exception();
        start = Instant.now();
        no = nextNo++;
        this.pathName = pathName;
        this.config = config;
        this.operation = operation;
        this.vfs4JUnclosedPlugins = vfs4JUnclosedPlugins;
        logOpen();
    }

    private void logOpen() {
        if (config.isLogOpen() && isActive()) {
            if (config.isExceptionLogOpen()) {
                log("open for {} (path={})", no, pathName, exception);
            } else {
                log("open for {} (path={})", no, pathName);
            }
        }
    }

    protected void log(String message, Object... parameters) {
        sendMessageToListener(false, message, null, parameters);
        switch (config.getLoglevel()) {
            case ERROR:
                LOGGER.error(message, parameters);
                break;
            case WARN:
                LOGGER.warn(message, parameters);
                break;
            case INFO:
                LOGGER.info(message, parameters);
                break;
            case DEBUG:
                LOGGER.debug(message, parameters);
                break;
            case TRACE:
                LOGGER.trace(message, parameters);
                break;
        }
    }

    public void finalizeResources() {
        if (!this.closed) {
            LOGGER.error("finalizer for {} (path={}, duration={})", no, pathName, Duration.between(start, Instant.now()));
        }
    }

    public void closed() {
        this.closed = true;
        LOGGER.debug("closed for no {} ({})", no, Duration.between(start, Instant.now()));
        if (config.isLogClose() && isActive()) {
            if (config.isExceptionLogClose()) {
                log("close for {} (path={}, duration={})", no, pathName, Duration.between(start, Instant.now()), exception);
            } else {
                log("close for {} (path={}, duration={})", no, pathName, Duration.between(start, Instant.now()));
            }
        }
    }

    protected boolean isActive() {
        if (config.getOperations() == null) {
            // aucune opération n'est configuré => on n'affiche rien
            return false;
        } else if (config.getOperations().contains(operation)) {
            if (config.getFilterPath() != null &&
                    !config.getFilterPath().isEmpty() &&
                    pathName != null) {
                return isFilterPathMatch(pathName);
            } else {
                return true;
            }
        } else {
            // l'operation n'est pas dans la liste => on ne fait rien
            return false;
        }
    }


    private boolean isFilterPathMatch(VFS4JPathName pathNames) {
        FileSystem fs = FileSystems.getDefault();
        for (String glob : config.getFilterPath()) {
            PathMatcher pathMatcher = fs.getPathMatcher("glob:" + glob);
            if (pathMatcher.matches(Paths.get(pathNames.getPath()))) {
                return true;
            }
        }
        return false;
    }

    private void sendMessageToListener(boolean error, String message, Exception exception, Object[] parameters) {
        if (vfs4JUnclosedPlugins.getListener() != null &&
                !vfs4JUnclosedPlugins.getListener().isEmpty()) {
            for (VFS4JLogUnclosed logAudit : vfs4JUnclosedPlugins.getListener()) {
                logAudit.log(config.getLoglevel(), error, message, exception, parameters);
            }
        }
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
