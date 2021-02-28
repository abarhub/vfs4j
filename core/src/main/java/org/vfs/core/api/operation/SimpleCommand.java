package org.vfs.core.api.operation;

import org.vfs.core.api.AbstractOperation;
import org.vfs.core.api.FileManager;
import org.vfs.core.api.PathName;
import org.vfs.core.exception.VFS4JInvalidPathException;
import org.vfs.core.util.VFS4JErrorMessages;
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
    public PathName createFile(PathName file, FileAttribute<?>... attrs) throws IOException {
        ValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        writeOperation(file, "createFile");
        Path p = getRealFile(file);
        Path pathRes = Files.createFile(p, attrs);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    @Override
    public PathName createDirectory(PathName file, FileAttribute<?>... attrs) throws IOException {
        ValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        writeOperation(file, "createDirectory");
        Path p = getRealFile(file);
        Path pathRes = Files.createDirectory(p, attrs);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    @Override
    public PathName createDirectories(PathName file, FileAttribute<?>... attrs) throws IOException {
        ValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        writeOperation(file, "createDirectories");
        Path p = getRealFile(file);
        Path pathRes = Files.createDirectories(p, attrs);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    @Override
    public void delete(PathName file) throws IOException {
        ValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        writeOperation(file, "delete");
        Path p = getRealFile(file);
        Files.delete(p);
    }

    @Override
    public boolean deleteIfExists(PathName file) throws IOException {
        ValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        writeOperation(file, "deleteIfExists");
        Path p = getRealFile(file);
        return Files.deleteIfExists(p);
    }

    @Override
    public PathName createLink(PathName file, PathName target) throws IOException {
        ValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        ValidationUtils.checkNotNull(file, VFS4JErrorMessages.TARGET_IS_NULL);
        writeOperation(file, "createLink");
        Path p = getRealFile(file);
        Path targetPath = getRealFile(target);
        Path pathRes = Files.createLink(p, targetPath);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    @Override
    public PathName createSymbolicLink(PathName link, PathName target, FileAttribute<?>... attrs) throws IOException {
        ValidationUtils.checkNotNull(link, VFS4JErrorMessages.PATH_IS_NULL);
        ValidationUtils.checkNotNull(target, VFS4JErrorMessages.TARGET_IS_NULL);
        writeOperation(link, "createSymbolicLink");
        Path p = getRealFile(link);
        Path targetPath = getRealFile(target);
        Path pathRes = Files.createSymbolicLink(p, targetPath, attrs);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    @Override
    public long copy(InputStream input, PathName target, CopyOption... options) throws IOException {
        ValidationUtils.checkNotNull(input, VFS4JErrorMessages.SOURCE_IS_NULL);
        ValidationUtils.checkNotNull(target, VFS4JErrorMessages.TARGET_IS_NULL);
        writeOperation(target, "copy");
        Path targetPath = getRealFile(target);
        return Files.copy(input, targetPath, options);
    }

    @Override
    public long copy(PathName source, OutputStream target) throws IOException {
        ValidationUtils.checkNotNull(source, VFS4JErrorMessages.SOURCE_IS_NULL);
        ValidationUtils.checkNotNull(target, VFS4JErrorMessages.TARGET_IS_NULL);
        Path sourcePath = getRealFile(source);
        return Files.copy(sourcePath, target);
    }

    @Override
    public PathName copy(PathName source, PathName target, CopyOption... options) throws IOException {
        ValidationUtils.checkNotNull(source, VFS4JErrorMessages.SOURCE_IS_NULL);
        ValidationUtils.checkNotNull(target, VFS4JErrorMessages.TARGET_IS_NULL);
        writeOperation(target, "copy");
        Path sourcePath = getRealFile(source);
        Path targetPath = getRealFile(target);
        Path path = Files.copy(sourcePath, targetPath, options);
        return convertFromRealPath(path).orElseThrow(throwInvalidePath(path));
    }

    @Override
    public PathName move(PathName source, PathName target, CopyOption... options) throws IOException {
        ValidationUtils.checkNotNull(source, VFS4JErrorMessages.SOURCE_IS_NULL);
        ValidationUtils.checkNotNull(target, VFS4JErrorMessages.TARGET_IS_NULL);
        writeOperation(source, "move");
        writeOperation(target, "move");
        Path sourcePath = getRealFile(source);
        Path targetPath = getRealFile(target);
        Path path = Files.move(sourcePath, targetPath, options);
        return convertFromRealPath(path).orElseThrow(throwInvalidePath(path));
    }

    @Override
    public PathName write(PathName target, byte[] bytes, OpenOption... options) throws IOException {
        ValidationUtils.checkNotNull(target, VFS4JErrorMessages.TARGET_IS_NULL);
        ValidationUtils.checkNotNull(bytes, "bytes is null");
        writeOperation(target, "target");
        Path sourcePath = getRealFile(target);
        Path path = Files.write(sourcePath, bytes, options);
        return convertFromRealPath(path).orElseThrow(throwInvalidePath(path));
    }

    @Override
    public PathName write(PathName target, Iterable<? extends CharSequence> lines, Charset cs, OpenOption... options) throws IOException {
        ValidationUtils.checkNotNull(target, VFS4JErrorMessages.TARGET_IS_NULL);
        ValidationUtils.checkNotNull(lines, "lines is null");
        ValidationUtils.checkNotNull(cs, "cs is null");
        writeOperation(target, "target");
        Path sourcePath = getRealFile(target);
        Path path = Files.write(sourcePath, lines, cs, options);
        return convertFromRealPath(path).orElseThrow(throwInvalidePath(path));
    }

    private Supplier<VFS4JInvalidPathException> throwInvalidePath(Path pathRes) {
        return () -> new VFS4JInvalidPathException(VFS4JErrorMessages.INVALIDE_PATH, pathRes);
    }
}
