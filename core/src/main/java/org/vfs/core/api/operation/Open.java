package org.vfs.core.api.operation;

import org.vfs.core.api.PathName;

import java.io.*;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Set;

public interface Open {
    InputStream newInputStream(PathName pathName, OpenOption... options) throws IOException;

    OutputStream newOutputStream(PathName pathName, OpenOption... options) throws IOException;

    FileReader newReader(PathName pathName) throws IOException;

    FileWriter newWriter(PathName pathName, boolean append) throws IOException;

    SeekableByteChannel newByteChannel(PathName pathName, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException;

    DirectoryStream<PathName> newDirectoryStream(PathName pathName, DirectoryStream.Filter<? super PathName> filter) throws IOException;
}
