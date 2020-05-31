package org.vfs.core.config;

import java.util.Map;

public class VFSConfigFile {

    private Map<String, PathParameter> listeConfig;

    public Map<String, PathParameter> getListeConfig() {
        return listeConfig;
    }

    public void setListeConfig(Map<String, PathParameter> listeConfig) {
        this.listeConfig = listeConfig;
    }
}
