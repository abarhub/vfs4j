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

class VFSConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VFSConfigTest.class);

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
        VFSConfig vfsConfig=new VFSConfig();

        // methode testée
        vfsConfig.addPath(nom, path, readOnly);

        // vérifications
        Parameter parameter=vfsConfig.getPath(nom);
        assertNotNull(parameter);
        assertEquals(path,parameter.getPath());
        assertEquals(readOnly,parameter.isReadonly());
    }

    @Test
    @DisplayName("getPath sur un nom qui existe (une seule configuration)")
    void getPath_test1() {
        LOGGER.info("addPath_test1");
        VFSConfig vfsConfig=new VFSConfig();

        String nom="name1";
        Path path=Paths.get("/tmp/dddd");
        boolean readOnly=true;

        vfsConfig.addPath(nom, path, readOnly);

        // methode testée
        Parameter parameter = vfsConfig.getPath(nom);

        // vérifications
        assertNotNull(parameter);
        assertEquals(path,parameter.getPath());
        assertEquals(readOnly,parameter.isReadonly());
    }

    @Test
    @DisplayName("getPath sur un nom qui existe (deux configurations)")
    void getPath_test2() {
        LOGGER.info("addPath_test2");
        VFSConfig vfsConfig=new VFSConfig();

        String nom="name1";
        Path path=Paths.get("/tmp/dddd");
        boolean readOnly=true;

        vfsConfig.addPath(nom, path, readOnly);

        final String nom2 = "ggg";
        final Path path2 = Paths.get("/tmp/mmm");
        final boolean readonly2 = false;
        vfsConfig.addPath(nom2, path2, readonly2);

        // methode testée
        Parameter parameter = vfsConfig.getPath(nom2);

        // vérifications
        assertNotNull(parameter);
        assertEquals(path2,parameter.getPath());
        assertEquals(readonly2,parameter.isReadonly());
    }

    @Test
    @DisplayName("getPath sur un nom qui n'existe pas (une seule configuration)")
    void getPath_test3() {
        LOGGER.info("addPath_test3");
        VFSConfig vfsConfig=new VFSConfig();

        String nom="name01";
        Path path=Paths.get("/tmp/nnn");
        boolean readOnly=true;

        vfsConfig.addPath(nom, path, readOnly);

        // methode testée
        Parameter parameter = vfsConfig.getPath("name02");

        // vérifications
        assertNull(parameter);
    }

    @Test
    @DisplayName("getPath sur un nom qui n'existe pas (aucune configuration)")
    void getPath_test4() {
        LOGGER.info("addPath_test4");
        VFSConfig vfsConfig=new VFSConfig();

        // methode testée
        Parameter parameter = vfsConfig.getPath("name03");

        // vérifications
        assertNull(parameter);
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

        VFSConfig vfsConfig=new VFSConfig();

        for(int i=0;i<10;i++){
            int no=i+1;

            String nom="name"+no;
            Path path=Paths.get("/tmp/ccc"+no);
            boolean readOnly=(no%2==1);

            vfsConfig.addPath(nom, path, readOnly);
        }

        // methode testée
        Parameter parameter = vfsConfig.getPath(nameRef);

        // vérifications
        if(!existRef){
            assertNull(parameter);
        } else {
            assertNotNull(parameter);
            assertEquals(pathRef, parameter.getPath());
            assertEquals(readonlyRef, parameter.isReadonly());
        }
    }

}