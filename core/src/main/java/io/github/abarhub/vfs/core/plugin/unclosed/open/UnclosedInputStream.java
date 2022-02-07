package io.github.abarhub.vfs.core.plugin.unclosed.open;

import io.github.abarhub.vfs.core.api.path.VFS4JPathName;
import io.github.abarhub.vfs.core.plugin.unclosed.UnclosableRunnable;
import io.github.abarhub.vfs.core.plugin.unclosed.VFS4JUnclosedOperation;

import java.io.IOException;
import java.io.InputStream;

public class UnclosedInputStream extends InputStream implements UnclosedObjectFinalizer {

    private final InputStream inputStream;
    private final UnclosedFinalizer unclosedFinalizer;

    public UnclosedInputStream(InputStream inputStream, UnclosableRunnable unclosableRunnable, VFS4JPathName pathName) {
        this.inputStream = inputStream;
        this.unclosedFinalizer = unclosableRunnable.newUnclosedFinalizer(this, pathName, VFS4JUnclosedOperation.NEW_INPUT_STREAM);
        unclosableRunnable.add(this);
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

    @Override
    public int available() throws IOException {
        return inputStream.available();
    }

    @Override
    public void close() throws IOException {
        unclosedFinalizer.closed();
        inputStream.close();
        super.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        inputStream.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        inputStream.reset();
    }

    @Override
    public boolean markSupported() {
        return inputStream.markSupported();
    }

    public UnclosedFinalizer getUnclosedFinalizer() {
        return unclosedFinalizer;
    }

}
