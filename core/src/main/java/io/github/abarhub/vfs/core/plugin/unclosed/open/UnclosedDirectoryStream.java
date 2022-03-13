package io.github.abarhub.vfs.core.plugin.unclosed.open;

import io.github.abarhub.vfs.core.api.path.VFS4JPathName;
import io.github.abarhub.vfs.core.plugin.unclosed.UnclosableRunnable;
import io.github.abarhub.vfs.core.plugin.unclosed.VFS4JLogIfNotClosedAfterDelay;
import io.github.abarhub.vfs.core.plugin.unclosed.VFS4JUnclosedOperation;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.util.Iterator;

public class UnclosedDirectoryStream implements DirectoryStream<VFS4JPathName>, UnclosedObjectFinalizer {

    private final DirectoryStream<VFS4JPathName> directoryStream;
    private final UnclosedFinalizer unclosedFinalizer;
    private final VFS4JLogIfNotClosedAfterDelay logIfNotClosedAfterDelay;

    public UnclosedDirectoryStream(DirectoryStream<VFS4JPathName> directoryStream, UnclosableRunnable unclosableRunnable,
                                   VFS4JPathName pathName, VFS4JLogIfNotClosedAfterDelay logIfNotClosedAfterDelay) {
        this.directoryStream = directoryStream;
        this.unclosedFinalizer = unclosableRunnable.newUnclosedFinalizer(this, pathName, VFS4JUnclosedOperation.NEW_DIRECTORY_STREAM);
        unclosableRunnable.add(this);
        this.logIfNotClosedAfterDelay = logIfNotClosedAfterDelay;
        if (logIfNotClosedAfterDelay != null) {
            logIfNotClosedAfterDelay.create(this, unclosedFinalizer.getNo());
        }
    }

    @Override
    public Iterator<VFS4JPathName> iterator() {
        return directoryStream.iterator();
    }

    @Override
    public void close() throws IOException {
        unclosedFinalizer.closed();
        directoryStream.close();
        if (logIfNotClosedAfterDelay != null) {
            logIfNotClosedAfterDelay.close(this);
        }
    }

    public UnclosedFinalizer getUnclosedFinalizer() {
        return unclosedFinalizer;
    }

}
