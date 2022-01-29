package io.github.abarhub.vfs.core.api;

import io.github.abarhub.vfs.core.util.VFS4JConstants;
import io.github.abarhub.vfs.core.util.VFS4JErrorMessages;
import io.github.abarhub.vfs.core.util.VFS4JValidationUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * Return the parent of path. If there is no parent, path is empty ("").
     * For examples, parent of /aaa/bbb/ccc is /aaa/bbb/cc and parent of /aaa/bbb/ccc/ is /aaa/bbb/ccc
     * Special charactere ("." or "..") is not removed.
     *
     * @return Parent
     */
    public VFS4JPathName getParent() {
        if (path.isEmpty()) {
            return this;
        } else {
            int pos = path.lastIndexOf(VFS4JConstants.PATH_SEPARATOR);
            int pos2 = path.lastIndexOf(VFS4JConstants.PATH_WINDOWS_SEPARATOR);
            if (pos == -1 && pos2 == -1) {
                return new VFS4JPathName(name, "");
            } else if (pos != -1 && pos2 == -1) {
                return new VFS4JPathName(name, path.substring(0, pos));
            } else if (pos == -1 && pos2 != -1) {
                return new VFS4JPathName(name, path.substring(0, pos2));
            } else {
                return new VFS4JPathName(name, path.substring(0, Math.max(pos, pos2)));
            }
        }
    }

    /**
     * Return a new VFS4JPathName with :
     * name = this.name
     * path = this.path+"/"+path
     * if path end with "/" ou "\\" or path start with "/" or "\\", "/" is not added
     *
     * @param path path to append
     * @return a new VFS4JPathName
     */
    public VFS4JPathName resolve(String path) {
        if (path == null || path.isEmpty()) {
            return this;
        } else {
            String pathComplet = this.path;
            if (endsWithSeparator(pathComplet) ||
                    startsWithSeparator(path)) {
                pathComplet += path;
            } else {
                pathComplet += VFS4JConstants.PATH_SEPARATOR + path;
            }
            return new VFS4JPathName(name, pathComplet);
        }
    }

    /**
     * Return number of element in the path. Ignore the first or the last "/".
     *
     * @return Number of element in the path
     */
    public int getNameCount() {
        String path = this.path;
        if (startsWithSeparator(path)) {
            path = path.substring(1);
        }
        String[] pathList = splitPath(path);
        return pathList.length;
    }

    /**
     * Return element number no. Ignore the first or the last "/".
     *
     * @param no The element to get
     * @return The element
     */
    public String getName(int no) {
        VFS4JValidationUtils.checkParameter(no >= 0 && no < getNameCount(), "No is invalid");
        String path = this.path;
        if (startsWithSeparator(path)) {
            path = path.substring(1);
        }
        String[] pathList = splitPath(path);
        return pathList[no];
    }

    /**
     * Return a normalised path. The normalized path remove "." and ".."
     *
     * @return a path normalized
     */
    public VFS4JPathName normalize() {
        if (path == null || path.isEmpty()) {
            return this;
        } else {
            String path = this.path;
            String begin = "";
            while (startsWithSeparator(path)) {
                begin = begin + path.substring(0, 1);
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
            return new VFS4JPathName(name, pathNormalized);
        }
    }

    private boolean startsWithSeparator(String path) {
        return path != null && (path.startsWith(VFS4JConstants.PATH_SEPARATOR) || path.startsWith(VFS4JConstants.PATH_WINDOWS_SEPARATOR));
    }

    private boolean endsWithSeparator(String path) {
        return path != null && (path.endsWith(VFS4JConstants.PATH_SEPARATOR) || path.endsWith(VFS4JConstants.PATH_WINDOWS_SEPARATOR));
    }

    private String[] splitPath(String path) {
        return path.split("[" + VFS4JConstants.PATH_SEPARATOR + "\\" + VFS4JConstants.PATH_WINDOWS_SEPARATOR + "]");
    }

    /**
     * Return relative path.
     * Example :
     * new VFS4JPathName("name", "aaa/bbb").relativize("aaa/bbb/ccc").equals("ccc")
     *
     * @param other path to relativize. It must start by path
     * @return The relative path
     */
    public String relativize(String other) {
        VFS4JValidationUtils.checkNotEmpty(other, "Path must not be empty");
        VFS4JValidationUtils.checkParameter(other.startsWith(this.path), "Path parameter must start with path");
        String pathResult = other.substring(this.path.length());
        while (startsWithSeparator(pathResult)) {
            pathResult = pathResult.substring(1);
        }
        return pathResult;
    }

    /**
     * Return true if path starts with other
     *
     * @param other the path. Must not be null.
     * @return true if path starts with other
     */
    public boolean startsWith(String other) {
        VFS4JValidationUtils.checkNotNull(other, "Path must not be null");
        return this.path.startsWith(other);
    }

    /**
     * Return true if path end with other
     *
     * @param other the path. Must not be null.
     * @return true if path ends with other
     */
    public boolean endsWith(String other) {
        VFS4JValidationUtils.checkNotNull(other, "Path must not be null");
        return this.path.endsWith(other);
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
        VFS4JPathName pathName = (VFS4JPathName) o;
        if (!Objects.equals(name, pathName.name)) {
            return false;
        }
        Path p = Paths.get(path);
        Path p2 = Paths.get(pathName.path);
        return p.equals(p2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path);
    }
}
