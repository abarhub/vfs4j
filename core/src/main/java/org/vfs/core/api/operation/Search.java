package org.vfs.core.api.operation;

import org.vfs.core.api.AbstractOperation;
import org.vfs.core.api.FileManager;
import org.vfs.core.api.PathName;
import org.vfs.core.exception.VFSInvalidPathException;
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

public class Search extends AbstractOperation {

    public Search(FileManager fileManager) {
        super(fileManager);
    }

    public Stream<PathName> list(PathName file) throws IOException {
        ValidationUtils.checkNotNull(file,"Path is null");
        Path p=getRealFile(file);
        return Files.list(p)
                .map(x -> getFileManager()
                        .convertFromRealPath(x)
                        .orElseThrow(throwInvalidePath(x)));
    }

    public Stream<PathName> walk(PathName file, int maxDepth, FileVisitOption... options) throws IOException {
        ValidationUtils.checkNotNull(file,"Path is null");
        Path p=getRealFile(file);
        return Files.walk(p, maxDepth, options)
                .map(x -> getFileManager()
                        .convertFromRealPath(x)
                        .orElseThrow(throwInvalidePath(x)));
    }

    public Stream<PathName> walk(PathName file, FileVisitOption... options) throws IOException {
        ValidationUtils.checkNotNull(file,"Path is null");
        Path p=getRealFile(file);
        return Files.walk(p, options)
                .map(x -> getFileManager()
                        .convertFromRealPath(x)
                        .orElseThrow(throwInvalidePath(x)));
    }

    public Stream<PathName> find(PathName file, int maxDepth,
                                 BiPredicate<PathName, BasicFileAttributes> matcher, FileVisitOption... options) throws IOException {
        ValidationUtils.checkNotNull(file,"Path is null");
        Path p=getRealFile(file);
        BiPredicate<Path, BasicFileAttributes> matcher2=(path, attr)->{
            Optional<PathName> p2 = getFileManager().convertFromRealPath(path);
            if(p2.isPresent()){
                return matcher.test(p2.get(),attr);
            } else {
                throw new VFSInvalidPathException("Invalide Path", path);
            }
        };
        return Files.find(p, maxDepth,matcher2, options)
                .map(x -> getFileManager()
                        .convertFromRealPath(x)
                        .orElseThrow(throwInvalidePath(x)));
    }

    private Supplier<VFSInvalidPathException> throwInvalidePath(Path pathRes) {
        return () -> new VFSInvalidPathException("Invalide Path", pathRes);
    }
}
