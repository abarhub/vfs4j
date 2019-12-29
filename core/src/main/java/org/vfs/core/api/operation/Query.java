package org.vfs.core.api.operation;

import org.vfs.core.api.PathName;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.LinkOption;
import java.util.List;
import java.util.stream.Stream;

public interface Query {
    boolean exists(PathName file, LinkOption... options);

    boolean isDirectory(PathName file, LinkOption... options);

    boolean isRegularFile(PathName file, LinkOption... options);

    boolean isSameFile(PathName file, PathName file2) throws IOException;

    boolean isSymbolicLink(PathName file);

    Stream<String> lines(PathName file) throws IOException;

    Stream<String> lines(PathName file, Charset charsets) throws IOException;

    boolean notExists(PathName file, LinkOption... options);

    byte[] readAllBytes(PathName file) throws IOException;

    List<String> readAllLines(PathName file) throws IOException;

    List<String> readAllLines(PathName file, Charset charset) throws IOException;

    long size(PathName file) throws IOException;
}
