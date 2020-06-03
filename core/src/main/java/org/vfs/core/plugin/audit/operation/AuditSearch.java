package org.vfs.core.plugin.audit.operation;

import org.vfs.core.api.PathName;
import org.vfs.core.api.operation.Search;
import org.vfs.core.plugin.audit.AuditOperation;
import org.vfs.core.plugin.audit.VFS4JAuditPlugins;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class AuditSearch extends AbstractAuditOperation implements Search {

    private Search search;

    public AuditSearch(VFS4JAuditPlugins vfs4JAuditPlugins, Search search) {
        super(vfs4JAuditPlugins);
        this.search = search;
    }

    @Override
    public Stream<PathName> list(PathName file) throws IOException {
        boolean active = isActive(AuditOperation.LIST, file);
        if (active) {
            try {
                Stream<PathName> res = search.list(file);
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
    public Stream<PathName> walk(PathName file, int maxDepth, FileVisitOption... options) throws IOException {
        boolean active = isActive(AuditOperation.WALK, file);
        if (active) {
            try {
                Stream<PathName> res = search.walk(file, maxDepth, options);
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
    public Stream<PathName> walk(PathName file, FileVisitOption... options) throws IOException {
        boolean active = isActive(AuditOperation.WALK, file);
        if (active) {
            try {
                Stream<PathName> res = search.walk(file, options);
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
    public Stream<PathName> find(PathName file, int maxDepth, BiPredicate<PathName, BasicFileAttributes> matcher, FileVisitOption... options) throws IOException {
        boolean active = isActive(AuditOperation.FIND, file);
        if (active) {
            try {
                Stream<PathName> res = search.find(file, maxDepth, matcher, options);
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
