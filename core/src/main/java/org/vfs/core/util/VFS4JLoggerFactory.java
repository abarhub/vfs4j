package org.vfs.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VFS4JLoggerFactory {

    private VFS4JLoggerFactory() {
    }

    public static <T> Logger getLogger(Class<T> tClass) {
        ValidationUtils.checkNotNull(tClass, "Null Class");
        return LoggerFactory.getLogger(tClass.getName());

    }
}
