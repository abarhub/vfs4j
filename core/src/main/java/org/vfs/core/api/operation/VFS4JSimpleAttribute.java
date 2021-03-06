package org.vfs.core.api.operation;

import org.vfs.core.api.VFS4JAbstractOperation;
import org.vfs.core.api.VFS4JFileManager;
import org.vfs.core.api.VFS4JPathName;
import org.vfs.core.exception.VFS4JInvalidPathException;
import org.vfs.core.util.VFS4JErrorMessages;
import org.vfs.core.util.VFS4JValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.*;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class VFS4JSimpleAttribute extends VFS4JAbstractOperation implements VFS4JAttribute {

    public VFS4JSimpleAttribute(VFS4JFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public VFS4JPathName setAttribute(VFS4JPathName path, String attribute, Object value, LinkOption... options) throws IOException {
        VFS4JValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        VFS4JValidationUtils.checkNotEmpty(attribute, VFS4JErrorMessages.ATTRIBUTE_IS_EMPTY);
        writeOperation(path, "setAttribute");
        Path p = getRealFile(path);
        Path pathRes = Files.setAttribute(p, attribute, value, options);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    @Override
    public VFS4JPathName setLastModifiedTime(VFS4JPathName path, FileTime time) throws IOException {
        VFS4JValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        VFS4JValidationUtils.checkNotNull(time, "time is null");
        writeOperation(path, "setLastModifiedTime");
        Path p = getRealFile(path);
        Path pathRes = Files.setLastModifiedTime(p, time);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    @Override
    public VFS4JPathName setOwner(VFS4JPathName path, UserPrincipal userPrincipal) throws IOException {
        VFS4JValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        VFS4JValidationUtils.checkNotNull(userPrincipal, "userPrincipal is null");
        writeOperation(path, "setOwner");
        Path p = getRealFile(path);
        Path pathRes = Files.setOwner(p, userPrincipal);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    @Override
    public VFS4JPathName setPosixFilePermissions(VFS4JPathName path, Set<PosixFilePermission> posixFilePermissions) throws IOException {
        VFS4JValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        VFS4JValidationUtils.checkNotNull(posixFilePermissions, "posixFilePermissions is null");
        writeOperation(path, "setPosixFilePermissions");
        Path p = getRealFile(path);
        Path pathRes = Files.setPosixFilePermissions(p, posixFilePermissions);
        return convertFromRealPath(pathRes).orElseThrow(throwInvalidePath(pathRes));
    }

    @Override
    public Object getAttribute(VFS4JPathName path, String attribute, LinkOption... options) throws IOException {
        VFS4JValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        VFS4JValidationUtils.checkNotEmpty(attribute, VFS4JErrorMessages.ATTRIBUTE_IS_EMPTY);
        Path p = getRealFile(path);
        return Files.getAttribute(p, attribute, options);
    }

    @Override
    public <T extends FileAttributeView> T getFileAttributeView(VFS4JPathName path, Class<T> type, LinkOption... options) throws IOException {
        VFS4JValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(path);
        return Files.getFileAttributeView(p, type, options);
    }

    @Override
    public FileTime getLastModifiedTime(VFS4JPathName path, LinkOption... options) throws IOException {
        VFS4JValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(path);
        return Files.getLastModifiedTime(p, options);
    }

    @Override
    public UserPrincipal getOwner(VFS4JPathName path, LinkOption... options) throws IOException {
        VFS4JValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(path);
        return Files.getOwner(p, options);
    }

    @Override
    public Set<PosixFilePermission> getPosixFilePermissions(VFS4JPathName path, LinkOption... options) throws IOException {
        VFS4JValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(path);
        return Files.getPosixFilePermissions(p, options);
    }

    @Override
    public boolean isExecutable(VFS4JPathName path) throws IOException {
        VFS4JValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(path);
        return Files.isExecutable(p);
    }

    @Override
    public boolean isReadable(VFS4JPathName path) throws IOException {
        VFS4JValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(path);
        return Files.isReadable(p);
    }

    @Override
    public boolean isHidden(VFS4JPathName path) throws IOException {
        VFS4JValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(path);
        return Files.isHidden(p);
    }

    @Override
    public boolean isWritable(VFS4JPathName file) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        return Files.isWritable(p);
    }

    @Override
    public Map<String, Object> readAttributes(VFS4JPathName file, String attribute, LinkOption... options) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        VFS4JValidationUtils.checkNotEmpty(attribute, VFS4JErrorMessages.ATTRIBUTE_IS_EMPTY);
        Path p = getRealFile(file);
        return Files.readAttributes(p, attribute, options);
    }

    @Override
    public <T extends BasicFileAttributes> T readAttributes(VFS4JPathName file, Class<T> type, LinkOption... options) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        VFS4JValidationUtils.checkNotNull(type, "type is null");
        Path p = getRealFile(file);
        return Files.readAttributes(p, type, options);
    }

    private Supplier<VFS4JInvalidPathException> throwInvalidePath(Path path) {
        return () -> new VFS4JInvalidPathException("Invalide Path", path);
    }
}
