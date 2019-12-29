package org.vfs.core.api.operation;

import org.vfs.core.api.PathName;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.attribute.*;
import java.util.Map;
import java.util.Set;

public interface Attribute {
    PathName setAttribute(PathName path, String attribute, Object value, LinkOption... options) throws IOException;

    PathName setLastModifiedTime(PathName path, FileTime time) throws IOException;

    PathName setOwner(PathName path, UserPrincipal userPrincipal) throws IOException;

    PathName setPosixFilePermissions(PathName path, Set<PosixFilePermission> posixFilePermissions) throws IOException;

    Object getAttribute(PathName path, String attribute, LinkOption... options) throws IOException;

    <T extends FileAttributeView> T getFileAttributeView(PathName path, Class<T> type, LinkOption... options) throws IOException;

    FileTime getLastModifiedTime(PathName path, LinkOption... options) throws IOException;

    UserPrincipal getOwner(PathName path, LinkOption... options) throws IOException;

    Set<PosixFilePermission> getPosixFilePermissions(PathName path, LinkOption... options) throws IOException;

    boolean isExecutable(PathName path) throws IOException;

    boolean isReadable(PathName path) throws IOException;

    boolean isHidden(PathName path) throws IOException;

    boolean isWritable(PathName file);

    Map<String, Object> readAttributes(PathName file, String attribute, LinkOption... options) throws IOException;

    <T extends BasicFileAttributes> T readAttributes(PathName file, Class<T> type, LinkOption... options) throws IOException;
}
