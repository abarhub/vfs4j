package org.vfs.core.api;

import org.slf4j.Logger;
import org.vfs.core.exception.VFSException;
import org.vfs.core.util.VFSLoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ParseConfigFile {

    private static final Logger LOGGER = VFSLoggerFactory.getLogger(ParseConfigFile.class);

    public static final String PREFIX = "vfs.";
    public static final String PREFIX_PATHS = PREFIX+"paths.";
    public static final String SUFFIX_PATH = ".path";
    public static final String VALIDE_NAME = "[a-zA-Z][a-zA-Z0-9]*";

    // construction de la map des propriétés (on enlève ce qui n'est pas de type string)
    public FileManagerBuilder parse(Properties properties){
        FileManagerBuilder fileManagerBuilder=new FileManagerBuilder();

        Set<Object> keys=properties.keySet();
        Map<String, String> map=new HashMap<>();
        for(Object o:keys){
            if(o!=null&&o instanceof String){
                Object o2=properties.get(o);
                if(o2 instanceof String){
                    String key= (String) o;
                    String value= (String) o2;
                    map.put(key, value);
                }
            }
        }
        // détermination des noms
        List<String> liste=new ArrayList<>();
        for(Map.Entry<String,String> entry:map.entrySet()){
            String key=entry.getKey();
            if(key.startsWith(PREFIX_PATHS)){
                String s=key.substring(PREFIX_PATHS.length());
                if(s.endsWith(SUFFIX_PATH)){
                    s=s.substring(0, s.indexOf(SUFFIX_PATH));
                    if(!s.trim().isEmpty() && s.matches(VALIDE_NAME)){
                        liste.add(s);
                    } else {
                        LOGGER.debug("key '{}' ignored (bad name)", key);
                    }
                } else {
                    LOGGER.debug("key '{}' ignored (bad suffix)", key);
                }
            } else {
                LOGGER.debug("key '{}' ignored (bad prefix)", key);
            }
        }

        // ajout des paths dans fileManagerBuilder
        for(String nom:liste){
            String key= PREFIX_PATHS +nom+ SUFFIX_PATH;
            if(map.containsKey(key)){
                String value=map.get(key);
                if(value==null||value.trim().isEmpty()){
                    throw new VFSException("Path for '"+key+"' is empty");
                } else {
                    Path p= Paths.get(value);
                    fileManagerBuilder.addPath(nom, p, false);
                }
            }
        }

        return fileManagerBuilder;
    }
}
