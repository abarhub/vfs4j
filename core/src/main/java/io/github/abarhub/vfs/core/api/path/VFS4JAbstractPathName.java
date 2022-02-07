package io.github.abarhub.vfs.core.api.path;

import io.github.abarhub.vfs.core.util.VFS4JErrorMessages;
import io.github.abarhub.vfs.core.util.VFS4JValidationUtils;

public abstract class VFS4JAbstractPathName implements VFS4JPathName {

    private final String name;
    private final String path;

    protected VFS4JAbstractPathName(String name, String path) {
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

}
