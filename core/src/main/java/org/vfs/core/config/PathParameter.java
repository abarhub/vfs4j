package org.vfs.core.config;

import org.vfs.core.util.ValidationUtils;

import java.nio.file.Path;

public class PathParameter {

    private final Path path;
    private final boolean readonly;

    public PathParameter(Path path, boolean readonly) {
        ValidationUtils.checkNotNull(path,"Path is null");
        this.path = path;
        this.readonly = readonly;
    }

    public Path getPath() {
        return path;
    }

    public boolean isReadonly() {
        return readonly;
    }
}
