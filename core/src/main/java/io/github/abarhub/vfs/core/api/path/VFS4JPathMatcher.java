package io.github.abarhub.vfs.core.api.path;

@FunctionalInterface
public interface VFS4JPathMatcher {
    boolean matches(VFS4JPathName path);
}
