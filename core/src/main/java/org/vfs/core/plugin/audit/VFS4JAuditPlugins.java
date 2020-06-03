package org.vfs.core.plugin.audit;

import org.slf4j.Logger;
import org.vfs.core.api.operation.*;
import org.vfs.core.config.VFS4JConfig;
import org.vfs.core.plugin.audit.operation.AuditAttribute;
import org.vfs.core.plugin.audit.operation.AuditCommand;
import org.vfs.core.plugin.audit.operation.AuditOpen;
import org.vfs.core.plugin.audit.operation.AuditSearch;
import org.vfs.core.plugin.common.VFS4JPlugins;
import org.vfs.core.util.VFS4JLoggerFactory;

import java.util.*;

public class VFS4JAuditPlugins implements VFS4JPlugins {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(VFS4JAuditPlugins.class);

    private AuditLogLevel logLevel;

    private Set<AuditOperation> listOperations;

    private List<String> filterPath;

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

        listOperations = new HashSet<>();
        if (config.containsKey("operations")) {
            String s = config.get("operations");
            if (s != null && !s.trim().isEmpty()) {
                s = s.trim();
                if (s.contains(",")) {
                    String[] tab = s.split(",");
                    if (tab != null) {
                        for (String s2 : tab) {
                            addOperation(listOperations, s2);
                        }
                    }
                } else {
                    addOperation(listOperations, s);
                }
            }
        } else {
            // non configuré => on ajoute tout
            for (AuditOperation operation : AuditOperation.values()) {
                listOperations.add(operation);
            }
        }

        filterPath = new ArrayList<>();
        if (config.containsKey("filterPath")) {
            String s = config.get("filterPath");
            if (s != null && !s.trim().isEmpty()) {
                s = s.trim();
                if (s.contains(",")) {
                    String[] tab = s.split(",");
                    if (tab != null) {
                        for (String s2 : tab) {
                            s2 = s2.trim();
                            if (s2.length() > 0) {
                                filterPath.add(s2);
                            }
                        }
                    }
                } else {
                    filterPath.add(s);
                }
            }
        }
    }

    private void addOperation(Set<AuditOperation> listOperations, String s2) {
        s2 = s2.trim();
        if (s2.length() > 0) {
            boolean trouve = false;
            for (AuditGroupOperations groupeOperation : AuditGroupOperations.values()) {
                if (s2.equalsIgnoreCase(groupeOperation.name())) {
                    listOperations.addAll(groupeOperation.getOperations());
                    trouve = true;
                    break;
                }
            }
            if (!trouve) {
                for (AuditOperation operation : AuditOperation.values()) {
                    if (s2.equalsIgnoreCase(operation.name())) {
                        listOperations.add(operation);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public Optional<Command> getCommand(Command command) {
        return Optional.of(new AuditCommand(this, command));
    }

    @Override
    public Optional<Attribute> getAttribute(Attribute attribute) {
        return Optional.of(new AuditAttribute(this, attribute));
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
        return Optional.of(new AuditSearch(this, search));
    }

    public AuditLogLevel getLogLevel() {
        return logLevel;
    }

    public VFS4JConfig getVfs4JConfig() {
        return vfs4JConfig;
    }

    public Set<AuditOperation> getListOperations() {
        return listOperations;
    }

    public List<String> getFilterPath() {
        return filterPath;
    }
}
