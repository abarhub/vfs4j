package io.github.abarhub.vfs.core.config;

import io.github.abarhub.vfs.core.api.VFS4JParseConfigFile;
import io.github.abarhub.vfs.core.exception.VFS4JConfigException;
import io.github.abarhub.vfs.core.exception.VFS4JErrorTemporaryCreationException;
import io.github.abarhub.vfs.core.exception.VFS4JPathNotExistsException;
import io.github.abarhub.vfs.core.plugin.common.VFS4JPlugins;
import io.github.abarhub.vfs.core.plugin.common.VFS4JPluginsFactory;
import io.github.abarhub.vfs.core.util.VFS4JErrorMessages;
import io.github.abarhub.vfs.core.util.VFS4JLoggerFactory;
import io.github.abarhub.vfs.core.util.VFS4JValidationUtils;
import org.slf4j.Logger;

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
        VFS4JValidationUtils.checkNotNull(configFile, "configFile is null");
        VFS4JValidationUtils.checkNotNull(configFile.getListeConfig(), "listeConfig is null");
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
        Map<String, VFS4JParameter> config = configFile.getListeConfig();
        Map<String, VFS4JParameter> map = new HashMap<>();
        for (Map.Entry<String, VFS4JParameter> entry : config.entrySet()) {
            String name = entry.getKey();
            VFS4JParameter param = entry.getValue();
            if (param.getMode() == null) {
                throw new VFS4JConfigException("Mode is empty for name '" + name + "'");
            } else if (param.getMode() == VFS4JPathMode.STANDARD) {
                VFS4JPathParameter path = (VFS4JPathParameter) param;
                Path path2 = path.getPath();
                if (!path2.isAbsolute()) {
                    path2 = path2.toAbsolutePath().normalize();
                }
                VFS4JPathParameter param2 = new VFS4JPathParameter(path2, param.isReadonly(), param.getMode());
                map.put(name, param2);
            } else if (param.getMode() == VFS4JPathMode.CLASSPATH) {
                VFS4JClasspathParameter classpath = (VFS4JClasspathParameter) param;
                VFS4JClasspathParameter param2 = new VFS4JClasspathParameter(classpath.getPath());
                map.put(name, param2);
            } else if (param.getMode() == VFS4JPathMode.TEMPORARY) {
                Path path = createTemporaryDirectory(name);
                VFS4JPathParameter param2 = new VFS4JPathParameter(path, param.isReadonly(), param.getMode());
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
                String keyClass = VFS4JParseConfigFile.SUFFIX_CLASS_EXTENSION;
                if (mapConfig.containsKey(keyClass)) {
                    addPlugin(map, mapFactory, name, mapConfig, keyClass);
                } else {
                    throw new VFS4JConfigException("Plugins for name '" + name + "' has no class");
                }
            }
        }
        if (map.isEmpty()) {
            LOGGER.info("config plugins: none");
        } else {
            LOGGER.info("config plugins: {}", map);
        }
        return map;
    }

    private void addPlugin(Map<String, VFS4JPlugins> map, Map<String, VFS4JPluginsFactory> mapFactory, String name, Map<String, String> mapConfig, String keyClass) {
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
        VFS4JValidationUtils.checkNotEmpty(name, VFS4JErrorMessages.NAME_IS_EMPTY);
        VFS4JValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        addNewPath(name, path, readonly, VFS4JPathMode.STANDARD);
    }

    public void addPath(String name, Path path) {
        VFS4JValidationUtils.checkNotEmpty(name, VFS4JErrorMessages.NAME_IS_EMPTY);
        VFS4JValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        addNewPath(name, path, false, VFS4JPathMode.STANDARD);
    }

    public void addTemporaryPath(String name) throws VFS4JErrorTemporaryCreationException {
        VFS4JValidationUtils.checkNotEmpty(name, VFS4JErrorMessages.NAME_IS_EMPTY);
        Path path;
        path = createTemporaryDirectory(name);
        addNewPath(name, path, false, VFS4JPathMode.TEMPORARY);
    }

    private Path createTemporaryDirectory(String name) {
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
        listeConfig.put(name, new VFS4JPathParameter(path, b, standard));
    }

    public VFS4JParameter getPath(String name) {
        VFS4JValidationUtils.checkNotEmpty(name, VFS4JErrorMessages.NAME_IS_EMPTY);
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
        VFS4JValidationUtils.checkNotEmpty(name, VFS4JErrorMessages.NAME_IS_EMPTY);
        return listePlugins.get(name);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", VFS4JConfig.class.getSimpleName() + "[", "]")
                .add("listeConfig=" + listeConfig)
                .toString();
    }
}
