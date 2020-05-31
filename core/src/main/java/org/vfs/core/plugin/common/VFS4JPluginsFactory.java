package org.vfs.core.plugin.common;

import java.util.Map;

public interface VFS4JPluginsFactory {

    VFS4JPlugins createPlugins(String name, Map<String,String> config);

}
