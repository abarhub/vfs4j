package org.vfs.core.plugin.common;

import org.vfs.core.api.operation.*;

import java.util.Optional;

public interface VFS4JPlugins {

    Optional<Command> getCommand(Command command);

    Optional<Attribute> getAttribute(Attribute attribute);

    Optional<Open> getOpen(Open open);

    Optional<Query> getQuery(Query query);

    Optional<Search> getSearch(Search search);

}
