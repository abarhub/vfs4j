package org.vfs.core.util;

import org.vfs.core.core.FileManager;
import org.vfs.core.core.PathName;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.Iterator;

public class DirectoryStreamPathName implements DirectoryStream<PathName> {

    private final DirectoryStream<Path> directoryStream;
    private final FileManager fileManager;

    public DirectoryStreamPathName(DirectoryStream<Path> directoryStream, FileManager fileManager) {
        ValidationUtils.checkNotNull(directoryStream,"directoryStream is null");
        ValidationUtils.checkNotNull(fileManager,"fileManager is null");
        this.directoryStream = directoryStream;
        this.fileManager=fileManager;
    }

    @Override
    public Iterator<PathName> iterator() {
        Iterator<Path> iter=directoryStream.iterator();
        return new IteratorPathName(iter, fileManager);
    }

    @Override
    public void close() throws IOException {
        directoryStream.close();
    }
}
