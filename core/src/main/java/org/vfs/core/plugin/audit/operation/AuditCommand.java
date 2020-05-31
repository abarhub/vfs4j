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
    public void createFile(PathName file, FileAttribute<?>... attrs) throws IOException {
        log("createFile for file {}", file);
        command.createFile(file, attrs);
    }

    @Override
    public void createDirectory(PathName file, FileAttribute<?>... attrs) throws IOException {
        log("createDirectory for file {}", file);
        command.createDirectory(file, attrs);
    }

    @Override
    public void createDirectories(PathName file, FileAttribute<?>... attrs) throws IOException {
        log("createDirectories for file {}", file);
        command.createDirectories(file, attrs);
    }

    @Override
    public void delete(PathName file) throws IOException {
        log("delete for file {}", file);
        command.delete(file);
    }

    @Override
    public boolean deleteIfExists(PathName file) throws IOException {
        log("deleteIfExists for file {}", file);
        return command.deleteIfExists(file);
    }

    @Override
    public PathName createLink(PathName file, PathName target) throws IOException {
        log("createLink for file {}", file);
        return command.createLink(file, target);
    }

    @Override
    public PathName createSymbolicLink(PathName link, PathName target, FileAttribute<?>... attrs) throws IOException {
        log("createSymbolicLink for file {}", link);
        return command.createSymbolicLink(link, target, attrs);
    }

    @Override
    public long copy(InputStream input, PathName target, CopyOption... options) throws IOException {
        log("copy to file {}", target);
        return command.copy(input, target, options);
    }

    @Override
    public long copy(PathName source, OutputStream out) throws IOException {
        log("copy from file {}", source);
        return command.copy(source, out);
    }

    @Override
    public PathName copy(PathName source, PathName target, CopyOption... options) throws IOException {
        log("copy from file {} to file {}", source, target);
        return command.copy(source, target, options);
    }

    @Override
    public PathName move(PathName source, PathName target, CopyOption... options) throws IOException {
        log("move from file {} to file {}", source, target);
        return command.move(source, target, options);
    }

    @Override
    public PathName write(PathName source, byte[] bytes, OpenOption... options) throws IOException {
        log("write for file {}", source);
        return command.write(source, bytes, options);
    }

    @Override
    public PathName write(PathName source, Iterable<? extends CharSequence> lines, Charset cs, OpenOption... options) throws IOException {
        log("write for file {}", source);
        return command.write(source, lines, cs, options);
    }
}
