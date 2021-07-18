package io.github.abarhub.vfs.core.plugin.audit.operation;

import io.github.abarhub.vfs.core.api.VFS4JPathName;
import io.github.abarhub.vfs.core.api.operation.VFS4JOpen;
import io.github.abarhub.vfs.core.plugin.audit.VFS4JAuditOperation;
import io.github.abarhub.vfs.core.plugin.audit.VFS4JAuditPlugins;

import java.io.*;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Set;

public class VFS4JAuditOpen extends VFS4JAbstractAuditOperation implements VFS4JOpen {

    private VFS4JOpen open;

    public VFS4JAuditOpen(VFS4JAuditPlugins auditPlugins, VFS4JOpen open) {
        super(auditPlugins);
        this.open = open;
    }

    @Override
    public InputStream newInputStream(VFS4JPathName pathName, OpenOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.NEW_INPUT_STREAM, pathName);
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
    public OutputStream newOutputStream(VFS4JPathName pathName, OpenOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.NEW_OUTPUT_STREAM, pathName);
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
    public FileReader newReader(VFS4JPathName pathName) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.NEW_READER, pathName);
        if (active) {

            try {
                FileReader res = open.newReader(pathName);
                log("newReader for file {}", pathName);
                return res;
            } catch (IOException e) {
                logError("Error for newReader for file {}", e, pathName);
                throw e;
            }
        } else {
            return open.newReader(pathName);
        }
    }

    @Override
    public FileWriter newWriter(VFS4JPathName pathName, boolean append) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.NEW_WRITER, pathName);
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
    public SeekableByteChannel newByteChannel(VFS4JPathName pathName, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.NEW_BYTE_CHANNEL, pathName);
        if (active) {
            try {
                SeekableByteChannel res = open.newByteChannel(pathName, options, attrs);
                log("newByteChannel for file {}", pathName);
                return res;
            } catch (IOException e) {
                logError("Error for newByteChannel for file {}", e, pathName);
                throw e;
            }
        } else {
            return open.newByteChannel(pathName, options, attrs);
        }
    }

    @Override
    public DirectoryStream<VFS4JPathName> newDirectoryStream(VFS4JPathName pathName, DirectoryStream.Filter<? super VFS4JPathName> filter) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.NEW_DIRECTORY_STREAM, pathName);
        if (active) {
            try {
                DirectoryStream<VFS4JPathName> res = open.newDirectoryStream(pathName, filter);
                log("newDirectoryStream for file {}", pathName);
                return res;
            } catch (IOException e) {
                logError("Error for newDirectoryStream for file {}", e, pathName);
                throw e;
            }
        } else {
            return open.newDirectoryStream(pathName, filter);
        }
    }
}
