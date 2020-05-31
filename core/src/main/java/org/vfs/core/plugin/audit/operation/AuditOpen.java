package org.vfs.core.plugin.audit.operation;

import org.vfs.core.api.PathName;
import org.vfs.core.api.operation.Open;
import org.vfs.core.plugin.audit.VFS4JAuditPlugins;

import java.io.*;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Set;

public class AuditOpen extends AbstractAuditOperation implements Open {

    private Open open;

    public AuditOpen(VFS4JAuditPlugins vfs4JAuditPlugins, Open open) {
        super(vfs4JAuditPlugins);
        this.open = open;
    }

    @Override
    public InputStream newInputStream(PathName pathName, OpenOption... options) throws IOException {
        log("newInputStream for file {}", pathName);
        return open.newInputStream(pathName, options);
    }

    @Override
    public OutputStream newOutputStream(PathName pathName, OpenOption... options) throws IOException {
        log("newOutputStream for file {}", pathName);
        return open.newOutputStream(pathName, options);
    }

    @Override
    public FileReader newReader(PathName pathName) throws IOException {
        log("newReader for file {}", pathName);
        return open.newReader(pathName);
    }

    @Override
    public FileWriter newWriter(PathName pathName, boolean append) throws IOException {
        log("newWriter for file {}", pathName);
        return open.newWriter(pathName, append);
    }

    @Override
    public SeekableByteChannel newByteChannel(PathName pathName, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        log("newByteChannel for file {}", pathName);
        return open.newByteChannel(pathName, options, attrs);
    }

    @Override
    public DirectoryStream<PathName> newDirectoryStream(PathName pathName, DirectoryStream.Filter<? super PathName> filter) throws IOException {
        log("newDirectoryStream for file {}", pathName);
        return open.newDirectoryStream(pathName, filter);
    }
}
