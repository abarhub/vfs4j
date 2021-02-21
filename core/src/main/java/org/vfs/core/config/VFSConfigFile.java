package org.vfs.core.config;

import java.util.Map;

public class VFSConfigFile {

    private Map<String, VFS4JParameter> listeConfig;

    private Map<String, Map<String, String>> listePlugins;

    public Map<String, VFS4JParameter> getListeConfig() {
        return listeConfig;
    }

    public void setListeConfig(Map<String, VFS4JParameter> listeConfig) {
        this.listeConfig = listeConfig;
    }

    public Map<String, Map<String, String>> getListePlugins() {
        return listePlugins;
    }

    public void setListePlugins(Map<String, Map<String, String>> listePlugins) {
        this.listePlugins = listePlugins;
    }
}
