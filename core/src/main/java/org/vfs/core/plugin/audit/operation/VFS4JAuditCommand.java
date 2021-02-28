package org.vfs.core.plugin.audit.operation;

import org.vfs.core.api.VFS4JPathName;
import org.vfs.core.api.operation.VFS4JCommand;
import org.vfs.core.plugin.audit.VFS4JAuditOperation;
import org.vfs.core.plugin.audit.VFS4JAuditPlugins;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;

public class VFS4JAuditCommand extends VFS4JAbstractAuditOperation implements VFS4JCommand {

    private VFS4JCommand command;

    public VFS4JAuditCommand(VFS4JAuditPlugins auditPlugins, VFS4JCommand command) {
        super(auditPlugins);
        this.command = command;
    }

    @Override
    public VFS4JPathName createFile(VFS4JPathName file, FileAttribute<?>... attrs) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.CREATE_FILE, file);
        if (active) {
            try {
                VFS4JPathName res = command.createFile(file, attrs);
                log("createFile for file {}", file);
                return res;
            } catch (IOException e) {
                logError("Error for createFile for file {}", e, file);
                throw e;
            }
        } else {
            return command.createFile(file, attrs);
        }
    }

    @Override
    public VFS4JPathName createDirectory(VFS4JPathName file, FileAttribute<?>... attrs) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.CREATE_DIRECTORY, file);
        if (active) {
            try {
                VFS4JPathName res = command.createDirectory(file, attrs);
                log("createDirectory for file {}", file);
                return res;
            } catch (IOException e) {
                logError("Error for createDirectory for file {}", e, file);
                throw e;
            }
        } else {
            return command.createDirectory(file, attrs);
        }
    }

    @Override
    public VFS4JPathName createDirectories(VFS4JPathName file, FileAttribute<?>... attrs) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.CREATE_DIRECTORIES, file);
        if (active) {
            try {
                VFS4JPathName res = command.createDirectories(file, attrs);
                log("createDirectories for file {}", file);
                return res;
            } catch (IOException e) {
                logError("Error for createDirectories for file {}", e, file);
                throw e;
            }
        } else {
            return command.createDirectories(file, attrs);
        }
    }

    @Override
    public void delete(VFS4JPathName file) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.DELETE, file);
        if (active) {
            try {
                command.delete(file);
                log("delete for file {}", file);
            } catch (IOException e) {
                logError("Error for delete for file {}", e, file);
                throw e;
            }
        } else {
            command.delete(file);
        }
    }

    @Override
    public boolean deleteIfExists(VFS4JPathName file) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.DELETE_IF_EXISTS, file);
        if (active) {
            try {
                boolean res = command.deleteIfExists(file);
                log("deleteIfExists for file {}", file);
                return res;
            } catch (IOException e) {
                logError("Error for deleteIfExists for file {}", e, file);
                throw e;
            }
        } else {
            return command.deleteIfExists(file);
        }
    }

    @Override
    public VFS4JPathName createLink(VFS4JPathName file, VFS4JPathName target) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.CREATE_LINK, file, target);
        if (active) {
            try {
                VFS4JPathName res = command.createLink(file, target);
                log("createLink for file {}", file);
                return res;
            } catch (IOException e) {
                logError("Error for createLink for file {}", e, file);
                throw e;
            }
        } else {
            return command.createLink(file, target);
        }
    }

    @Override
    public VFS4JPathName createSymbolicLink(VFS4JPathName link, VFS4JPathName target, FileAttribute<?>... attrs) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.CREATE_SYMBOLIC_LINK, link, target);
        if (active) {
            try {
                VFS4JPathName res = command.createSymbolicLink(link, target, attrs);
                log("createSymbolicLink for file {}", link);
                return res;
            } catch (IOException e) {
                logError("Error for createSymbolicLink for file {}", e, link);
                throw e;
            }
        } else {
            return command.createSymbolicLink(link, target, attrs);
        }
    }

    @Override
    public long copy(InputStream input, VFS4JPathName target, CopyOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.COPY, target);
        if (active) {
            try {
                long res = command.copy(input, target, options);
                log("copy to file {}", target);
                return res;
            } catch (IOException e) {
                logError("Error for copy to file {}", e, target);
                throw e;
            }
        } else {
            return command.copy(input, target, options);
        }
    }

    @Override
    public long copy(VFS4JPathName source, OutputStream out) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.COPY, source);
        if (active) {
            try {
                long res = command.copy(source, out);
                log("copy from file {}", source);
                return res;
            } catch (IOException e) {
                logError("Error for copy from file {}", e, source);
                throw e;
            }
        } else {
            return command.copy(source, out);
        }
    }

    @Override
    public VFS4JPathName copy(VFS4JPathName source, VFS4JPathName target, CopyOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.COPY, source, target);
        if (active) {
            try {
                VFS4JPathName res = command.copy(source, target, options);
                log("copy from file {} to file {}", source, target);
                return res;
            } catch (IOException e) {
                logError("Error for copy from file {} to file {}", e, source, target);
                throw e;
            }
        } else {
            return command.copy(source, target, options);
        }
    }

    @Override
    public VFS4JPathName move(VFS4JPathName source, VFS4JPathName target, CopyOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.MOVE, source, target);
        if (active) {
            try {
                VFS4JPathName res = command.move(source, target, options);
                log("move from file {} to file {}", source, target);
                return res;
            } catch (IOException e) {
                logError("Error for move from file {} to file {}", e, source, target);
                throw e;
            }
        } else {
            return command.move(source, target, options);
        }
    }

    @Override
    public VFS4JPathName write(VFS4JPathName source, byte[] bytes, OpenOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.WRITE, source);
        if (active) {
            try {
                VFS4JPathName res = command.write(source, bytes, options);
                log("write for file {}", source);
                return res;
            } catch (IOException e) {
                logError("Error for write for file {}", e, source);
                throw e;
            }
        } else {
            return command.write(source, bytes, options);
        }
    }

    @Override
    public VFS4JPathName write(VFS4JPathName source, Iterable<? extends CharSequence> lines, Charset cs, OpenOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.WRITE, source);
        if (active) {
            try {
                VFS4JPathName res = command.write(source, lines, cs, options);
                log("write for file {}", source);
                return res;
            } catch (IOException e) {
                logError("Error for write for file {}", e, source);
                throw e;
            }
        } else {
            return command.write(source, lines, cs, options);
        }
    }
}
