package org.vfs.core.exception;

public class VFSException extends RuntimeException {
    public VFSException(String message) {
        super(message);
    }

    public VFSException(String message, Throwable cause) {
        super(message, cause);
    }
}
