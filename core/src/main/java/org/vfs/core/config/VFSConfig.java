package org.vfs.core.config;

import org.vfs.core.util.ValidationUtils;

import java.nio.file.Path;
import java.util.*;

public class VFSConfig {

    private final Map<String,Parameter> listeConfig;

    public VFSConfig() {
        listeConfig=new HashMap<>();
    }

    public VFSConfig(Map<String,Parameter> listeConfig) {
        this.listeConfig=new HashMap<>(listeConfig);
    }

    public void addPath(String name, Path path,boolean readonly){
        ValidationUtils.checkNotEmpty(name,"Name is empty");
        ValidationUtils.checkNotNull(path,"Path is null");
        listeConfig.put(name,new Parameter(path,readonly));
    }

    public Parameter getPath(String name){
        ValidationUtils.checkNotEmpty(name,"Name is empty");
        return listeConfig.get(name);
    }

    public List<String> getNames(){
        List<String> liste=new ArrayList<>();
        liste.addAll(listeConfig.keySet());
        return liste;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", VFSConfig.class.getSimpleName() + "[", "]")
                .add("listeConfig=" + listeConfig)
                .toString();
    }
}
