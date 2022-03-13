package io.github.abarhub.vfs.core.api.path;

import io.github.abarhub.vfs.core.api.VFS4JFileManager;
import io.github.abarhub.vfs.core.util.VFS4JErrorMessages;
import io.github.abarhub.vfs.core.util.VFS4JValidationUtils;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.IdentityHashMap;
import java.util.Map;

public class VFS4JDefaultPathMatcher implements VFS4JPathMatcher {

    private final String pattern;
    private final VFS4JFileManager fileManager;
    private final Map<FileSystem, PathMatcher> map = new IdentityHashMap<>();

    public VFS4JDefaultPathMatcher(String pattern, VFS4JFileManager fileManager) {
        VFS4JValidationUtils.checkNotEmpty(pattern, VFS4JErrorMessages.PATTERN_IS_EMPTY);
        VFS4JValidationUtils.checkNotNull(fileManager, VFS4JErrorMessages.FILESYSTEM_IS_NULL);
        this.pattern = pattern;
        this.fileManager = fileManager;
    }

    @Override
    public boolean matches(VFS4JPathName path) {
        VFS4JValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        Path realPath = fileManager.getRealFile(path);
        PathMatcher pathMatcher = getPatternForFS(realPath, pattern);
        return pathMatcher.matches(realPath);
    }

    private synchronized PathMatcher getPatternForFS(Path realPath, String pattern) {
        FileSystem fs = realPath.getFileSystem();
        if (map.containsKey(fs)) {
            return map.get(fs);
        } else {
            map.put(fs, realPath.getFileSystem().getPathMatcher(pattern));
            return map.get(fs);
        }
    }
}
