package org.vfs.core.plugin.audit.operation;

import org.slf4j.Logger;
import org.vfs.core.api.PathName;
import org.vfs.core.plugin.audit.AuditOperation;
import org.vfs.core.plugin.audit.VFS4JAuditPlugins;
import org.vfs.core.plugin.audit.VFS4JLogAudit;
import org.vfs.core.util.VFS4JLoggerFactory;
import org.vfs.core.util.ValidationUtils;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;

public abstract class AbstractAuditOperation {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(VFS4JAuditPlugins.class);

    private VFS4JAuditPlugins vfs4JAuditPlugins;

    public AbstractAuditOperation(VFS4JAuditPlugins vfs4JAuditPlugins) {
        ValidationUtils.checkNotNull(vfs4JAuditPlugins, "vfs4JAuditPlugins is null");
        this.vfs4JAuditPlugins = vfs4JAuditPlugins;
    }

    public VFS4JAuditPlugins getVfs4JAuditPlugins() {
        return vfs4JAuditPlugins;
    }

    protected void log(String message, Object... parameters) {
        sendMessageToListener(false, message, parameters);
        switch (vfs4JAuditPlugins.getLogLevel()) {
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

    protected void logError(String message, Exception e, Object... parameters) {
        sendMessageToListener(true, message, parameters);
        switch (vfs4JAuditPlugins.getLogLevel()) {
            case ERROR:
                LOGGER.error(message, parameters, e);
                break;
            case WARN:
                LOGGER.warn(message, parameters, e);
                break;
            case INFO:
                LOGGER.info(message, parameters, e);
                break;
            case DEBUG:
                LOGGER.debug(message, parameters, e);
                break;
            case TRACE:
                LOGGER.trace(message, parameters, e);
                break;
        }
    }

    private void sendMessageToListener(boolean error, String message, Object[] parameters) {
        if (vfs4JAuditPlugins.getListener() != null &&
                !vfs4JAuditPlugins.getListener().isEmpty()) {
            for (VFS4JLogAudit logAudit : vfs4JAuditPlugins.getListener()) {
                logAudit.log(vfs4JAuditPlugins.getLogLevel(), error, message, parameters);
            }
        }
    }

    protected boolean isActive(AuditOperation operation, PathName... pathNames) {
        if (vfs4JAuditPlugins.getListOperations() == null) {
            // aucune opération n'est configuré => on n'affiche rien
            return false;
        } else if (vfs4JAuditPlugins.getListOperations().contains(operation)) {
            if (vfs4JAuditPlugins.getFilterPath() != null &&
                    !vfs4JAuditPlugins.getFilterPath().isEmpty() &&
                    pathNames != null && pathNames.length > 0) {
                return isFilterPathMatch(pathNames);
            } else {
                return true;
            }
        } else {
            // l'operation n'est pas dans la liste => on ne fait rien
            return false;
        }
    }

    private boolean isFilterPathMatch(PathName[] pathNames) {
        FileSystem fs = FileSystems.getDefault();
        for (String glob : vfs4JAuditPlugins.getFilterPath()) {
            PathMatcher pathMatcher = fs.getPathMatcher("glob:" + glob);
            for (PathName p : pathNames) {
                if (pathMatcher.matches(Paths.get(p.getPath()))) {
                    return true;
                }
            }
        }
        return false;
    }
}
