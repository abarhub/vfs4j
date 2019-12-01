package org.vfs.core.api.operation;

import org.vfs.core.api.AbstractOperation;
import org.vfs.core.api.FileManager;
import org.vfs.core.api.PathName;
import org.vfs.core.exception.VFSInvalidPathException;
import org.vfs.core.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.*;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class Attribute extends AbstractOperation {

    public Attribute(FileManager fileManager) {
        super(fileManager);
    }

    public PathName setAttribute(PathName path, String attribute, Object value, LinkOption... options) throws IOException {
        ValidationUtils.checkNotNull(path,"Path is null");
        ValidationUtils.checkNotEmpty(attribute,"attribute is empty");
        Path p=getRealFile(path);
        Path pathRes= Files.setAttribute(p, attribute,value, options);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    public PathName setLastModifiedTime(PathName path, FileTime time) throws IOException {
        ValidationUtils.checkNotNull(path,"Path is null");
        ValidationUtils.checkNotNull(time,"time is null");
        Path p=getRealFile(path);
        Path pathRes= Files.setLastModifiedTime(p, time);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    public PathName setOwner(PathName path, UserPrincipal userPrincipal) throws IOException {
        ValidationUtils.checkNotNull(path,"Path is null");
        ValidationUtils.checkNotNull(userPrincipal,"userPrincipal is null");
        Path p=getRealFile(path);
        Path pathRes= Files.setOwner(p, userPrincipal);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    public PathName setPosixFilePermissions(PathName path, Set<PosixFilePermission> posixFilePermissions) throws IOException {
        ValidationUtils.checkNotNull(path,"Path is null");
        ValidationUtils.checkNotNull(posixFilePermissions,"posixFilePermissions is null");
        Path p=getRealFile(path);
        Path pathRes= Files.setPosixFilePermissions(p, posixFilePermissions);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    public Object getAttribute(PathName path, String attribute, LinkOption... options) throws IOException {
        ValidationUtils.checkNotNull(path,"Path is null");
        ValidationUtils.checkNotEmpty(attribute,"attribute is empty");
        Path p=getRealFile(path);
        return Files.getAttribute(p, attribute, options);
    }

    public <T extends FileAttributeView> T getFileAttributeView(PathName path, Class<T> type, LinkOption... options) throws IOException {
        ValidationUtils.checkNotNull(path,"Path is null");
        Path p=getRealFile(path);
        return Files.getFileAttributeView(p, type, options);
    }

    public FileTime getLastModifiedTime(PathName path, LinkOption... options) throws IOException {
        ValidationUtils.checkNotNull(path,"Path is null");
        Path p=getRealFile(path);
        return Files.getLastModifiedTime(p, options);
    }

    public UserPrincipal getOwner(PathName path, LinkOption... options) throws IOException {
        ValidationUtils.checkNotNull(path,"Path is null");
        Path p=getRealFile(path);
        return Files.getOwner(p, options);
    }

    public Set<PosixFilePermission> getPosixFilePermissions(PathName path, LinkOption... options) throws IOException {
        ValidationUtils.checkNotNull(path,"Path is null");
        Path p=getRealFile(path);
        return Files.getPosixFilePermissions(p, options);
    }

    public boolean isExecutable(PathName path) throws IOException {
        ValidationUtils.checkNotNull(path,"Path is null");
        Path p=getRealFile(path);
        return Files.isExecutable(p);
    }

    public boolean isReadable(PathName path) throws IOException {
        ValidationUtils.checkNotNull(path,"Path is null");
        Path p=getRealFile(path);
        return Files.isReadable(p);
    }

    public boolean isHidden(PathName path) throws IOException {
        ValidationUtils.checkNotNull(path,"Path is null");
        Path p=getRealFile(path);
        return Files.isHidden(p);
    }

    public boolean isWritable(PathName file) {
        ValidationUtils.checkNotNull(file,"Path is null");
        Path p=getRealFile(file);
        return Files.isWritable(p);
    }

    public Map<String, Object> readAttributes(PathName file, String attribute, LinkOption... options) throws IOException {
        ValidationUtils.checkNotNull(file,"Path is null");
        ValidationUtils.checkNotEmpty(attribute,"attribute is empty");
        Path p=getRealFile(file);
        return Files.readAttributes(p, attribute, options);
    }

    public <T extends BasicFileAttributes> T readAttributes(PathName file, Class<T> type, LinkOption... options) throws IOException {
        ValidationUtils.checkNotNull(file,"Path is null");
        ValidationUtils.checkNotNull(type,"type is null");
        Path p=getRealFile(file);
        return Files.readAttributes(p, type, options);
    }

    private Supplier<VFSInvalidPathException> throwInvalidePath(Path path) {
        return () -> new VFSInvalidPathException("Invalide Path", path);
    }
}
