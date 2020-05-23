package org.vfs.core.config;

import org.vfs.core.util.ValidationUtils;

import java.nio.file.Path;
import java.util.*;

public class VFS4JConfig {

    private final Map<String, PathParameter> listeConfig;

    public VFS4JConfig() {
        listeConfig = new HashMap<>();
    }

    public VFS4JConfig(Map<String, PathParameter> listeConfig) {
        this.listeConfig = new HashMap<>(listeConfig);
    }

    public void addPath(String name, Path path, boolean readonly) {
        ValidationUtils.checkNotEmpty(name, "Name is empty");
        ValidationUtils.checkNotNull(path, "Path is null");
        listeConfig.put(name, new PathParameter(path, readonly));
    }

    public PathParameter getPath(String name) {
        ValidationUtils.checkNotEmpty(name, "Name is empty");
        return listeConfig.get(name);
    }

    public List<String> getNames() {
        List<String> liste = new ArrayList<>();
        liste.addAll(listeConfig.keySet());
        return liste;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", VFS4JConfig.class.getSimpleName() + "[", "]")
                .add("listeConfig=" + listeConfig)
                .toString();
    }
}
