package org.vfs.core.exception;

import java.nio.file.Path;

public class VFSInvalidPathException extends VFSException {

    private final Path path;

    public VFSInvalidPathException(String message, Path path) {
        super(message);
        this.path = path;
    }

    public VFSInvalidPathException(String message, Path path, Throwable cause) {
        super(message, cause);
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}
