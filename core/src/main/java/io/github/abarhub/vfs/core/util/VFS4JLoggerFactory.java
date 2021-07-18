package io.github.abarhub.vfs.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VFS4JLoggerFactory {

    private VFS4JLoggerFactory() {
    }

    public static <T> Logger getLogger(Class<T> tClass) {
        VFS4JValidationUtils.checkNotNull(tClass, "Null Class");
        return LoggerFactory.getLogger(tClass.getName());

    }
}
