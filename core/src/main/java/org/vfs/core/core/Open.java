package org.vfs.core.core;

import org.vfs.core.util.DirectoryStreamPathName;

import java.io.*;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Set;

public class Open extends AbstractOperation {

    public Open(FileManager fileManager) {
        super(fileManager);
    }

    public InputStream newInputStream(PathName pathName, OpenOption... options) throws IOException {
        Path path=getRealFile(pathName);
        return Files.newInputStream(path, options);
    }

    public OutputStream newOutputStream(PathName pathName, OpenOption... options) throws IOException {
        Path path=getRealFile(pathName);
        return Files.newOutputStream(path, options);
    }

    public FileReader newReader(PathName pathName) throws IOException {
        Path path=getRealFile(pathName);
        // TODO: voir comment gérer l'encodage des caractères
        return new FileReader(path.toFile());
    }

    public FileWriter newWriter(PathName pathName, boolean append) throws IOException {
        Path path=getRealFile(pathName);
        // TODO: voir comment gérer l'encodage des caractères
        return new FileWriter(path.toFile(), append);
    }

    public SeekableByteChannel newByteChannel(PathName pathName, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        Path path=getRealFile(pathName);
        return Files.newByteChannel(path,options,attrs);
    }

    public DirectoryStream<PathName> newDirectoryStream(PathName pathName, DirectoryStream.Filter<? super Path> filter) throws IOException {
        Path path=getRealFile(pathName);
        final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path, filter);
        return new DirectoryStreamPathName(directoryStream, getFileManager());
    }
}
