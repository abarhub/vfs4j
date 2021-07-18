package io.github.abarhub.vfs.core.api.operation;

import io.github.abarhub.vfs.core.api.VFS4JAbstractOperation;
import io.github.abarhub.vfs.core.api.VFS4JFileManager;
import io.github.abarhub.vfs.core.api.VFS4JPathName;
import io.github.abarhub.vfs.core.exception.VFS4JInvalidPathException;
import io.github.abarhub.vfs.core.util.VFS4JErrorMessages;
import io.github.abarhub.vfs.core.util.VFS4JValidationUtils;

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

public class VFS4JSimpleCommand extends VFS4JAbstractOperation implements VFS4JCommand {

    public VFS4JSimpleCommand(VFS4JFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public VFS4JPathName createFile(VFS4JPathName file, FileAttribute<?>... attrs) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        writeOperation(file, "createFile");
        Path p = getRealFile(file);
        Path pathRes = Files.createFile(p, attrs);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    @Override
    public VFS4JPathName createDirectory(VFS4JPathName file, FileAttribute<?>... attrs) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        writeOperation(file, "createDirectory");
        Path p = getRealFile(file);
        Path pathRes = Files.createDirectory(p, attrs);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    @Override
    public VFS4JPathName createDirectories(VFS4JPathName file, FileAttribute<?>... attrs) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        writeOperation(file, "createDirectories");
        Path p = getRealFile(file);
        Path pathRes = Files.createDirectories(p, attrs);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    @Override
    public void delete(VFS4JPathName file) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        writeOperation(file, "delete");
        Path p = getRealFile(file);
        Files.delete(p);
    }

    @Override
    public boolean deleteIfExists(VFS4JPathName file) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        writeOperation(file, "deleteIfExists");
        Path p = getRealFile(file);
        return Files.deleteIfExists(p);
    }

    @Override
    public VFS4JPathName createLink(VFS4JPathName file, VFS4JPathName target) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.TARGET_IS_NULL);
        writeOperation(file, "createLink");
        Path p = getRealFile(file);
        Path targetPath = getRealFile(target);
        Path pathRes = Files.createLink(p, targetPath);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    @Override
    public VFS4JPathName createSymbolicLink(VFS4JPathName link, VFS4JPathName target, FileAttribute<?>... attrs) throws IOException {
        VFS4JValidationUtils.checkNotNull(link, VFS4JErrorMessages.PATH_IS_NULL);
        VFS4JValidationUtils.checkNotNull(target, VFS4JErrorMessages.TARGET_IS_NULL);
        writeOperation(link, "createSymbolicLink");
        Path p = getRealFile(link);
        Path targetPath = getRealFile(target);
        Path pathRes = Files.createSymbolicLink(p, targetPath, attrs);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    @Override
    public long copy(InputStream input, VFS4JPathName target, CopyOption... options) throws IOException {
        VFS4JValidationUtils.checkNotNull(input, VFS4JErrorMessages.SOURCE_IS_NULL);
        VFS4JValidationUtils.checkNotNull(target, VFS4JErrorMessages.TARGET_IS_NULL);
        writeOperation(target, "copy");
        Path targetPath = getRealFile(target);
        return Files.copy(input, targetPath, options);
    }

    @Override
    public long copy(VFS4JPathName source, OutputStream target) throws IOException {
        VFS4JValidationUtils.checkNotNull(source, VFS4JErrorMessages.SOURCE_IS_NULL);
        VFS4JValidationUtils.checkNotNull(target, VFS4JErrorMessages.TARGET_IS_NULL);
        Path sourcePath = getRealFile(source);
        return Files.copy(sourcePath, target);
    }

    @Override
    public VFS4JPathName copy(VFS4JPathName source, VFS4JPathName target, CopyOption... options) throws IOException {
        VFS4JValidationUtils.checkNotNull(source, VFS4JErrorMessages.SOURCE_IS_NULL);
        VFS4JValidationUtils.checkNotNull(target, VFS4JErrorMessages.TARGET_IS_NULL);
        writeOperation(target, "copy");
        Path sourcePath = getRealFile(source);
        Path targetPath = getRealFile(target);
        Path path = Files.copy(sourcePath, targetPath, options);
        return convertFromRealPath(path).orElseThrow(throwInvalidePath(path));
    }

    @Override
    public VFS4JPathName move(VFS4JPathName source, VFS4JPathName target, CopyOption... options) throws IOException {
        VFS4JValidationUtils.checkNotNull(source, VFS4JErrorMessages.SOURCE_IS_NULL);
        VFS4JValidationUtils.checkNotNull(target, VFS4JErrorMessages.TARGET_IS_NULL);
        writeOperation(source, "move");
        writeOperation(target, "move");
        Path sourcePath = getRealFile(source);
        Path targetPath = getRealFile(target);
        Path path = Files.move(sourcePath, targetPath, options);
        return convertFromRealPath(path).orElseThrow(throwInvalidePath(path));
    }

    @Override
    public VFS4JPathName write(VFS4JPathName target, byte[] bytes, OpenOption... options) throws IOException {
        VFS4JValidationUtils.checkNotNull(target, VFS4JErrorMessages.TARGET_IS_NULL);
        VFS4JValidationUtils.checkNotNull(bytes, "bytes is null");
        writeOperation(target, "target");
        Path sourcePath = getRealFile(target);
        Path path = Files.write(sourcePath, bytes, options);
        return convertFromRealPath(path).orElseThrow(throwInvalidePath(path));
    }

    @Override
    public VFS4JPathName write(VFS4JPathName target, Iterable<? extends CharSequence> lines, Charset cs, OpenOption... options) throws IOException {
        VFS4JValidationUtils.checkNotNull(target, VFS4JErrorMessages.TARGET_IS_NULL);
        VFS4JValidationUtils.checkNotNull(lines, "lines is null");
        VFS4JValidationUtils.checkNotNull(cs, "cs is null");
        writeOperation(target, "target");
        Path sourcePath = getRealFile(target);
        Path path = Files.write(sourcePath, lines, cs, options);
        return convertFromRealPath(path).orElseThrow(throwInvalidePath(path));
    }

    private Supplier<VFS4JInvalidPathException> throwInvalidePath(Path pathRes) {
        return () -> new VFS4JInvalidPathException(VFS4JErrorMessages.INVALIDE_PATH, pathRes);
    }
}
