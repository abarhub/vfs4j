package io.github.abarhub.vfs.core.plugin.unclosed;

import io.github.abarhub.vfs.core.api.operation.*;
import io.github.abarhub.vfs.core.config.VFS4JConfig;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class VFS4JUnclosedPluginsTest {


    private VFS4JUnclosedPlugins unclosedPlugins;

    @Nested
    class TestInit {

        @Test
        void initSansConfiguration() {
            final String name = "unclosed";
            final Map<String, String> config = new HashMap<>();
            final VFS4JConfig vfs4JConfig = new VFS4JConfig();

            unclosedPlugins = new VFS4JUnclosedPlugins();

            // methode testée
            unclosedPlugins.init(name, config, vfs4JConfig);

            // vérifications
            UnclosedConfig config2 = unclosedPlugins.getConfig();
            assertNotNull(config2);
            assertEquals(VFS4JUnclosedLogLevel.DEBUG, config2.getLoglevel());
            assertNotNull(unclosedPlugins.getConfig().getOperations());
            Set<VFS4JUnclosedOperation> set = new HashSet<>(config2.getOperations());
            Set<VFS4JUnclosedOperation> setRef = new HashSet<>(Arrays.asList(VFS4JUnclosedOperation.values()));
            assertEquals(setRef, set);
            assertNotNull(config2.getFilterPath());
            assertTrue(config2.getFilterPath().isEmpty());

            VFS4JCommand command = mock(VFS4JCommand.class);
            Optional<VFS4JCommand> commande = unclosedPlugins.getCommand(command);
            assertFalse(commande.isPresent());

            VFS4JAttribute attribute = mock(VFS4JAttribute.class);
            Optional<VFS4JAttribute> attribut = unclosedPlugins.getAttribute(attribute);
            assertFalse(attribut.isPresent());

            VFS4JOpen open = mock(VFS4JOpen.class);
            Optional<VFS4JOpen> openUnclosed = unclosedPlugins.getOpen(open);
            assertTrue(openUnclosed.isPresent());
            assertTrue(openUnclosed.get() instanceof VFS4JUnclosedOpen);

            VFS4JQuery query = mock(VFS4JQuery.class);
            Optional<VFS4JQuery> queryAudit = unclosedPlugins.getQuery(query);
            assertFalse(queryAudit.isPresent());

            VFS4JSearch search = mock(VFS4JSearch.class);
            Optional<VFS4JSearch> searchAudit = unclosedPlugins.getSearch(search);
            assertFalse(searchAudit.isPresent());

        }

        @Test
        void initAvecConfiguration() {
            final String name = "unclosed";
            final Map<String, String> config = new HashMap<>();
            final VFS4JConfig vfs4JConfig = new VFS4JConfig();

            config.put("loglevel", VFS4JUnclosedLogLevel.WARN.name());
            config.put("operations", VFS4JUnclosedOperation.NEW_INPUT_STREAM + "," + VFS4JUnclosedOperation.NEW_OUTPUT_STREAM);
            config.put("filterPath", "*.txt,*.jpg");

            unclosedPlugins = new VFS4JUnclosedPlugins();

            // methode testée
            unclosedPlugins.init(name, config, vfs4JConfig);

            // vérifications
            UnclosedConfig config2 = unclosedPlugins.getConfig();
            assertNotNull(config2);
            assertEquals(VFS4JUnclosedLogLevel.WARN, config2.getLoglevel());
            assertNotNull(config2.getOperations());
            Set<VFS4JUnclosedOperation> set = new HashSet<>(config2.getOperations());
            Set<VFS4JUnclosedOperation> setRef = new HashSet<>(Arrays.asList(VFS4JUnclosedOperation.NEW_INPUT_STREAM, VFS4JUnclosedOperation.NEW_OUTPUT_STREAM));
            assertEquals(setRef, set);
            assertNotNull(config2.getFilterPath());
            assertEquals(Arrays.asList("*.txt", "*.jpg"), config2.getFilterPath());
            assertFalse(config2.isLogOpen());
            assertFalse(config2.isLogClose());
            assertFalse(config2.isExceptionLogOpen());
            assertFalse(config2.isExceptionLogClose());
            assertEquals(0L, config2.getLogIfNotClosedAfterMs());
        }

        @Test
        void initAvecConfiguration2() {
            final String name = "unclosed";
            final Map<String, String> config = new HashMap<>();
            final VFS4JConfig vfs4JConfig = new VFS4JConfig();

            config.put("loglevel", VFS4JUnclosedLogLevel.WARN.name());
            config.put("operations", VFS4JUnclosedOperation.NEW_INPUT_STREAM + "," + VFS4JUnclosedOperation.NEW_OUTPUT_STREAM);
            config.put("filterPath", "*.txt");
            config.put("logopen", "true");
            config.put("logclose", "true");
            config.put("exceptionlogopen", "true");
            config.put("exceptionlogclose", "true");
            config.put("logIfNotClosedAfterMs", "2500");

            unclosedPlugins = new VFS4JUnclosedPlugins();

            // methode testée
            unclosedPlugins.init(name, config, vfs4JConfig);

            // vérifications
            UnclosedConfig config2 = unclosedPlugins.getConfig();
            assertNotNull(config2);
            assertEquals(VFS4JUnclosedLogLevel.WARN, config2.getLoglevel());
            assertNotNull(config2.getOperations());
            Set<VFS4JUnclosedOperation> set = new HashSet<>(config2.getOperations());
            Set<VFS4JUnclosedOperation> setRef = new HashSet<>(Arrays.asList(VFS4JUnclosedOperation.NEW_INPUT_STREAM, VFS4JUnclosedOperation.NEW_OUTPUT_STREAM));
            assertEquals(setRef, set);
            assertNotNull(config2.getFilterPath());
            assertEquals(Arrays.asList("*.txt"), config2.getFilterPath());
            assertTrue(config2.isLogOpen());
            assertTrue(config2.isLogClose());
            assertTrue(config2.isExceptionLogOpen());
            assertTrue(config2.isExceptionLogClose());
            assertEquals(2500L, config2.getLogIfNotClosedAfterMs());
        }


    }

}
