package org.vfs.core.config;

import org.slf4j.Logger;
import org.vfs.core.exception.VFS4JConfigException;
import org.vfs.core.exception.VFS4JErrorTemporaryCreationException;
import org.vfs.core.exception.VFS4JPathNotExistsException;
import org.vfs.core.util.VFS4JLoggerFactory;
import org.vfs.core.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class VFS4JConfig {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(VFS4JConfig.class);

    private final Map<String, PathParameter> listeConfig;

    public VFS4JConfig() {
        listeConfig = new HashMap<>();
    }

    public VFS4JConfig(VFSConfigFile configFile) {
        ValidationUtils.checkNotNull(configFile, "configFile is null");
        ValidationUtils.checkNotNull(configFile.getListeConfig(), "listeConfig is null");
        this.listeConfig = initListConfig(configFile.getListeConfig());
    }

    private Map<String, PathParameter> initListConfig(Map<String, PathParameter> listeConfig) {
        LOGGER.debug("init configuration ...");
        Map<String, PathParameter> map = new HashMap<>();
        for (Map.Entry<String, PathParameter> entry : listeConfig.entrySet()) {
            String name = entry.getKey();
            PathParameter param = entry.getValue();
            if (param.getMode() == null) {
                throw new VFS4JConfigException("Mode is empty for name '" + name + "'");
            } else if (param.getMode() == VFS4JPathMode.STANDARD) {
                Path path2 = param.getPath();
                if (!path2.isAbsolute()) {
                    path2 = path2.toAbsolutePath().normalize();
                }
                PathParameter param2 = new PathParameter(path2, param.isReadonly(), param.getMode());
                map.put(name, param2);
            } else if (param.getMode() == VFS4JPathMode.TEMPORARY) {
                Path path = createTempraryDiractory(name);
                PathParameter param2 = new PathParameter(path, param.isReadonly(), param.getMode());
                map.put(name, param2);
            } else {
                throw new VFS4JConfigException("Mode is invalide for name '" + name + "'");
            }
        }
        LOGGER.info("config map: {}", map);
        LOGGER.debug("init configuration OK");
        return map;
    }

    public void addPath(String name, Path path, boolean readonly) {
        ValidationUtils.checkNotEmpty(name, "Name is empty");
        ValidationUtils.checkNotNull(path, "Path is null");
        addNewPath(name, path, readonly, VFS4JPathMode.STANDARD);
    }

    public void addPath(String name, Path path) {
        ValidationUtils.checkNotEmpty(name, "Name is empty");
        ValidationUtils.checkNotNull(path, "Path is null");
        addNewPath(name, path, false, VFS4JPathMode.STANDARD);
    }

    public void addTemporaryPath(String name) throws VFS4JErrorTemporaryCreationException {
        ValidationUtils.checkNotEmpty(name, "Name is empty");
        Path path;
        path = createTempraryDiractory(name);
        addNewPath(name, path, false, VFS4JPathMode.TEMPORARY);
    }

    private Path createTempraryDiractory(String name) {
        Path path;
        try {
            path = Files.createTempDirectory("vsf4j");
        } catch (IOException exception) {
            throw new VFS4JErrorTemporaryCreationException("Error creating temporary directory for name '" + name + "'", exception);
        }
        return path;
    }

    private void addNewPath(String name, Path path, boolean b, VFS4JPathMode standard) {
        if (Files.notExists(path)) {
            throw new VFS4JPathNotExistsException("Path '" + path + "' not exists for name '" + name + "'");
        }
        listeConfig.put(name, new PathParameter(path, b, standard));
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
