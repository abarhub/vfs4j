package org.vfs.core.plugin.audit.operation;

import org.vfs.core.api.VFS4JPathName;
import org.vfs.core.api.operation.VFS4JAttribute;
import org.vfs.core.plugin.audit.VFS4JAuditOperation;
import org.vfs.core.plugin.audit.VFS4JAuditPlugins;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.attribute.*;
import java.util.Map;
import java.util.Set;

public class VFS4JAuditAttribute extends VFS4JAbstractAuditOperation implements VFS4JAttribute {

    private VFS4JAttribute attribute;

    public VFS4JAuditAttribute(VFS4JAuditPlugins auditPlugins, VFS4JAttribute attribute) {
        super(auditPlugins);
        this.attribute = attribute;
    }

    @Override
    public VFS4JPathName setAttribute(VFS4JPathName path, String attribute, Object value, LinkOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.SET_ATTRIBUTE, path);
        if (active) {
            try {
                VFS4JPathName res = this.attribute.setAttribute(path, attribute, value, options);
                log("setAttribute for file {} with attribute {} to {}", path, attribute, value);
                return res;
            } catch (IOException e) {
                logError("Error for setAttribute for file {} with attribute {} to {}", e, path, attribute, value);
                throw e;
            }
        } else {
            return this.attribute.setAttribute(path, attribute, value, options);
        }
    }

    @Override
    public VFS4JPathName setLastModifiedTime(VFS4JPathName path, FileTime time) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.SET_LAST_MODIFIED_TIME, path);
        if (active) {
            try {
                VFS4JPathName res = attribute.setLastModifiedTime(path, time);
                log("setLastModifiedTime for file {} to {}", path, time);
                return res;
            } catch (IOException e) {
                logError("Error for setLastModifiedTime for file {} to {}", e, path, time);
                throw e;
            }
        } else {
            return attribute.setLastModifiedTime(path, time);
        }
    }

    @Override
    public VFS4JPathName setOwner(VFS4JPathName path, UserPrincipal userPrincipal) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.SET_OWNER, path);
        if (active) {
            try {
                VFS4JPathName res = attribute.setOwner(path, userPrincipal);
                log("setOwner for file {} to {}", path, userPrincipal);
                return res;
            } catch (IOException e) {
                logError("Error for setOwner for file {} to {}", e, path, userPrincipal);
                throw e;
            }
        } else {
            return attribute.setOwner(path, userPrincipal);
        }
    }

    @Override
    public VFS4JPathName setPosixFilePermissions(VFS4JPathName path, Set<PosixFilePermission> posixFilePermissions) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.SET_POSIX_FILE_PERMISSIONS, path);
        if (active) {
            try {
                VFS4JPathName res = attribute.setPosixFilePermissions(path, posixFilePermissions);
                log("setPosixFilePermissions for file {} to {}", path, posixFilePermissions);
                return res;
            } catch (IOException e) {
                logError("Error for setPosixFilePermissions for file {} to {}", e, path, posixFilePermissions);
                throw e;
            }
        } else {
            return attribute.setPosixFilePermissions(path, posixFilePermissions);
        }
    }

    @Override
    public Object getAttribute(VFS4JPathName path, String attribute, LinkOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.GET_ATTRIBUTE, path);
        if (active) {
            try {
                Object res = this.attribute.getAttribute(path, attribute, options);
                log("getAttribute for file {} for {} with options {}", path, attribute, options);
                return res;
            } catch (IOException e) {
                logError("Error for getAttribute for file {} for {} with options {}", e, path, attribute, options);
                throw e;
            }
        } else {
            return this.attribute.getAttribute(path, attribute, options);
        }
    }

    @Override
    public <T extends FileAttributeView> T getFileAttributeView(VFS4JPathName path, Class<T> type, LinkOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.GET_FILE_ATTRIBUTE_VIEW, path);
        if (active) {
            try {
                T res = attribute.getFileAttributeView(path, type, options);
                log("getFileAttributeView for file {} for {} with options {}", path, type, options);
                return res;
            } catch (IOException e) {
                logError("Error for getFileAttributeView for file {} for {} with options {}", e, path, type, options);
                throw e;
            }
        } else {
            return attribute.getFileAttributeView(path, type, options);
        }
    }

    @Override
    public FileTime getLastModifiedTime(VFS4JPathName path, LinkOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.GET_LAST_MODIFIED_TIME, path);
        if (active) {
            try {
                FileTime res = attribute.getLastModifiedTime(path, options);
                log("getLastModifiedTime for file {} with options {}", path, options);
                return res;
            } catch (IOException e) {
                logError("Error for getLastModifiedTime for file {} with options {}", e, path, options);
                throw e;
            }
        } else {
            return attribute.getLastModifiedTime(path, options);
        }
    }

    @Override
    public UserPrincipal getOwner(VFS4JPathName path, LinkOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.GET_OWNER, path);
        if (active) {
            try {
                UserPrincipal res = attribute.getOwner(path, options);
                log("getOwner for file {} with options {}", path, options);
                return res;
            } catch (IOException e) {
                logError("Error for getOwner for file {} with options {}", e, path, options);
                throw e;
            }
        } else {
            return attribute.getOwner(path, options);
        }
    }

    @Override
    public Set<PosixFilePermission> getPosixFilePermissions(VFS4JPathName path, LinkOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.GET_POSIX_FILE_PERMISSIONS, path);
        if (active) {
            try {
                Set<PosixFilePermission> res = attribute.getPosixFilePermissions(path, options);
                log("getPosixFilePermissions for file {} with options {}", path, options);
                return res;
            } catch (IOException e) {
                logError("Error for getPosixFilePermissions for file {} with options {}", e, path, options);
                throw e;
            }
        } else {
            return attribute.getPosixFilePermissions(path, options);
        }
    }

    @Override
    public boolean isExecutable(VFS4JPathName path) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.IS_EXECUTABLE, path);
        if (active) {
            try {
                boolean res = attribute.isExecutable(path);
                log("isExecutable for file {}", path);
                return res;
            } catch (IOException e) {
                logError("Error for isExecutable for file {}", e, path);
                throw e;
            }
        } else {
            return attribute.isExecutable(path);
        }
    }

    @Override
    public boolean isReadable(VFS4JPathName path) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.IS_READABLE, path);
        if (active) {
            try {
                boolean res = attribute.isReadable(path);
                log("isReadable for file {}", path);
                return res;
            } catch (IOException e) {
                logError("Error for isReadable for file {}", e, path);
                throw e;
            }
        } else {
            return attribute.isReadable(path);
        }
    }

    @Override
    public boolean isHidden(VFS4JPathName path) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.IS_HIDDEN, path);
        if (active) {
            try {
                boolean res = attribute.isHidden(path);
                log("isHidden for file {}", path);
                return res;
            } catch (IOException e) {
                logError("Error for isHidden for file {}", e, path);
                throw e;
            }
        } else {
            return attribute.isHidden(path);
        }
    }

    @Override
    public boolean isWritable(VFS4JPathName file) {
        boolean active = isActive(VFS4JAuditOperation.IS_WRITABLE, file);
        if (active) {
            boolean res = attribute.isWritable(file);
            log("isWritable for file {}", file);
            return res;
        } else {
            return attribute.isWritable(file);
        }
    }

    @Override
    public Map<String, Object> readAttributes(VFS4JPathName file, String attribute, LinkOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.READ_ATTRIBUTES, file);
        if (active) {
            try {
                Map<String, Object> res = this.attribute.readAttributes(file, attribute, options);
                log("readAttributes for file {} for attribute {} with options {}", file, attribute, options);
                return res;
            } catch (IOException e) {
                logError("Error for readAttributes for file {} for attribute {} with options {}", e, file, attribute, options);
                throw e;
            }
        } else {
            return this.attribute.readAttributes(file, attribute, options);
        }
    }

    @Override
    public <T extends BasicFileAttributes> T readAttributes(VFS4JPathName file, Class<T> type, LinkOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.READ_ATTRIBUTES, file);
        if (active) {
            try {
                T res = attribute.readAttributes(file, type, options);
                log("readAttributes for file {} for attribute {} with options {}", file, type, options);
                return res;
            } catch (IOException e) {
                logError("Error for readAttributes for file {} for attribute {} with options {}", e, file, type, options);
                throw e;
            }
        } else {
            return attribute.readAttributes(file, type, options);
        }
    }
}
