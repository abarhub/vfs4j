package io.github.abarhub.vfs.core.plugin.unclosed.open;

import io.github.abarhub.vfs.core.api.path.VFS4JPathName;
import io.github.abarhub.vfs.core.plugin.unclosed.UnclosableRunnable;
import io.github.abarhub.vfs.core.plugin.unclosed.VFS4JUnclosedOperation;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.util.Iterator;

public class UnclosedDirectoryStream implements DirectoryStream<VFS4JPathName>, UnclosedObjectFinalizer {

    private final DirectoryStream<VFS4JPathName> directoryStream;
    private final UnclosedFinalizer unclosedFinalizer;

    public UnclosedDirectoryStream(DirectoryStream<VFS4JPathName> directoryStream, UnclosableRunnable unclosableRunnable, VFS4JPathName pathName) {
        this.directoryStream = directoryStream;
        this.unclosedFinalizer = unclosableRunnable.newUnclosedFinalizer(this, pathName, VFS4JUnclosedOperation.NEW_DIRECTORY_STREAM);
        unclosableRunnable.add(this);
    }

    @Override
    public Iterator<VFS4JPathName> iterator() {
        return directoryStream.iterator();
    }

    @Override
    public void close() throws IOException {
        unclosedFinalizer.closed();
        directoryStream.close();
    }

    public UnclosedFinalizer getUnclosedFinalizer() {
        return unclosedFinalizer;
    }

}
