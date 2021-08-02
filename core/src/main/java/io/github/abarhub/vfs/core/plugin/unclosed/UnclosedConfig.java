package io.github.abarhub.vfs.core.plugin.unclosed;

import java.util.List;
import java.util.Set;

public class UnclosedConfig {

    private boolean logOpen;
    private boolean logClose;
    private long logIfNotClosedAfterMs;
    private boolean exceptionLogOpen;
    private boolean exceptionLogClose;
    private Set<VFS4JUnclosedOperation> operations;
    private List<String> filterPath;
    private VFS4JUnclosedLogLevel loglevel;

    public boolean isLogOpen() {
        return logOpen;
    }

    public void setLogOpen(boolean logOpen) {
        this.logOpen = logOpen;
    }

    public boolean isLogClose() {
        return logClose;
    }

    public void setLogClose(boolean logClose) {
        this.logClose = logClose;
    }

    public long getLogIfNotClosedAfterMs() {
        return logIfNotClosedAfterMs;
    }

    public void setLogIfNotClosedAfterMs(long logIfNotClosedAfterMs) {
        this.logIfNotClosedAfterMs = logIfNotClosedAfterMs;
    }

    public boolean isExceptionLogOpen() {
        return exceptionLogOpen;
    }

    public void setExceptionLogOpen(boolean exceptionLogOpen) {
        this.exceptionLogOpen = exceptionLogOpen;
    }

    public boolean isExceptionLogClose() {
        return exceptionLogClose;
    }

    public void setExceptionLogClose(boolean exceptionLogClose) {
        this.exceptionLogClose = exceptionLogClose;
    }

    public Set<VFS4JUnclosedOperation> getOperations() {
        return operations;
    }

    public void setOperations(Set<VFS4JUnclosedOperation> operations) {
        this.operations = operations;
    }

    public List<String> getFilterPath() {
        return filterPath;
    }

    public void setFilterPath(List<String> filterPath) {
        this.filterPath = filterPath;
    }

    public VFS4JUnclosedLogLevel getLoglevel() {
        return loglevel;
    }

    public void setLoglevel(VFS4JUnclosedLogLevel loglevel) {
        this.loglevel = loglevel;
    }
}
