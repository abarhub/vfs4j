package org.vfs.core.api;

import org.vfs.core.api.operation.*;
import org.vfs.core.config.PathParameter;
import org.vfs.core.config.VFS4JConfig;
import org.vfs.core.util.ConvertFile;
import org.vfs.core.util.ValidationUtils;

import java.nio.file.Path;
import java.util.Optional;

public class FileManager {

    private VFS4JConfig vfs4JConfig;
    private Command command;
    private Query query;
    private Open open;
    private Search search;
    private Attribute attribute;
    private ConvertFile convertFile;

    public FileManager() {
        vfs4JConfig = new VFS4JConfig();
        command = new SimpleCommand(this);
        convertFile = new ConvertFile(vfs4JConfig);
        query = new SimpleQuery(this);
        open = new SimpleOpen(this);
        search = new SimpleSearch(this);
        attribute = new SimpleAttribute(this);
    }

    public FileManager(FileManagerBuilder fileManagerBuilder) {
        vfs4JConfig = fileManagerBuilder.build();
        command = new SimpleCommand(this);
        convertFile = new ConvertFile(vfs4JConfig);
        query = new SimpleQuery(this);
        open = new SimpleOpen(this);
        search = new SimpleSearch(this);
        attribute = new SimpleAttribute(this);
    }

    public void addPath(String name, Path path) {
        ValidationUtils.checkNotEmpty(name, "Name is empty");
        ValidationUtils.checkNotNull(path, "Path is null");
        vfs4JConfig.addPath(name, path, false);
    }

    public void addPathRealOnly(String name, Path path) {
        ValidationUtils.checkNotEmpty(name, "Name is empty");
        ValidationUtils.checkNotNull(path, "Path is null");
        vfs4JConfig.addPath(name, path, true);
    }

    public PathParameter getPath(String name) {
        ValidationUtils.checkNotEmpty(name, "Name is empty");
        return vfs4JConfig.getPath(name);
    }

    protected Path getRealFile(PathName file) {
        ValidationUtils.checkNotNull(file, "Path is null");
        return convertFile.getRealFile(file);
    }

    public Command getCommand() {
        return command;
    }

    public Optional<PathName> convertFromRealPath(Path file) {
        ValidationUtils.checkNotNull(file, "Path is null");
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

    public Attribute getAttribute() {
        return attribute;
    }
}
