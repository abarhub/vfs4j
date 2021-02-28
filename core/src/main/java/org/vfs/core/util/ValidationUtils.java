package org.vfs.core.util;

import org.vfs.core.exception.VFS4JInvalideParameterException;

public class ValidationUtils {

    private ValidationUtils() {
    }

    public static void checkNotNull(Object o, String msgError) {
        if (o == null) {
            throw new VFS4JInvalideParameterException(msgError);
        }
    }

    public static void checkNotEmpty(String s, String msgError) {
        if (s == null || s.length() == 0) {
            throw new VFS4JInvalideParameterException(msgError);
        }
    }

    public static void checkParameter(boolean b, String msgError) {
        if (!b) {
            throw new VFS4JInvalideParameterException(msgError);
        }
    }
}
