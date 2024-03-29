package io.github.abarhub.vfs.core.plugin.common;

import io.github.abarhub.vfs.core.config.VFS4JConfig;

import java.util.Map;

public interface VFS4JPluginsFactory {

    VFS4JPlugins createPlugins(String name, Map<String, String> config, VFS4JConfig vfs4JConfig);

}
