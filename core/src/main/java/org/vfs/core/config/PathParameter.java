package org.vfs.core.config;

import org.vfs.core.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringJoiner;

public class PathParameter {

    private final Path path;
    private final boolean readonly;
    private final VFS4JPathMode mode;

    public PathParameter(Path path, boolean readonly, VFS4JPathMode mode) {
        ValidationUtils.checkNotNull(path, "Path is null");
        ValidationUtils.checkNotNull(mode, "mode is null");
        this.path = path;
        this.readonly = readonly;
        this.mode = mode;
    }

    public Path getPath() {
        return path;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public VFS4JPathMode getMode() {
        return mode;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PathParameter.class.getSimpleName() + "[", "]")
                .add("path=" + path)
                .add("readonly=" + readonly)
                .add("mode=" + mode)
                .toString();
    }
}
