package org.vfs.core.plugin.audit.operation;

import org.slf4j.Logger;
import org.vfs.core.api.VFS4JPathName;
import org.vfs.core.plugin.audit.VFS4JAuditOperation;
import org.vfs.core.plugin.audit.VFS4JAuditPlugins;
import org.vfs.core.plugin.audit.VFS4JLogAudit;
import org.vfs.core.util.VFS4JLoggerFactory;
import org.vfs.core.util.VFS4JValidationUtils;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;

public abstract class VFS4JAbstractAuditOperation {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(VFS4JAuditPlugins.class);

    private VFS4JAuditPlugins auditPlugins;

    protected VFS4JAbstractAuditOperation(VFS4JAuditPlugins auditPlugins) {
        VFS4JValidationUtils.checkNotNull(auditPlugins, "vfs4JAuditPlugins is null");
        this.auditPlugins = auditPlugins;
    }

    public VFS4JAuditPlugins getAuditPlugins() {
        return auditPlugins;
    }

    protected void log(String message, Object... parameters) {
        sendMessageToListener(false, message, parameters);
        switch (auditPlugins.getLogLevel()) {
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
        switch (auditPlugins.getLogLevel()) {
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
        if (auditPlugins.getListener() != null &&
                !auditPlugins.getListener().isEmpty()) {
            for (VFS4JLogAudit logAudit : auditPlugins.getListener()) {
                logAudit.log(auditPlugins.getLogLevel(), error, message, parameters);
            }
        }
    }

    protected boolean isActive(VFS4JAuditOperation operation, VFS4JPathName... pathNames) {
        if (auditPlugins.getListOperations() == null) {
            // aucune opération n'est configuré => on n'affiche rien
            return false;
        } else if (auditPlugins.getListOperations().contains(operation)) {
            if (auditPlugins.getFilterPath() != null &&
                    !auditPlugins.getFilterPath().isEmpty() &&
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

    private boolean isFilterPathMatch(VFS4JPathName[] pathNames) {
        FileSystem fs = FileSystems.getDefault();
        for (String glob : auditPlugins.getFilterPath()) {
            PathMatcher pathMatcher = fs.getPathMatcher("glob:" + glob);
            for (VFS4JPathName p : pathNames) {
                if (pathMatcher.matches(Paths.get(p.getPath()))) {
                    return true;
                }
            }
        }
        return false;
    }
}
