package org.vfs.core.plugin.audit.operation;

import org.vfs.core.plugin.audit.VFS4JAuditLogLevel;

import java.util.List;

public class LogMessage {

    private VFS4JAuditLogLevel logLevel;

    private boolean error;

    private String message;

    private List<Object> parameters;

    public LogMessage(VFS4JAuditLogLevel logLevel, boolean error,
                      String message, List<Object> parameters) {
        this.logLevel = logLevel;
        this.error = error;
        this.message = message;
        this.parameters = parameters;
    }

    public VFS4JAuditLogLevel getLogLevel() {
        return logLevel;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<Object> getParameters() {
        return parameters;
    }
}
