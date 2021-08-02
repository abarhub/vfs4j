package io.github.abarhub.vfs.core.plugin.unclosed.open;

import io.github.abarhub.vfs.core.plugin.unclosed.UnclosableRunnable;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

public class UnclosedReader extends Reader implements UnclosedObjectFinalizer {

    private final Reader reader;
    private final UnclosedFinalizer unclosedFinalizer;

    public UnclosedReader(Reader reader, UnclosableRunnable unclosableRunnable) {
        this.reader = reader;
        this.unclosedFinalizer = unclosableRunnable.newUnclosedFinalizer(this);
        unclosableRunnable.add(this);
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
