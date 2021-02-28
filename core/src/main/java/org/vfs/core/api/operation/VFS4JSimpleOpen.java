package org.vfs.core.api.operation;

import org.vfs.core.api.VFS4JAbstractOperation;
import org.vfs.core.api.VFS4JFileManager;
import org.vfs.core.api.VFS4JPathName;
import org.vfs.core.exception.VFS4JInvalidPathException;
import org.vfs.core.util.VFS4JDirectoryStreamPathName;
import org.vfs.core.util.VFS4JErrorMessages;
import org.vfs.core.util.VFS4JValidationUtils;

import java.io.*;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.util.Optional;
import java.util.Set;

public class VFS4JSimpleOpen extends VFS4JAbstractOperation implements VFS4JOpen {

    public VFS4JSimpleOpen(VFS4JFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public InputStream newInputStream(VFS4JPathName pathName, OpenOption... options) throws IOException {
        VFS4JValidationUtils.checkNotNull(pathName, VFS4JErrorMessages.PATH_IS_NULL);
        Path path = getRealFile(pathName);
        return Files.newInputStream(path, options);
    }

    @Override
    public OutputStream newOutputStream(VFS4JPathName VFS4JPathName, OpenOption... options) throws IOException {
        VFS4JValidationUtils.checkNotNull(VFS4JPathName, VFS4JErrorMessages.PATH_IS_NULL);
        writeOperation(VFS4JPathName, "newOutputStream");
        Path path = getRealFile(VFS4JPathName);
        return Files.newOutputStream(path, options);
    }

    @Override
    public FileReader newReader(VFS4JPathName VFS4JPathName) throws IOException {
        VFS4JValidationUtils.checkNotNull(VFS4JPathName, VFS4JErrorMessages.PATH_IS_NULL);
        Path path = getRealFile(VFS4JPathName);
        // TODO: voir comment gérer l'encodage des caractères
        return new FileReader(path.toFile());
    }

    @Override
    public FileWriter newWriter(VFS4JPathName VFS4JPathName, boolean append) throws IOException {
        VFS4JValidationUtils.checkNotNull(VFS4JPathName, VFS4JErrorMessages.PATH_IS_NULL);
        writeOperation(VFS4JPathName, "newWriter");
        Path path = getRealFile(VFS4JPathName);
        // TODO: voir comment gérer l'encodage des caractères
        return new FileWriter(path.toFile(), append);
    }

    @Override
    public SeekableByteChannel newByteChannel(VFS4JPathName VFS4JPathName, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        VFS4JValidationUtils.checkNotNull(VFS4JPathName, VFS4JErrorMessages.PATH_IS_NULL);
        if (options != null && options.contains(StandardOpenOption.WRITE)) {
            writeOperation(VFS4JPathName, "newByteChannel");
        }
        Path path = getRealFile(VFS4JPathName);
        return Files.newByteChannel(path, options, attrs);
    }

    @Override
    public DirectoryStream<VFS4JPathName> newDirectoryStream(VFS4JPathName VFS4JPathName, DirectoryStream.Filter<? super VFS4JPathName> filter) throws IOException {
        VFS4JValidationUtils.checkNotNull(VFS4JPathName, VFS4JErrorMessages.PATH_IS_NULL);
        Path path = getRealFile(VFS4JPathName);
        DirectoryStream.Filter<? super Path> filter2 = null;
        if (filter != null) {
            filter2 = (p) -> {
                Optional<VFS4JPathName> res = convertFromRealPath(p);
                if (res.isPresent()) {
                    return filter.accept(res.get());
                } else {
                    throw new VFS4JInvalidPathException(VFS4JErrorMessages.INVALIDE_PATH, p);
                }
            };
        }
        final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path, filter2);
        return new VFS4JDirectoryStreamPathName(directoryStream, getFileManager());
    }
}
