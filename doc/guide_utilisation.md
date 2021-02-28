# Guide d'utilisation

## Exemple simple

Il faut créer dans le classpath le fichier suivant :

```code
vfs.paths.dir1.path=/var/local/dir
vfs.paths.dir1.mode=STANDARD
vfs.paths.dir2.mode=TEMPORARY
vfs.paths.dir3.path=/var/local/dir2
vfs.paths.dir3.readonly=true
```

Ensuite dans le code il faut faire :

```java
VFS4JFiles.createFile(new VFS4JPathName("dir1","file01.txt"));
        VFS4JFiles.createDirectories(new VFS4JPathName("dir2","direcory1/dir2/dir3"));
        VFS4JFiles.copy(new VFS4JPathName("dir1","file01.txt"),new VFS4JPathName("dir2","file01.txt"));
```

## Exemple plugin audit

Il faut créer le fichier de configuration suivant :

```code
vfs.paths.rep01.path", path1.toString());
        properties.setProperty("vfs.paths.rep01.readonly", "false");
        properties.setProperty("vfs.plugins.plugins1.class", "org.vfs.core.plugin.audit.VFS4JAuditPluginsFactory");
        properties.setProperty("vfs.plugins.plugins1.operations", "COMMAND");
        properties.setProperty("vfs.plugins.plugins1.filterPath", "*.txt");
vfs.paths.dir1.path=/var/local/dir
vfs.paths.dir1.readonly=false
vfs.plugins.plugins1.class=org.vfs.core.plugin.audit.VFS4JAuditPluginsFactory
vfs.plugins.plugins1.operations=COMMAND
vfs.plugins.plugins1.filterPath=*.txt
```

Ce fichier va auditer toutes les opérations de modification (création de fichier, copie, etc...) sur les fichiers qui
ont l'extention `*.txt`.


