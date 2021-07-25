package io.github.abarhub.vfs.core.plugin.audit;

import io.github.abarhub.vfs.core.config.VFS4JConfig;
import io.github.abarhub.vfs.core.plugin.common.VFS4JPlugins;
import io.github.abarhub.vfs.core.plugin.common.VFS4JPluginsFactory;
import io.github.abarhub.vfs.core.util.VFS4JLoggerFactory;
import org.slf4j.Logger;

import java.util.Map;

public class VFS4JAuditPluginsFactory implements VFS4JPluginsFactory {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(VFS4JAuditPluginsFactory.class);

    public VFS4JAuditPluginsFactory() {
        LOGGER.debug("create");
    }

    @Override
    public VFS4JPlugins createPlugins(String name, Map<String, String> config, VFS4JConfig vfs4JConfig) {
        VFS4JAuditPlugins auditPlugins = new VFS4JAuditPlugins();
        auditPlugins.init(name, config, vfs4JConfig);
        return auditPlugins;
    }
}
