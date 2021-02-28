package org.vfs.core.plugin.audit.operation;

import org.vfs.core.api.VFS4JPathName;
import org.vfs.core.api.operation.VFS4JSearch;
import org.vfs.core.plugin.audit.VFS4JAuditOperation;
import org.vfs.core.plugin.audit.VFS4JAuditPlugins;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class VFS4JAuditSearch extends VFS4JAbstractAuditOperation implements VFS4JSearch {

    private VFS4JSearch search;

    public VFS4JAuditSearch(VFS4JAuditPlugins vfs4JAuditPlugins, VFS4JSearch search) {
        super(vfs4JAuditPlugins);
        this.search = search;
    }

    @Override
    public Stream<VFS4JPathName> list(VFS4JPathName file) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.LIST, file);
        if (active) {
            try {
                Stream<VFS4JPathName> res = search.list(file);
                log("list for file {}", file);
                return res;
            } catch (IOException e) {
                logError("Error for list for file {}", e, file);
                throw e;
            }
        } else {
            return search.list(file);
        }
    }

    @Override
    public Stream<VFS4JPathName> walk(VFS4JPathName file, int maxDepth, FileVisitOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.WALK, file);
        if (active) {
            try {
                Stream<VFS4JPathName> res = search.walk(file, maxDepth, options);
                log("walk for file {}", file);
                return res;
            } catch (IOException e) {
                logError("Error for walk for file {}", e, file);
                throw e;
            }
        } else {
            return search.walk(file, maxDepth, options);
        }
    }

    @Override
    public Stream<VFS4JPathName> walk(VFS4JPathName file, FileVisitOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.WALK, file);
        if (active) {
            try {
                Stream<VFS4JPathName> res = search.walk(file, options);
                log("walk for file {}", file);
                return res;
            } catch (IOException e) {
                logError("Error for walk for file {}", e, file);
                throw e;
            }
        } else {
            return search.walk(file, options);
        }
    }

    @Override
    public Stream<VFS4JPathName> find(VFS4JPathName file, int maxDepth, BiPredicate<VFS4JPathName, BasicFileAttributes> matcher, FileVisitOption... options) throws IOException {
        boolean active = isActive(VFS4JAuditOperation.FIND, file);
        if (active) {
            try {
                Stream<VFS4JPathName> res = search.find(file, maxDepth, matcher, options);
                log("find for file {}", file);
                return res;
            } catch (IOException e) {
                logError("Error for find for file {}", e, file);
                throw e;
            }
        } else {
            return search.find(file, maxDepth, matcher, options);
        }
    }
}
