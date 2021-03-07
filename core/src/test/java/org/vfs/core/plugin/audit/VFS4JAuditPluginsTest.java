package org.vfs.core.plugin.audit;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.vfs.core.api.operation.*;
import org.vfs.core.config.VFS4JConfig;
import org.vfs.core.plugin.audit.operation.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class VFS4JAuditPluginsTest {

    private VFS4JAuditPlugins auditPlugins;

    @Nested
    class TestInit {

        @Test
        void initSansConfiguration() {
            final String name = "audit";
            final Map<String, String> config = new HashMap<>();
            final VFS4JConfig vfs4JConfig = new VFS4JConfig();

            auditPlugins = new VFS4JAuditPlugins();

            // methode testée
            auditPlugins.init(name, config, vfs4JConfig);

            // vérifications
            assertEquals(VFS4JAuditLogLevel.INFO, auditPlugins.getLogLevel());
            assertNotNull(auditPlugins.getListOperations());
            Set<VFS4JAuditOperation> set = new HashSet<>(auditPlugins.getListOperations());
            Set<VFS4JAuditOperation> setRef = new HashSet<>(Arrays.asList(VFS4JAuditOperation.values()));
            assertEquals(setRef, set);
            assertNotNull(auditPlugins.getFilterPath());
            assertTrue(auditPlugins.getFilterPath().isEmpty());

            VFS4JCommand command = mock(VFS4JCommand.class);
            Optional<VFS4JCommand> commande = auditPlugins.getCommand(command);
            assertTrue(commande.isPresent());
            assertTrue(commande.get() instanceof VFS4JAuditCommand);

            VFS4JAttribute attribute = mock(VFS4JAttribute.class);
            Optional<VFS4JAttribute> attribut = auditPlugins.getAttribute(attribute);
            assertTrue(attribut.isPresent());
            assertTrue(attribut.get() instanceof VFS4JAuditAttribute);

            VFS4JOpen open = mock(VFS4JOpen.class);
            Optional<VFS4JOpen> openAudit = auditPlugins.getOpen(open);
            assertTrue(openAudit.isPresent());
            assertTrue(openAudit.get() instanceof VFS4JAuditOpen);

            VFS4JQuery query = mock(VFS4JQuery.class);
            Optional<VFS4JQuery> queryAudit = auditPlugins.getQuery(query);
            assertTrue(queryAudit.isPresent());
            assertTrue(queryAudit.get() instanceof VFS4JAuditQuery);

            VFS4JSearch search = mock(VFS4JSearch.class);
            Optional<VFS4JSearch> searchAudit = auditPlugins.getSearch(search);
            assertTrue(searchAudit.isPresent());
            assertTrue(searchAudit.get() instanceof VFS4JAuditSearch);

            assertNotNull(auditPlugins.getVfs4JConfig());
        }

        @Test
        void initAvecConfiguration() {
            final String name = "audit";
            final Map<String, String> config = new HashMap<>();
            final VFS4JConfig vfs4JConfig = new VFS4JConfig();

            config.put("loglevel", VFS4JAuditLogLevel.DEBUG.name());
            config.put("operations", VFS4JAuditOperation.COPY + "," + VFS4JAuditOperation.EXISTS);
            config.put("filterPath", "*.txt,*.jpg");

            auditPlugins = new VFS4JAuditPlugins();

            // methode testée
            auditPlugins.init(name, config, vfs4JConfig);

            // vérifications
            assertEquals(VFS4JAuditLogLevel.DEBUG, auditPlugins.getLogLevel());
            assertNotNull(auditPlugins.getListOperations());
            Set<VFS4JAuditOperation> set = new HashSet<>(auditPlugins.getListOperations());
            Set<VFS4JAuditOperation> setRef = new HashSet<>(Arrays.asList(VFS4JAuditOperation.COPY, VFS4JAuditOperation.EXISTS));
            assertEquals(setRef, set);
            assertNotNull(auditPlugins.getFilterPath());
            assertEquals(Arrays.asList("*.txt", "*.jpg"), auditPlugins.getFilterPath());
        }

        @Test
        void initAvecConfiguration2() {
            final String name = "audit";
            final Map<String, String> config = new HashMap<>();
            final VFS4JConfig vfs4JConfig = new VFS4JConfig();

            config.put("loglevel", VFS4JAuditLogLevel.ERROR.name());
            config.put("operations", VFS4JAuditOperation.COPY.name());
            config.put("filterPath", "*.txt");

            auditPlugins = new VFS4JAuditPlugins();

            // methode testée
            auditPlugins.init(name, config, vfs4JConfig);

            // vérifications
            assertEquals(VFS4JAuditLogLevel.ERROR, auditPlugins.getLogLevel());
            assertNotNull(auditPlugins.getListOperations());
            Set<VFS4JAuditOperation> set = new HashSet<>(auditPlugins.getListOperations());
            Set<VFS4JAuditOperation> setRef = new HashSet<>(Collections.singletonList(VFS4JAuditOperation.COPY));
            assertEquals(setRef, set);
            assertNotNull(auditPlugins.getFilterPath());
            assertEquals(Collections.singletonList("*.txt"), auditPlugins.getFilterPath());
        }

        @Test
        void initAvecConfigurationVide() {
            final String name = "audit";
            final Map<String, String> config = new HashMap<>();
            final VFS4JConfig vfs4JConfig = new VFS4JConfig();

            config.put("loglevel", VFS4JAuditLogLevel.DEBUG.name());
            config.put("operations", null);
            config.put("filterPath", null);

            auditPlugins = new VFS4JAuditPlugins();

            // methode testée
            auditPlugins.init(name, config, vfs4JConfig);

            // vérifications
            assertEquals(VFS4JAuditLogLevel.DEBUG, auditPlugins.getLogLevel());
            assertNotNull(auditPlugins.getListOperations());
            Set<VFS4JAuditOperation> set = new HashSet<>(auditPlugins.getListOperations());
            Set<VFS4JAuditOperation> setRef = new HashSet<>();
            assertEquals(setRef, set);
            assertNotNull(auditPlugins.getFilterPath());
            assertTrue(auditPlugins.getFilterPath().isEmpty());
        }

        @Test
        void initAvecConfigurationOperationDouble() {
            final String name = "audit";
            final Map<String, String> config = new HashMap<>();
            final VFS4JConfig vfs4JConfig = new VFS4JConfig();

            config.put("loglevel", VFS4JAuditLogLevel.DEBUG.name());
            config.put("operations", VFS4JAuditOperation.COPY + "," + VFS4JAuditOperation.EXISTS);
            config.put("filterPath", "*.txt,*.jpg");

            auditPlugins = new VFS4JAuditPlugins();

            // methode testée
            auditPlugins.init(name, config, vfs4JConfig);

            // vérifications
            assertEquals(VFS4JAuditLogLevel.DEBUG, auditPlugins.getLogLevel());
            assertNotNull(auditPlugins.getListOperations());
            Set<VFS4JAuditOperation> set = new HashSet<>(auditPlugins.getListOperations());
            Set<VFS4JAuditOperation> setRef = new HashSet<>(Arrays.asList(VFS4JAuditOperation.COPY,
                    VFS4JAuditOperation.EXISTS, VFS4JAuditOperation.COPY));
            assertEquals(setRef, set);
            assertNotNull(auditPlugins.getFilterPath());
            assertEquals(Arrays.asList("*.txt", "*.jpg"), auditPlugins.getFilterPath());
        }

        @Test
        void initAvecConfigurationGroupeOperation() {
            final String name = "audit";
            final Map<String, String> config = new HashMap<>();
            final VFS4JConfig vfs4JConfig = new VFS4JConfig();

            config.put("loglevel", VFS4JAuditLogLevel.DEBUG.name());
            config.put("operations", VFS4JAuditGroupOperations.QUERY.name());
            config.put("filterPath", "*.txt,*.jpg");

            auditPlugins = new VFS4JAuditPlugins();

            // methode testée
            auditPlugins.init(name, config, vfs4JConfig);

            // vérifications
            assertEquals(VFS4JAuditLogLevel.DEBUG, auditPlugins.getLogLevel());
            assertNotNull(auditPlugins.getListOperations());
            Set<VFS4JAuditOperation> set = new HashSet<>(auditPlugins.getListOperations());
            Set<VFS4JAuditOperation> setRef = new HashSet<>(VFS4JAuditGroupOperations.QUERY.getOperations());
            assertEquals(setRef, set);
            assertNotNull(auditPlugins.getFilterPath());
            assertEquals(Arrays.asList("*.txt", "*.jpg"), auditPlugins.getFilterPath());
        }
    }

    @Nested
    class TestListener {


        @Test
        void addListener() {
            final String name = "audit";
            final Map<String, String> config = new HashMap<>();
            final VFS4JConfig vfs4JConfig = new VFS4JConfig();

            auditPlugins = new VFS4JAuditPlugins();
            auditPlugins.init(name, config, vfs4JConfig);

            VFS4JLogAudit logAudit = mock(VFS4JLogAudit.class);

            // methode testée
            auditPlugins.addListener(logAudit);

            // vérifications
            assertNotNull(auditPlugins.getListener());
            assertEquals(1, auditPlugins.getListener().size());
            assertEquals(logAudit, auditPlugins.getListener().get(0));
        }

        @Test
        void removeListener() {
            final String name = "audit";
            final Map<String, String> config = new HashMap<>();
            final VFS4JConfig vfs4JConfig = new VFS4JConfig();

            auditPlugins = new VFS4JAuditPlugins();
            auditPlugins.init(name, config, vfs4JConfig);

            VFS4JLogAudit logAudit = mock(VFS4JLogAudit.class);

            auditPlugins.addListener(logAudit);

            assertNotNull(auditPlugins.getListener());
            assertEquals(1, auditPlugins.getListener().size());
            assertEquals(logAudit, auditPlugins.getListener().get(0));

            // methode testée
            auditPlugins.removeListener(logAudit);

            // vérifications
            assertNotNull(auditPlugins.getListener());
            assertTrue(auditPlugins.getListener().isEmpty());
        }

    }

}