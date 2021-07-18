package io.github.abarhub.vfs.core.exception;

import java.nio.file.Path;

public class VFS4JInvalidPathException extends VFS4JException {

    private final Path path;

    public VFS4JInvalidPathException(String message, Path path) {
        super(message);
        this.path = path;
    }

    public VFS4JInvalidPathException(String message, Path path, Throwable cause) {
        super(message, cause);
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}
