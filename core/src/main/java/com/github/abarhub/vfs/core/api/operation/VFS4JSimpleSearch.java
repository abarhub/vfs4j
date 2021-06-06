package com.github.abarhub.vfs.core.api.operation;

import com.github.abarhub.vfs.core.api.VFS4JAbstractOperation;
import com.github.abarhub.vfs.core.api.VFS4JFileManager;
import com.github.abarhub.vfs.core.api.VFS4JPathName;
import com.github.abarhub.vfs.core.exception.VFS4JInvalidPathException;
import com.github.abarhub.vfs.core.util.VFS4JErrorMessages;
import com.github.abarhub.vfs.core.util.VFS4JValidationUtils;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class VFS4JSimpleSearch extends VFS4JAbstractOperation implements VFS4JSearch {

    public VFS4JSimpleSearch(VFS4JFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public Stream<VFS4JPathName> list(VFS4JPathName file) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        return Files.list(p)
                .map(x -> getFileManager()
                        .convertFromRealPath(x)
                        .orElseThrow(throwInvalidePath(x)));
    }

    @Override
    public Stream<VFS4JPathName> walk(VFS4JPathName file, int maxDepth, FileVisitOption... options) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        return Files.walk(p, maxDepth, options)
                .map(x -> getFileManager()
                        .convertFromRealPath(x)
                        .orElseThrow(throwInvalidePath(x)));
    }

    @Override
    public Stream<VFS4JPathName> walk(VFS4JPathName file, FileVisitOption... options) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        return Files.walk(p, options)
                .map(x -> getFileManager()
                        .convertFromRealPath(x)
                        .orElseThrow(throwInvalidePath(x)));
    }

    @Override
    public Stream<VFS4JPathName> find(VFS4JPathName file, int maxDepth,
                                      BiPredicate<VFS4JPathName, BasicFileAttributes> matcher, FileVisitOption... options) throws IOException {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        Path p = getRealFile(file);
        BiPredicate<Path, BasicFileAttributes> matcher2 = (path, attr) -> {
            Optional<VFS4JPathName> p2 = getFileManager().convertFromRealPath(path);
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
