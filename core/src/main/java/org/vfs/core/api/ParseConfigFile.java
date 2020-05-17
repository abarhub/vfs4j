package org.vfs.core.api;

import org.vfs.core.exception.VFSException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ParseConfigFile {

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
            if(o instanceof String){
                Object o2=properties.get(o);
                if(o2 instanceof String){
                    String key= (String) o;
                    String value= (String) o2;
                    map.put(key, value);
                }
            }
        }
        // détermination des noms
        List<String> liste=map.keySet()
                .stream()
                .filter(x -> x.startsWith(PREFIX_PATHS))
                .map(x -> x.substring(PREFIX_PATHS.length()))
                .filter(x -> x.endsWith(SUFFIX_PATH))
                .map(x -> x.substring(0, x.indexOf(SUFFIX_PATH)))
                .filter(x -> !x.trim().isEmpty())
                .filter(x -> x.matches(VALIDE_NAME))
                .distinct()
                .collect(Collectors.toList());

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
