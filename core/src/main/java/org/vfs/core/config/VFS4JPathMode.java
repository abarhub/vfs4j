package org.vfs.core.config;

public enum VFS4JPathMode {
    STANDARD("standard"), TEMPORARY("temporary");

    private final String name;

    VFS4JPathMode(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
