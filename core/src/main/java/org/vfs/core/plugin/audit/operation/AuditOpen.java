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
        try {
            InputStream res = open.newInputStream(pathName, options);
            log("newInputStream for file {}", pathName);
            return res;
        } catch (IOException e) {
            logError("Error for newInputStream for file {}", e, pathName);
            throw e;
        }
    }

    @Override
    public OutputStream newOutputStream(PathName pathName, OpenOption... options) throws IOException {
        try {
            OutputStream res = open.newOutputStream(pathName, options);
            log("newOutputStream for file {}", pathName);
            return res;
        } catch (IOException e) {
            logError("Error for newOutputStream for file {}", e, pathName);
            throw e;
        }
    }

    @Override
    public FileReader newReader(PathName pathName) throws IOException {
        try {
            FileReader res = open.newReader(pathName);
            log("newReader for file {}", pathName);
            return res;
        } catch (IOException e) {
            logError("newReader for file {}", e, pathName);
            throw e;
        }
    }

    @Override
    public FileWriter newWriter(PathName pathName, boolean append) throws IOException {
        try {
            FileWriter res = open.newWriter(pathName, append);
            log("newWriter for file {}", pathName);
            return res;
        } catch (IOException e) {
            logError("Error for newWriter for file {}", e, pathName);
            throw e;
        }
    }

    @Override
    public SeekableByteChannel newByteChannel(PathName pathName, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        try {
            SeekableByteChannel res = open.newByteChannel(pathName, options, attrs);
            log("newByteChannel for file {}", pathName);
            return res;
        } catch (IOException e) {
            logError("Error newByteChannel for file {}", e, pathName);
            throw e;
        }
    }

    @Override
    public DirectoryStream<PathName> newDirectoryStream(PathName pathName, DirectoryStream.Filter<? super PathName> filter) throws IOException {
        try {
            DirectoryStream<PathName> res = open.newDirectoryStream(pathName, filter);
            log("newDirectoryStream for file {}", pathName);
            return res;
        } catch (IOException e) {
            logError("newDirectoryStream for file {}", e, pathName);
            throw e;
        }
    }
}
