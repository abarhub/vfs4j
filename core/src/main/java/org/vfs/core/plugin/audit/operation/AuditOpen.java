package org.vfs.core.plugin.audit.operation;

import org.vfs.core.api.PathName;
import org.vfs.core.api.operation.Open;
import org.vfs.core.plugin.audit.AuditOperation;
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
        boolean active = isActive(AuditOperation.NEW_INPUT_STREAM, pathName);
        if (active) {
            try {
                InputStream res = open.newInputStream(pathName, options);
                log("newInputStream for file {}", pathName);
                return res;
            } catch (IOException e) {
                logError("Error for newInputStream for file {}", e, pathName);
                throw e;
            }
        } else {
            return open.newInputStream(pathName, options);
        }
    }

    @Override
    public OutputStream newOutputStream(PathName pathName, OpenOption... options) throws IOException {
        boolean active = isActive(AuditOperation.NEW_OUTPUT_STREAM, pathName);
        if (active) {
            try {
                OutputStream res = open.newOutputStream(pathName, options);
                log("newOutputStream for file {}", pathName);
                return res;
            } catch (IOException e) {
                logError("Error for newOutputStream for file {}", e, pathName);
                throw e;
            }
        } else {
            return open.newOutputStream(pathName, options);
        }
    }

    @Override
    public FileReader newReader(PathName pathName) throws IOException {
        boolean active = isActive(AuditOperation.NEW_READER, pathName);
        if (active) {

            try {
                FileReader res = open.newReader(pathName);
                log("newReader for file {}", pathName);
                return res;
            } catch (IOException e) {
                logError("newReader for file {}", e, pathName);
                throw e;
            }
        } else {
            return open.newReader(pathName);
        }
    }

    @Override
    public FileWriter newWriter(PathName pathName, boolean append) throws IOException {
        boolean active = isActive(AuditOperation.NEW_WRITER, pathName);
        if (active) {
            try {
                FileWriter res = open.newWriter(pathName, append);
                log("newWriter for file {}", pathName);
                return res;
            } catch (IOException e) {
                logError("Error for newWriter for file {}", e, pathName);
                throw e;
            }
        } else {
            return open.newWriter(pathName, append);
        }
    }

    @Override
    public SeekableByteChannel newByteChannel(PathName pathName, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        boolean active = isActive(AuditOperation.NEW_BYTE_CHANNEL, pathName);
        if (active) {
            try {
                SeekableByteChannel res = open.newByteChannel(pathName, options, attrs);
                log("newByteChannel for file {}", pathName);
                return res;
            } catch (IOException e) {
                logError("Error newByteChannel for file {}", e, pathName);
                throw e;
            }
        } else {
            return open.newByteChannel(pathName, options, attrs);
        }
    }

    @Override
    public DirectoryStream<PathName> newDirectoryStream(PathName pathName, DirectoryStream.Filter<? super PathName> filter) throws IOException {
        boolean active = isActive(AuditOperation.NEW_DIRECTORY_STREAM, pathName);
        if (active) {
            try {
                DirectoryStream<PathName> res = open.newDirectoryStream(pathName, filter);
                log("newDirectoryStream for file {}", pathName);
                return res;
            } catch (IOException e) {
                logError("newDirectoryStream for file {}", e, pathName);
                throw e;
            }
        } else {
            return open.newDirectoryStream(pathName, filter);
        }
    }
}
