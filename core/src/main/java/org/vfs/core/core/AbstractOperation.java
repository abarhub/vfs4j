package org.vfs.core.core;

import java.nio.file.Path;
import java.util.Optional;

public abstract class AbstractOperation {

    private final FileManager fileManager;

    public AbstractOperation(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    protected Path getRealFile(PathName file){
        return fileManager.getRealFile(file);
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    protected Optional<PathName> convertFromRealPath(Path file) {
        return fileManager.convertFromRealPath(file);
    }
}
