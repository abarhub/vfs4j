package io.github.abarhub.vfs.core.util;

import io.github.abarhub.vfs.core.exception.VFS4JInvalideParameterException;

public class VFS4JValidationUtils {

    private VFS4JValidationUtils() {
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
