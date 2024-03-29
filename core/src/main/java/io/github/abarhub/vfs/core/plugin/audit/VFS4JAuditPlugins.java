package io.github.abarhub.vfs.core.plugin.audit;

import io.github.abarhub.vfs.core.api.operation.*;
import io.github.abarhub.vfs.core.config.VFS4JConfig;
import io.github.abarhub.vfs.core.plugin.audit.operation.*;
import io.github.abarhub.vfs.core.plugin.common.VFS4JPlugins;
import io.github.abarhub.vfs.core.util.VFS4JLoggerFactory;
import io.github.abarhub.vfs.core.util.VFS4JValidationUtils;
import org.slf4j.Logger;

import java.util.*;

public class VFS4JAuditPlugins implements VFS4JPlugins {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(VFS4JAuditPlugins.class);

    private VFS4JAuditLogLevel logLevel;

    private Set<VFS4JAuditOperation> listOperations;

    private List<String> filterPath;

    private VFS4JConfig vfs4JConfig;

    private List<VFS4JLogAudit> listener = new ArrayList<>();

    public VFS4JAuditPlugins() {
        LOGGER.info("create VFS4JAuditPlugins");
    }

    public void init(String name, Map<String, String> config, VFS4JConfig vfs4JConfig) {
        VFS4JValidationUtils.checkNotNull(name, "Name is null");
        VFS4JValidationUtils.checkNotNull(config, "Config is null");
        VFS4JValidationUtils.checkNotNull(vfs4JConfig, "vfs4JConfig is null");
        LOGGER.info("init VFS4JAuditPlugins: {}", name);

        this.vfs4JConfig = vfs4JConfig;

        // set logLevel
        initLogLevel(config);

        // set listOperations
        initOperationList(config);

        // set filterPath
        initFilterPath(config);
    }

    private void initLogLevel(Map<String, String> config) {
        logLevel = VFS4JAuditLogLevel.INFO;
        if (config.containsKey("loglevel")) {
            String s = config.get("loglevel");
            if (s != null && !s.trim().isEmpty()) {
                s = s.trim();
                VFS4JAuditLogLevel trouve = null;
                for (VFS4JAuditLogLevel level : VFS4JAuditLogLevel.values()) {
                    if (level.name().equalsIgnoreCase(s)) {
                        trouve = level;
                        break;
                    }
                }
                logLevel = trouve;
            }
        }
    }

    private void initOperationList(Map<String, String> config) {
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
            listOperations.addAll(Arrays.asList(VFS4JAuditOperation.values()));
        }
    }

    private void initFilterPath(Map<String, String> config) {
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

    private void addOperation(Set<VFS4JAuditOperation> listOperations, String s2) {
        s2 = s2.trim();
        if (s2.length() > 0) {
            boolean trouve = false;
            for (VFS4JAuditGroupOperations groupeOperation : VFS4JAuditGroupOperations.values()) {
                if (s2.equalsIgnoreCase(groupeOperation.name())) {
                    listOperations.addAll(groupeOperation.getOperations());
                    trouve = true;
                    break;
                }
            }
            if (!trouve) {
                for (VFS4JAuditOperation operation : VFS4JAuditOperation.values()) {
                    if (s2.equalsIgnoreCase(operation.name())) {
                        listOperations.add(operation);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public Optional<VFS4JCommand> getCommand(VFS4JCommand command) {
        return Optional.of(new VFS4JAuditCommand(this, command));
    }

    @Override
    public Optional<VFS4JAttribute> getAttribute(VFS4JAttribute attribute) {
        return Optional.of(new VFS4JAuditAttribute(this, attribute));
    }

    @Override
    public Optional<VFS4JOpen> getOpen(VFS4JOpen open) {
        return Optional.of(new VFS4JAuditOpen(this, open));
    }

    @Override
    public Optional<VFS4JQuery> getQuery(VFS4JQuery query) {
        return Optional.of(new VFS4JAuditQuery(this, query));
    }

    @Override
    public Optional<VFS4JSearch> getSearch(VFS4JSearch search) {
        return Optional.of(new VFS4JAuditSearch(this, search));
    }

    public VFS4JAuditLogLevel getLogLevel() {
        return logLevel;
    }

    public VFS4JConfig getVfs4JConfig() {
        return vfs4JConfig;
    }

    public Set<VFS4JAuditOperation> getListOperations() {
        return listOperations;
    }

    public List<String> getFilterPath() {
        return filterPath;
    }

    public void addListener(VFS4JLogAudit logAudit) {
        VFS4JValidationUtils.checkNotNull(logAudit, "logAudit is null");
        listener.add(logAudit);
    }

    public void removeListener(VFS4JLogAudit logAudit) {
        VFS4JValidationUtils.checkNotNull(logAudit, "logAudit is null");
        listener.remove(logAudit);
    }

    public List<VFS4JLogAudit> getListener() {
        return Collections.unmodifiableList(listener);
    }
}
