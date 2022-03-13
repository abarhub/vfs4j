package io.github.abarhub.vfs.core.plugin.unclosed;

import io.github.abarhub.vfs.core.util.VFS4JLoggerFactory;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class VFS4JLogIfNotClosedAfterDelay {

    private static final Logger LOGGER = VFS4JLoggerFactory.getLogger(VFS4JLogIfNotClosedAfterDelay.class);

    private final Timer timer = new Timer("unclosed");
    private final VFS4JUnclosedPlugins vfs4JUnclosedPlugins;
    private final long logIfNotClosedAfterMs;
    private final Map<Long, Object> objectList = new ConcurrentHashMap<>();

    public VFS4JLogIfNotClosedAfterDelay(VFS4JUnclosedPlugins vfs4JUnclosedPlugins, long logIfNotClosedAfterMs) {
        this.vfs4JUnclosedPlugins = vfs4JUnclosedPlugins;
        this.logIfNotClosedAfterMs = logIfNotClosedAfterMs;
    }

    public void init() {

    }

    public synchronized void create(Object o, long no) {
        objectList.put(no, o);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Long no = findObject(o);
                if (no != null && no > -1) {
                    notClosed(no, o);
                }
            }
        }, logIfNotClosedAfterMs);
    }

    protected void notClosed(Long no, Object o) {
        LOGGER.info("Object {} not close in {} millisecondes", no, logIfNotClosedAfterMs);
    }

    public synchronized void close(Object o) {
        Long no = findObject(o);
        if (no != null && no > -1) {
            objectList.remove(no);
        }
    }

    private Long findObject(Object o) {
        for (Map.Entry<Long, Object> tmp : objectList.entrySet()) {
            if (tmp.getValue() == o) {
                return tmp.getKey();
            }
        }
        return -1L;
    }
}
