package io.github.abarhub.vfs.logback;

import io.github.abarhub.vfs.core.api.VFS4JFiles;
import io.github.abarhub.vfs.core.api.path.VFS4JPathName;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

public class VFS4JResilientFileOutputStream extends VFS4JResilientOutputStreamBase {
    private VFS4JPathName file;
    private OutputStream fos;

    public VFS4JResilientFileOutputStream(VFS4JPathName file, boolean append, long bufferSize) throws IOException {
        this.file = file;
        OpenOption option[];
        if (append) {
            option = new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND};
        } else {
            option = new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING};
        }
        this.fos = VFS4JFiles.newOutputStream(file, option);
        //this.os = new BufferedOutputStream(this.fos, (int) bufferSize);
        //fos = VFS4JFiles.newOutputStream(file, (append) ? StandardOpenOption.APPEND : null);
        this.os = new BufferedOutputStream(this.fos, (int) bufferSize);
        this.presumedClean = true;
    }

//    public FileChannel getChannel() {
//        //return this.os == null ? null : this.fos.getChannel();
//        try {
//            if (true) throw new RuntimeException("Erreur");
//            return (FileChannel) VFS4JFiles.newByteChannel(file, null);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    public File getFile() {
//        return null;
//    }

    //    @Override
    String getDescription() {
        return "file [" + this.file + "]";
    }

    //    @Override
    OutputStream openNewOutputStream() throws IOException {
        //this.fos = new FileOutputStream(this.file, true);
        return new BufferedOutputStream(this.fos);
    }

//    String getDescription() {
//        return null;
//    }
//
//    OutputStream openNewOutputStream() throws IOException {
//        return null;
//    }

}
