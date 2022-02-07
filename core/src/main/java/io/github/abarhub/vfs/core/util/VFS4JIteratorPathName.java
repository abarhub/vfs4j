package io.github.abarhub.vfs.core.util;

import io.github.abarhub.vfs.core.api.VFS4JFileManager;
import io.github.abarhub.vfs.core.api.path.VFS4JPathName;
import io.github.abarhub.vfs.core.exception.VFS4JException;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.Optional;

public class VFS4JIteratorPathName implements Iterator<VFS4JPathName> {

    private final Iterator<Path> pathIterator;
    private final VFS4JFileManager fileManager;

    public VFS4JIteratorPathName(Iterator<Path> pathIterator, VFS4JFileManager fileManager) {
        this.pathIterator = pathIterator;
        this.fileManager = fileManager;
    }

    @Override
    public boolean hasNext() {
        return pathIterator.hasNext();
    }

    @Override
    public VFS4JPathName next() {
        Path path = pathIterator.next();
        Optional<VFS4JPathName> pathNameOpt = fileManager.convertFromRealPath(path);
        return pathNameOpt.orElseThrow(() -> new VFS4JException(VFS4JErrorMessages.INVALIDE_PATH));
    }
}
