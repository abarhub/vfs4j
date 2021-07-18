package io.github.abarhub.vfs.core.api.operation;

import io.github.abarhub.vfs.core.api.VFS4JPathName;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.attribute.*;
import java.util.Map;
import java.util.Set;

public interface VFS4JAttribute {
    VFS4JPathName setAttribute(VFS4JPathName path, String attribute, Object value, LinkOption... options) throws IOException;

    VFS4JPathName setLastModifiedTime(VFS4JPathName path, FileTime time) throws IOException;

    VFS4JPathName setOwner(VFS4JPathName path, UserPrincipal userPrincipal) throws IOException;

    VFS4JPathName setPosixFilePermissions(VFS4JPathName path, Set<PosixFilePermission> posixFilePermissions) throws IOException;

    Object getAttribute(VFS4JPathName path, String attribute, LinkOption... options) throws IOException;

    <T extends FileAttributeView> T getFileAttributeView(VFS4JPathName path, Class<T> type, LinkOption... options) throws IOException;

    FileTime getLastModifiedTime(VFS4JPathName path, LinkOption... options) throws IOException;

    UserPrincipal getOwner(VFS4JPathName path, LinkOption... options) throws IOException;

    Set<PosixFilePermission> getPosixFilePermissions(VFS4JPathName path, LinkOption... options) throws IOException;

    boolean isExecutable(VFS4JPathName path) throws IOException;

    boolean isReadable(VFS4JPathName path) throws IOException;

    boolean isHidden(VFS4JPathName path) throws IOException;

    boolean isWritable(VFS4JPathName file) throws IOException;

    Map<String, Object> readAttributes(VFS4JPathName file, String attribute, LinkOption... options) throws IOException;

    <T extends BasicFileAttributes> T readAttributes(VFS4JPathName file, Class<T> type, LinkOption... options) throws IOException;
}
