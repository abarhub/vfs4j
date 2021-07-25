package io.github.abarhub.vfs.core.plugin.unclosed;

import io.github.abarhub.vfs.core.config.VFS4JConfig;
import io.github.abarhub.vfs.core.plugin.common.VFS4JPlugins;
import io.github.abarhub.vfs.core.plugin.common.VFS4JPluginsFactory;

import java.util.Map;

public class VFS4JUnclosedPluginsFactory implements VFS4JPluginsFactory {

    @Override
    public VFS4JPlugins createPlugins(String name, Map<String, String> config, VFS4JConfig vfs4JConfig) {
        VFS4JUnclosedPlugins unclosedPlugins = new VFS4JUnclosedPlugins();
        unclosedPlugins.init(name, config, vfs4JConfig);
        return unclosedPlugins;
    }
}
