package org.vfs.core.plugin.audit;

import org.slf4j.Logger;
import org.vfs.core.plugin.common.VFS4JPlugins;
import org.vfs.core.util.VFS4JLoggerFactory;

import java.util.Map;

public class VFS4JAuditPlugins implements VFS4JPlugins {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(VFS4JAuditPlugins.class);

    public VFS4JAuditPlugins() {
        LOGGER.info("create VFS4JAuditPlugins");
    }

    public void init(String name, Map<String, String> config) {
        LOGGER.info("init VFS4JAuditPlugins: {}", name);

    }

}
