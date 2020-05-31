package org.vfs.core.api;

import org.vfs.core.config.PathParameter;
import org.vfs.core.config.VFS4JConfig;
import org.vfs.core.config.VFS4JPathMode;
import org.vfs.core.config.VFSConfigFile;
import org.vfs.core.util.ValidationUtils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileManagerBuilder {

    private final Map<String, PathParameter> listeConfig;

    public FileManagerBuilder() {
        listeConfig = new HashMap<>();
    }

    public FileManagerBuilder addPath(String name, Path path, boolean readonly) {
        ValidationUtils.checkNotEmpty(name, "Name is empty");
        ValidationUtils.checkNotNull(path, "Path is null");
        listeConfig.put(name, new PathParameter(path, readonly, VFS4JPathMode.STANDARD));
        return this;
    }

    public FileManagerBuilder addPath(String name, PathParameter parameter) {
        ValidationUtils.checkNotEmpty(name, "Name is empty");
        ValidationUtils.checkNotNull(parameter, "Paramater is null");
        listeConfig.put(name, parameter);
        return this;
    }

    public VFS4JConfig build() {
        Map<String, PathParameter> conf=new HashMap<>();
        conf.putAll(listeConfig);
        VFSConfigFile vfsConfigFile=new VFSConfigFile();
        vfsConfigFile.setListeConfig(conf);
        return new VFS4JConfig(vfsConfigFile);
    }


}
