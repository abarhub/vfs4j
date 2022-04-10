package io.github.abarhub.vfs.logback;

import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.recovery.ResilientFileOutputStream;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.FileUtil;
import io.github.abarhub.vfs.core.api.VFS4JFiles;
import io.github.abarhub.vfs.core.api.path.VFS4JPathName;
import io.github.abarhub.vfs.core.api.path.VFS4JPaths;

import java.io.File;
import java.io.IOException;

// https://github.com/qos-ch/logback/blob/master/logback-core/src/main/java/ch/qos/logback/core/FileAppender.java

public class VFS4JFileAppender<E> extends FileAppender<E> {

    private FileSize bufferSize= new FileSize(8192L);;

//    public void openFile0(String file_name) throws IOException {
//        System.out.println("openFile"+file_name);
//        lock.lock();
//        try {
//            File file = new File(file_name);
//            boolean result = FileUtil.createMissingParentDirectories(file);
//            if (!result) {
//                addError("Failed to create parent directories for [" + file.getAbsolutePath() + "]");
//            }
//
//            ResilientFileOutputStream resilientFos = new ResilientFileOutputStream(file, append, 10);
//            resilientFos.setContext(context);
//            setOutputStream(resilientFos);
//        } finally {
//            lock.unlock();
//        }
//    }

    public void openFile(String file_name) throws IOException {
        //System.out.println("openFile"+file_name);
        lock.lock();
        try {
//            File file = new File(file_name);
//            boolean result = FileUtil.createMissingParentDirectories(file);
//            if (!result) {
//                addError("Failed to create parent directories for [" + file.getAbsolutePath() + "]");
//            }
            VFS4JPathName pathName= VFS4JPaths.parsePath(file_name);
            VFS4JPathName parent=pathName.getParent();
            if(VFS4JFiles.notExists(parent)) {
                VFS4JFiles.createDirectories(parent);
            }
            //VFS4JFiles.createFile(pathName);

            VFS4JResilientFileOutputStream resilientFos = new VFS4JResilientFileOutputStream(pathName, append, bufferSize.getSize());
            resilientFos.setContext(context);
            setOutputStream(resilientFos);
        } finally {
            lock.unlock();
        }
    }

    public void setBufferSize(FileSize bufferSize) {
        super.setBufferSize(bufferSize);
        this.bufferSize = bufferSize;
    }

}
