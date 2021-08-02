package io.github.abarhub.vfs.core.plugin.unclosed;

import io.github.abarhub.vfs.core.api.operation.*;
import io.github.abarhub.vfs.core.config.VFS4JConfig;
import io.github.abarhub.vfs.core.plugin.common.VFS4JPlugins;
import io.github.abarhub.vfs.core.util.VFS4JLoggerFactory;
import io.github.abarhub.vfs.core.util.VFS4JValidationUtils;
import org.slf4j.Logger;

import java.util.*;

public class VFS4JUnclosedPlugins implements VFS4JPlugins {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(VFS4JUnclosedPlugins.class);

    private Thread thread;
    private UnclosableRunnable unclosableRunnable;
    private UnclosedConfig unclosedConfig;
    private List<VFS4JLogUnclosed> listener = new ArrayList<>();

    public void init(String name, Map<String, String> config, VFS4JConfig vfs4JConfig) {
        VFS4JValidationUtils.checkNotNull(name, "Name is null");
        VFS4JValidationUtils.checkNotNull(config, "Config is null");
        VFS4JValidationUtils.checkNotNull(vfs4JConfig, "vfs4JConfig is null");
        LOGGER.debug("init: {}", name);

        unclosedConfig = createConfig(config);

        this.unclosableRunnable = newUnclosableRunnable(unclosedConfig);

        thread = new Thread(this.unclosableRunnable, getClass().getSimpleName() + "." + name);
        thread.setDaemon(true);
        thread.start();
    }

    private UnclosedConfig createConfig(Map<String, String> config) {
        UnclosedConfig unclosedConfig = new UnclosedConfig();

        addLogLevel(config, unclosedConfig);

        addOperations(config, unclosedConfig);

        addFilter(config, unclosedConfig);

        addOtherConfig(config, unclosedConfig);

        return unclosedConfig;
    }

    private void addOtherConfig(Map<String, String> config, UnclosedConfig unclosedConfig) {
        if (isTrue(config, "logopen")) {
            unclosedConfig.setLogOpen(true);
        }
        if (isTrue(config, "logclose")) {
            unclosedConfig.setLogClose(true);
        }
        if (isTrue(config, "exceptionlogopen")) {
            unclosedConfig.setExceptionLogOpen(true);
        }
        if (isTrue(config, "exceptionlogclose")) {
            unclosedConfig.setExceptionLogClose(true);
        }
        if (config.containsKey("logIfNotClosedAfterMs")) {
            String value = config.get("logIfNotClosedAfterMs");
            if (value != null && value.trim().length() > 0) {
                try {
                    long valueLong = Long.parseLong(value.trim());
                    if (valueLong > 0) {
                        unclosedConfig.setLogIfNotClosedAfterMs(valueLong);
                    }
                } catch (NumberFormatException e) {
                    LOGGER.error("Error for parameter logIfNotClosedAfterMs (value:{})", value);
                }
            }
        }
    }

    private boolean isTrue(Map<String, String> config, String name) {
        if (config.containsKey(name)) {
            String value = config.get(name);
            return value != null && value.trim().equals("true");
        } else {
            return false;
        }
    }

    private void addFilter(Map<String, String> config, UnclosedConfig unclosedConfig) {
        List<String> filterPath = new ArrayList<>();
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
        unclosedConfig.setFilterPath(filterPath);
    }

    private void addOperations(Map<String, String> config, UnclosedConfig unclosedConfig) {
        Set<VFS4JUnclosedOperation> listOperations = new HashSet<>();
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
            listOperations.addAll(Arrays.asList(VFS4JUnclosedOperation.values()));
        }
        unclosedConfig.setOperations(listOperations);
    }

    private void addLogLevel(Map<String, String> config, UnclosedConfig unclosedConfig) {
        VFS4JUnclosedLogLevel logLevel = VFS4JUnclosedLogLevel.DEBUG;
        if (config.containsKey("loglevel")) {
            String s = config.get("loglevel");
            if (s != null && !s.trim().isEmpty()) {
                s = s.trim();
                VFS4JUnclosedLogLevel trouve = null;
                for (VFS4JUnclosedLogLevel level : VFS4JUnclosedLogLevel.values()) {
                    if (level.name().equalsIgnoreCase(s)) {
                        trouve = level;
                        break;
                    }
                }
                logLevel = trouve;
            }
        }
        unclosedConfig.setLoglevel(logLevel);
    }

    private void addOperation(Set<VFS4JUnclosedOperation> listOperations, String s2) {
        s2 = s2.trim();
        if (s2.length() > 0) {
            for (VFS4JUnclosedOperation operation : VFS4JUnclosedOperation.values()) {
                if (s2.equalsIgnoreCase(operation.name())) {
                    listOperations.add(operation);
                    break;
                }
            }
        }
    }

    protected UnclosableRunnable newUnclosableRunnable(UnclosedConfig config) {
        return new UnclosableRunnable(config, this);
    }

    @Override
    public Optional<VFS4JCommand> getCommand(VFS4JCommand command) {
        return Optional.empty();
    }

    @Override
    public Optional<VFS4JAttribute> getAttribute(VFS4JAttribute attribute) {
        return Optional.empty();
    }

    @Override
    public Optional<VFS4JOpen> getOpen(VFS4JOpen open) {
        return Optional.of(new VFS4JUnclosedOpen(this, open, this.unclosableRunnable));
    }

    @Override
    public Optional<VFS4JQuery> getQuery(VFS4JQuery query) {
        return Optional.empty();
    }

    @Override
    public Optional<VFS4JSearch> getSearch(VFS4JSearch search) {
        return Optional.empty();
    }

    public UnclosableRunnable getUnclosableRunnable() {
        return unclosableRunnable;
    }

    public Thread getThread() {
        return thread;
    }

    public List<VFS4JLogUnclosed> getListener() {
        return listener;
    }

    public UnclosedConfig getConfig() {
        return unclosedConfig;
    }
}
