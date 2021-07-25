package io.github.abarhub.vfs.core.plugin.unclosed.open;

import io.github.abarhub.vfs.core.plugin.unclosed.UnclosableRunnable;

import java.io.IOException;
import java.io.InputStream;

public class UnclosedInputStream extends InputStream {

    private final InputStream inputStream;
    private final UnclosedFinalizer unclosedFinalizer;

    public UnclosedInputStream(InputStream inputStream, UnclosableRunnable unclosableRunnable) {
        this.inputStream = inputStream;
        this.unclosedFinalizer = unclosableRunnable.newUnclosedFinalizer(this);
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
    }

    @Override
    public void mark(int readlimit) {
        inputStream.mark(readlimit);
    }

    @Override
    public void reset() throws IOException {
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
