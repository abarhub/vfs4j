package org.vfs.core.config;

import org.slf4j.Logger;
import org.vfs.core.api.ParseConfigFile;
import org.vfs.core.exception.VFS4JConfigException;
import org.vfs.core.exception.VFS4JErrorTemporaryCreationException;
import org.vfs.core.exception.VFS4JPathNotExistsException;
import org.vfs.core.plugin.common.VFS4JPlugins;
import org.vfs.core.plugin.common.VFS4JPluginsFactory;
import org.vfs.core.util.VFS4JErrorMessages;
import org.vfs.core.util.VFS4JLoggerFactory;
import org.vfs.core.util.ValidationUtils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class VFS4JConfig {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(VFS4JConfig.class);

    private final Map<String, VFS4JParameter> listeConfig;

    private final Map<String, VFS4JPlugins> listePlugins;

    private final List<String> listPluginsOrder;

    public VFS4JConfig() {
        listeConfig = new HashMap<>();
        listePlugins = new HashMap<>();
        listPluginsOrder = new ArrayList<>();
    }

    public void init(VFSConfigFile configFile) {
        ValidationUtils.checkNotNull(configFile, "configFile is null");
        ValidationUtils.checkNotNull(configFile.getListeConfig(), "listeConfig is null");
        initListConfig(configFile);
    }

    private void initListConfig(VFSConfigFile configFile) {
        LOGGER.debug("init configuration ...");
        Map<String, VFS4JParameter> map = initPaths(configFile);
        listeConfig.clear();
        listeConfig.putAll(map);
        listPluginsOrder.clear();
        Map<String, VFS4JPlugins> mapPlugins = initPlugins(configFile);
        listePlugins.clear();
        listePlugins.putAll(mapPlugins);
        LOGGER.debug("init configuration OK");
    }

    private Map<String, VFS4JParameter> initPaths(VFSConfigFile configFile) {
        Map<String, VFS4JParameter> listeConfig = configFile.getListeConfig();
        Map<String, VFS4JParameter> map = new HashMap<>();
        for (Map.Entry<String, VFS4JParameter> entry : listeConfig.entrySet()) {
            String name = entry.getKey();
            VFS4JParameter param = entry.getValue();
            if (param.getMode() == null) {
                throw new VFS4JConfigException("Mode is empty for name '" + name + "'");
            } else if (param.getMode() == VFS4JPathMode.STANDARD) {
                PathParameter path = (PathParameter) param;
                Path path2 = path.getPath();
                if (!path2.isAbsolute()) {
                    path2 = path2.toAbsolutePath().normalize();
                }
                PathParameter param2 = new PathParameter(path2, param.isReadonly(), param.getMode());
                map.put(name, param2);
            } else if (param.getMode() == VFS4JPathMode.CLASSPATH) {
                VFS4JClasspathParameter classpath = (VFS4JClasspathParameter) param;
                VFS4JClasspathParameter param2 = new VFS4JClasspathParameter(classpath.getPath());
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
        return map;
    }

    private Map<String, VFS4JPlugins> initPlugins(VFSConfigFile configFile) {
        Map<String, VFS4JPlugins> map = new HashMap<>();
        if (configFile.getListePlugins() != null) {
            Map<String, VFS4JPluginsFactory> mapFactory = new HashMap<>();
            for (String name : configFile.getListePlugins().keySet()) {
                Map<String, String> mapConfig = configFile.getListePlugins().get(name);
                String keyClass = ParseConfigFile.SUFFIX_CLASS_EXTENSION;
                if (mapConfig.containsKey(keyClass)) {
                    String className = mapConfig.get(keyClass);
                    if (className != null && !className.trim().isEmpty()) {
                        VFS4JPluginsFactory pluginsFactory;
                        if (mapFactory.containsKey(className)) {
                            pluginsFactory = mapFactory.get(className);
                        } else {
                            pluginsFactory = createPluginsFactory(name, className);
                            mapFactory.put(className, pluginsFactory);
                        }
                        VFS4JPlugins plugins = pluginsFactory.createPlugins(name, mapConfig, this);
                        map.put(name, plugins);
                        listPluginsOrder.add(name);
                    }
                } else {
                    throw new VFS4JConfigException("Plugins for name '" + name + "' has no class");
                }
            }
        }
        LOGGER.info("config plugins: {}", map);
        return map;
    }

    private VFS4JPluginsFactory createPluginsFactory(String name, String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> ctor = clazz.getConstructor();
            Object object = ctor.newInstance();
            if (VFS4JPluginsFactory.class.isInstance(object)) {
                return (VFS4JPluginsFactory) object;
            } else {
                throw new VFS4JConfigException("Object '" + className + "' is not of type PluginsFactory for name '" + name + "'");
            }
        } catch (Exception e) {
            throw new VFS4JConfigException("Can't create PluginsFactory '" + className + "' for name '" + name + "'", e);
        }
    }

    public void addPath(String name, Path path, boolean readonly) {
        ValidationUtils.checkNotEmpty(name, VFS4JErrorMessages.NAME_IS_EMPTY);
        ValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        addNewPath(name, path, readonly, VFS4JPathMode.STANDARD);
    }

    public void addPath(String name, Path path) {
        ValidationUtils.checkNotEmpty(name, VFS4JErrorMessages.NAME_IS_EMPTY);
        ValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        addNewPath(name, path, false, VFS4JPathMode.STANDARD);
    }

    public void addTemporaryPath(String name) throws VFS4JErrorTemporaryCreationException {
        ValidationUtils.checkNotEmpty(name, VFS4JErrorMessages.NAME_IS_EMPTY);
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

    public VFS4JParameter getPath(String name) {
        ValidationUtils.checkNotEmpty(name, VFS4JErrorMessages.NAME_IS_EMPTY);
        return listeConfig.get(name);
    }

    public List<String> getNames() {
        List<String> liste = new ArrayList<>();
        liste.addAll(listeConfig.keySet());
        return liste;
    }

    public List<String> getPluginsOrder() {
        return listPluginsOrder;
    }

    public VFS4JPlugins getPlugins(String name) {
        ValidationUtils.checkNotEmpty(name, VFS4JErrorMessages.NAME_IS_EMPTY);
        return listePlugins.get(name);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", VFS4JConfig.class.getSimpleName() + "[", "]")
                .add("listeConfig=" + listeConfig)
                .toString();
    }
}
