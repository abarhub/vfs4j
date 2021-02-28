package org.vfs.core.api;

import org.vfs.core.config.*;
import org.vfs.core.util.VFS4JErrorMessages;
import org.vfs.core.util.VFS4JValidationUtils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class VFS4JFileManagerBuilder {

    private final Map<String, VFS4JParameter> listeConfig;

    private final Map<String, Map<String, String>> listePlugins;

    public VFS4JFileManagerBuilder() {
        listeConfig = new HashMap<>();
        listePlugins = new HashMap<>();
    }

    public VFS4JFileManagerBuilder addPath(String name, Path path, boolean readonly) {
        VFS4JValidationUtils.checkNotEmpty(name, VFS4JErrorMessages.NAME_IS_EMPTY);
        VFS4JValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        listeConfig.put(name, new VFS4JPathParameter(path, readonly, VFS4JPathMode.STANDARD));
        return this;
    }

    public VFS4JFileManagerBuilder addPath(String name, VFS4JParameter parameter) {
        VFS4JValidationUtils.checkNotEmpty(name, VFS4JErrorMessages.NAME_IS_EMPTY);
        VFS4JValidationUtils.checkNotNull(parameter, "Paramater is null");
        listeConfig.put(name, parameter);
        return this;
    }

    public VFS4JFileManagerBuilder addPlugins(String name, Map<String, String> configPlugins) {
        VFS4JValidationUtils.checkNotEmpty(name, VFS4JErrorMessages.NAME_IS_EMPTY);
        VFS4JValidationUtils.checkNotNull(configPlugins, "Config is null");
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
