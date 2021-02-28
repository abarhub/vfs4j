package org.vfs.core.api.operation;

import org.vfs.core.api.VFS4JAbstractOperation;
import org.vfs.core.api.VFS4JFileManager;
import org.vfs.core.api.VFS4JPathName;
import org.vfs.core.util.VFS4JErrorMessages;
import org.vfs.core.util.VFS4JValidationUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class VFS4JSimpleQuery extends VFS4JAbstractOperation implements VFS4JQuery {

    public VFS4JSimpleQuery(VFS4JFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public boolean exists(VFS4JPathName file, LinkOption... options) {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        return Files.exists(p, options);
    }

    @Override
    public boolean isDirectory(VFS4JPathName file, LinkOption... options) {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        return Files.isDirectory(p, options);
    }

    @Override
    public boolean isRegularFile(VFS4JPathName file, LinkOption... options) {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        return Files.isRegularFile(p, options);
    }

    @Override
    public boolean isSameFile(VFS4JPathName file, VFS4JPathName file2) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        VFS4JValidationUtils.checkNotNull(file2, "Path2 is null");
        Path p = getRealFile(file);
        Path p2 = getRealFile(file2);
        return Files.isSameFile(p, p2);
    }

    @Override
    public boolean isSymbolicLink(VFS4JPathName file) {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        return Files.isSymbolicLink(p);
    }

    @Override
    public Stream<String> lines(VFS4JPathName file) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        return Files.lines(p);
    }

    @Override
    public Stream<String> lines(VFS4JPathName file, Charset charsets) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        return Files.lines(p, charsets);
    }

    @Override
    public boolean notExists(VFS4JPathName file, LinkOption... options) {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        return Files.notExists(p, options);
    }

    @Override
    public byte[] readAllBytes(VFS4JPathName file) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        return Files.readAllBytes(p);
    }

    @Override
    public List<String> readAllLines(VFS4JPathName file) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        return Files.readAllLines(p);
    }

    @Override
    public List<String> readAllLines(VFS4JPathName file, Charset charset) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        return Files.readAllLines(p, charset);
    }

    @Override
    public long size(VFS4JPathName file) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        return Files.size(p);
    }

}
