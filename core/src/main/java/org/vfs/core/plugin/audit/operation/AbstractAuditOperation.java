package org.vfs.core.plugin.audit.operation;

import org.slf4j.Logger;
import org.vfs.core.api.PathName;
import org.vfs.core.plugin.audit.AuditOperation;
import org.vfs.core.plugin.audit.VFS4JAuditPlugins;
import org.vfs.core.util.VFS4JLoggerFactory;
import org.vfs.core.util.ValidationUtils;

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

    protected boolean isActive(AuditOperation operation, PathName... pathNames) {
        return true;
    }
}
