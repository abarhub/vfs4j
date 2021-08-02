package io.github.abarhub.vfs.core.plugin.unclosed;

public interface VFS4JLogUnclosed {
    void log(VFS4JUnclosedLogLevel logLevel, boolean error, String message, Exception e, Object... parameters);
}
