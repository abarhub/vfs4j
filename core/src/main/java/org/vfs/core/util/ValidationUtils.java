package org.vfs.core.util;

import org.vfs.core.exception.VFSInvalideParameterException;

public class ValidationUtils {

    public static void checkNotNull(Object o,String msgError){
        if(o==null){
            throw new VFSInvalideParameterException(msgError);
        }
    }

    public static void checkNotEmpty(String s,String msgError){
        if(s==null||s.length()==0){
            throw new VFSInvalideParameterException(msgError);
        }
    }

    public static void checkParameter(boolean b,String msgError){
        if(!b){
            throw new VFSInvalideParameterException(msgError);
        }
    }
}
