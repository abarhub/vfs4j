package io.github.abarhub.vfs.core.plugin.audit;

import io.github.abarhub.vfs.core.config.VFS4JConfig;
import io.github.abarhub.vfs.core.plugin.common.VFS4JPlugins;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class VFS4JAuditPluginsFactoryTest {

    @Test
    void createPlugins() {
        final String name = "audit";
        final Map<String, String> config = new HashMap<>();
        final VFS4JConfig vfs4JConfig = new VFS4JConfig();

        VFS4JAuditPluginsFactory auditPluginsFactory = new VFS4JAuditPluginsFactory();

        // methode testée
        VFS4JPlugins resultat = auditPluginsFactory.createPlugins(name, config, vfs4JConfig);

        // vérifications
        assertNotNull(resultat);
    }
}