package org.vfs.core.plugin.audit;

import org.slf4j.Logger;
import org.vfs.core.api.operation.*;
import org.vfs.core.config.VFS4JConfig;
import org.vfs.core.plugin.audit.operation.AuditOpen;
import org.vfs.core.plugin.common.VFS4JPlugins;
import org.vfs.core.util.VFS4JLoggerFactory;

import java.util.Map;
import java.util.Optional;

public class VFS4JAuditPlugins implements VFS4JPlugins {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(VFS4JAuditPlugins.class);

    private AuditLogLevel logLevel;

    private VFS4JConfig vfs4JConfig;

    public VFS4JAuditPlugins() {
        LOGGER.info("create VFS4JAuditPlugins");
    }

    public void init(String name, Map<String, String> config, VFS4JConfig vfs4JConfig) {
        LOGGER.info("init VFS4JAuditPlugins: {}", name);

        this.vfs4JConfig = vfs4JConfig;

        logLevel = AuditLogLevel.INFO;
        if (config.containsKey("loglevel")) {
            String s = config.get("loglevel");
            if (s != null && !s.trim().isEmpty()) {
                s = s.trim();
                AuditLogLevel trouve = null;
                for (AuditLogLevel level : AuditLogLevel.values()) {
                    if (level.name().equalsIgnoreCase(s)) {
                        trouve = level;
                        break;
                    }
                }
                logLevel = trouve;
            }
        }

    }

    @Override
    public Optional<Command> getCommand(Command command) {
        return Optional.empty();
    }

    @Override
    public Optional<Attribute> getAttribute(Attribute attribute) {
        return Optional.empty();
    }

    @Override
    public Optional<Open> getOpen(Open open) {
        return Optional.of(new AuditOpen(this, open));
    }

    @Override
    public Optional<Query> getQuery(Query query) {
        return Optional.empty();
    }

    @Override
    public Optional<Search> getSearch(Search search) {
        return Optional.empty();
    }

    public AuditLogLevel getLogLevel() {
        return logLevel;
    }

    public VFS4JConfig getVfs4JConfig() {
        return vfs4JConfig;
    }
}
