package io.github.abarhub.vfs.core.api.path;

public interface VFS4JPathName {

    String getName();

    String getPath();

    /**
     * Return the parent of path. If there is no parent, path is empty ("").
     * For examples, parent of /aaa/bbb/ccc is /aaa/bbb/cc and parent of /aaa/bbb/ccc/ is /aaa/bbb/ccc
     * Special charactere ("." or "..") is not removed.
     *
     * @return Parent
     */
    VFS4JPathName getParent();

    /**
     * Return a new VFS4JPathName with :
     * name = this.name
     * path = this.path+"/"+path
     * if path end with "/" ou "\\" or path start with "/" or "\\", "/" is not added
     *
     * @param path path to append
     * @return a new VFS4JPathName
     */
    VFS4JPathName resolve(String path);

    /**
     * Return number of element in the path. Ignore the first or the last "/".
     *
     * @return Number of element in the path
     */
    int getNameCount();

    /**
     * Return element number no. Ignore the first or the last "/".
     *
     * @param no The element to get
     * @return The element
     */
    String getName(int no);

    /**
     * Return le filename. If path is empty, return "".
     * Filename is the last element of path.
     *
     * @return The filename or "" if empty
     * @since 0.7.3
     */
    String getFilename();

    /**
     * Return a normalised path. The normalized path remove "." and ".."
     *
     * @return a path normalized
     */
    VFS4JPathName normalize();

    /**
     * Return relative path.
     * Example :
     * new VFS4JPathName("name", "aaa/bbb").relativize("aaa/bbb/ccc").equals("ccc")
     *
     * @param other path to relativize. It must start by path
     * @return The relative path
     */
    String relativize(String other);

    /**
     * Return true if path starts with other
     *
     * @param other the path. Must not be null.
     * @return true if path starts with other
     */
    boolean startsWith(String other);

    /**
     * Return true if path end with other
     *
     * @param other the path. Must not be null.
     * @return true if path ends with other
     */
    boolean endsWith(String other);

}
