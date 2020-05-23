package org.vfs.core.api;

import org.slf4j.Logger;
import org.vfs.core.config.PathParameter;
import org.vfs.core.config.VFS4JPathMode;
import org.vfs.core.exception.VFS4JException;
import org.vfs.core.exception.VFS4JInvalideConfigFileException;
import org.vfs.core.util.VFS4JLoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ParseConfigFile {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(ParseConfigFile.class);

    public static final String PREFIX = "vfs.";
    public static final String PREFIX_PATHS = PREFIX + "paths.";
    public static final String SUFFIX_PATH = ".path";
    public static final String SUFFIX_MODE = ".mode";
    public static final String SUFFIX_READONLY = ".readonly";
    public static final String VALIDE_NAME = "[a-zA-Z][a-zA-Z0-9]*";

    // construction de la map des propriétés (on enlève ce qui n'est pas de type string)
    public FileManagerBuilder parse(Properties properties) {
        FileManagerBuilder fileManagerBuilder = new FileManagerBuilder();

        Set<Object> keys = properties.keySet();
        Map<String, String> map = new HashMap<>();
        for (Object o : keys) {
            if (o != null && o instanceof String) {
                Object o2 = properties.get(o);
                if (o2 instanceof String) {
                    String key = (String) o;
                    String value = (String) o2;
                    map.put(key, value);
                }
            }
        }
        // détermination des noms
        List<String> liste = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(PREFIX_PATHS)) {
                String s = key.substring(PREFIX_PATHS.length());
                if (s.endsWith(SUFFIX_PATH)) {
                    s = s.substring(0, s.indexOf(SUFFIX_PATH));
                    if (!s.trim().isEmpty() && s.matches(VALIDE_NAME)) {
                        liste.add(s);
                    } else {
                        LOGGER.debug("key '{}' ignored (bad name)", key);
                    }
                } else if (s.endsWith(SUFFIX_MODE)) {
                    s = s.substring(0, s.indexOf(SUFFIX_MODE));
                    if (!s.trim().isEmpty() && s.matches(VALIDE_NAME)) {
                        liste.add(s);
                    } else {
                        LOGGER.debug("key '{}' ignored (bad name)", key);
                    }
                } else {
                    LOGGER.debug("key '{}' ignored (bad suffix)", key);
                }
            } else {
                LOGGER.debug("key '{}' ignored (bad prefix)", key);
            }
        }

        // ajout des paths dans fileManagerBuilder
        for (String nom : liste) {
            String keyPath = PREFIX_PATHS + nom + SUFFIX_PATH;
            String keyMode = PREFIX_PATHS + nom + SUFFIX_MODE;
            String keyReadonly = PREFIX_PATHS + nom + SUFFIX_READONLY;
            if (map.containsKey(keyPath) || map.containsKey(keyMode)) {
                String valuePath = map.get(keyPath);
                String valueMode = map.get(keyMode);
                String valueReadonly = map.get(keyReadonly);
                Path p = null;
                VFS4JPathMode mode = VFS4JPathMode.STANDARD;
                boolean readonly = false;
                if (valuePath != null && !valuePath.trim().isEmpty()) {
                    p = Paths.get(valuePath);
                }
                if (valueMode != null && !valueMode.trim().isEmpty()) {
                    if (Objects.equals(valueMode, VFS4JPathMode.STANDARD.getName())) {
                        mode = VFS4JPathMode.STANDARD;
                    } else if (Objects.equals(valueMode, VFS4JPathMode.TEMPORARY.getName())) {
                        mode = VFS4JPathMode.TEMPORARY;
                    } else {
                        throw new VFS4JInvalideConfigFileException("mode for '" + nom + "' is invalide (value='" + valueMode + "')");
                    }
                }
                if (valueReadonly != null && !valueReadonly.trim().isEmpty()) {
                    if (Objects.equals(valueReadonly, "true")) {
                        readonly = true;
                    } else if (Objects.equals(valueReadonly, "false")) {
                        readonly = false;
                    } else {
                        throw new VFS4JInvalideConfigFileException("readonly for '" + nom + "' is invalide (value='" + valueReadonly + "')");
                    }
                }
                if (mode == VFS4JPathMode.STANDARD) {
                    if (p == null) {
                        throw new VFS4JException("Path for '" + nom + "' is empty");
                    }
                    fileManagerBuilder.addPath(nom, p, readonly);
                } else if (mode == VFS4JPathMode.TEMPORARY) {
                    if (p != null) {
                        throw new VFS4JException("Path for '" + nom + "' is not empty");
                    }
                    fileManagerBuilder.addPath(nom, new PathParameter(p, readonly, VFS4JPathMode.TEMPORARY));
                } else {
                    throw new VFS4JException("Path or temporary mode must be completed '" + nom + "'");
                }
            }
        }

        return fileManagerBuilder;
    }
}
