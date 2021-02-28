package org.vfs.core.config;

import org.vfs.core.util.ValidationUtils;

public class VFS4JClasspathParameter implements VFS4JParameter {

    private final String path;

    public VFS4JClasspathParameter(String path) {
        ValidationUtils.checkNotNull(path, "Path is null");
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean isReadonly() {
        return true;
    }

    @Override
    public VFS4JPathMode getMode() {
        return VFS4JPathMode.CLASSPATH;
    }
}
