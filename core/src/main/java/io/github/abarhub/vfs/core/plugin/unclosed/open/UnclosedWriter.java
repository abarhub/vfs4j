package io.github.abarhub.vfs.core.plugin.unclosed.open;

import io.github.abarhub.vfs.core.plugin.unclosed.UnclosableRunnable;

import java.io.IOException;
import java.io.Writer;

public class UnclosedWriter extends Writer implements UnclosedObjectFinalizer {

    private final Writer writer;
    private final UnclosedFinalizer unclosedFinalizer;

    public UnclosedWriter(Writer writer, UnclosableRunnable unclosableRunnable) {
        this.writer = writer;
        this.unclosedFinalizer = unclosableRunnable.newUnclosedFinalizer(this);
        unclosableRunnable.add(this);
    }

    @Override
    public void write(int c) throws IOException {
        writer.write(c);
    }

    @Override
    public void write(char[] cbuf) throws IOException {
        writer.write(cbuf);
    }

    @Override
    public void write(String str) throws IOException {
        writer.write(str);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        writer.write(str, off, len);
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {
        return writer.append(csq);
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        return writer.append(csq, start, end);
    }

    @Override
    public Writer append(char c) throws IOException {
        return writer.append(c);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        writer.write(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        unclosedFinalizer.closed();
        writer.close();
    }

    public UnclosedFinalizer getUnclosedFinalizer() {
        return unclosedFinalizer;
    }

}
