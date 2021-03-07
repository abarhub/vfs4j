package org.vfs.core.plugin.audit.operation;

import org.vfs.core.plugin.audit.VFS4JAuditLogLevel;

import java.util.List;

public class LogMessage {

    private final VFS4JAuditLogLevel logLevel;

    private final boolean error;

    private final String message;

    private final List<Object> parameters;

    private final Exception exception;

    public LogMessage(VFS4JAuditLogLevel logLevel, boolean error,
                      String message, List<Object> parameters,
                      Exception exception) {
        this.logLevel = logLevel;
        this.error = error;
        this.message = message;
        this.parameters = parameters;
        this.exception = exception;
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

    public Exception getException() {
        return exception;
    }
}
