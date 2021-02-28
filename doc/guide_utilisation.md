il faut créer dans le classpath le fichier suivant :

```code
vfs.paths.dir1.path=/var/local/dir
vfs.paths.dir1.mode=STANDARD
vfs.paths.dir2.mode=TEMPORARY
vfs.paths.dir3.path=/var/local/dir2
vfs.paths.dir3.readonly=true
```

Ensuite dans le code il faut faire :

```java
VFS4JFiles.createFile(new PathName("dir1","file01.txt"));
        VFS4JFiles.createDirectories(new PathName("dir2","direcory1/dir2/dir3"));
        VFS4JFiles.copy(new PathName("dir1","file01.txt"),new PathName("dir2","file01.txt"));
```
