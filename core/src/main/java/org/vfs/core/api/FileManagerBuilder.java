package org.vfs.core.api;

import org.vfs.core.config.PathParameter;
import org.vfs.core.config.VFS4JConfig;
import org.vfs.core.util.ValidationUtils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileManagerBuilder {

    private final Map<String, PathParameter> listeConfig;

    public FileManagerBuilder() {
        listeConfig=new HashMap<>();
    }

    public FileManagerBuilder addPath(String name, Path path, boolean readonly){
        ValidationUtils.checkNotEmpty(name,"Name is empty");
        ValidationUtils.checkNotNull(path,"Path is null");
        listeConfig.put(name,new PathParameter(path,readonly));
        return this;
    }

    public VFS4JConfig build(){
        return new VFS4JConfig(listeConfig);
    }


}
