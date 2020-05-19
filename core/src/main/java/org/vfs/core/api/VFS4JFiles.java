package org.vfs.core.api;

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

    protected static FileManager fileManager = DefaultFileManager.get();

    private static FileManager getFileManager() {
        return fileManager;
    }

    protected static void setFileManager(FileManager aFileManager) {
        fileManager = aFileManager;
    }

    public static PathName setAttribute(PathName path, String attribute, Object value, LinkOption... options) throws IOException {
        return getFileManager().getAttribute().setAttribute(path, attribute, value, options);
    }

    public static PathName setLastModifiedTime(PathName path, FileTime time) throws IOException {
        return getFileManager().getAttribute().setLastModifiedTime(path, time);
    }

    public static PathName setOwner(PathName path, UserPrincipal userPrincipal) throws IOException {
        return getFileManager().getAttribute().setOwner(path, userPrincipal);
    }

    public static PathName setPosixFilePermissions(PathName path, Set<PosixFilePermission> posixFilePermissions) throws IOException {
        return getFileManager().getAttribute().setPosixFilePermissions(path, posixFilePermissions);
    }

    public static Object getAttribute(PathName path, String attribute, LinkOption... options) throws IOException {
        return getFileManager().getAttribute().getAttribute(path, attribute, options);
    }

    public static <T extends FileAttributeView> T getFileAttributeView(PathName path, Class<T> type, LinkOption... options) throws IOException {
        return getFileManager().getAttribute().getFileAttributeView(path, type, options);
    }

    public static FileTime getLastModifiedTime(PathName path, LinkOption... options) throws IOException {
        return getFileManager().getAttribute().getLastModifiedTime(path, options);
    }

    public static UserPrincipal getOwner(PathName path, LinkOption... options) throws IOException {
        return getFileManager().getAttribute().getOwner(path, options);
    }

    public static Set<PosixFilePermission> getPosixFilePermissions(PathName path, LinkOption... options) throws IOException {
        return getFileManager().getAttribute().getPosixFilePermissions(path, options);
    }

    public static boolean isExecutable(PathName path) throws IOException {
        return getFileManager().getAttribute().isExecutable(path);
    }

    public static boolean isReadable(PathName path) throws IOException {
        return getFileManager().getAttribute().isReadable(path);
    }

    public static boolean isHidden(PathName path) throws IOException {
        return getFileManager().getAttribute().isHidden(path);
    }

    public static boolean isWritable(PathName file) {
        return getFileManager().getAttribute().isWritable(file);
    }

    public static Map<String, Object> readAttributes(PathName file, String attribute, LinkOption... options) throws IOException {
        return getFileManager().getAttribute().readAttributes(file, attribute, options);
    }

    public static <T extends BasicFileAttributes> T readAttributes(PathName file, Class<T> type, LinkOption... options) throws IOException {
        return getFileManager().getAttribute().readAttributes(file, type, options);
    }

    public static void createFile(PathName file, FileAttribute<?>... attrs) throws IOException {
        getFileManager().getCommand().createFile(file, attrs);
    }

    public static void createDirectory(PathName file, FileAttribute<?>... attrs) throws IOException {
        getFileManager().getCommand().createDirectory(file, attrs);
    }

    public static void createDirectories(PathName file, FileAttribute<?>... attrs) throws IOException {
        getFileManager().getCommand().createDirectories(file, attrs);
    }

    public static void delete(PathName file) throws IOException {
        getFileManager().getCommand().delete(file);
    }

    public static boolean deleteIfExists(PathName file) throws IOException {
        return getFileManager().getCommand().deleteIfExists(file);
    }

    public static PathName createLink(PathName file, PathName target) throws IOException {
        return getFileManager().getCommand().createLink(file, target);
    }

    public static PathName createSymbolicLink(PathName link, PathName target, FileAttribute<?>... attrs) throws IOException {
        return getFileManager().getCommand().createSymbolicLink(link, target, attrs);
    }

    public static long copy(InputStream input, PathName target, CopyOption... options) throws IOException {
        return getFileManager().getCommand().copy(input, target, options);
    }

    public static long copy(PathName source, OutputStream out) throws IOException {
        return getFileManager().getCommand().copy(source, out);
    }

    public static PathName copy(PathName source, PathName target, CopyOption... options) throws IOException {
        return getFileManager().getCommand().copy(source, target, options);
    }

    public static PathName move(PathName source, PathName target, CopyOption... options) throws IOException {
        return getFileManager().getCommand().move(source, target, options);
    }

    public static PathName write(PathName source, byte[] bytes, OpenOption... options) throws IOException {
        return getFileManager().getCommand().write(source, bytes, options);
    }

    public static PathName write(PathName source, Iterable<? extends CharSequence> lines, Charset cs, OpenOption... options) throws IOException {
        return getFileManager().getCommand().write(source, lines, cs, options);
    }

    public static InputStream newInputStream(PathName pathName, OpenOption... options) throws IOException {
        return getFileManager().getOpen().newInputStream(pathName, options);
    }

    public static OutputStream newOutputStream(PathName pathName, OpenOption... options) throws IOException {
        return getFileManager().getOpen().newOutputStream(pathName, options);
    }

    public static FileReader newReader(PathName pathName) throws IOException {
        return getFileManager().getOpen().newReader(pathName);
    }

    public static FileWriter newWriter(PathName pathName, boolean append) throws IOException {
        return getFileManager().getOpen().newWriter(pathName, append);
    }

    public static SeekableByteChannel newByteChannel(PathName pathName, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        return getFileManager().getOpen().newByteChannel(pathName, options, attrs);
    }

    public static DirectoryStream<PathName> newDirectoryStream(PathName pathName, DirectoryStream.Filter<? super PathName> filter) throws IOException {
        return getFileManager().getOpen().newDirectoryStream(pathName, filter);
    }

    public static boolean exists(PathName file, LinkOption... options) {
        return getFileManager().getQuery().exists(file, options);
    }

    public static boolean isDirectory(PathName file, LinkOption... options) {
        return getFileManager().getQuery().isDirectory(file, options);
    }

    public static boolean isRegularFile(PathName file, LinkOption... options) {
        return getFileManager().getQuery().isRegularFile(file, options);
    }

    public static boolean isSameFile(PathName file, PathName file2) throws IOException {
        return getFileManager().getQuery().isSameFile(file, file2);
    }

    public static boolean isSymbolicLink(PathName file) {
        return getFileManager().getQuery().isSymbolicLink(file);
    }

    public static Stream<String> lines(PathName file) throws IOException {
        return getFileManager().getQuery().lines(file);
    }

    public static Stream<String> lines(PathName file, Charset charsets) throws IOException {
        return getFileManager().getQuery().lines(file, charsets);
    }

    public static boolean notExists(PathName file, LinkOption... options) {
        return getFileManager().getQuery().notExists(file, options);
    }

    public static byte[] readAllBytes(PathName file) throws IOException {
        return getFileManager().getQuery().readAllBytes(file);
    }

    public static List<String> readAllLines(PathName file) throws IOException {
        return getFileManager().getQuery().readAllLines(file);
    }

    public static List<String> readAllLines(PathName file, Charset charset) throws IOException {
        return getFileManager().getQuery().readAllLines(file, charset);
    }

    public static long size(PathName file) throws IOException {
        return getFileManager().getQuery().size(file);
    }

    public static Stream<PathName> list(PathName file) throws IOException {
        return getFileManager().getSearch().list(file);
    }

    public static Stream<PathName> walk(PathName file, int maxDepth, FileVisitOption... options) throws IOException {
        return getFileManager().getSearch().walk(file, maxDepth, options);
    }

    public static Stream<PathName> walk(PathName file, FileVisitOption... options) throws IOException {
        return getFileManager().getSearch().walk(file, options);
    }

    public static Stream<PathName> find(PathName file, int maxDepth, BiPredicate<PathName, BasicFileAttributes> matcher, FileVisitOption... options) throws IOException {
        return getFileManager().getSearch().find(file, maxDepth, matcher, options);
    }

}
