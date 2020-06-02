package org.vfs.core.api.operation;

import org.vfs.core.api.PathName;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;

public interface Command {
    PathName createFile(PathName file, FileAttribute<?>... attrs) throws IOException;

    PathName createDirectory(PathName file, FileAttribute<?>... attrs) throws IOException;

    PathName createDirectories(PathName file, FileAttribute<?>... attrs) throws IOException;

    void delete(PathName file) throws IOException;

    boolean deleteIfExists(PathName file) throws IOException;

    PathName createLink(PathName file, PathName target) throws IOException;

    PathName createSymbolicLink(PathName link, PathName target, FileAttribute<?>... attrs) throws IOException;

    long copy(InputStream input, PathName target, CopyOption... options) throws IOException;

    long copy(PathName source, OutputStream out) throws IOException;

    PathName copy(PathName source, PathName target, CopyOption... options) throws IOException;

    PathName move(PathName source, PathName target, CopyOption... options) throws IOException;

    PathName write(PathName source, byte[] bytes, OpenOption... options) throws IOException;

    PathName write(PathName source, Iterable<? extends CharSequence> lines, Charset cs, OpenOption... options) throws IOException;
}
