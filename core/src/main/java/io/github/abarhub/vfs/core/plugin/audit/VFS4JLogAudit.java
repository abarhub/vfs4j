package io.github.abarhub.vfs.core.plugin.audit;

public interface VFS4JLogAudit {
    void log(VFS4JAuditLogLevel logLevel, boolean error, String message, Exception e, Object... parameters);
}
