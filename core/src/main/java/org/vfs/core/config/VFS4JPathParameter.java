package org.vfs.core.config;

import org.vfs.core.util.VFS4JValidationUtils;

import java.nio.file.Path;
import java.util.StringJoiner;

public class VFS4JPathParameter implements VFS4JParameter {

    private final Path path;
    private final boolean readonly;
    private final VFS4JPathMode mode;

    public VFS4JPathParameter(Path path, boolean readonly, VFS4JPathMode mode) {
        VFS4JValidationUtils.checkNotNull(path, "Path is null");
        VFS4JValidationUtils.checkNotNull(mode, "mode is null");
        VFS4JValidationUtils.checkParameter(mode == VFS4JPathMode.STANDARD ||
                mode == VFS4JPathMode.TEMPORARY, "mode must be STANDARD or TEMPORARY");
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
        return new StringJoiner(", ", VFS4JPathParameter.class.getSimpleName() + "[", "]")
                .add("path=" + path)
                .add("readonly=" + readonly)
                .add("mode=" + mode)
                .toString();
    }
}
