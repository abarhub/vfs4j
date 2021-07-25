package io.github.abarhub.vfs.core.plugin.unclosed;

import io.github.abarhub.vfs.core.api.operation.*;
import io.github.abarhub.vfs.core.config.VFS4JConfig;
import io.github.abarhub.vfs.core.plugin.common.VFS4JPlugins;
import io.github.abarhub.vfs.core.util.VFS4JLoggerFactory;
import io.github.abarhub.vfs.core.util.VFS4JValidationUtils;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Optional;

public class VFS4JUnclosedPlugins implements VFS4JPlugins {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(VFS4JUnclosedPlugins.class);

    private Thread thread;
    private UnclosableRunnable unclosableRunnable;

    public void init(String name, Map<String, String> config, VFS4JConfig vfs4JConfig) {
        VFS4JValidationUtils.checkNotNull(name, "Name is null");
        VFS4JValidationUtils.checkNotNull(config, "Config is null");
        VFS4JValidationUtils.checkNotNull(vfs4JConfig, "vfs4JConfig is null");
        LOGGER.debug("init: {}", name);
        this.unclosableRunnable = newUnclosableRunnable();

        thread = new Thread(this.unclosableRunnable, getClass().getSimpleName() + "." + name);
        thread.setDaemon(true);
        thread.start();
    }

    protected UnclosableRunnable newUnclosableRunnable(){
        return new UnclosableRunnable();
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
}
