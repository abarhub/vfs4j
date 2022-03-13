package io.github.abarhub.vfs.core.api.path;

import io.github.abarhub.vfs.core.util.VFS4JValidationUtils;

import java.util.Arrays;
import java.util.List;

import static io.github.abarhub.vfs.core.util.VFS4JConstants.PATH_SEPARATOR;

public class VFS4JPaths {

    private VFS4JPaths() {
    }

    public static VFS4JPathName get(String name, String... path) {
        VFS4JValidationUtils.checkNotEmpty(name, "Name is empty");
        VFS4JPathName pathName;
        if (path == null || path.length == 0) {
            pathName = new VFS4JDefaultPathName(name, "");
        } else if (path.length == 1) {
            pathName = new VFS4JDefaultPathName(name, path[0]);
        } else {
            List<String> list = Arrays.asList(path);
            String paths = String.join(PATH_SEPARATOR, list);
            pathName = new VFS4JDefaultPathName(name, paths);
        }
        return pathName;
    }

    public static VFS4JPathName parsePath(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path is empty");
        }
        int i = path.indexOf(':');
        if (i < 0) {
            throw new IllegalArgumentException("Path must contains ':' (path='" + path + "')");
        }
        String name = path.substring(0, i);
        String path2 = path.substring(i + 1);
        return VFS4JPaths.get(name, path2);
    }
}
