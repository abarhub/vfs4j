package org.vfs.core.api.operation;

import org.vfs.core.api.AbstractOperation;
import org.vfs.core.api.FileManager;
import org.vfs.core.api.PathName;
import org.vfs.core.util.ValidationUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class SimpleQuery extends AbstractOperation implements Query {

    public SimpleQuery(FileManager fileManager) {
        super(fileManager);
    }

    @Override
    public boolean exists(PathName file, LinkOption... options) {
        ValidationUtils.checkNotNull(file, "Path is null");
        Path p = getRealFile(file);
        return Files.exists(p, options);
    }

    @Override
    public boolean isDirectory(PathName file, LinkOption... options) {
        ValidationUtils.checkNotNull(file, "Path is null");
        Path p = getRealFile(file);
        return Files.isDirectory(p, options);
    }

    @Override
    public boolean isRegularFile(PathName file, LinkOption... options) {
        ValidationUtils.checkNotNull(file, "Path is null");
        Path p = getRealFile(file);
        return Files.isRegularFile(p, options);
    }

    @Override
    public boolean isSameFile(PathName file, PathName file2) throws IOException {
        ValidationUtils.checkNotNull(file, "Path is null");
        ValidationUtils.checkNotNull(file2, "Path2 is null");
        Path p = getRealFile(file);
        Path p2 = getRealFile(file2);
        return Files.isSameFile(p, p2);
    }

    @Override
    public boolean isSymbolicLink(PathName file) {
        ValidationUtils.checkNotNull(file, "Path is null");
        Path p = getRealFile(file);
        return Files.isSymbolicLink(p);
    }

    @Override
    public Stream<String> lines(PathName file) throws IOException {
        ValidationUtils.checkNotNull(file, "Path is null");
        Path p = getRealFile(file);
        return Files.lines(p);
    }

    @Override
    public Stream<String> lines(PathName file, Charset charsets) throws IOException {
        ValidationUtils.checkNotNull(file, "Path is null");
        Path p = getRealFile(file);
        return Files.lines(p, charsets);
    }

    @Override
    public boolean notExists(PathName file, LinkOption... options) {
        ValidationUtils.checkNotNull(file, "Path is null");
        Path p = getRealFile(file);
        return Files.notExists(p, options);
    }

    @Override
    public byte[] readAllBytes(PathName file) throws IOException {
        ValidationUtils.checkNotNull(file, "Path is null");
        Path p = getRealFile(file);
        return Files.readAllBytes(p);
    }

    @Override
    public List<String> readAllLines(PathName file) throws IOException {
        ValidationUtils.checkNotNull(file, "Path is null");
        Path p = getRealFile(file);
        return Files.readAllLines(p);
    }

    @Override
    public List<String> readAllLines(PathName file, Charset charset) throws IOException {
        ValidationUtils.checkNotNull(file, "Path is null");
        Path p = getRealFile(file);
        return Files.readAllLines(p, charset);
    }

    @Override
    public long size(PathName file) throws IOException {
        ValidationUtils.checkNotNull(file, "Path is null");
        Path p = getRealFile(file);
        return Files.size(p);
    }

}
