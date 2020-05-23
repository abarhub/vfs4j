package org.vfs.core.api.operation;

import org.vfs.core.api.AbstractOperation;
import org.vfs.core.api.FileManager;
import org.vfs.core.api.PathName;
import org.vfs.core.exception.VFS4JInvalidPathException;
import org.vfs.core.util.DirectoryStreamPathName;
import org.vfs.core.util.ValidationUtils;

import java.io.*;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.util.Optional;
import java.util.Set;

public class SimpleOpen extends AbstractOperation implements Open {

    public SimpleOpen(FileManager fileManager) {
        super(fileManager);
    }

    @Override
    public InputStream newInputStream(PathName pathName, OpenOption... options) throws IOException {
        ValidationUtils.checkNotNull(pathName, "Path is null");
        Path path = getRealFile(pathName);
        return Files.newInputStream(path, options);
    }

    @Override
    public OutputStream newOutputStream(PathName pathName, OpenOption... options) throws IOException {
        ValidationUtils.checkNotNull(pathName, "Path is null");
        writeOperation(pathName, "newOutputStream");
        Path path = getRealFile(pathName);
        return Files.newOutputStream(path, options);
    }

    @Override
    public FileReader newReader(PathName pathName) throws IOException {
        ValidationUtils.checkNotNull(pathName, "Path is null");
        Path path = getRealFile(pathName);
        // TODO: voir comment gérer l'encodage des caractères
        return new FileReader(path.toFile());
    }

    @Override
    public FileWriter newWriter(PathName pathName, boolean append) throws IOException {
        ValidationUtils.checkNotNull(pathName, "Path is null");
        writeOperation(pathName, "newWriter");
        Path path = getRealFile(pathName);
        // TODO: voir comment gérer l'encodage des caractères
        return new FileWriter(path.toFile(), append);
    }

    @Override
    public SeekableByteChannel newByteChannel(PathName pathName, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        ValidationUtils.checkNotNull(pathName, "Path is null");
        if (options != null && options.contains(StandardOpenOption.WRITE)) {
            writeOperation(pathName, "newByteChannel");
        }
        Path path = getRealFile(pathName);
        return Files.newByteChannel(path, options, attrs);
    }

    @Override
    public DirectoryStream<PathName> newDirectoryStream(PathName pathName, DirectoryStream.Filter<? super PathName> filter) throws IOException {
        ValidationUtils.checkNotNull(pathName, "Path is null");
        Path path = getRealFile(pathName);
        DirectoryStream.Filter<? super Path> filter2=null;
        if(filter!=null) {
            filter2 = (p) -> {
                Optional<PathName> res = convertFromRealPath(p);
                if (res.isPresent()) {
                    return filter.accept(res.get());
                } else {
                    throw new VFS4JInvalidPathException("Path invalide", p);
                }
            };
        }
        final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path, filter2);
        return new DirectoryStreamPathName(directoryStream, getFileManager());
    }
}
