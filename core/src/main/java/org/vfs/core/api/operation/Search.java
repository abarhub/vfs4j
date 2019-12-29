package org.vfs.core.api.operation;

import org.vfs.core.api.PathName;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public interface Search {
    Stream<PathName> list(PathName file) throws IOException;

    Stream<PathName> walk(PathName file, int maxDepth, FileVisitOption... options) throws IOException;

    Stream<PathName> walk(PathName file, FileVisitOption... options) throws IOException;

    Stream<PathName> find(PathName file, int maxDepth,
                          BiPredicate<PathName, BasicFileAttributes> matcher, FileVisitOption... options) throws IOException;
}
