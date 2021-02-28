package org.vfs.core.api;

import org.vfs.core.util.VFS4JValidationUtils;

import java.util.Arrays;
import java.util.List;

public class VFS4JPaths {

    private VFS4JPaths() {
    }

    public static VFS4JPathName get(String name, String... path) {
        VFS4JValidationUtils.checkNotEmpty(name, "Name is empty");
        VFS4JPathName VFS4JPathName;
        if (path == null || path.length == 0) {
            VFS4JPathName = new VFS4JPathName(name, "");
        } else if (path.length == 1) {
            VFS4JPathName = new VFS4JPathName(name, path[0]);
        } else {
            List<String> list = Arrays.asList(path);
            String paths = String.join("/", list);
            VFS4JPathName = new VFS4JPathName(name, paths);
        }
        return VFS4JPathName;
    }
}
