package io.github.abarhub.vfs.core.plugin.unclosed;

import io.github.abarhub.vfs.core.api.operation.VFS4JOpen;
import io.github.abarhub.vfs.core.api.path.VFS4JPathName;
import io.github.abarhub.vfs.core.plugin.unclosed.open.*;

import java.io.*;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Set;

public class VFS4JUnclosedOpen implements VFS4JOpen {

    private final VFS4JUnclosedPlugins vfs4JUnclosedPlugins;
    private final VFS4JOpen open;
    private final UnclosableRunnable unclosableRunnable;
    private final VFS4JLogIfNotClosedAfterDelay logIfNotClosedAfterDelay;

    public VFS4JUnclosedOpen(VFS4JUnclosedPlugins vfs4JUnclosedPlugins, VFS4JOpen open,
                             UnclosableRunnable unclosableRunnable, VFS4JLogIfNotClosedAfterDelay logIfNotClosedAfterDelay) {
        this.vfs4JUnclosedPlugins = vfs4JUnclosedPlugins;
        this.open = open;
        this.unclosableRunnable = unclosableRunnable;
        this.logIfNotClosedAfterDelay = logIfNotClosedAfterDelay;
    }

    @Override
    public InputStream newInputStream(VFS4JPathName pathName, OpenOption... options) throws IOException {
        return new UnclosedInputStream(open.newInputStream(pathName, options), this.unclosableRunnable,
                pathName, logIfNotClosedAfterDelay);
    }

    @Override
    public OutputStream newOutputStream(VFS4JPathName pathName, OpenOption... options) throws IOException {
        return new UnclosedOutputStream(open.newOutputStream(pathName, options), this.unclosableRunnable,
                pathName, logIfNotClosedAfterDelay);
    }

    @Override
    public Reader newReader(VFS4JPathName pathName) throws IOException {
        return new UnclosedReader(open.newReader(pathName), this.unclosableRunnable,
                pathName, logIfNotClosedAfterDelay);
    }

    @Override
    public Writer newWriter(VFS4JPathName pathName, boolean append) throws IOException {
        return new UnclosedWriter(open.newWriter(pathName, append), this.unclosableRunnable,
                pathName, logIfNotClosedAfterDelay);
    }

    @Override
    public SeekableByteChannel newByteChannel(VFS4JPathName pathName, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        return new UnclosedSeekableByteChannel(open.newByteChannel(pathName, options, attrs),
                this.unclosableRunnable, pathName, logIfNotClosedAfterDelay);
    }

    @Override
    public DirectoryStream<VFS4JPathName> newDirectoryStream(VFS4JPathName pathName, DirectoryStream.Filter<? super VFS4JPathName> filter) throws IOException {
        return new UnclosedDirectoryStream(open.newDirectoryStream(pathName, filter),
                this.unclosableRunnable, pathName, logIfNotClosedAfterDelay);
    }
}
