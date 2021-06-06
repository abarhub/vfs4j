package com.github.abarhub.vfs.core.config;

public enum VFS4JPathMode {
    STANDARD("standard"), TEMPORARY("temporary"),
    CLASSPATH("classpath");

    private final String name;

    VFS4JPathMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
