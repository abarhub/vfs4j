package io.github.abarhub.vfs.core.util;

import io.github.abarhub.vfs.core.api.VFS4JFileManager;
import io.github.abarhub.vfs.core.api.path.VFS4JPathName;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.Iterator;

public class VFS4JDirectoryStreamPathName implements DirectoryStream<VFS4JPathName> {

    private final DirectoryStream<Path> directoryStream;
    private final VFS4JFileManager fileManager;

    public VFS4JDirectoryStreamPathName(DirectoryStream<Path> directoryStream, VFS4JFileManager fileManager) {
        VFS4JValidationUtils.checkNotNull(directoryStream, "directoryStream is null");
        VFS4JValidationUtils.checkNotNull(fileManager, "fileManager is null");
        this.directoryStream = directoryStream;
        this.fileManager = fileManager;
    }

    @Override
    public Iterator<VFS4JPathName> iterator() {
        Iterator<Path> iter = directoryStream.iterator();
        return new VFS4JIteratorPathName(iter, fileManager);
    }

    @Override
    public void close() throws IOException {
        directoryStream.close();
    }
}
