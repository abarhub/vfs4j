package org.vfs.core;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FsUtils {

    public static byte[] readAll(InputStream input) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        try {
            while ((nRead = input.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
        }finally{
            if(input!=null) {
                input.close();
            }
        }

        buffer.flush();
        return buffer.toByteArray();
    }

    public static List<String> readAll(FileReader reader) throws IOException {
        List<String> lignes=new ArrayList<>();
        try(BufferedReader bufferedReader=new BufferedReader(reader)){
            String line;
            while((line=bufferedReader.readLine())!=null){
                lignes.add(line);
            }
        }
        return lignes;
    }

    public static List<String> newList(String... contenu) {
        final List<String> liste=new ArrayList<>();
        if(contenu!=null){
            liste.addAll(Arrays.asList(contenu));
        }
        return liste;
    }
}
