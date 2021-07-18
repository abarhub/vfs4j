package io.github.abarhub.vfs.core.api.operation;

import io.github.abarhub.vfs.core.api.VFS4JPathName;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.LinkOption;
import java.util.List;
import java.util.stream.Stream;

public interface VFS4JQuery {
    boolean exists(VFS4JPathName file, LinkOption... options);

    boolean isDirectory(VFS4JPathName file, LinkOption... options);

    boolean isRegularFile(VFS4JPathName file, LinkOption... options);

    boolean isSameFile(VFS4JPathName file, VFS4JPathName file2) throws IOException;

    boolean isSymbolicLink(VFS4JPathName file);

    Stream<String> lines(VFS4JPathName file) throws IOException;

    Stream<String> lines(VFS4JPathName file, Charset charsets) throws IOException;

    boolean notExists(VFS4JPathName file, LinkOption... options);

    byte[] readAllBytes(VFS4JPathName file) throws IOException;

    List<String> readAllLines(VFS4JPathName file) throws IOException;

    List<String> readAllLines(VFS4JPathName file, Charset charset) throws IOException;

    long size(VFS4JPathName file) throws IOException;
}
