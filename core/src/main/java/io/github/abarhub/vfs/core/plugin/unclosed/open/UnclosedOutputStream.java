package io.github.abarhub.vfs.core.plugin.unclosed.open;

import io.github.abarhub.vfs.core.api.path.VFS4JPathName;
import io.github.abarhub.vfs.core.plugin.unclosed.UnclosableRunnable;
import io.github.abarhub.vfs.core.plugin.unclosed.VFS4JUnclosedOperation;

import java.io.IOException;
import java.io.OutputStream;

public class UnclosedOutputStream extends OutputStream implements UnclosedObjectFinalizer {

    private final OutputStream outputStream;
    private final UnclosedFinalizer unclosedFinalizer;

    public UnclosedOutputStream(OutputStream outputStream, UnclosableRunnable unclosableRunnable, VFS4JPathName pathName) {
        this.outputStream = outputStream;
        this.unclosedFinalizer = unclosableRunnable.newUnclosedFinalizer(this, pathName, VFS4JUnclosedOperation.NEW_OUTPUT_STREAM);
        unclosableRunnable.add(this);
    }

    @Override
    public void write(byte[] b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        outputStream.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        unclosedFinalizer.closed();
        outputStream.close();
        super.close();
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
    }

    public UnclosedFinalizer getUnclosedFinalizer() {
        return unclosedFinalizer;
    }

}
