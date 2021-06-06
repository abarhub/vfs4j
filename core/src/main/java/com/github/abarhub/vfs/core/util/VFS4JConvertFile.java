package com.github.abarhub.vfs.core.util;

import com.github.abarhub.vfs.core.api.VFS4JPathName;
import com.github.abarhub.vfs.core.config.*;
import com.github.abarhub.vfs.core.exception.VFS4JInvalideParameterException;
import org.slf4j.Logger;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class VFS4JConvertFile {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(VFS4JConvertFile.class);

    private final VFS4JConfig config;

    public VFS4JConvertFile(VFS4JConfig config) {
        VFS4JValidationUtils.checkNotNull(config, "vfsConfig is null");
        this.config = config;
    }

    public Path getRealFile(VFS4JPathName file) {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        VFS4JParameter p = config.getPath(file.getName());
        if (p == null) {
            throw new VFS4JInvalideParameterException("PathName '" + file.getName() + "' doesn't exist");
        }
        Path path;
        if (p.getMode() == VFS4JPathMode.CLASSPATH) {
            path = getPathFromClasspath(file, (VFS4JClasspathParameter) p);
        } else {
            if (p instanceof VFS4JPathParameter) {
                path = getPath(file, (VFS4JPathParameter) p);
            } else {
                throw new VFS4JInvalideParameterException("path type '" + file.getName() + "' invalide");
            }
        }
        return path;
    }

    private Path getPathFromClasspath(VFS4JPathName file, VFS4JClasspathParameter parameter) {
        Path path;
        try {
            String directory = "";
            if (!Objects.equals(parameter.getPath(), "")) {
                directory = parameter.getPath();
            } else {
                directory = "";
            }
            if (file.getPath() == null || file.getPath().isEmpty()) {
                path = Paths.get(ClassLoader.getSystemResource(directory).toURI());
            } else {
                Path p2 = Paths.get(directory, file.getPath()).normalize();
                p2 = removeReferenceParentInBegin(p2);
                URL url = ClassLoader.getSystemResource(p2.toString());
                if (url == null) {
                    // file not found. Find in classpath from current class. For Linux
                    url = VFS4JConvertFile.class.getResource(p2.toString());
                    LOGGER.debug("correct classpath url={}", url);
                }
                LOGGER.debug("classpath url={}", url);
                path = Paths.get(url.toURI());
            }
        } catch (URISyntaxException e) {
            throw new VFS4JInvalideParameterException("path '" + file.getName() + "' invalide", e);
        }
        return path;
    }

    private Path getPath(VFS4JPathName file, VFS4JPathParameter p) {
        Path path;
        VFS4JPathParameter pathParameter = p;
        if (file.getPath() == null || file.getPath().isEmpty()) {
            path = pathParameter.getPath();
        } else {
            Path p2 = Paths.get(file.getPath()).normalize();
            p2 = removeReferenceParentInBegin(p2);
            path = pathParameter.getPath().resolve(p2);
        }
        return path;
    }

    /**
     * Supprime les .. au debut du path
     *
     * @param path
     * @return
     */
    private Path removeReferenceParentInBegin(Path path) {
        VFS4JValidationUtils.checkNotNull(path, VFS4JErrorMessages.PATH_IS_NULL);
        boolean aucunTraitement;
        do {
            aucunTraitement = true;
            String name = path.getName(0).toString();
            if (name != null && name.equals("..")) {
                path = path.subpath(1, path.getNameCount());
                aucunTraitement = false;
            }
        } while (!aucunTraitement);
        return path;
    }

    public Optional<VFS4JPathName> convertFromRealPath(Path file) {
        VFS4JValidationUtils.checkNotNull(file, VFS4JErrorMessages.PATH_IS_NULL);
        List<String> nameList = config.getNames();
        if (nameList != null && !nameList.isEmpty()) {
            Path trouve = null;
            VFS4JPathName VFS4JPathNameTrouve = null;
            Path fileNormalized = file.normalize();
            for (String name : nameList) {
                VFS4JParameter pathParameter = config.getPath(name);
                if (pathParameter instanceof VFS4JPathParameter) {
                    VFS4JPathParameter parameter = (VFS4JPathParameter) pathParameter;
                    Path path = parameter.getPath();
                    if (fileNormalized.startsWith(path)) {
                        if (trouve == null) {
                            trouve = path;
                            VFS4JPathNameTrouve = createPathName(fileNormalized, name, path);
                        } else {
                            if (trouve.getNameCount() < path.getNameCount()) {
                                trouve = path;
                                VFS4JPathNameTrouve = createPathName(fileNormalized, name, path);
                            }
                        }
                    }
                } else if (pathParameter instanceof VFS4JClasspathParameter) {
                    // on ignore
                } else {
                    throw new VFS4JInvalideParameterException("Invalide parameter " + name);
                }
            }
            return Optional.ofNullable(VFS4JPathNameTrouve);
        } else {
            return Optional.empty();
        }
    }

    private VFS4JPathName createPathName(Path fileNormalized, String name, Path path) {
        VFS4JPathName VFS4JPathNameTrouve;
        Path p = path.relativize(fileNormalized);
        VFS4JPathNameTrouve = new VFS4JPathName(name, p.toString());
        return VFS4JPathNameTrouve;
    }
}
