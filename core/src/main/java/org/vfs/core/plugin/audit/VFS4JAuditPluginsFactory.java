package org.vfs.core.plugin.audit;

import org.slf4j.Logger;
import org.vfs.core.config.VFS4JConfig;
import org.vfs.core.plugin.common.VFS4JPlugins;
import org.vfs.core.plugin.common.VFS4JPluginsFactory;
import org.vfs.core.util.VFS4JLoggerFactory;

import java.util.Map;

public class VFS4JAuditPluginsFactory implements VFS4JPluginsFactory {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(VFS4JAuditPluginsFactory.class);

    public VFS4JAuditPluginsFactory() {
        LOGGER.info("create");
    }

    @Override
    public VFS4JPlugins createPlugins(String name, Map<String, String> config, VFS4JConfig vfs4JConfig) {
        VFS4JAuditPlugins auditPlugins=new VFS4JAuditPlugins();
        auditPlugins.init(name,config, vfs4JConfig);
        return auditPlugins;
    }
}
