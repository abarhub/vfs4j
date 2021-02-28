package org.vfs.core.api.operation;

import org.vfs.core.api.AbstractOperation;
import org.vfs.core.api.FileManager;
import org.vfs.core.api.PathName;
import org.vfs.core.exception.VFS4JInvalidPathException;
import org.vfs.core.util.VFS4JErrorMessages;
import org.vfs.core.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SimpleSearch extends AbstractOperation implements Search {

    public SimpleSearch(FileManager fileManager) {
        super(fileManager);
    }

    @Override
    public Stream<PathName> list(PathName file) throws IOException {
        ValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        return Files.list(p)
                .map(x -> getFileManager()
                        .convertFromRealPath(x)
                        .orElseThrow(throwInvalidePath(x)));
    }

    @Override
    public Stream<PathName> walk(PathName file, int maxDepth, FileVisitOption... options) throws IOException {
        ValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        return Files.walk(p, maxDepth, options)
                .map(x -> getFileManager()
                        .convertFromRealPath(x)
                        .orElseThrow(throwInvalidePath(x)));
    }

    @Override
    public Stream<PathName> walk(PathName file, FileVisitOption... options) throws IOException {
        ValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        return Files.walk(p, options)
                .map(x -> getFileManager()
                        .convertFromRealPath(x)
                        .orElseThrow(throwInvalidePath(x)));
    }

    @Override
    public Stream<PathName> find(PathName file, int maxDepth,
                                 BiPredicate<PathName, BasicFileAttributes> matcher, FileVisitOption... options) throws IOException {
        ValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        BiPredicate<Path, BasicFileAttributes> matcher2 = (path, attr) -> {
            Optional<PathName> p2 = getFileManager().convertFromRealPath(path);
            if (p2.isPresent()) {
                return matcher.test(p2.get(), attr);
            } else {
                throw new VFS4JInvalidPathException(VFS4JErrorMessages.INVALIDE_PATH, path);
            }
        };
        return Files.find(p, maxDepth, matcher2, options)
                .map(x -> getFileManager()
                        .convertFromRealPath(x)
                        .orElseThrow(throwInvalidePath(x)));
    }

    private Supplier<VFS4JInvalidPathException> throwInvalidePath(Path pathRes) {
        return () -> new VFS4JInvalidPathException(VFS4JErrorMessages.INVALIDE_PATH, pathRes);
    }
}
