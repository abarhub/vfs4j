package org.vfs.core.api.operation;

import org.vfs.core.api.AbstractOperation;
import org.vfs.core.api.FileManager;
import org.vfs.core.api.PathName;
import org.vfs.core.exception.VFS4JInvalidPathException;
import org.vfs.core.util.ValidationUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.function.Supplier;

public class SimpleCommand extends AbstractOperation implements Command {

    public SimpleCommand(FileManager fileManager) {
        super(fileManager);
    }

    @Override
    public void createFile(PathName file, FileAttribute<?>... attrs) throws IOException {
        ValidationUtils.checkNotNull(file, "Path is null");
        writeOperation(file, "createFile");
        Path p = getRealFile(file);
        Files.createFile(p, attrs);
    }

    @Override
    public void createDirectory(PathName file, FileAttribute<?>... attrs) throws IOException {
        ValidationUtils.checkNotNull(file, "Path is null");
        writeOperation(file, "createDirectory");
        Path p = getRealFile(file);
        Files.createDirectory(p, attrs);
    }

    @Override
    public void createDirectories(PathName file, FileAttribute<?>... attrs) throws IOException {
        ValidationUtils.checkNotNull(file, "Path is null");
        writeOperation(file, "createDirectories");
        Path p = getRealFile(file);
        Files.createDirectories(p, attrs);
    }

    @Override
    public void delete(PathName file) throws IOException {
        ValidationUtils.checkNotNull(file, "Path is null");
        writeOperation(file, "delete");
        Path p = getRealFile(file);
        Files.delete(p);
    }

    @Override
    public boolean deleteIfExists(PathName file) throws IOException {
        ValidationUtils.checkNotNull(file, "Path is null");
        writeOperation(file, "deleteIfExists");
        Path p = getRealFile(file);
        return Files.deleteIfExists(p);
    }

    @Override
    public PathName createLink(PathName file, PathName target) throws IOException {
        ValidationUtils.checkNotNull(file, "Path is null");
        ValidationUtils.checkNotNull(file, "target is null");
        writeOperation(file, "createLink");
        Path p = getRealFile(file);
        Path targetPath = getRealFile(target);
        Path pathRes = Files.createLink(p, targetPath);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    @Override
    public PathName createSymbolicLink(PathName link, PathName target, FileAttribute<?>... attrs) throws IOException {
        ValidationUtils.checkNotNull(link, "Path is null");
        ValidationUtils.checkNotNull(target, "target is null");
        writeOperation(link, "createSymbolicLink");
        Path p = getRealFile(link);
        Path targetPath = getRealFile(target);
        Path pathRes = Files.createSymbolicLink(p, targetPath, attrs);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    @Override
    public long copy(InputStream input, PathName target, CopyOption... options) throws IOException {
        ValidationUtils.checkNotNull(input, "input is null");
        ValidationUtils.checkNotNull(target, "target is null");
        writeOperation(target, "copy");
        Path targetPath = getRealFile(target);
        return Files.copy(input, targetPath, options);
    }

    @Override
    public long copy(PathName source, OutputStream out) throws IOException {
        ValidationUtils.checkNotNull(source, "source is null");
        ValidationUtils.checkNotNull(out, "out is null");
        Path sourcePath = getRealFile(source);
        return Files.copy(sourcePath, out);
    }

    @Override
    public PathName copy(PathName source, PathName target, CopyOption... options) throws IOException {
        ValidationUtils.checkNotNull(source, "source is null");
        ValidationUtils.checkNotNull(target, "target is null");
        writeOperation(target, "copy");
        Path sourcePath = getRealFile(source);
        Path targetPath = getRealFile(target);
        Path path = Files.copy(sourcePath, targetPath, options);
        return convertFromRealPath(path).orElseThrow(throwInvalidePath(path));
    }

    @Override
    public PathName move(PathName source, PathName target, CopyOption... options) throws IOException {
        ValidationUtils.checkNotNull(source, "source is null");
        ValidationUtils.checkNotNull(target, "target is null");
        writeOperation(source, "move");
        writeOperation(target, "move");
        Path sourcePath = getRealFile(source);
        Path targetPath = getRealFile(target);
        Path path = Files.move(sourcePath, targetPath, options);
        return convertFromRealPath(path).orElseThrow(throwInvalidePath(path));
    }

    @Override
    public PathName write(PathName target, byte[] bytes, OpenOption... options) throws IOException {
        ValidationUtils.checkNotNull(target, "target is null");
        ValidationUtils.checkNotNull(bytes, "bytes is null");
        writeOperation(target, "target");
        Path sourcePath = getRealFile(target);
        Path path = Files.write(sourcePath, bytes, options);
        return convertFromRealPath(path).orElseThrow(throwInvalidePath(path));
    }

    @Override
    public PathName write(PathName target, Iterable<? extends CharSequence> lines, Charset cs, OpenOption... options) throws IOException {
        ValidationUtils.checkNotNull(target, "target is null");
        ValidationUtils.checkNotNull(lines, "lines is null");
        ValidationUtils.checkNotNull(cs, "cs is null");
        writeOperation(target, "target");
        Path sourcePath = getRealFile(target);
        Path path = Files.write(sourcePath, lines, cs, options);
        return convertFromRealPath(path).orElseThrow(throwInvalidePath(path));
    }

    private Supplier<VFS4JInvalidPathException> throwInvalidePath(Path pathRes) {
        return () -> new VFS4JInvalidPathException("Invalide Path", pathRes);
    }
}
