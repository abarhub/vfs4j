package org.vfs.core.config;

public class VFS4JConfigFactory {

    private VFS4JConfigFactory() {
    }

    public static VFS4JConfig createVfs4JConfig() {
        VFS4JConfig vfs4JConfig = new VFS4JConfig();
        return vfs4JConfig;
    }

    public static VFS4JConfig createVfs4JConfig(VFSConfigFile configFile) {
        VFS4JConfig vfs4JConfig = new VFS4JConfig();
        vfs4JConfig.init(configFile);
        return vfs4JConfig;
    }

}
