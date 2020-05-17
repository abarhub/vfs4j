package org.vfs.core.util;

import java.util.logging.Logger;

public class VFSLoggerFactory {

    public static <T> Logger getLogger(Class<T> tClass) {
        ValidationUtils.checkNotNull(tClass, "Null Class");
        return Logger.getLogger(tClass.getName());
    }
}
