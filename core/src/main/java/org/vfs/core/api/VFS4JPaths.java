package org.vfs.core.api;

import org.vfs.core.util.ValidationUtils;

import java.util.Arrays;
import java.util.List;

public class VFS4JPaths {

    public static PathName get(String name, String... path) {
        ValidationUtils.checkNotEmpty(name, "Name is empty");
        PathName pathName;
        if (path == null || path.length == 0) {
            pathName = new PathName(name, "");
        } else if (path.length == 1) {
            pathName = new PathName(name, path[0]);
        } else {
            List<String> list = Arrays.asList(path);
            String paths = String.join("/", list);
            pathName = new PathName(name, paths);
        }
        return pathName;
    }
}
