package com.github.abarhub.vfs.core.config;

import com.github.abarhub.vfs.core.util.VFS4JValidationUtils;

public class VFS4JClasspathParameter implements VFS4JParameter {

    private final String path;

    public VFS4JClasspathParameter(String path) {
        VFS4JValidationUtils.checkNotNull(path, "Path is null");
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
