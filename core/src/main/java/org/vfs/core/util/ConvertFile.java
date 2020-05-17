package org.vfs.core.util;

import org.vfs.core.config.PathParameter;
import org.vfs.core.config.VFS4JConfig;
import org.vfs.core.api.PathName;
import org.vfs.core.exception.VFS4JInvalideParameterException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class ConvertFile {

    private final VFS4JConfig vfs4JConfig;

    public ConvertFile(VFS4JConfig vfs4JConfig) {
        ValidationUtils.checkNotNull(vfs4JConfig,"vfsConfig is null");
        this.vfs4JConfig = vfs4JConfig;
    }

    public Path getRealFile(PathName file){
        ValidationUtils.checkNotNull(file,"Path is null");
        PathParameter p= vfs4JConfig.getPath(file.getName());
        if(p==null){
            throw new VFS4JInvalideParameterException("PathName '"+file.getName()+"' doesn't exist");
        }
        Path path;
        if(file.getPath()==null||file.getPath().isEmpty()){
            path=p.getPath();
        } else {
            Path p2=Paths.get(file.getPath()).normalize();
            p2 = removeReferenceParentInBegin(p2);
            path=p.getPath().resolve(p2);
        }
        return path;
    }

    /**
     * Supprime les .. au debut du path
     * @param path
     * @return
     */
    private Path removeReferenceParentInBegin(Path path) {
        ValidationUtils.checkNotNull(path,"Path is null");
        boolean aucunTraitement;
        do {
            aucunTraitement=true;
            String name=path.getName(0).toString();
            if (name!=null&&name.equals("..")) {
                path = path.subpath(1, path.getNameCount());
                aucunTraitement=false;
            }
        } while(!aucunTraitement);
        return path;
    }

    public Optional<PathName> convertFromRealPath(Path file) {
        ValidationUtils.checkNotNull(file,"Path is null");
        List<String> nameList= vfs4JConfig.getNames();
        if(nameList!=null&&!nameList.isEmpty()){
            Path trouve=null;
            PathName pathNameTrouve=null;
            Path fileNormalized=file.normalize();
            for(String name:nameList){
                PathParameter pathParameter = vfs4JConfig.getPath(name);
                Path path = pathParameter.getPath();
                if(fileNormalized.startsWith(path)){
                    if(trouve==null){
                        trouve=path;
                        pathNameTrouve = createPathName(fileNormalized, name, path);
                    } else {
                        if(trouve.getNameCount()<path.getNameCount()){
                            trouve=path;
                            pathNameTrouve = createPathName(fileNormalized, name, path);
                        }
                    }
                }
            }
            return Optional.ofNullable(pathNameTrouve);
        } else {
            return Optional.empty();
        }
    }

    private PathName createPathName(Path fileNormalized, String name, Path path) {
        PathName pathNameTrouve;
        Path p=path.relativize(fileNormalized);
        pathNameTrouve=new PathName(name, p.toString());
        return pathNameTrouve;
    }

    private Path getNormalizedPath(String path){
        Path p=Paths.get(path).normalize();
        p = removeReferenceParentInBegin(p);
        return p;
    }
}
