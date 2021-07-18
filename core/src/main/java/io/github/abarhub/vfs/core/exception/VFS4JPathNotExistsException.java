package io.github.abarhub.vfs.core.exception;

public class VFS4JPathNotExistsException extends VFS4JConfigException {
    public VFS4JPathNotExistsException(String message) {
        super(message);
    }

    public VFS4JPathNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
