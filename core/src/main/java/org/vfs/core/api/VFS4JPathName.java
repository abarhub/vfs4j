package org.vfs.core.api;

import org.vfs.core.util.VFS4JErrorMessages;
import org.vfs.core.util.VFS4JValidationUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class VFS4JPathName {

    private final String name;
    private final String path;

    public VFS4JPathName(String name, String path) {
        VFS4JValidationUtils.checkNotEmpty(name, VFS4JErrorMessages.NAME_IS_EMPTY);
        VFS4JValidationUtils.checkParameter(isValideName(name), "Name is invalide");
        VFS4JValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        this.name = name;
        this.path = path;
    }

    private boolean isValideName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.matches("^[a-z][a-z0-9]*$");
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(name)
                .append(":")
                .append(path)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VFS4JPathName VFS4JPathName = (VFS4JPathName) o;
        if (!Objects.equals(name, VFS4JPathName.name)) {
            return false;
        }
        Path p = Paths.get(path);
        Path p2 = Paths.get(VFS4JPathName.path);
        return p.equals(p2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path);
    }
}
