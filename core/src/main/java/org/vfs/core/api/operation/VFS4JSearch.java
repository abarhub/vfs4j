package org.vfs.core.api.operation;

import org.vfs.core.api.VFS4JPathName;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public interface VFS4JSearch {
    Stream<VFS4JPathName> list(VFS4JPathName file) throws IOException;

    Stream<VFS4JPathName> walk(VFS4JPathName file, int maxDepth, FileVisitOption... options) throws IOException;

    Stream<VFS4JPathName> walk(VFS4JPathName file, FileVisitOption... options) throws IOException;

    Stream<VFS4JPathName> find(VFS4JPathName file, int maxDepth,
                               BiPredicate<VFS4JPathName, BasicFileAttributes> matcher, FileVisitOption... options) throws IOException;
}
