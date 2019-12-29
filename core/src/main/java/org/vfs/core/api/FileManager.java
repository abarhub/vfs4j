package org.vfs.core.api;

import org.vfs.core.api.operation.*;
import org.vfs.core.config.Parameter;
import org.vfs.core.config.VFSConfig;
import org.vfs.core.util.ConvertFile;
import org.vfs.core.util.ValidationUtils;

import java.nio.file.Path;
import java.util.Optional;

public class FileManager {

    private VFSConfig vfsConfig;
    private Command command;
    private Query query;
    private Open open;
    private Search search;
    private ConvertFile convertFile;

    public FileManager() {
        vfsConfig=new VFSConfig();
        command=new SimpleCommand(this);
        convertFile=new ConvertFile(vfsConfig);
        query=new SimpleQuery(this);
        open=new SimpleOpen(this);
        search=new SimpleSearch(this);
    }

    public FileManager(FileManagerBuilder fileManagerBuilder) {
        vfsConfig=fileManagerBuilder.build();
        command=new SimpleCommand(this);
        convertFile=new ConvertFile(vfsConfig);
        query=new SimpleQuery(this);
        open=new SimpleOpen(this);
        search=new SimpleSearch(this);
    }

    public void addPath(String name, Path path){
        ValidationUtils.checkNotEmpty(name,"Name is empty");
        ValidationUtils.checkNotNull(path,"Path is null");
        vfsConfig.addPath(name,path,false);
    }

    public void addPathRealOnly(String name, Path path){
        ValidationUtils.checkNotEmpty(name,"Name is empty");
        ValidationUtils.checkNotNull(path,"Path is null");
        vfsConfig.addPath(name,path,true);
    }

    public Parameter getPath(String name){
        ValidationUtils.checkNotEmpty(name,"Name is empty");
        return vfsConfig.getPath(name);
    }

    protected Path getRealFile(PathName file) {
        ValidationUtils.checkNotNull(file,"Path is null");
        return convertFile.getRealFile(file);
    }

    public Command getCommand() {
        return command;
    }

    public Optional<PathName> convertFromRealPath(Path file) {
        ValidationUtils.checkNotNull(file,"Path is null");
        return convertFile.convertFromRealPath(file);
    }

    public Query getQuery() {
        return query;
    }

    public Open getOpen() {
        return open;
    }

    public Search getSearch() {
        return search;
    }
}
