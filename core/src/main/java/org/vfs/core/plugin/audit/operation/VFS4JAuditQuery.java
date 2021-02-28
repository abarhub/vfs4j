package org.vfs.core.plugin.audit.operation;

import org.vfs.core.api.VFS4JPathName;
import org.vfs.core.api.operation.VFS4JQuery;
import org.vfs.core.plugin.audit.VFS4JAuditOperation;
import org.vfs.core.plugin.audit.VFS4JAuditPlugins;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.LinkOption;
import java.util.List;
import java.util.stream.Stream;

public class VFS4JAuditQuery extends VFS4JAbstractAuditOperation implements VFS4JQuery {

    private VFS4JQuery query;

    public VFS4JAuditQuery(VFS4JAuditPlugins vfs4JAuditPlugins, VFS4JQuery query) {
        super(vfs4JAuditPlugins);
        this.query = query;
    }

    @Override
    public boolean exists(VFS4JPathName file, LinkOption... options) {
        boolean active = isActive(VFS4JAuditOperation.EXISTS, file);
        if (active) {
            try {
                boolean res = query.exists(file, options);
                log("exists for file {}", file);
                return res;
            } catch (Exception e) {
                logError("Error for exists for file {}", e, file);
                throw e;
            }
        } else {
            return query.exists(file, options);
        }
    }

    @Override
    public boolean isDirectory(VFS4JPathName file, LinkOption... options) {
        boolean active = isActive(VFS4JAuditOperation.IS_DIRECTORY, file);
        if (active) {
            try {
                boolean res = query.isDirectory(file, options);
                log("isDirectory for file {}", file);
                return res;
            } catch (Exception e) {
                logError("Error for isDirectory for file {}", e, file);
                throw e;
            }
        } else {
            return query.isDirectory(file, options);
        }
    }

    @Override
    public boolean isRegularFile(VFS4JPathName file, LinkOption... options) {
        boolean active = isActive(VFS4JAuditOperation.IS_REGULAR_FILE, file);
        if (active) {
            try {
                boolean res = query.isRegularFile(file, options);
                log("isRegularFile for file {}", file);
                return res;
            } catch (Exception e) {
                logError("Error for isRegularFile for file {}", e, file);
                throw e;
            }
        } else {
            return query.isRegularFile(file, options);
        }
    }

    @Override
    public boolean isSameFile(VFS4JPathName file, VFS4JPathName file2) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.IS_SAME_FILE, file, file2);
        if (active) {
            try {
                boolean res = query.isSameFile(file, file2);
                log("isSameFile for file {} and {}", file, file2);
                return res;
            } catch (IOException e) {
                logError("Error for isSameFile for file {} and {}", e, file, file2);
                throw e;
            }
        } else {
            return query.isSameFile(file, file2);
        }
    }

    @Override
    public boolean isSymbolicLink(VFS4JPathName file) {
        boolean active = isActive(VFS4JAuditOperation.IS_SYMBOLIC_LINK, file);
        if (active) {
            try {
                boolean res = query.isSymbolicLink(file);
                log("isSymbolicLink for file {}", file);
                return res;
            } catch (Exception e) {
                logError("Error for isSymbolicLink for file {}", e, file);
                throw e;
            }
        } else {
            return query.isSymbolicLink(file);
        }
    }

    @Override
    public Stream<String> lines(VFS4JPathName file) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.LINES, file);
        if (active) {
            try {
                Stream<String> res = query.lines(file);
                log("lines for file {}", file);
                return res;
            } catch (IOException e) {
                logError("Error for lines for file {}", e, file);
                throw e;
            }
        } else {
            return query.lines(file);
        }
    }

    @Override
    public Stream<String> lines(VFS4JPathName file, Charset charsets) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.LINES, file);
        if (active) {
            try {
                Stream<String> res = query.lines(file, charsets);
                log("lines for file {}", file);
                return res;
            } catch (IOException e) {
                logError("Error for lines for file {}", e, file);
                throw e;
            }
        } else {
            return query.lines(file, charsets);
        }
    }

    @Override
    public boolean notExists(VFS4JPathName file, LinkOption... options) {
        boolean active = isActive(VFS4JAuditOperation.NOT_EXISTS, file);
        if (active) {
            try {
                boolean res = query.notExists(file, options);
                log("notExists for file {}", file);
                return res;
            } catch (Exception e) {
                logError("Error for notExists for file {}", e, file);
                throw e;
            }
        } else {
            return query.notExists(file, options);
        }
    }

    @Override
    public byte[] readAllBytes(VFS4JPathName file) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.READ_ALL_BYTES, file);
        if (active) {
            try {
                byte[] res = query.readAllBytes(file);
                log("readAllBytes for file {}", file);
                return res;
            } catch (IOException e) {
                logError("Error for readAllBytes for file {}", e, file);
                throw e;
            }
        } else {
            return query.readAllBytes(file);
        }
    }

    @Override
    public List<String> readAllLines(VFS4JPathName file) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.READ_ALL_LINES, file);
        if (active) {
            try {
                List<String> res = query.readAllLines(file);
                log("readAllLines for file {}", file);
                return res;
            } catch (IOException e) {
                logError("Error for readAllLines for file {}", e, file);
                throw e;
            }
        } else {
            return query.readAllLines(file);
        }
    }

    @Override
    public List<String> readAllLines(VFS4JPathName file, Charset charset) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.READ_ALL_LINES, file);
        if (active) {
            try {
                List<String> res = query.readAllLines(file, charset);
                log("readAllLines for file {}", file);
                return res;
            } catch (IOException e) {
                logError("Error for readAllLines for file {}", e, file);
                throw e;
            }
        } else {
            return query.readAllLines(file, charset);
        }
    }

    @Override
    public long size(VFS4JPathName file) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.SIZE, file);
        if (active) {
            try {
                long res = query.size(file);
                log("size for file {}", file);
                return res;
            } catch (IOException e) {
                logError("Error for size for file {}", e, file);
                throw e;
            }
        } else {
            return query.size(file);
        }
    }
}
