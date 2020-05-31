package org.vfs.core.api;

import org.vfs.core.config.*;
import org.vfs.core.util.ValidationUtils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileManagerBuilder {

    private final Map<String, PathParameter> listeConfig;

    private final Map<String, Map<String, String>> listePlugins;

    public FileManagerBuilder() {
        listeConfig = new HashMap<>();
        listePlugins = new HashMap<>();
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

    public FileManagerBuilder addPlugins(String name, Map<String, String> configPlugins) {
        ValidationUtils.checkNotEmpty(name, "Name is empty");
        ValidationUtils.checkNotNull(configPlugins, "Config is null");
        listePlugins.put(name, configPlugins);
        return this;
    }

    public VFS4JConfig build() {
        Map<String, PathParameter> confPaths = new HashMap<>();
        confPaths.putAll(listeConfig);
        VFSConfigFile vfsConfigFile = new VFSConfigFile();
        vfsConfigFile.setListeConfig(confPaths);
        Map<String, Map<String, String>> confPlugins = new HashMap<>();
        confPlugins.putAll(listePlugins);
        vfsConfigFile.setListePlugins(confPlugins);
        return VFS4JConfigFactory.createVfs4JConfig(vfsConfigFile);
    }


}
