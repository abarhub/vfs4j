package io.github.abarhub.vfs.core.plugin.audit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum VFS4JAuditGroupOperations {
    COMMAND(VFS4JAuditOperation.CREATE_FILE, VFS4JAuditOperation.CREATE_DIRECTORY, VFS4JAuditOperation.CREATE_DIRECTORIES, VFS4JAuditOperation.DELETE, VFS4JAuditOperation.DELETE_IF_EXISTS, VFS4JAuditOperation.CREATE_LINK, VFS4JAuditOperation.CREATE_SYMBOLIC_LINK, VFS4JAuditOperation.COPY,
            VFS4JAuditOperation.MOVE, VFS4JAuditOperation.WRITE),

    ATTRIBUTE(VFS4JAuditOperation.SET_ATTRIBUTE, VFS4JAuditOperation.SET_LAST_MODIFIED_TIME, VFS4JAuditOperation.SET_OWNER, VFS4JAuditOperation.SET_POSIX_FILE_PERMISSIONS, VFS4JAuditOperation.GET_ATTRIBUTE, VFS4JAuditOperation.GET_FILE_ATTRIBUTE_VIEW,
            VFS4JAuditOperation.GET_LAST_MODIFIED_TIME, VFS4JAuditOperation.GET_OWNER, VFS4JAuditOperation.GET_POSIX_FILE_PERMISSIONS, VFS4JAuditOperation.IS_EXECUTABLE, VFS4JAuditOperation.IS_READABLE, VFS4JAuditOperation.IS_HIDDEN,
            VFS4JAuditOperation.IS_WRITABLE, VFS4JAuditOperation.READ_ATTRIBUTES),

    OPEN(VFS4JAuditOperation.NEW_INPUT_STREAM, VFS4JAuditOperation.NEW_OUTPUT_STREAM, VFS4JAuditOperation.NEW_READER, VFS4JAuditOperation.NEW_WRITER, VFS4JAuditOperation.NEW_BYTE_CHANNEL, VFS4JAuditOperation.NEW_DIRECTORY_STREAM),

    SEARCH(VFS4JAuditOperation.LIST, VFS4JAuditOperation.WALK, VFS4JAuditOperation.FIND),

    QUERY(VFS4JAuditOperation.EXISTS, VFS4JAuditOperation.IS_DIRECTORY, VFS4JAuditOperation.IS_REGULAR_FILE, VFS4JAuditOperation.IS_SAME_FILE, VFS4JAuditOperation.IS_SYMBOLIC_LINK, VFS4JAuditOperation.LINES, VFS4JAuditOperation.NOT_EXISTS,
            VFS4JAuditOperation.READ_ALL_BYTES, VFS4JAuditOperation.READ_ALL_LINES, VFS4JAuditOperation.SIZE);

    VFS4JAuditGroupOperations(VFS4JAuditOperation... operations) {
        List<VFS4JAuditOperation> liste = new ArrayList<>();
        if (operations != null && operations.length > 0) {
            liste.addAll(Arrays.asList(operations));
        }
        this.operations = Collections.unmodifiableList(liste);
    }

    private final List<VFS4JAuditOperation> operations;

    public List<VFS4JAuditOperation> getOperations() {
        return operations;
    }
}
