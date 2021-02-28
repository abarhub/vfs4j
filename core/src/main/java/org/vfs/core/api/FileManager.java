package org.vfs.core.api;

import org.vfs.core.api.operation.*;
import org.vfs.core.config.VFS4JConfig;
import org.vfs.core.config.VFS4JParameter;
import org.vfs.core.plugin.common.VFS4JPlugins;
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
        this(new VFS4JConfig());
    }

    public FileManager(VFS4JConfig vfs4JConfig) {
        ValidationUtils.checkNotNull(vfs4JConfig, "vfs4JConfig is null");
        this.vfs4JConfig = vfs4JConfig;
        convertFile = new ConvertFile(vfs4JConfig);
        initOperations();
    }

    private void initOperations() {

        command = new SimpleCommand(this);
        query = new SimpleQuery(this);
        open = new SimpleOpen(this);
        search = new SimpleSearch(this);
        attribute = new SimpleAttribute(this);

        if (vfs4JConfig.getPluginsOrder() != null &&
                !vfs4JConfig.getPluginsOrder().isEmpty()) {


            for (String name : vfs4JConfig.getPluginsOrder()) {
                VFS4JPlugins plugins = vfs4JConfig.getPlugins(name);
                if (plugins != null) {
                    Optional<Attribute> attributeOpt = plugins.getAttribute(attribute);
                    if (attributeOpt.isPresent()) {
                        attribute = attributeOpt.get();
                    }
                    Optional<Command> commandOptional = plugins.getCommand(command);
                    if (commandOptional.isPresent()) {
                        command = commandOptional.get();
                    }
                    Optional<Query> queryOptional = plugins.getQuery(query);
                    if (queryOptional.isPresent()) {
                        query = queryOptional.get();
                    }
                    Optional<Open> optionalOpen = plugins.getOpen(open);
                    if (optionalOpen.isPresent()) {
                        open = optionalOpen.get();
                    }
                    Optional<Search> optionalSearch = plugins.getSearch(search);
                    if (optionalSearch.isPresent()) {
                        search = optionalSearch.get();
                    }
                }
            }
        }
    }

    public VFS4JConfig getConfig() {
        return vfs4JConfig;
    }

    public void setConfig(VFS4JConfig config) {
        ValidationUtils.checkNotNull(config, "config is null");
        this.vfs4JConfig = config;
        convertFile = new ConvertFile(vfs4JConfig);
        initOperations();
    }

    public VFS4JParameter getPath(String name) {
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
