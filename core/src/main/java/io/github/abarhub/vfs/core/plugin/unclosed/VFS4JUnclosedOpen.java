package io.github.abarhub.vfs.core.plugin.unclosed;

import io.github.abarhub.vfs.core.api.VFS4JPathName;
import io.github.abarhub.vfs.core.api.operation.VFS4JOpen;
import io.github.abarhub.vfs.core.plugin.unclosed.open.UnclosedInputStream;

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

    public VFS4JUnclosedOpen(VFS4JUnclosedPlugins vfs4JUnclosedPlugins, VFS4JOpen open, UnclosableRunnable unclosableRunnable) {
        this.vfs4JUnclosedPlugins = vfs4JUnclosedPlugins;
        this.open = open;
        this.unclosableRunnable = unclosableRunnable;
    }

    @Override
    public InputStream newInputStream(VFS4JPathName pathName, OpenOption... options) throws IOException {
        return new UnclosedInputStream(open.newInputStream(pathName, options), this.unclosableRunnable);
    }

    @Override
    public OutputStream newOutputStream(VFS4JPathName pathName, OpenOption... options) throws IOException {
        return null;
    }

    @Override
    public FileReader newReader(VFS4JPathName pathName) throws IOException {
        return null;
    }

    @Override
    public FileWriter newWriter(VFS4JPathName pathName, boolean append) throws IOException {
        return null;
    }

    @Override
    public SeekableByteChannel newByteChannel(VFS4JPathName pathName, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        return null;
    }

    @Override
    public DirectoryStream<VFS4JPathName> newDirectoryStream(VFS4JPathName pathName, DirectoryStream.Filter<? super VFS4JPathName> filter) throws IOException {
        return null;
    }
}
