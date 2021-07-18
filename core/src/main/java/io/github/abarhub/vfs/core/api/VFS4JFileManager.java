package io.github.abarhub.vfs.core.api;

import io.github.abarhub.vfs.core.api.operation.*;
import io.github.abarhub.vfs.core.config.VFS4JConfig;
import io.github.abarhub.vfs.core.config.VFS4JParameter;
import io.github.abarhub.vfs.core.plugin.common.VFS4JPlugins;
import io.github.abarhub.vfs.core.util.VFS4JConvertFile;
import io.github.abarhub.vfs.core.util.VFS4JValidationUtils;

import java.nio.file.Path;
import java.util.Optional;

public class VFS4JFileManager {

    private VFS4JConfig vfs4JConfig;
    private VFS4JCommand command;
    private VFS4JQuery query;
    private VFS4JOpen open;
    private VFS4JSearch search;
    private VFS4JAttribute attribute;
    private VFS4JConvertFile convertFile;

    public VFS4JFileManager() {
        this(new VFS4JConfig());
    }

    public VFS4JFileManager(VFS4JConfig vfs4JConfig) {
        VFS4JValidationUtils.checkNotNull(vfs4JConfig, "vfs4JConfig is null");
        this.vfs4JConfig = vfs4JConfig;
        convertFile = new VFS4JConvertFile(vfs4JConfig);
        initOperations();
    }

    private void initOperations() {

        command = new VFS4JSimpleCommand(this);
        query = new VFS4JSimpleQuery(this);
        open = new VFS4JSimpleOpen(this);
        search = new VFS4JSimpleSearch(this);
        attribute = new VFS4JSimpleAttribute(this);

        if (vfs4JConfig.getPluginsOrder() != null &&
                !vfs4JConfig.getPluginsOrder().isEmpty()) {


            for (String name : vfs4JConfig.getPluginsOrder()) {
                VFS4JPlugins plugins = vfs4JConfig.getPlugins(name);
                if (plugins != null) {
                    Optional<VFS4JAttribute> attributeOpt = plugins.getAttribute(attribute);
                    if (attributeOpt.isPresent()) {
                        attribute = attributeOpt.get();
                    }
                    Optional<VFS4JCommand> commandOptional = plugins.getCommand(command);
                    if (commandOptional.isPresent()) {
                        command = commandOptional.get();
                    }
                    Optional<VFS4JQuery> queryOptional = plugins.getQuery(query);
                    if (queryOptional.isPresent()) {
                        query = queryOptional.get();
                    }
                    Optional<VFS4JOpen> optionalOpen = plugins.getOpen(open);
                    if (optionalOpen.isPresent()) {
                        open = optionalOpen.get();
                    }
                    Optional<VFS4JSearch> optionalSearch = plugins.getSearch(search);
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
        VFS4JValidationUtils.checkNotNull(config, "config is null");
        this.vfs4JConfig = config;
        convertFile = new VFS4JConvertFile(vfs4JConfig);
        initOperations();
    }

    public VFS4JParameter getPath(String name) {
        VFS4JValidationUtils.checkNotEmpty(name, "Name is empty");
        return vfs4JConfig.getPath(name);
    }

    protected Path getRealFile(VFS4JPathName file) {
        VFS4JValidationUtils.checkNotNull(file, "Path is null");
        return convertFile.getRealFile(file);
    }

    public VFS4JCommand getCommand() {
        return command;
    }

    public Optional<VFS4JPathName> convertFromRealPath(Path file) {
        VFS4JValidationUtils.checkNotNull(file, "Path is null");
        return convertFile.convertFromRealPath(file);
    }

    public VFS4JQuery getQuery() {
        return query;
    }

    public VFS4JOpen getOpen() {
        return open;
    }

    public VFS4JSearch getSearch() {
        return search;
    }

    public VFS4JAttribute getAttribute() {
        return attribute;
    }
}
