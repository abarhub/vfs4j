package org.vfs.core.plugin.audit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.vfs.core.plugin.audit.VFS4JAuditOperation.*;

public enum VFS4JAuditGroupOperations {
    COMMAND(CREATE_FILE, CREATE_DIRECTORY, CREATE_DIRECTORIES, DELETE, DELETE_IF_EXISTS, CREATE_LINK, CREATE_SYMBOLIC_LINK, COPY,
            MOVE, WRITE),

    ATTRIBUTE(SET_ATTRIBUTE, SET_LAST_MODIFIED_TIME, SET_OWNER, SET_POSIX_FILE_PERMISSIONS, GET_ATTRIBUTE, GET_FILE_ATTRIBUTE_VIEW,
            GET_LAST_MODIFIED_TIME, GET_OWNER, GET_POSIX_FILE_PERMISSIONS, IS_EXECUTABLE, IS_READABLE, IS_HIDDEN,
            IS_WRITABLE, READ_ATTRIBUTES),

    OPEN(NEW_INPUT_STREAM, NEW_OUTPUT_STREAM, NEW_READER, NEW_WRITER, NEW_BYTE_CHANNEL, NEW_DIRECTORY_STREAM),

    SEARCH(LIST, WALK, FIND),

    QUERY(EXISTS, IS_DIRECTORY, IS_REGULAR_FILE, IS_SAME_FILE, IS_SYMBOLIC_LINK, LINES, NOT_EXISTS,
            READ_ALL_BYTES, READ_ALL_LINES, SIZE);

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
