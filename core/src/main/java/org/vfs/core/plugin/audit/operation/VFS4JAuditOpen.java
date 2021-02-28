package org.vfs.core.plugin.audit.operation;

import org.vfs.core.api.VFS4JPathName;
import org.vfs.core.api.operation.VFS4JOpen;
import org.vfs.core.plugin.audit.VFS4JAuditOperation;
import org.vfs.core.plugin.audit.VFS4JAuditPlugins;

import java.io.*;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Set;

public class VFS4JAuditOpen extends VFS4JAbstractAuditOperation implements VFS4JOpen {

    private VFS4JOpen open;

    public VFS4JAuditOpen(VFS4JAuditPlugins vfs4JAuditPlugins, VFS4JOpen open) {
        super(vfs4JAuditPlugins);
        this.open = open;
    }

    @Override
    public InputStream newInputStream(VFS4JPathName VFS4JPathName, OpenOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.NEW_INPUT_STREAM, VFS4JPathName);
        if (active) {
            try {
                InputStream res = open.newInputStream(VFS4JPathName, options);
                log("newInputStream for file {}", VFS4JPathName);
                return res;
            } catch (IOException e) {
                logError("Error for newInputStream for file {}", e, VFS4JPathName);
                throw e;
            }
        } else {
            return open.newInputStream(VFS4JPathName, options);
        }
    }

    @Override
    public OutputStream newOutputStream(VFS4JPathName VFS4JPathName, OpenOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.NEW_OUTPUT_STREAM, VFS4JPathName);
        if (active) {
            try {
                OutputStream res = open.newOutputStream(VFS4JPathName, options);
                log("newOutputStream for file {}", VFS4JPathName);
                return res;
            } catch (IOException e) {
                logError("Error for newOutputStream for file {}", e, VFS4JPathName);
                throw e;
            }
        } else {
            return open.newOutputStream(VFS4JPathName, options);
        }
    }

    @Override
    public FileReader newReader(VFS4JPathName VFS4JPathName) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.NEW_READER, VFS4JPathName);
        if (active) {

            try {
                FileReader res = open.newReader(VFS4JPathName);
                log("newReader for file {}", VFS4JPathName);
                return res;
            } catch (IOException e) {
                logError("Error for newReader for file {}", e, VFS4JPathName);
                throw e;
            }
        } else {
            return open.newReader(VFS4JPathName);
        }
    }

    @Override
    public FileWriter newWriter(VFS4JPathName VFS4JPathName, boolean append) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.NEW_WRITER, VFS4JPathName);
        if (active) {
            try {
                FileWriter res = open.newWriter(VFS4JPathName, append);
                log("newWriter for file {}", VFS4JPathName);
                return res;
            } catch (IOException e) {
                logError("Error for newWriter for file {}", e, VFS4JPathName);
                throw e;
            }
        } else {
            return open.newWriter(VFS4JPathName, append);
        }
    }

    @Override
    public SeekableByteChannel newByteChannel(VFS4JPathName VFS4JPathName, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.NEW_BYTE_CHANNEL, VFS4JPathName);
        if (active) {
            try {
                SeekableByteChannel res = open.newByteChannel(VFS4JPathName, options, attrs);
                log("newByteChannel for file {}", VFS4JPathName);
                return res;
            } catch (IOException e) {
                logError("Error for newByteChannel for file {}", e, VFS4JPathName);
                throw e;
            }
        } else {
            return open.newByteChannel(VFS4JPathName, options, attrs);
        }
    }

    @Override
    public DirectoryStream<VFS4JPathName> newDirectoryStream(VFS4JPathName VFS4JPathName, DirectoryStream.Filter<? super VFS4JPathName> filter) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.NEW_DIRECTORY_STREAM, VFS4JPathName);
        if (active) {
            try {
                DirectoryStream<VFS4JPathName> res = open.newDirectoryStream(VFS4JPathName, filter);
                log("newDirectoryStream for file {}", VFS4JPathName);
                return res;
            } catch (IOException e) {
                logError("Error for newDirectoryStream for file {}", e, VFS4JPathName);
                throw e;
            }
        } else {
            return open.newDirectoryStream(VFS4JPathName, filter);
        }
    }
}
