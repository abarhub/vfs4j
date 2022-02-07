package io.github.abarhub.vfs.core.api;

import io.github.abarhub.vfs.core.api.path.VFS4JPathName;
import io.github.abarhub.vfs.core.config.VFS4JParameter;
import io.github.abarhub.vfs.core.exception.VFS4JWriteException;

import java.nio.file.Path;
import java.util.Optional;

public abstract class VFS4JAbstractOperation {

    private final VFS4JFileManager fileManager;

    protected VFS4JAbstractOperation(VFS4JFileManager fileManager) {
        this.fileManager = fileManager;
    }

    protected Path getRealFile(VFS4JPathName file) {
        return fileManager.getRealFile(file);
    }

    public VFS4JFileManager getFileManager() {
        return fileManager;
    }

    protected Optional<VFS4JPathName> convertFromRealPath(Path file) {
        return fileManager.convertFromRealPath(file);
    }

    protected void writeOperation(VFS4JPathName pathName, String operationName) {
        VFS4JParameter param = getFileManager().getConfig().getPath(pathName.getName());
        if (param != null && param.isReadonly()) {
            throw new VFS4JWriteException("write operation forbidden for " + operationName + " on " + pathName.getName());
        }
    }
}
