package org.vfs.core.plugin.audit.operation;

import org.vfs.core.plugin.audit.AuditLogLevel;

import java.util.List;

public class LogMessage {

    private AuditLogLevel logLevel;

    private boolean error;

    private String message;

    private List<Object> parameters;

    public LogMessage(AuditLogLevel logLevel, boolean error,
                      String message, List<Object> parameters) {
        this.logLevel = logLevel;
        this.error = error;
        this.message = message;
        this.parameters = parameters;
    }

    public AuditLogLevel getLogLevel() {
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
