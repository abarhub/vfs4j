package org.vfs.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VFSLoggerFactory {

    public static <T> Logger getLogger(Class<T> tClass) {
        ValidationUtils.checkNotNull(tClass, "Null Class");
        return LoggerFactory.getLogger(tClass.getName());

    }
}
