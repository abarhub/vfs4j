package io.github.abarhub.vfs.core.plugin.unclosed.open;

import io.github.abarhub.vfs.core.api.path.VFS4JPathName;
import io.github.abarhub.vfs.core.plugin.unclosed.UnclosableRunnable;
import io.github.abarhub.vfs.core.plugin.unclosed.VFS4JLogIfNotClosedAfterDelay;
import io.github.abarhub.vfs.core.plugin.unclosed.VFS4JUnclosedOperation;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

public class UnclosedSeekableByteChannel implements SeekableByteChannel, UnclosedObjectFinalizer {

    private final SeekableByteChannel seekableByteChannel;
    private final UnclosedFinalizer unclosedFinalizer;
    private final VFS4JLogIfNotClosedAfterDelay logIfNotClosedAfterDelay;

    public UnclosedSeekableByteChannel(SeekableByteChannel seekableByteChannel,
                                       UnclosableRunnable unclosableRunnable,
                                       VFS4JPathName pathName,
                                       VFS4JLogIfNotClosedAfterDelay logIfNotClosedAfterDelay) {
        this.seekableByteChannel = seekableByteChannel;
        this.unclosedFinalizer = unclosableRunnable.newUnclosedFinalizer(this, pathName, VFS4JUnclosedOperation.NEW_BYTE_CHANNEL);
        unclosableRunnable.add(this);
        this.logIfNotClosedAfterDelay = logIfNotClosedAfterDelay;
        if (logIfNotClosedAfterDelay != null) {
            logIfNotClosedAfterDelay.create(this, unclosedFinalizer.getNo());
        }
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
        if (logIfNotClosedAfterDelay != null) {
            logIfNotClosedAfterDelay.close(this);
        }
    }

    public UnclosedFinalizer getUnclosedFinalizer() {
        return unclosedFinalizer;
    }

}
