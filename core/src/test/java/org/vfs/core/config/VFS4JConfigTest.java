package org.vfs.core.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class VFS4JConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VFS4JConfigTest.class);

    private static Stream<Arguments> addPath_test1Parameter() {
        return Stream.of(
                Arguments.of("test1", Paths.get("/tmp/aaa/bbb"), false),
                Arguments.of("test2",Paths.get("/tmp/aaa/bbb/ccc"), true)
        );
    }

    @ParameterizedTest
    @MethodSource("addPath_test1Parameter")
    void addPath_test1(final String nom,final Path path,final boolean readOnly) {
        LOGGER.info("addPath_test1({}, {}, {})",nom,path,readOnly);
        VFS4JConfig vfs4JConfig =new VFS4JConfig();

        // methode testée
        vfs4JConfig.addPath(nom, path, readOnly);

        // vérifications
        PathParameter pathParameter = vfs4JConfig.getPath(nom);
        assertNotNull(pathParameter);
        assertEquals(path, pathParameter.getPath());
        assertEquals(readOnly, pathParameter.isReadonly());
    }

    @Test
    @DisplayName("getPath sur un nom qui existe (une seule configuration)")
    void getPath_test1() {
        LOGGER.info("addPath_test1");
        VFS4JConfig vfs4JConfig =new VFS4JConfig();

        String nom="name1";
        Path path=Paths.get("/tmp/dddd");
        boolean readOnly=true;

        vfs4JConfig.addPath(nom, path, readOnly);

        // methode testée
        PathParameter pathParameter = vfs4JConfig.getPath(nom);

        // vérifications
        assertNotNull(pathParameter);
        assertEquals(path, pathParameter.getPath());
        assertEquals(readOnly, pathParameter.isReadonly());
    }

    @Test
    @DisplayName("getPath sur un nom qui existe (deux configurations)")
    void getPath_test2() {
        LOGGER.info("addPath_test2");
        VFS4JConfig vfs4JConfig =new VFS4JConfig();

        String nom="name1";
        Path path=Paths.get("/tmp/dddd");
        boolean readOnly=true;

        vfs4JConfig.addPath(nom, path, readOnly);

        final String nom2 = "ggg";
        final Path path2 = Paths.get("/tmp/mmm");
        final boolean readonly2 = false;
        vfs4JConfig.addPath(nom2, path2, readonly2);

        // methode testée
        PathParameter pathParameter = vfs4JConfig.getPath(nom2);

        // vérifications
        assertNotNull(pathParameter);
        assertEquals(path2, pathParameter.getPath());
        assertEquals(readonly2, pathParameter.isReadonly());
    }

    @Test
    @DisplayName("getPath sur un nom qui n'existe pas (une seule configuration)")
    void getPath_test3() {
        LOGGER.info("addPath_test3");
        VFS4JConfig vfs4JConfig =new VFS4JConfig();

        String nom="name01";
        Path path=Paths.get("/tmp/nnn");
        boolean readOnly=true;

        vfs4JConfig.addPath(nom, path, readOnly);

        // methode testée
        PathParameter pathParameter = vfs4JConfig.getPath("name02");

        // vérifications
        assertNull(pathParameter);
    }

    @Test
    @DisplayName("getPath sur un nom qui n'existe pas (aucune configuration)")
    void getPath_test4() {
        LOGGER.info("addPath_test4");
        VFS4JConfig vfs4JConfig =new VFS4JConfig();

        // methode testée
        PathParameter pathParameter = vfs4JConfig.getPath("name03");

        // vérifications
        assertNull(pathParameter);
    }

    private static Stream<Arguments> getPath_test5Parameter() {
        return Stream.of(
                Arguments.of("name1", true, Paths.get("/tmp/ccc1"), true),
                Arguments.of("name2", true, Paths.get("/tmp/ccc2"), false),
                Arguments.of("name3", true, Paths.get("/tmp/ccc3"), true),
                Arguments.of("name4", true, Paths.get("/tmp/ccc4"), false),
                Arguments.of("name5", true, Paths.get("/tmp/ccc5"), true),
                Arguments.of("name6", true, Paths.get("/tmp/ccc6"), false),
                Arguments.of("name7", true, Paths.get("/tmp/ccc7"), true),
                Arguments.of("name8", true, Paths.get("/tmp/ccc8"), false),
                Arguments.of("name9", true, Paths.get("/tmp/ccc9"), true),
                Arguments.of("name10", true, Paths.get("/tmp/ccc10"), false),
                Arguments.of("name11", false, null, false),
                Arguments.of("name0", false, null, false),
                Arguments.of("name", false, null, false)
        );
    }


    @ParameterizedTest
    @MethodSource("getPath_test5Parameter")
    @DisplayName("getPath sur un nom qui existe (10 configurations)")
    void getPath_test5(final String nameRef,final boolean existRef,final Path pathRef,final boolean readonlyRef) {
        LOGGER.info("getPath_test5");

        assertNotNull(nameRef);

        if(existRef){
            assertNotNull(pathRef);
        } else {
            assertNull(pathRef);
        }

        VFS4JConfig vfs4JConfig =new VFS4JConfig();

        for(int i=0;i<10;i++){
            int no=i+1;

            String nom="name"+no;
            Path path=Paths.get("/tmp/ccc"+no);
            boolean readOnly=(no%2==1);

            vfs4JConfig.addPath(nom, path, readOnly);
        }

        // methode testée
        PathParameter pathParameter = vfs4JConfig.getPath(nameRef);

        // vérifications
        if(!existRef){
            assertNull(pathParameter);
        } else {
            assertNotNull(pathParameter);
            assertEquals(pathRef, pathParameter.getPath());
            assertEquals(readonlyRef, pathParameter.isReadonly());
        }
    }

}