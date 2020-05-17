package org.vfs.core.api;

import org.junit.jupiter.api.Test;
import org.vfs.core.config.PathParameter;
import org.vfs.core.config.VFS4JConfig;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class ParseConfigFileTest {

    @Test
    void parse() {
        final String dir1 = "dir1";
        final String path1 = "dir1";
        final String dir2 = "dir2";
        final String path2 = "dir2";
        Properties properties=new Properties();
        properties.put("vfs.paths.dir1.path",path1);
        properties.put("vfs.paths.dir2.path",path2);
        ParseConfigFile parseConfigFile=new ParseConfigFile();

        // methode testée
        FileManagerBuilder res = parseConfigFile.parse(properties);

        // vérifications
        assertNotNull(res);
        VFS4JConfig res2 = res.build();
        assertNotNull(res2);
        List<String> liste=new ArrayList<>();
        liste.add(dir1);
        liste.add(dir2);
        assertEquals(new HashSet<>(liste),new HashSet<>(res2.getNames()));
        PathParameter pathParameter =res2.getPath(dir1);
        assertNotNull(pathParameter);
        assertEquals(Paths.get(path1), pathParameter.getPath());
        assertFalse(pathParameter.isReadonly());
        pathParameter =res2.getPath(dir2);
        assertNotNull(pathParameter);
        assertEquals(Paths.get(path2), pathParameter.getPath());
        assertFalse(pathParameter.isReadonly());
    }

}