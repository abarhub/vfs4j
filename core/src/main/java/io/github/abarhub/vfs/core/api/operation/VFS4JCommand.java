package io.github.abarhub.vfs.core.api.operation;

import io.github.abarhub.vfs.core.api.path.VFS4JPathName;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;

public interface VFS4JCommand {
    VFS4JPathName createFile(VFS4JPathName file, FileAttribute<?>... attrs) throws IOException;

    VFS4JPathName createDirectory(VFS4JPathName file, FileAttribute<?>... attrs) throws IOException;

    VFS4JPathName createDirectories(VFS4JPathName file, FileAttribute<?>... attrs) throws IOException;

    void delete(VFS4JPathName file) throws IOException;

    boolean deleteIfExists(VFS4JPathName file) throws IOException;

    VFS4JPathName createLink(VFS4JPathName file, VFS4JPathName target) throws IOException;

    VFS4JPathName createSymbolicLink(VFS4JPathName link, VFS4JPathName target, FileAttribute<?>... attrs) throws IOException;

    long copy(InputStream input, VFS4JPathName target, CopyOption... options) throws IOException;

    long copy(VFS4JPathName source, OutputStream out) throws IOException;

    VFS4JPathName copy(VFS4JPathName source, VFS4JPathName target, CopyOption... options) throws IOException;

    VFS4JPathName move(VFS4JPathName source, VFS4JPathName target, CopyOption... options) throws IOException;

    VFS4JPathName write(VFS4JPathName source, byte[] bytes, OpenOption... options) throws IOException;

    VFS4JPathName write(VFS4JPathName source, Iterable<? extends CharSequence> lines, Charset cs, OpenOption... options) throws IOException;
}
