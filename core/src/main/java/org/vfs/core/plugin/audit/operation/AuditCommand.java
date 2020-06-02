package org.vfs.core.plugin.audit.operation;

import org.vfs.core.api.PathName;
import org.vfs.core.api.operation.Command;
import org.vfs.core.plugin.audit.VFS4JAuditPlugins;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;

public class AuditCommand extends AbstractAuditOperation implements Command {

    private Command command;

    public AuditCommand(VFS4JAuditPlugins vfs4JAuditPlugins, Command command) {
        super(vfs4JAuditPlugins);
        this.command = command;
    }

    @Override
    public PathName createFile(PathName file, FileAttribute<?>... attrs) throws IOException {
        try {
            PathName res = command.createFile(file, attrs);
            log("createFile for file {}", file);
            return res;
        } catch (IOException e) {
            logError("Error for createFile for file {}", e, file);
            throw e;
        }
    }

    @Override
    public PathName createDirectory(PathName file, FileAttribute<?>... attrs) throws IOException {
        try {
            PathName res = command.createDirectory(file, attrs);
            log("createDirectory for file {}", file);
            return res;
        } catch (IOException e) {
            logError("Error for createDirectory for file {}", e, file);
            throw e;
        }
    }

    @Override
    public PathName createDirectories(PathName file, FileAttribute<?>... attrs) throws IOException {
        try {
            PathName res = command.createDirectories(file, attrs);
            log("createDirectories for file {}", file);
            return res;
        } catch (IOException e) {
            logError("Error for createDirectories for file {}", e, file);
            throw e;
        }
    }

    @Override
    public void delete(PathName file) throws IOException {
        try {
            command.delete(file);
            log("delete for file {}", file);
        } catch (IOException e) {
            logError("Error for delete for file {}", e, file);
            throw e;
        }
    }

    @Override
    public boolean deleteIfExists(PathName file) throws IOException {
        try {
            boolean res = command.deleteIfExists(file);
            log("deleteIfExists for file {}", file);
            return res;
        } catch (IOException e) {
            logError("Error for deleteIfExists for file {}", e, file);
            throw e;
        }
    }

    @Override
    public PathName createLink(PathName file, PathName target) throws IOException {
        try {
            PathName res = command.createLink(file, target);
            log("createLink for file {}", file);
            return res;
        } catch (IOException e) {
            logError("Error for createLink for file {}", e, file);
            throw e;
        }
    }

    @Override
    public PathName createSymbolicLink(PathName link, PathName target, FileAttribute<?>... attrs) throws IOException {
        try {
            PathName res = command.createSymbolicLink(link, target, attrs);
            log("createSymbolicLink for file {}", link);
            return res;
        } catch (IOException e) {
            logError("Error for createSymbolicLink for file {}", e, link);
            throw e;
        }
    }

    @Override
    public long copy(InputStream input, PathName target, CopyOption... options) throws IOException {
        try {
            long res = command.copy(input, target, options);
            log("copy to file {}", target);
            return res;
        } catch (IOException e) {
            logError("Error for copy to file {}", e, target);
            throw e;
        }
    }

    @Override
    public long copy(PathName source, OutputStream out) throws IOException {
        try {
            long res = command.copy(source, out);
            log("copy from file {}", source);
            return res;
        } catch (IOException e) {
            logError("Error for copy from file {}", e, source);
            throw e;
        }
    }

    @Override
    public PathName copy(PathName source, PathName target, CopyOption... options) throws IOException {
        try {
            PathName res = command.copy(source, target, options);
            log("copy from file {} to file {}", source, target);
            return res;
        } catch (IOException e) {
            logError("Error for copy from file {} to file {}", e, source, target);
            throw e;
        }
    }

    @Override
    public PathName move(PathName source, PathName target, CopyOption... options) throws IOException {
        try {
            PathName res = command.move(source, target, options);
            log("move from file {} to file {}", source, target);
            return res;
        } catch (IOException e) {
            logError("Error for move from file {} to file {}", e, source, target);
            throw e;
        }
    }

    @Override
    public PathName write(PathName source, byte[] bytes, OpenOption... options) throws IOException {
        try {
            PathName res = command.write(source, bytes, options);
            log("write for file {}", source);
            return res;
        } catch (IOException e) {
            logError("Error for write for file {}", e, source);
            throw e;
        }
    }

    @Override
    public PathName write(PathName source, Iterable<? extends CharSequence> lines, Charset cs, OpenOption... options) throws IOException {
        try {
            PathName res = command.write(source, lines, cs, options);
            log("write for file {}", source);
            return res;
        } catch (IOException e) {
            logError("Error for write for file {}", e, source);
            throw e;
        }
    }
}
