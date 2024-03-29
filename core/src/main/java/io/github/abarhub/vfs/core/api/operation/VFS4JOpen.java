package io.github.abarhub.vfs.core.api.operation;

import io.github.abarhub.vfs.core.api.path.VFS4JPathName;

import java.io.*;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Set;

public interface VFS4JOpen {
    InputStream newInputStream(VFS4JPathName pathName, OpenOption... options) throws IOException;

    OutputStream newOutputStream(VFS4JPathName pathName, OpenOption... options) throws IOException;

    Reader newReader(VFS4JPathName pathName) throws IOException;

    Writer newWriter(VFS4JPathName pathName, boolean append) throws IOException;

    SeekableByteChannel newByteChannel(VFS4JPathName pathName, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException;

    DirectoryStream<VFS4JPathName> newDirectoryStream(VFS4JPathName pathName, DirectoryStream.Filter<? super VFS4JPathName> filter) throws IOException;
}
