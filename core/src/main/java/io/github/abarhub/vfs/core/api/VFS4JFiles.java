package io.github.abarhub.vfs.core.api;

import java.io.*;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class VFS4JFiles {

    protected static VFS4JFileManager fileManager = VFS4JDefaultFileManager.get();

    private VFS4JFiles() {
    }

    private static VFS4JFileManager getFileManager() {
        return fileManager;
    }

    protected static void setFileManager(VFS4JFileManager fileManager) {
        VFS4JFiles.fileManager = fileManager;
    }

    public static void reinit() {
        fileManager = VFS4JDefaultFileManager.get();
    }

    public static VFS4JPathName setAttribute(VFS4JPathName path, String attribute, Object value, LinkOption... options) throws IOException {
        return getFileManager().getAttribute().setAttribute(path, attribute, value, options);
    }

    public static VFS4JPathName setLastModifiedTime(VFS4JPathName path, FileTime time) throws IOException {
        return getFileManager().getAttribute().setLastModifiedTime(path, time);
    }

    public static VFS4JPathName setOwner(VFS4JPathName path, UserPrincipal userPrincipal) throws IOException {
        return getFileManager().getAttribute().setOwner(path, userPrincipal);
    }

    public static VFS4JPathName setPosixFilePermissions(VFS4JPathName path, Set<PosixFilePermission> posixFilePermissions) throws IOException {
        return getFileManager().getAttribute().setPosixFilePermissions(path, posixFilePermissions);
    }

    public static Object getAttribute(VFS4JPathName path, String attribute, LinkOption... options) throws IOException {
        return getFileManager().getAttribute().getAttribute(path, attribute, options);
    }

    public static <T extends FileAttributeView> T getFileAttributeView(VFS4JPathName path, Class<T> type, LinkOption... options) throws IOException {
        return getFileManager().getAttribute().getFileAttributeView(path, type, options);
    }

    public static FileTime getLastModifiedTime(VFS4JPathName path, LinkOption... options) throws IOException {
        return getFileManager().getAttribute().getLastModifiedTime(path, options);
    }

    public static UserPrincipal getOwner(VFS4JPathName path, LinkOption... options) throws IOException {
        return getFileManager().getAttribute().getOwner(path, options);
    }

    public static Set<PosixFilePermission> getPosixFilePermissions(VFS4JPathName path, LinkOption... options) throws IOException {
        return getFileManager().getAttribute().getPosixFilePermissions(path, options);
    }

    public static boolean isExecutable(VFS4JPathName path) throws IOException {
        return getFileManager().getAttribute().isExecutable(path);
    }

    public static boolean isReadable(VFS4JPathName path) throws IOException {
        return getFileManager().getAttribute().isReadable(path);
    }

    public static boolean isHidden(VFS4JPathName path) throws IOException {
        return getFileManager().getAttribute().isHidden(path);
    }

    public static boolean isWritable(VFS4JPathName file) throws IOException {
        return getFileManager().getAttribute().isWritable(file);
    }

    public static Map<String, Object> readAttributes(VFS4JPathName file, String attribute, LinkOption... options) throws IOException {
        return getFileManager().getAttribute().readAttributes(file, attribute, options);
    }

    public static <T extends BasicFileAttributes> T readAttributes(VFS4JPathName file, Class<T> type, LinkOption... options) throws IOException {
        return getFileManager().getAttribute().readAttributes(file, type, options);
    }

    public static void createFile(VFS4JPathName file, FileAttribute<?>... attrs) throws IOException {
        getFileManager().getCommand().createFile(file, attrs);
    }

    public static void createDirectory(VFS4JPathName file, FileAttribute<?>... attrs) throws IOException {
        getFileManager().getCommand().createDirectory(file, attrs);
    }

    public static void createDirectories(VFS4JPathName file, FileAttribute<?>... attrs) throws IOException {
        getFileManager().getCommand().createDirectories(file, attrs);
    }

    public static void delete(VFS4JPathName file) throws IOException {
        getFileManager().getCommand().delete(file);
    }

    public static boolean deleteIfExists(VFS4JPathName file) throws IOException {
        return getFileManager().getCommand().deleteIfExists(file);
    }

    public static VFS4JPathName createLink(VFS4JPathName file, VFS4JPathName target) throws IOException {
        return getFileManager().getCommand().createLink(file, target);
    }

    public static VFS4JPathName createSymbolicLink(VFS4JPathName link, VFS4JPathName target, FileAttribute<?>... attrs) throws IOException {
        return getFileManager().getCommand().createSymbolicLink(link, target, attrs);
    }

    public static long copy(InputStream input, VFS4JPathName target, CopyOption... options) throws IOException {
        return getFileManager().getCommand().copy(input, target, options);
    }

    public static long copy(VFS4JPathName source, OutputStream out) throws IOException {
        return getFileManager().getCommand().copy(source, out);
    }

    public static VFS4JPathName copy(VFS4JPathName source, VFS4JPathName target, CopyOption... options) throws IOException {
        return getFileManager().getCommand().copy(source, target, options);
    }

    public static VFS4JPathName move(VFS4JPathName source, VFS4JPathName target, CopyOption... options) throws IOException {
        return getFileManager().getCommand().move(source, target, options);
    }

    public static VFS4JPathName write(VFS4JPathName source, byte[] bytes, OpenOption... options) throws IOException {
        return getFileManager().getCommand().write(source, bytes, options);
    }

    public static VFS4JPathName write(VFS4JPathName source, Iterable<? extends CharSequence> lines, Charset cs, OpenOption... options) throws IOException {
        return getFileManager().getCommand().write(source, lines, cs, options);
    }

    public static InputStream newInputStream(VFS4JPathName pathName, OpenOption... options) throws IOException {
        return getFileManager().getOpen().newInputStream(pathName, options);
    }

    public static OutputStream newOutputStream(VFS4JPathName pathName, OpenOption... options) throws IOException {
        return getFileManager().getOpen().newOutputStream(pathName, options);
    }

    public static FileReader newReader(VFS4JPathName pathName) throws IOException {
        return getFileManager().getOpen().newReader(pathName);
    }

    public static FileWriter newWriter(VFS4JPathName pathName, boolean append) throws IOException {
        return getFileManager().getOpen().newWriter(pathName, append);
    }

    public static SeekableByteChannel newByteChannel(VFS4JPathName pathName, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        return getFileManager().getOpen().newByteChannel(pathName, options, attrs);
    }

    public static DirectoryStream<VFS4JPathName> newDirectoryStream(VFS4JPathName pathName, DirectoryStream.Filter<? super VFS4JPathName> filter) throws IOException {
        return getFileManager().getOpen().newDirectoryStream(pathName, filter);
    }

    public static boolean exists(VFS4JPathName file, LinkOption... options) {
        return getFileManager().getQuery().exists(file, options);
    }

    public static boolean isDirectory(VFS4JPathName file, LinkOption... options) {
        return getFileManager().getQuery().isDirectory(file, options);
    }

    public static boolean isRegularFile(VFS4JPathName file, LinkOption... options) {
        return getFileManager().getQuery().isRegularFile(file, options);
    }

    public static boolean isSameFile(VFS4JPathName file, VFS4JPathName file2) throws IOException {
        return getFileManager().getQuery().isSameFile(file, file2);
    }

    public static boolean isSymbolicLink(VFS4JPathName file) {
        return getFileManager().getQuery().isSymbolicLink(file);
    }

    public static Stream<String> lines(VFS4JPathName file) throws IOException {
        return getFileManager().getQuery().lines(file);
    }

    public static Stream<String> lines(VFS4JPathName file, Charset charsets) throws IOException {
        return getFileManager().getQuery().lines(file, charsets);
    }

    public static boolean notExists(VFS4JPathName file, LinkOption... options) {
        return getFileManager().getQuery().notExists(file, options);
    }

    public static byte[] readAllBytes(VFS4JPathName file) throws IOException {
        return getFileManager().getQuery().readAllBytes(file);
    }

    public static List<String> readAllLines(VFS4JPathName file) throws IOException {
        return getFileManager().getQuery().readAllLines(file);
    }

    public static List<String> readAllLines(VFS4JPathName file, Charset charset) throws IOException {
        return getFileManager().getQuery().readAllLines(file, charset);
    }

    public static long size(VFS4JPathName file) throws IOException {
        return getFileManager().getQuery().size(file);
    }

    public static Stream<VFS4JPathName> list(VFS4JPathName file) throws IOException {
        return getFileManager().getSearch().list(file);
    }

    public static Stream<VFS4JPathName> walk(VFS4JPathName file, int maxDepth, FileVisitOption... options) throws IOException {
        return getFileManager().getSearch().walk(file, maxDepth, options);
    }

    public static Stream<VFS4JPathName> walk(VFS4JPathName file, FileVisitOption... options) throws IOException {
        return getFileManager().getSearch().walk(file, options);
    }

    public static Stream<VFS4JPathName> find(VFS4JPathName file, int maxDepth, BiPredicate<VFS4JPathName, BasicFileAttributes> matcher, FileVisitOption... options) throws IOException {
        return getFileManager().getSearch().find(file, maxDepth, matcher, options);
    }

}
