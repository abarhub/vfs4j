package io.github.abarhub.vfs.core.plugin.unclosed.open;

import io.github.abarhub.vfs.core.api.path.VFS4JPathName;
import io.github.abarhub.vfs.core.plugin.unclosed.UnclosableRunnable;
import io.github.abarhub.vfs.core.plugin.unclosed.VFS4JLogIfNotClosedAfterDelay;
import io.github.abarhub.vfs.core.plugin.unclosed.VFS4JUnclosedOperation;

import java.io.IOException;
import java.io.Writer;

public class UnclosedWriter extends Writer implements UnclosedObjectFinalizer {

    private final Writer writer;
    private final UnclosedFinalizer unclosedFinalizer;
    private final VFS4JLogIfNotClosedAfterDelay logIfNotClosedAfterDelay;

    public UnclosedWriter(Writer writer, UnclosableRunnable unclosableRunnable,
                          VFS4JPathName pathName,
                          VFS4JLogIfNotClosedAfterDelay logIfNotClosedAfterDelay) {
        this.writer = writer;
        this.unclosedFinalizer = unclosableRunnable.newUnclosedFinalizer(this, pathName, VFS4JUnclosedOperation.NEW_WRITER);
        unclosableRunnable.add(this);
        this.logIfNotClosedAfterDelay = logIfNotClosedAfterDelay;
        if (logIfNotClosedAfterDelay != null) {
            logIfNotClosedAfterDelay.create(this, unclosedFinalizer.getNo());
        }
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
        if (logIfNotClosedAfterDelay != null) {
            logIfNotClosedAfterDelay.close(this);
        }
    }

    public UnclosedFinalizer getUnclosedFinalizer() {
        return unclosedFinalizer;
    }

}
