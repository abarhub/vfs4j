package org.vfs.core.util;

import org.slf4j.Logger;
import org.vfs.core.api.PathName;
import org.vfs.core.config.*;
import org.vfs.core.exception.VFS4JInvalideParameterException;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ConvertFile {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(ConvertFile.class);

    private final VFS4JConfig vfs4JConfig;

    public ConvertFile(VFS4JConfig vfs4JConfig) {
        ValidationUtils.checkNotNull(vfs4JConfig, "vfsConfig is null");
        this.vfs4JConfig = vfs4JConfig;
    }

    public Path getRealFile(PathName file) {
        ValidationUtils.checkNotNull(file, "Path is null");
        VFS4JParameter p = vfs4JConfig.getPath(file.getName());
        if (p == null) {
            throw new VFS4JInvalideParameterException("PathName '" + file.getName() + "' doesn't exist");
        }
        Path path;
        if (p.getMode() == VFS4JPathMode.CLASSPATH) {
            VFS4JClasspathParameter parameter = (VFS4JClasspathParameter) p;
            try {
                String directory = "";
                LOGGER.info("parameter.getPath()={}", parameter.getPath());
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
                    LOGGER.info("p2={}", p2);
                    URL url = ClassLoader.getSystemResource(p2.toString());
                    LOGGER.info("getSystemResource={}", url);
                    path = Paths.get(ClassLoader.getSystemResource(p2.toString()).toURI());
                }
            } catch (URISyntaxException e) {
                throw new VFS4JInvalideParameterException("path '" + file.getName() + "' invalide", e);
            }
        } else {
            if (p instanceof PathParameter) {
                PathParameter pathParameter = (PathParameter) p;
                if (file.getPath() == null || file.getPath().isEmpty()) {
                    path = pathParameter.getPath();
                } else {
                    Path p2 = Paths.get(file.getPath()).normalize();
                    p2 = removeReferenceParentInBegin(p2);
                    path = pathParameter.getPath().resolve(p2);
                }
            } else {
                throw new VFS4JInvalideParameterException("path type '" + file.getName() + "' invalide");
            }
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
        ValidationUtils.checkNotNull(path, "Path is null");
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

    public Optional<PathName> convertFromRealPath(Path file) {
        ValidationUtils.checkNotNull(file, "Path is null");
        List<String> nameList = vfs4JConfig.getNames();
        if (nameList != null && !nameList.isEmpty()) {
            Path trouve = null;
            PathName pathNameTrouve = null;
            Path fileNormalized = file.normalize();
            for (String name : nameList) {
                VFS4JParameter pathParameter = vfs4JConfig.getPath(name);
                if (pathParameter instanceof PathParameter) {
                    PathParameter parameter = (PathParameter) pathParameter;
                    Path path = parameter.getPath();
                    if (fileNormalized.startsWith(path)) {
                        if (trouve == null) {
                            trouve = path;
                            pathNameTrouve = createPathName(fileNormalized, name, path);
                        } else {
                            if (trouve.getNameCount() < path.getNameCount()) {
                                trouve = path;
                                pathNameTrouve = createPathName(fileNormalized, name, path);
                            }
                        }
                    }
                } else {
                    throw new VFS4JInvalideParameterException("Invalide parameter " + name);
                }
            }
            return Optional.ofNullable(pathNameTrouve);
        } else {
            return Optional.empty();
        }
    }

    private PathName createPathName(Path fileNormalized, String name, Path path) {
        PathName pathNameTrouve;
        Path p = path.relativize(fileNormalized);
        pathNameTrouve = new PathName(name, p.toString());
        return pathNameTrouve;
    }

    private Path getNormalizedPath(String path) {
        Path p = Paths.get(path).normalize();
        p = removeReferenceParentInBegin(p);
        return p;
    }
}
