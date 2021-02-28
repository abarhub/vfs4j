package org.vfs.core.api;

import org.vfs.core.config.*;
import org.vfs.core.util.VFS4JErrorMessages;
import org.vfs.core.util.ValidationUtils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileManagerBuilder {

    private final Map<String, VFS4JParameter> listeConfig;

    private final Map<String, Map<String, String>> listePlugins;

    public FileManagerBuilder() {
        listeConfig = new HashMap<>();
        listePlugins = new HashMap<>();
    }

    public FileManagerBuilder addPath(String name, Path path, boolean readonly) {
        ValidationUtils.checkNotEmpty(name, VFS4JErrorMessages.NAME_IS_EMPTY);
        ValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        listeConfig.put(name, new PathParameter(path, readonly, VFS4JPathMode.STANDARD));
        return this;
    }

    public FileManagerBuilder addPath(String name, VFS4JParameter parameter) {
        ValidationUtils.checkNotEmpty(name, VFS4JErrorMessages.NAME_IS_EMPTY);
        ValidationUtils.checkNotNull(parameter, "Paramater is null");
        listeConfig.put(name, parameter);
        return this;
    }

    public FileManagerBuilder addPlugins(String name, Map<String, String> configPlugins) {
        ValidationUtils.checkNotEmpty(name, VFS4JErrorMessages.NAME_IS_EMPTY);
        ValidationUtils.checkNotNull(configPlugins, "Config is null");
        listePlugins.put(name, configPlugins);
        return this;
    }

    public VFS4JConfig build() {
        Map<String, VFS4JParameter> confPaths = new HashMap<>(listeConfig);
        VFSConfigFile vfsConfigFile = new VFSConfigFile();
        vfsConfigFile.setListeConfig(confPaths);
        Map<String, Map<String, String>> confPlugins = new HashMap<>(listePlugins);
        vfsConfigFile.setListePlugins(confPlugins);
        return VFS4JConfigFactory.createVfs4JConfig(vfsConfigFile);
    }


}
