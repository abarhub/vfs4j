package org.vfs.core.plugin.audit;

public interface VFS4JLogAudit {
    public void log(AuditLogLevel logLevel, boolean error, String message, Object... parameters);
}
