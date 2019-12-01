package org.vfs.core.core;

import org.vfs.core.util.ValidationUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.StringJoiner;

public class PathName {

    private final String name;
    private final String path;

    public PathName(String name, String path) {
        ValidationUtils.checkNotEmpty(name,"Name is empty");
        ValidationUtils.checkParameter(isValideName(name),"Name is invalide");
        ValidationUtils.checkNotNull(path,"Path is null");
        this.name = name;
        this.path = path;
    }

    private boolean isValideName(String name) {
        if(name==null||name.trim().isEmpty()){
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
        PathName pathName = (PathName) o;
        if(!Objects.equals(name, pathName.name)) {
            return false;
        }
        Path p=Paths.get(path);
        Path p2=Paths.get(pathName.path);
        return p.equals(p2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path);
    }
}
