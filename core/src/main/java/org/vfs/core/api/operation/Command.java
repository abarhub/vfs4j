package org.vfs.core.api.operation;

import org.vfs.core.api.AbstractOperation;
import org.vfs.core.api.FileManager;
import org.vfs.core.api.PathName;
import org.vfs.core.exception.VFSInvalidPathException;
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

public class Command extends AbstractOperation {

    public Command(FileManager fileManager) {
        super(fileManager);
    }

    public void createFile(PathName file, FileAttribute<?>... attrs) throws IOException {
        ValidationUtils.checkNotNull(file,"Path is null");
        Path p=getRealFile(file);
        Files.createFile(p, attrs);
    }

    public void createDirectory(PathName file, FileAttribute<?>... attrs) throws IOException {
        ValidationUtils.checkNotNull(file,"Path is null");
        Path p=getRealFile(file);
        Files.createDirectory(p, attrs);
    }

    public void createDirectories(PathName file, FileAttribute<?>... attrs) throws IOException {
        ValidationUtils.checkNotNull(file,"Path is null");
        Path p=getRealFile(file);
        Files.createDirectories(p, attrs);
    }

    public void delete(PathName file) throws IOException {
        ValidationUtils.checkNotNull(file,"Path is null");
        Path p=getRealFile(file);
        Files.delete(p);
    }

    public boolean deleteIfExists(PathName file) throws IOException {
        ValidationUtils.checkNotNull(file,"Path is null");
        Path p=getRealFile(file);
        return Files.deleteIfExists(p);
    }

    public PathName createLink(PathName file, PathName target) throws IOException {
        ValidationUtils.checkNotNull(file,"Path is null");
        ValidationUtils.checkNotNull(file,"target is null");
        Path p=getRealFile(file);
        Path targetPath=getRealFile(target);
        Path pathRes=Files.createLink(p, targetPath);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    public PathName createSymbolicLink(PathName link, PathName target, FileAttribute<?>... attrs) throws IOException {
        ValidationUtils.checkNotNull(link,"Path is null");
        ValidationUtils.checkNotNull(target,"target is null");
        Path p=getRealFile(link);
        Path targetPath=getRealFile(target);
        Path pathRes=Files.createSymbolicLink(p, targetPath, attrs);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    public long copy(InputStream input, PathName target, CopyOption... options) throws IOException {
        ValidationUtils.checkNotNull(input,"input is null");
        ValidationUtils.checkNotNull(target,"target is null");
        Path targetPath=getRealFile(target);
        return Files.copy(input, targetPath, options);
    }

    public long copy(PathName source, OutputStream out) throws IOException {
        ValidationUtils.checkNotNull(source,"source is null");
        ValidationUtils.checkNotNull(out,"out is null");
        Path sourcePath=getRealFile(source);
        return Files.copy(sourcePath, out);
    }

    public PathName copy(PathName source, PathName target, CopyOption... options) throws IOException {
        ValidationUtils.checkNotNull(source,"source is null");
        ValidationUtils.checkNotNull(target,"target is null");
        Path sourcePath=getRealFile(source);
        Path targetPath=getRealFile(target);
        Path path = Files.copy(sourcePath, targetPath, options);
        return convertFromRealPath(path).orElseThrow(throwInvalidePath(path));
    }

    public PathName move(PathName source, PathName target, CopyOption... options) throws IOException {
        ValidationUtils.checkNotNull(source,"source is null");
        ValidationUtils.checkNotNull(target,"target is null");
        Path sourcePath=getRealFile(source);
        Path targetPath=getRealFile(target);
        Path path = Files.move(sourcePath, targetPath, options);
        return convertFromRealPath(path).orElseThrow(throwInvalidePath(path));
    }

    public PathName write(PathName source, byte[] bytes, OpenOption... options) throws IOException {
        ValidationUtils.checkNotNull(source,"source is null");
        ValidationUtils.checkNotNull(bytes,"bytes is null");
        Path sourcePath=getRealFile(source);
        Path path = Files.write(sourcePath, bytes, options);
        return convertFromRealPath(path).orElseThrow(throwInvalidePath(path));
    }

    public PathName write(PathName source, Iterable<? extends CharSequence> lines, Charset cs, OpenOption... options) throws IOException {
        ValidationUtils.checkNotNull(source,"source is null");
        ValidationUtils.checkNotNull(lines,"lines is null");
        ValidationUtils.checkNotNull(cs,"cs is null");
        Path sourcePath=getRealFile(source);
        Path path = Files.write(sourcePath, lines, cs, options);
        return convertFromRealPath(path).orElseThrow(throwInvalidePath(path));
    }

    private Supplier<VFSInvalidPathException> throwInvalidePath(Path pathRes) {
        return () -> new VFSInvalidPathException("Invalide Path", pathRes);
    }
}
