package io.github.abarhub.vfs.core.plugin.unclosed.open;

import io.github.abarhub.vfs.core.api.path.VFS4JPathName;
import io.github.abarhub.vfs.core.plugin.unclosed.UnclosableRunnable;
import io.github.abarhub.vfs.core.plugin.unclosed.VFS4JLogIfNotClosedAfterDelay;
import io.github.abarhub.vfs.core.plugin.unclosed.VFS4JUnclosedOperation;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

public class UnclosedReader extends Reader implements UnclosedObjectFinalizer {

    private final Reader reader;
    private final UnclosedFinalizer unclosedFinalizer;
    private final VFS4JLogIfNotClosedAfterDelay logIfNotClosedAfterDelay;

    public UnclosedReader(Reader reader, UnclosableRunnable unclosableRunnable,
                          VFS4JPathName pathName, VFS4JLogIfNotClosedAfterDelay logIfNotClosedAfterDelay) {
        this.reader = reader;
        this.unclosedFinalizer = unclosableRunnable.newUnclosedFinalizer(this, pathName, VFS4JUnclosedOperation.NEW_READER);
        unclosableRunnable.add(this);
        this.logIfNotClosedAfterDelay = logIfNotClosedAfterDelay;
        if (logIfNotClosedAfterDelay != null) {
            logIfNotClosedAfterDelay.create(this, unclosedFinalizer.getNo());
        }
    }

    @Override
    public int read() throws IOException {
        return reader.read();
    }

    @Override
    public int read(char[] cbuf, int offset, int length) throws IOException {
        return reader.read(cbuf, offset, length);
    }

    @Override
    public boolean ready() throws IOException {
        return reader.ready();
    }

    @Override
    public void close() throws IOException {
        unclosedFinalizer.closed();
        reader.close();
        if (logIfNotClosedAfterDelay != null) {
            logIfNotClosedAfterDelay.close(this);
        }
    }

    @Override
    public int read(CharBuffer target) throws IOException {
        return reader.read(target);
    }

    @Override
    public int read(char[] cbuf) throws IOException {
        return reader.read(cbuf);
    }

    @Override
    public long skip(long n) throws IOException {
        return reader.skip(n);
    }

    @Override
    public boolean markSupported() {
        return reader.markSupported();
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        reader.mark(readAheadLimit);
    }

    @Override
    public void reset() throws IOException {
        reader.reset();
    }

    public UnclosedFinalizer getUnclosedFinalizer() {
        return unclosedFinalizer;
    }

}
