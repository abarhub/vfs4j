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
}
