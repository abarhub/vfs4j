package org.vfs.core.api;

import org.vfs.core.config.VFS4JParameter;
import org.vfs.core.exception.VFS4JWriteException;

import java.nio.file.Path;
import java.util.Optional;

public abstract class AbstractOperation {

    private final FileManager fileManager;

    public AbstractOperation(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    protected Path getRealFile(PathName file) {
        return fileManager.getRealFile(file);
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    protected Optional<PathName> convertFromRealPath(Path file) {
        return fileManager.convertFromRealPath(file);
    }

    protected void writeOperation(PathName pathName, String operationName) {
        VFS4JParameter param = getFileManager().getConfig().getPath(pathName.getName());
        if (param != null && param.isReadonly()) {
            throw new VFS4JWriteException("write operation forbidden for " + operationName + " on " + pathName.getName());
        }
    }
}
