package com.github.abarhub.vfs.core.plugin.audit.operation;

import com.github.abarhub.vfs.core.api.VFS4JPathName;
import com.github.abarhub.vfs.core.config.VFS4JConfig;
import com.github.abarhub.vfs.core.plugin.audit.VFS4JAuditLogLevel;
import com.github.abarhub.vfs.core.plugin.audit.VFS4JAuditOperation;
import com.github.abarhub.vfs.core.plugin.audit.VFS4JAuditPlugins;
import com.github.abarhub.vfs.core.plugin.audit.VFS4JLogAudit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class VFS4JAbstractAuditOperationTest implements VFS4JLogAudit {

    private static final Logger LOGGER = LoggerFactory.getLogger(VFS4JAbstractAuditOperationTest.class);

    public static final String PATH1 = "path1";

    private VFS4JAbstractAuditOperation operation;

    private final List<LogMessage> listLog = new ArrayList<>();

    private VFS4JAuditPlugins auditPlugins;

    @BeforeEach
    void setUp() {
        LOGGER.info("setUp");
        auditPlugins = new VFS4JAuditPlugins();
        auditPlugins.addListener(this);
        Map<String, String> config = defautConfig();
        VFS4JConfig config2 = new VFS4JConfig();
        auditPlugins.init("exemple1", config, config2);
        operation = new VFS4JAbstractAuditOperation(auditPlugins) {

        };
    }

    @Test
    void getAuditPlugins() {

        // methode testée
        VFS4JAuditPlugins res = operation.getAuditPlugins();

        // vérifications
        assertNotNull(res);
    }

    @ParameterizedTest
    @EnumSource(VFS4JAuditLogLevel.class)
    void log(VFS4JAuditLogLevel loglevel) {
        final String message = "abc";

        Map<String, String> config = defautConfig();
        config.put("loglevel", loglevel.name());
        VFS4JConfig config2 = new VFS4JConfig();
        auditPlugins.init("exemple1", config, config2);

        // methode testée
        operation.log(message, 1, 2, 3);

        // vérifications
        assertEquals(1, listLog.size());
        LogMessage logMessage = listLog.get(0);
        Assertions.assertEquals(loglevel, logMessage.getLogLevel());
        assertEquals(message, logMessage.getMessage());
        assertEquals(Arrays.asList(1, 2, 3), logMessage.getParameters());
        assertFalse(logMessage.isError());
    }

    @ParameterizedTest
    @EnumSource(VFS4JAuditLogLevel.class)
    void logError(VFS4JAuditLogLevel loglevel) {
        final String message = "abc error";
        final NumberFormatException exception = new NumberFormatException("format incorrecte");

        Map<String, String> config = defautConfig();
        config.put("loglevel", loglevel.name());
        VFS4JConfig config2 = new VFS4JConfig();
        auditPlugins.init("exemple1", config, config2);

        // methode testée
        operation.logError(message, exception, 1, 2, 3, 4);

        // vérifications
        assertEquals(1, listLog.size());
        LogMessage logMessage = listLog.get(0);
        Assertions.assertEquals(loglevel, logMessage.getLogLevel());
        assertEquals(message, logMessage.getMessage());
        assertEquals(Arrays.asList(1, 2, 3, 4), logMessage.getParameters());
        assertEquals(exception, logMessage.getException());
        assertTrue(logMessage.isError());
    }

    private static Stream<Arguments> provideIsActive() {
        return Stream.of(
                Arguments.of("*.txt", list(VFS4JAuditOperation.COPY), VFS4JAuditOperation.COPY, list(getPathName("test1.txt")), true),
                Arguments.of(null, list(VFS4JAuditOperation.COPY), VFS4JAuditOperation.COPY, list(getPathName("test1.txt")), true),
                Arguments.of("*.txt", list(VFS4JAuditOperation.COPY), VFS4JAuditOperation.COPY, list(getPathName("test1.jpg")), false),
                Arguments.of(null, list(VFS4JAuditOperation.COPY), VFS4JAuditOperation.COPY, list(getPathName("test1.jpg")), true),
                Arguments.of(null, list(VFS4JAuditOperation.COPY), VFS4JAuditOperation.LIST, list(getPathName("test1.txt")), false),
                Arguments.of(null, null, VFS4JAuditOperation.COPY, list(getPathName("test1.txt")), true),
                Arguments.of(null, list(), VFS4JAuditOperation.COPY, list(getPathName("test1.txt")), false),
                Arguments.of("*.txt,*.jpg", list(VFS4JAuditOperation.COPY), VFS4JAuditOperation.COPY, list(getPathName("test1.jpg")), true),
                Arguments.of(null, list(VFS4JAuditOperation.COPY, VFS4JAuditOperation.LIST), VFS4JAuditOperation.LIST, list(getPathName("test1.txt")), true),
                Arguments.of("*.txt", list(VFS4JAuditOperation.COPY), VFS4JAuditOperation.COPY, list(getPathName("test1.jpg"), getPathName("test1.txt")), true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideIsActive")
    void isActive(String filterPath, List<VFS4JAuditOperation> listeOperationAutorisee,
                  VFS4JAuditOperation operation, List<VFS4JPathName> listePath,
                  boolean resultat) {
        Map<String, String> config = new HashMap<>();
        config.put("loglevel", VFS4JAuditLogLevel.INFO.name());
        if (listeOperationAutorisee != null) {
            if (listeOperationAutorisee.isEmpty()) {
                config.put("operations", null);
            } else {
                StringJoiner joiner = new StringJoiner(",");
                for (VFS4JAuditOperation operation1 : listeOperationAutorisee) {
                    joiner.add(operation1.name());
                }
                config.put("operations", joiner.toString());
            }
        }
        if (filterPath != null) {
            config.put("filterPath", filterPath);
        }

        VFS4JConfig config2 = new VFS4JConfig();
        auditPlugins.init("exemple1", config, config2);

        VFS4JPathName[] pathNames = listePath.toArray(new VFS4JPathName[0]);

        // methode testée
        boolean res = this.operation.isActive(operation, pathNames);

        // vérifications
        assertEquals(resultat, res);
        LOGGER.info("res={}", res);
    }

    // methodes utilitaires

    @Override
    public void log(VFS4JAuditLogLevel logLevel, boolean error, String message, Exception exception, Object... parameters) {

        LogMessage logMessage;
        if (parameters != null && parameters.length > 0) {
            logMessage = new LogMessage(logLevel, error, message, Arrays.asList(parameters), exception);
        } else {
            logMessage = new LogMessage(logLevel, error, message, new ArrayList<>(), exception);
        }
        listLog.add(logMessage);
    }

    private Map<String, String> defautConfig() {

        Map<String, String> config = new HashMap<>();
        config.put("loglevel", VFS4JAuditLogLevel.INFO.name());
        config.put("filterPath", "*.txt");
        return config;
    }

    private static VFS4JPathName getPathName(String filename) {
        return new VFS4JPathName(PATH1, filename);
    }

    private static <T> List<T> list() {
        return new ArrayList<>();
    }

    private static List<VFS4JAuditOperation> list(VFS4JAuditOperation... liste) {
        List<VFS4JAuditOperation> resultat = new ArrayList<>();
        if (liste != null && liste.length > 0) {
            Collections.addAll(resultat, liste);
        }
        return resultat;
    }

    // duplicate for remove 'Possible heap pollution from parameterized vararg type'
    private static List<VFS4JPathName> list(VFS4JPathName... liste) {
        List<VFS4JPathName> resultat = new ArrayList<>();
        if (liste != null && liste.length > 0) {
            Collections.addAll(resultat, liste);
        }
        return resultat;
    }
}