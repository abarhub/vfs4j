package org.vfs.core.plugin.audit.operation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vfs.core.api.VFS4JPathName;
import org.vfs.core.api.operation.VFS4JAttribute;
import org.vfs.core.config.VFS4JConfig;
import org.vfs.core.plugin.audit.VFS4JAuditLogLevel;
import org.vfs.core.plugin.audit.VFS4JAuditPlugins;
import org.vfs.core.plugin.audit.VFS4JLogAudit;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.LinkOption;
import java.nio.file.attribute.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VFS4JAuditAttributeTest implements VFS4JLogAudit {

    private static final Logger LOGGER = LoggerFactory.getLogger(VFS4JAuditAttributeTest.class);

    public static final String PATH1 = "path1";

    private final VFS4JAttribute attribute = mock(VFS4JAttribute.class);

    private VFS4JAuditAttribute auditAttribute;

    private final List<LogMessage> listLog = new ArrayList<>();

    @BeforeEach
    void setUp() {
        VFS4JAuditPlugins vfs4JAuditPlugins = new VFS4JAuditPlugins();
        vfs4JAuditPlugins.addListener(this);
        Map<String, String> config = defautConfig();
        VFS4JConfig config2 = new VFS4JConfig();
        vfs4JAuditPlugins.init("exemple1", config, config2);
        auditAttribute = new VFS4JAuditAttribute(vfs4JAuditPlugins, attribute);
    }

    private static Stream<Arguments> provideTestOK() {
        List<Arguments> liste;
        liste = new ArrayList<>();

        Supplier<MethodeATester> supplier = () -> {
            String attributName = "nom";
            final Object valeur = new Object();
            VFS4JPathName VFS4JPathName2 = getPathName("abc.txt");
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.txt");

            MethodeATester methodeATester = new MethodeATester("setAttribute",
                    Arrays.asList(VFS4JPathName2, attributName, valeur, null), VFS4JPathNameRes,
                    Arrays.asList(VFS4JPathName.class, String.class, Object.class, LinkOption[].class), VFS4JPathName.class,
                    "setAttribute for file {} with attribute {} to {}",
                    Arrays.asList(VFS4JPathName2, attributName, valeur));

            return methodeATester;
        };
        liste.add(Arguments.of(supplier.get()));

        return liste.stream();

    }

    @ParameterizedTest(name = "{index} test {0}")
    @MethodSource("provideTestOK")
    void testOK(MethodeATester methodeATester) throws Exception {

        final Object[] parametresMethodeTestee = methodeATester.getParametres().toArray(new Object[0]);
        final Class<?>[] typeParametresMethodeTestee = methodeATester.getTypeParametres().toArray(new Class[0]);

        Class<?> clazz = attribute.getClass();
        Method method = clazz.getMethod(methodeATester.getNomMethode(), typeParametresMethodeTestee);

        when(method.invoke(attribute, methodeATester.getParametres().toArray(new Object[0]))).thenReturn(methodeATester.getValeurRetour());

        Class<?> clazz2 = auditAttribute.getClass();
        Method method2 = clazz2.getMethod(methodeATester.getNomMethode(), typeParametresMethodeTestee);

        // methode testée
        Object resultat = method2.invoke(auditAttribute, parametresMethodeTestee);

        // vérifications
        assertNotNull(resultat);
        assertEquals(methodeATester.getValeurRetour(), resultat);
        final Object verify = verify(attribute);
        method.invoke(verify, parametresMethodeTestee);
        verifyNoMoreInteractions(attribute);
        assertEquals(1, listLog.size());
        LogMessage logMessage = listLog.get(0);
        assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
        assertEquals(methodeATester.getMessageLog(), logMessage.getMessage());
        assertEquals(methodeATester.getParametreLog(), logMessage.getParameters());
        assertFalse(logMessage.isError());
    }

    @Nested
    class TestSetAttribute {

        @Test
        void setAttributeOK() throws IOException {
            final String attributName = "nom";
            final Object valeur = new Object();
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.txt");
            when(attribute.setAttribute(VFS4JPathName, attributName, valeur)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditAttribute.setAttribute(VFS4JPathName, attributName, valeur);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(attribute).setAttribute(VFS4JPathName, attributName, valeur);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("setAttribute for file {} with attribute {} to {}", logMessage.getMessage());
            assertEquals(Arrays.asList(VFS4JPathName, attributName, valeur), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void setAttributeDisabled() throws IOException {
            final String attributName = "nom";
            final Object valeur = new Object();
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.jpg");
            when(attribute.setAttribute(VFS4JPathName, attributName, valeur)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditAttribute.setAttribute(VFS4JPathName, attributName, valeur);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(attribute).setAttribute(VFS4JPathName, attributName, valeur);
            verifyNoMoreInteractions(attribute);
            assertEquals(0, listLog.size());
        }

        @Test
        void setAttributeException() throws IOException {
            final String attributName = "nom";
            final Object valeur = new Object();
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(attribute.setAttribute(VFS4JPathName, attributName, valeur)).thenThrow(new IOException("Error setAttribut"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditAttribute.setAttribute(VFS4JPathName, attributName, valeur));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error setAttribut", exception.getMessage());
            verify(attribute).setAttribute(VFS4JPathName, attributName, valeur);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for setAttribute for file {} with attribute {} to {}", logMessage.getMessage());
            assertEquals(Arrays.asList(VFS4JPathName, attributName, valeur), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestGetAttribute {

        @Test
        void getAttributeOK() throws IOException {
            final String attributName = "nom";
            final Object valeur = new Object();
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(attribute.getAttribute(VFS4JPathName, attributName)).thenReturn(valeur);

            // methode testée
            Object res = auditAttribute.getAttribute(VFS4JPathName, attributName);

            // vérifications
            assertNotNull(res);
            assertEquals(valeur, res);
            verify(attribute).getAttribute(VFS4JPathName, attributName);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("getAttribute for file {} for {} with options {}", logMessage.getMessage());
            assertEquals(3, logMessage.getParameters().size());
            assertEquals(VFS4JPathName, logMessage.getParameters().get(0));
            assertEquals(attributName, logMessage.getParameters().get(1));
            assertEquals(0, ((LinkOption[]) logMessage.getParameters().get(2)).length);
            assertFalse(logMessage.isError());
        }

        @Test
        void getAttributeDisabled() throws IOException {
            final String attributName = "nom";
            final Object valeur = new Object();
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            when(attribute.getAttribute(VFS4JPathName, attributName)).thenReturn(valeur);

            // methode testée
            Object res = auditAttribute.getAttribute(VFS4JPathName, attributName);

            // vérifications
            assertNotNull(res);
            assertEquals(valeur, res);
            verify(attribute).getAttribute(VFS4JPathName, attributName);
            verifyNoMoreInteractions(attribute);
            assertEquals(0, listLog.size());
        }

        @Test
        void getAttributeException() throws IOException {
            final String attributName = "nom";
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(attribute.getAttribute(VFS4JPathName, attributName)).thenThrow(new IOException("Error getAttribut"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditAttribute.getAttribute(VFS4JPathName, attributName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error getAttribut", exception.getMessage());
            verify(attribute).getAttribute(VFS4JPathName, attributName);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for getAttribute for file {} for {} with options {}", logMessage.getMessage());
            assertEquals(3, logMessage.getParameters().size());
            assertEquals(VFS4JPathName, logMessage.getParameters().get(0));
            assertEquals(attributName, logMessage.getParameters().get(1));
            assertEquals(0, ((LinkOption[]) logMessage.getParameters().get(2)).length);
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestSetLastModifiedTime {

        @Test
        void setLastModifiedTimeOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            FileTime fileTime = getFileTime();
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.txt");
            when(attribute.setLastModifiedTime(VFS4JPathName, fileTime)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditAttribute.setLastModifiedTime(VFS4JPathName, fileTime);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(attribute).setLastModifiedTime(VFS4JPathName, fileTime);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("setLastModifiedTime for file {} to {}", logMessage.getMessage());
            assertEquals(Arrays.asList(VFS4JPathName, fileTime), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void setLastModifiedTimeDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            FileTime fileTime = getFileTime();
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.jpg");
            when(attribute.setLastModifiedTime(VFS4JPathName, fileTime)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditAttribute.setLastModifiedTime(VFS4JPathName, fileTime);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(attribute).setLastModifiedTime(VFS4JPathName, fileTime);
            verifyNoMoreInteractions(attribute);
            assertEquals(0, listLog.size());
        }

        @Test
        void setLastModifiedTimeException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            FileTime fileTime = getFileTime();
            when(attribute.setLastModifiedTime(VFS4JPathName, fileTime)).thenThrow(new IOException("Error setLastModifiedTime"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditAttribute.setLastModifiedTime(VFS4JPathName, fileTime));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error setLastModifiedTime", exception.getMessage());
            verify(attribute).setLastModifiedTime(VFS4JPathName, fileTime);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for setLastModifiedTime for file {} to {}", logMessage.getMessage());
            assertEquals(Arrays.asList(VFS4JPathName, fileTime), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestGetLastModifiedTime {

        @Test
        void getLastModifiedTimeOK() throws IOException {
            final String attributName = "nom";
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            FileTime fileTime = getFileTime();
            when(attribute.getLastModifiedTime(VFS4JPathName)).thenReturn(fileTime);

            // methode testée
            FileTime res = auditAttribute.getLastModifiedTime(VFS4JPathName);

            // vérifications
            assertNotNull(res);
            assertEquals(fileTime, res);
            verify(attribute).getLastModifiedTime(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("getLastModifiedTime for file {} with options {}", logMessage.getMessage());
            assertEquals(2, logMessage.getParameters().size());
            assertEquals(VFS4JPathName, logMessage.getParameters().get(0));
            assertEquals(0, ((LinkOption[]) logMessage.getParameters().get(1)).length);
            assertFalse(logMessage.isError());
        }

        @Test
        void getLastModifiedTimeDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            FileTime fileTime = getFileTime();
            when(attribute.getLastModifiedTime(VFS4JPathName)).thenReturn(fileTime);

            // methode testée
            FileTime res = auditAttribute.getLastModifiedTime(VFS4JPathName);

            // vérifications
            assertNotNull(res);
            assertEquals(fileTime, res);
            verify(attribute).getLastModifiedTime(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(0, listLog.size());
        }

        @Test
        void getLastModifiedTimeException() throws IOException {
            final String attributName = "nom";
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(attribute.getLastModifiedTime(VFS4JPathName)).thenThrow(new IOException("Error getLastModifiedTime"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditAttribute.getLastModifiedTime(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error getLastModifiedTime", exception.getMessage());
            verify(attribute).getLastModifiedTime(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for getLastModifiedTime for file {} with options {}", logMessage.getMessage());
            assertEquals(2, logMessage.getParameters().size());
            assertEquals(VFS4JPathName, logMessage.getParameters().get(0));
            assertEquals(0, ((LinkOption[]) logMessage.getParameters().get(1)).length);
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestsetOwner {

        @Test
        void setOwnerOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.txt");
            UserPrincipal userPrincipal = getUserPrincipal();
            when(attribute.setOwner(VFS4JPathName, userPrincipal)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditAttribute.setOwner(VFS4JPathName, userPrincipal);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(attribute).setOwner(VFS4JPathName, userPrincipal);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("setOwner for file {} to {}", logMessage.getMessage());
            assertEquals(Arrays.asList(VFS4JPathName, userPrincipal), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void setOwnerDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            UserPrincipal userPrincipal = getUserPrincipal();
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.jpg");
            when(attribute.setOwner(VFS4JPathName, userPrincipal)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditAttribute.setOwner(VFS4JPathName, userPrincipal);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(attribute).setOwner(VFS4JPathName, userPrincipal);
            verifyNoMoreInteractions(attribute);
            assertEquals(0, listLog.size());
        }

        @Test
        void setOwnerException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            UserPrincipal userPrincipal = getUserPrincipal();
            when(attribute.setOwner(VFS4JPathName, userPrincipal)).thenThrow(new IOException("Error setOwner"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditAttribute.setOwner(VFS4JPathName, userPrincipal));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error setOwner", exception.getMessage());
            verify(attribute).setOwner(VFS4JPathName, userPrincipal);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for setOwner for file {} to {}", logMessage.getMessage());
            assertEquals(Arrays.asList(VFS4JPathName, userPrincipal), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestGetOwner {

        @Test
        void getLastModifiedTimeOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            UserPrincipal userPrincipal = getUserPrincipal();
            when(attribute.getOwner(VFS4JPathName)).thenReturn(userPrincipal);

            // methode testée
            UserPrincipal res = auditAttribute.getOwner(VFS4JPathName);

            // vérifications
            assertNotNull(res);
            assertEquals(userPrincipal, res);
            verify(attribute).getOwner(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("getOwner for file {} with options {}", logMessage.getMessage());
            assertEquals(2, logMessage.getParameters().size());
            assertEquals(VFS4JPathName, logMessage.getParameters().get(0));
            assertEquals(0, ((LinkOption[]) logMessage.getParameters().get(1)).length);
            assertFalse(logMessage.isError());
        }

        @Test
        void getOwnerDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            UserPrincipal userPrincipal = getUserPrincipal();
            when(attribute.getOwner(VFS4JPathName)).thenReturn(userPrincipal);

            // methode testée
            UserPrincipal res = auditAttribute.getOwner(VFS4JPathName);

            // vérifications
            assertNotNull(res);
            assertEquals(userPrincipal, res);
            verify(attribute).getOwner(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(0, listLog.size());
        }

        @Test
        void getOwnerException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(attribute.getOwner(VFS4JPathName)).thenThrow(new IOException("Error getOwner"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditAttribute.getOwner(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error getOwner", exception.getMessage());
            verify(attribute).getOwner(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for getOwner for file {} with options {}", logMessage.getMessage());
            assertEquals(2, logMessage.getParameters().size());
            assertEquals(VFS4JPathName, logMessage.getParameters().get(0));
            assertEquals(0, ((LinkOption[]) logMessage.getParameters().get(1)).length);
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestSetPosixFilePermissions {

        @Test
        void setPosixFilePermissionsOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.txt");
            Set<PosixFilePermission> set = getPosixFilePermissions();
            when(attribute.setPosixFilePermissions(VFS4JPathName, set)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditAttribute.setPosixFilePermissions(VFS4JPathName, set);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(attribute).setPosixFilePermissions(VFS4JPathName, set);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("setPosixFilePermissions for file {} to {}", logMessage.getMessage());
            assertEquals(Arrays.asList(VFS4JPathName, set), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void setPosixFilePermissionsDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            Set<PosixFilePermission> set = getPosixFilePermissions();
            VFS4JPathName VFS4JPathNameRes = getPathName("abc2.jpg");
            when(attribute.setPosixFilePermissions(VFS4JPathName, set)).thenReturn(VFS4JPathNameRes);

            // methode testée
            VFS4JPathName res = auditAttribute.setPosixFilePermissions(VFS4JPathName, set);

            // vérifications
            assertNotNull(res);
            assertEquals(VFS4JPathNameRes, res);
            verify(attribute).setPosixFilePermissions(VFS4JPathName, set);
            verifyNoMoreInteractions(attribute);
            assertEquals(0, listLog.size());
        }

        @Test
        void setPosixFilePermissionsException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            Set<PosixFilePermission> set = getPosixFilePermissions();
            when(attribute.setPosixFilePermissions(VFS4JPathName, set)).thenThrow(new IOException("Error setPosixFilePermissions"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditAttribute.setPosixFilePermissions(VFS4JPathName, set));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error setPosixFilePermissions", exception.getMessage());
            verify(attribute).setPosixFilePermissions(VFS4JPathName, set);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for setPosixFilePermissions for file {} to {}", logMessage.getMessage());
            assertEquals(Arrays.asList(VFS4JPathName, set), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestGetPosixFilePermissions {

        @Test
        void getPosixFilePermissionsOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            Set<PosixFilePermission> set = getPosixFilePermissions();
            when(attribute.getPosixFilePermissions(VFS4JPathName)).thenReturn(set);

            // methode testée
            Set<PosixFilePermission> res = auditAttribute.getPosixFilePermissions(VFS4JPathName);

            // vérifications
            assertNotNull(res);
            assertEquals(set, res);
            verify(attribute).getPosixFilePermissions(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("getPosixFilePermissions for file {} with options {}", logMessage.getMessage());
            assertEquals(2, logMessage.getParameters().size());
            assertEquals(VFS4JPathName, logMessage.getParameters().get(0));
            assertEquals(0, ((LinkOption[]) logMessage.getParameters().get(1)).length);
            assertFalse(logMessage.isError());
        }

        @Test
        void getPosixFilePermissionsDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            Set<PosixFilePermission> set = getPosixFilePermissions();
            when(attribute.getPosixFilePermissions(VFS4JPathName)).thenReturn(set);

            // methode testée
            Set<PosixFilePermission> res = auditAttribute.getPosixFilePermissions(VFS4JPathName);

            // vérifications
            assertNotNull(res);
            assertEquals(set, res);
            verify(attribute).getPosixFilePermissions(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(0, listLog.size());
        }

        @Test
        void getPosixFilePermissionsException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(attribute.getPosixFilePermissions(VFS4JPathName)).thenThrow(new IOException("Error getPosixFilePermissions"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditAttribute.getPosixFilePermissions(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error getPosixFilePermissions", exception.getMessage());
            verify(attribute).getPosixFilePermissions(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for getPosixFilePermissions for file {} with options {}", logMessage.getMessage());
            assertEquals(2, logMessage.getParameters().size());
            assertEquals(VFS4JPathName, logMessage.getParameters().get(0));
            assertEquals(0, ((LinkOption[]) logMessage.getParameters().get(1)).length);
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestGetFileAttributeView {

        @Test
        void getFileAttributeViewOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            BasicFileAttributeView fileAttributeView = mock(BasicFileAttributeView.class);
            when(attribute.getFileAttributeView(VFS4JPathName, BasicFileAttributeView.class)).thenReturn(fileAttributeView);

            // methode testée
            FileAttributeView res = auditAttribute.getFileAttributeView(VFS4JPathName, BasicFileAttributeView.class);

            // vérifications
            assertNotNull(res);
            assertEquals(fileAttributeView, res);
            verify(attribute).getFileAttributeView(VFS4JPathName, BasicFileAttributeView.class);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("getFileAttributeView for file {} for {} with options {}", logMessage.getMessage());
            assertEquals(3, logMessage.getParameters().size());
            assertEquals(VFS4JPathName, logMessage.getParameters().get(0));
            assertEquals(BasicFileAttributeView.class, logMessage.getParameters().get(1));
            assertEquals(0, ((LinkOption[]) logMessage.getParameters().get(2)).length);
            assertFalse(logMessage.isError());
        }

        @Test
        void getFileAttributeViewDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            BasicFileAttributeView fileAttributeView = mock(BasicFileAttributeView.class);
            when(attribute.getFileAttributeView(VFS4JPathName, BasicFileAttributeView.class)).thenReturn(fileAttributeView);

            // methode testée
            BasicFileAttributeView res = auditAttribute.getFileAttributeView(VFS4JPathName, BasicFileAttributeView.class);

            // vérifications
            assertNotNull(res);
            assertEquals(fileAttributeView, res);
            verify(attribute).getFileAttributeView(VFS4JPathName, BasicFileAttributeView.class);
            verifyNoMoreInteractions(attribute);
            assertEquals(0, listLog.size());
        }

        @Test
        void getFileAttributeViewException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(attribute.getFileAttributeView(VFS4JPathName, BasicFileAttributeView.class)).thenThrow(new IOException("Error getFileAttributeView"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditAttribute.getFileAttributeView(VFS4JPathName, BasicFileAttributeView.class));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error getFileAttributeView", exception.getMessage());
            verify(attribute).getFileAttributeView(VFS4JPathName, BasicFileAttributeView.class);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for getFileAttributeView for file {} for {} with options {}", logMessage.getMessage());
            assertEquals(3, logMessage.getParameters().size());
            assertEquals(VFS4JPathName, logMessage.getParameters().get(0));
            assertEquals(BasicFileAttributeView.class, logMessage.getParameters().get(1));
            assertEquals(0, ((LinkOption[]) logMessage.getParameters().get(2)).length);
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestIsExecutable {

        @Test
        void isExecutableOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(attribute.isExecutable(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditAttribute.isExecutable(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(attribute).isExecutable(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("isExecutable for file {}", logMessage.getMessage());
            assertEquals(Arrays.asList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void isExecutableDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            when(attribute.isExecutable(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditAttribute.isExecutable(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(attribute).isExecutable(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(0, listLog.size());
        }

        @Test
        void isExecutableException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(attribute.isExecutable(VFS4JPathName)).thenThrow(new IOException("Error isExecutable"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditAttribute.isExecutable(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error isExecutable", exception.getMessage());
            verify(attribute).isExecutable(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for isExecutable for file {}", logMessage.getMessage());
            assertEquals(Arrays.asList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestIsReadable {

        @Test
        void isReadableOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(attribute.isReadable(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditAttribute.isReadable(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(attribute).isReadable(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("isReadable for file {}", logMessage.getMessage());
            assertEquals(Arrays.asList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void isReadableDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            when(attribute.isReadable(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditAttribute.isReadable(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(attribute).isReadable(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(0, listLog.size());
        }

        @Test
        void isReadableException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(attribute.isReadable(VFS4JPathName)).thenThrow(new IOException("Error isReadable"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditAttribute.isReadable(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error isReadable", exception.getMessage());
            verify(attribute).isReadable(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for isReadable for file {}", logMessage.getMessage());
            assertEquals(Arrays.asList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestIsHidden {

        @Test
        void isHiddenOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(attribute.isHidden(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditAttribute.isHidden(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(attribute).isHidden(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("isHidden for file {}", logMessage.getMessage());
            assertEquals(Arrays.asList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void isHiddenDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            when(attribute.isHidden(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditAttribute.isHidden(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(attribute).isHidden(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(0, listLog.size());
        }

        @Test
        void isHiddenException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(attribute.isHidden(VFS4JPathName)).thenThrow(new IOException("Error isHidden"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditAttribute.isHidden(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error isHidden", exception.getMessage());
            verify(attribute).isHidden(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for isHidden for file {}", logMessage.getMessage());
            assertEquals(Arrays.asList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestIsWritable {

        @Test
        void isWritableOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(attribute.isWritable(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditAttribute.isWritable(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(attribute).isWritable(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("isWritable for file {}", logMessage.getMessage());
            assertEquals(Arrays.asList(VFS4JPathName), logMessage.getParameters());
            assertFalse(logMessage.isError());
        }

        @Test
        void isWritableDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            when(attribute.isWritable(VFS4JPathName)).thenReturn(true);

            // methode testée
            boolean res = auditAttribute.isWritable(VFS4JPathName);

            // vérifications
            assertTrue(res);
            verify(attribute).isWritable(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(0, listLog.size());
        }

        @Test
        void isWritableException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(attribute.isWritable(VFS4JPathName)).thenThrow(new IOException("Error isWritable"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditAttribute.isWritable(VFS4JPathName));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error isWritable", exception.getMessage());
            verify(attribute).isWritable(VFS4JPathName);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for isWritable for file {}", logMessage.getMessage());
            assertEquals(Arrays.asList(VFS4JPathName), logMessage.getParameters());
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestReadAttributes {

        @Test
        void readAttributesOK() throws IOException {
            final String attribut = "name";
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            Map<String, Object> map = new HashMap<>();
            when(attribute.readAttributes(VFS4JPathName, attribut)).thenReturn(map);

            // methode testée
            Map<String, Object> res = auditAttribute.readAttributes(VFS4JPathName, attribut);

            // vérifications
            assertNotNull(res);
            assertEquals(map, res);
            verify(attribute).readAttributes(VFS4JPathName, attribut);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("readAttributes for file {} for attribute {} with options {}", logMessage.getMessage());
            assertEquals(3, logMessage.getParameters().size());
            assertEquals(VFS4JPathName, logMessage.getParameters().get(0));
            assertEquals(attribut, logMessage.getParameters().get(1));
            assertEquals(0, ((LinkOption[]) logMessage.getParameters().get(2)).length);
            assertFalse(logMessage.isError());
        }

        @Test
        void readAttributesDisabled() throws IOException {
            final String attribut = "name";
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            Map<String, Object> map = new HashMap<>();
            when(attribute.readAttributes(VFS4JPathName, attribut)).thenReturn(map);

            // methode testée
            Map<String, Object> res = auditAttribute.readAttributes(VFS4JPathName, attribut);

            // vérifications
            assertNotNull(res);
            assertEquals(map, res);
            verify(attribute).readAttributes(VFS4JPathName, attribut);
            verifyNoMoreInteractions(attribute);
            assertEquals(0, listLog.size());
        }

        @Test
        void readAttributesException() throws IOException {
            final String attribut = "name";
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(attribute.readAttributes(VFS4JPathName, attribut)).thenThrow(new IOException("Error readAttributes"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditAttribute.readAttributes(VFS4JPathName, attribut));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error readAttributes", exception.getMessage());
            verify(attribute).readAttributes(VFS4JPathName, attribut);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for readAttributes for file {} for attribute {} with options {}", logMessage.getMessage());
            assertEquals(3, logMessage.getParameters().size());
            assertEquals(VFS4JPathName, logMessage.getParameters().get(0));
            assertEquals(attribut, logMessage.getParameters().get(1));
            assertEquals(0, ((LinkOption[]) logMessage.getParameters().get(2)).length);
            assertTrue(logMessage.isError());
        }

    }

    @Nested
    class TestReadAttributesWithClass {

        @Test
        void readAttributesOK() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            Map<String, Object> map = new HashMap<>();
            BasicFileAttributes fileAttributes = mock(BasicFileAttributes.class);
            when(attribute.readAttributes(VFS4JPathName, BasicFileAttributes.class)).thenReturn(fileAttributes);

            // methode testée
            BasicFileAttributes res = auditAttribute.readAttributes(VFS4JPathName, BasicFileAttributes.class);

            // vérifications
            assertNotNull(res);
            assertEquals(fileAttributes, res);
            verify(attribute).readAttributes(VFS4JPathName, BasicFileAttributes.class);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("readAttributes for file {} for attribute {} with options {}", logMessage.getMessage());
            assertEquals(3, logMessage.getParameters().size());
            assertEquals(VFS4JPathName, logMessage.getParameters().get(0));
            assertEquals(BasicFileAttributes.class, logMessage.getParameters().get(1));
            assertEquals(0, ((LinkOption[]) logMessage.getParameters().get(2)).length);
            assertFalse(logMessage.isError());
        }

        @Test
        void readAttributesDisabled() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.jpg");
            BasicFileAttributes fileAttributes = mock(BasicFileAttributes.class);
            when(attribute.readAttributes(VFS4JPathName, BasicFileAttributes.class)).thenReturn(fileAttributes);

            // methode testée
            BasicFileAttributes res = auditAttribute.readAttributes(VFS4JPathName, BasicFileAttributes.class);

            // vérifications
            assertNotNull(res);
            assertEquals(fileAttributes, res);
            verify(attribute).readAttributes(VFS4JPathName, BasicFileAttributes.class);
            verifyNoMoreInteractions(attribute);
            assertEquals(0, listLog.size());
        }

        @Test
        void readAttributesException() throws IOException {
            VFS4JPathName VFS4JPathName = getPathName("abc.txt");
            when(attribute.readAttributes(VFS4JPathName, BasicFileAttributes.class)).thenThrow(new IOException("Error readAttributes"));

            // methode testée
            IOException exception = assertThrows(IOException.class, () -> auditAttribute.readAttributes(VFS4JPathName, BasicFileAttributes.class));

            // vérifications
            assertNotNull(exception);
            assertEquals("Error readAttributes", exception.getMessage());
            verify(attribute).readAttributes(VFS4JPathName, BasicFileAttributes.class);
            verifyNoMoreInteractions(attribute);
            assertEquals(1, listLog.size());
            LogMessage logMessage = listLog.get(0);
            assertEquals(VFS4JAuditLogLevel.INFO, logMessage.getLogLevel());
            assertEquals("Error for readAttributes for file {} for attribute {} with options {}", logMessage.getMessage());
            assertEquals(3, logMessage.getParameters().size());
            assertEquals(VFS4JPathName, logMessage.getParameters().get(0));
            assertEquals(BasicFileAttributes.class, logMessage.getParameters().get(1));
            assertEquals(0, ((LinkOption[]) logMessage.getParameters().get(2)).length);
            assertTrue(logMessage.isError());
        }

    }

    // méthodes utilitaires

    private static VFS4JPathName getPathName(String filename) {
        return new VFS4JPathName(PATH1, filename);
    }

    @Override
    public void log(VFS4JAuditLogLevel logLevel, boolean error, String message, Object... parameters) {
        LogMessage logMessage;
        if (parameters != null && parameters.length > 0) {
            logMessage = new LogMessage(logLevel, error, message, Arrays.asList(parameters));
        } else {
            logMessage = new LogMessage(logLevel, error, message, new ArrayList<>());
        }
        listLog.add(logMessage);
    }

    private Map<String, String> defautConfig() {

        Map<String, String> config = new HashMap<>();
        config.put("loglevel", VFS4JAuditLogLevel.INFO.name());
        config.put("filterPath", "*.txt");
        return config;
    }

    private FileTime getFileTime() {
        return FileTime.from(10, TimeUnit.MINUTES);
    }

    private UserPrincipal getUserPrincipal() throws IOException {
        return mock(UserPrincipal.class);
    }

    private Set<PosixFilePermission> getPosixFilePermissions() {
        Set<PosixFilePermission> set = new HashSet<>();
        set.add(PosixFilePermission.OWNER_READ);
        return set;
    }
}