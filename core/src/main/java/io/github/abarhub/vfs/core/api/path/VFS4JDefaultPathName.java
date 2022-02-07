package io.github.abarhub.vfs.core.api.path;

import io.github.abarhub.vfs.core.util.VFS4JConstants;
import io.github.abarhub.vfs.core.util.VFS4JValidationUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VFS4JDefaultPathName extends VFS4JAbstractPathName {

    protected VFS4JDefaultPathName(String name, String path) {
        super(name, path);
    }

    public VFS4JPathName getParent() {
        if (getPath().isEmpty()) {
            return this;
        } else {
            int pos = getPath().lastIndexOf(VFS4JConstants.PATH_SEPARATOR);
            int pos2 = getPath().lastIndexOf(VFS4JConstants.PATH_WINDOWS_SEPARATOR);
            if (pos == -1 && pos2 == -1) {
                return VFS4JPaths.get(getName(), "");
            } else if (pos != -1 && pos2 == -1) {
                return VFS4JPaths.get(getName(), getPath().substring(0, pos));
            } else if (pos == -1 && pos2 != -1) {
                return VFS4JPaths.get(getName(), getPath().substring(0, pos2));
            } else {
                return VFS4JPaths.get(getName(), getPath().substring(0, Math.max(pos, pos2)));
            }
        }
    }

    public VFS4JPathName resolve(String path) {
        if (path == null || path.isEmpty()) {
            return this;
        } else {
            String pathComplet = getPath();
            if (endsWithSeparator(pathComplet) ||
                    startsWithSeparator(path)) {
                pathComplet += path;
            } else {
                pathComplet += VFS4JConstants.PATH_SEPARATOR + path;
            }
            return VFS4JPaths.get(getName(), pathComplet);
        }
    }

    public int getNameCount() {
        String path = getPath();
        if (startsWithSeparator(path)) {
            path = path.substring(1);
        }
        String[] pathList = splitPath(path);
        return pathList.length;
    }

    public String getName(int no) {
        VFS4JValidationUtils.checkParameter(no >= 0 && no < getNameCount(), "No is invalid");
        String path = getPath();
        if (startsWithSeparator(path)) {
            path = path.substring(1);
        }
        String[] pathList = splitPath(path);
        return pathList[no];
    }

    public VFS4JPathName normalize() {
        if (getPath() == null || getPath().isEmpty()) {
            return this;
        } else {
            String path = getPath();
            StringBuilder begin = new StringBuilder();
            while (startsWithSeparator(path)) {
                begin.append(path.charAt(0));
                path = path.substring(1);
            }
            String end = "";
            while (endsWithSeparator(path)) {
                end = path.substring(path.length() - 1) + end;
                path = path.substring(0, path.length() - 1);
            }
            String[] pathArray = splitPath(path);
            List<String> pathList = new ArrayList<>();
            for (String s : pathArray) {
                if (s != null) {
                    if (s.equals(".") || s.isEmpty()) {
                        // ignore
                    } else if (s.equals("..")) {
                        if (!pathList.isEmpty()) {
                            pathList.remove(pathList.size() - 1);
                        }
                    } else {
                        pathList.add(s);
                    }
                }
            }
            String pathNormalized = begin + String.join(VFS4JConstants.PATH_SEPARATOR, pathList) + end;
            return VFS4JPaths.get(getName(), pathNormalized);
        }
    }

    private boolean startsWithSeparator(String path) {
        return path != null && (path.startsWith(VFS4JConstants.PATH_SEPARATOR) || path.startsWith(VFS4JConstants.PATH_WINDOWS_SEPARATOR));
    }

    private boolean endsWithSeparator(String path) {
        return path != null && (path.endsWith(VFS4JConstants.PATH_SEPARATOR) || path.endsWith(VFS4JConstants.PATH_WINDOWS_SEPARATOR));
    }

    private String[] splitPath(String path) {
        if (path == null) {
            return new String[0];
        } else {
            return path.split("[" + VFS4JConstants.PATH_SEPARATOR + "\\" + VFS4JConstants.PATH_WINDOWS_SEPARATOR + "]");
        }
    }

    public String relativize(String other) {
        VFS4JValidationUtils.checkNotEmpty(other, "Path must not be empty");
        VFS4JValidationUtils.checkParameter(other.startsWith(getPath()), "Path parameter must start with path");
        String pathResult = other.substring(getPath().length());
        while (startsWithSeparator(pathResult)) {
            pathResult = pathResult.substring(1);
        }
        return pathResult;
    }

    public boolean startsWith(String other) {
        VFS4JValidationUtils.checkNotNull(other, "Path must not be null");
        return getPath().startsWith(other);
    }

    public boolean endsWith(String other) {
        VFS4JValidationUtils.checkNotNull(other, "Path must not be null");
        return getPath().endsWith(other);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(getName())
                .append(":")
                .append(getPath())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VFS4JPathName pathName = (VFS4JPathName) o;
        if (!Objects.equals(getName(), pathName.getName())) {
            return false;
        }
        Path p = Paths.get(getPath());
        Path p2 = Paths.get(pathName.getPath());
        return p.equals(p2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPath());
    }
}
