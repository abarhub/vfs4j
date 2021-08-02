package io.github.abarhub.vfs.core.plugin.unclosed.open;

import io.github.abarhub.vfs.core.plugin.unclosed.UnclosableRunnable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

public class UnclosedSeekableByteChannel implements SeekableByteChannel, UnclosedObjectFinalizer {

    private final SeekableByteChannel seekableByteChannel;
    private final UnclosedFinalizer unclosedFinalizer;

    public UnclosedSeekableByteChannel(SeekableByteChannel seekableByteChannel, UnclosableRunnable unclosableRunnable) {
        this.seekableByteChannel = seekableByteChannel;
        this.unclosedFinalizer = unclosableRunnable.newUnclosedFinalizer(this);
        unclosableRunnable.add(this);
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        return seekableByteChannel.read(dst);
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        return seekableByteChannel.write(src);
    }

    @Override
    public long position() throws IOException {
        return seekableByteChannel.position();
    }

    @Override
    public SeekableByteChannel position(long newPosition) throws IOException {
        return seekableByteChannel.position(newPosition);
    }

    @Override
    public long size() throws IOException {
        return seekableByteChannel.size();
    }

    @Override
    public SeekableByteChannel truncate(long size) throws IOException {
        return seekableByteChannel.truncate(size);
    }

    @Override
    public boolean isOpen() {
        return seekableByteChannel.isOpen();
    }

    @Override
    public void close() throws IOException {
        unclosedFinalizer.closed();
        seekableByteChannel.close();
    }

    public UnclosedFinalizer getUnclosedFinalizer() {
        return unclosedFinalizer;
    }

}
