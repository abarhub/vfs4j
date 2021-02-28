package org.vfs.core.plugin.common;

import org.vfs.core.api.operation.*;

import java.util.Optional;

public interface VFS4JPlugins {

    Optional<VFS4JCommand> getCommand(VFS4JCommand command);

    Optional<VFS4JAttribute> getAttribute(VFS4JAttribute attribute);

    Optional<VFS4JOpen> getOpen(VFS4JOpen open);

    Optional<VFS4JQuery> getQuery(VFS4JQuery query);

    Optional<VFS4JSearch> getSearch(VFS4JSearch search);

}
