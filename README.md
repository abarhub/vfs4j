# vfs4j

[![Build Status](https://travis-ci.com/abarhub/vfs4j.svg?branch=master)](https://travis-ci.com/abarhub/vfs4j)


[![codecov](https://codecov.io/gh/abarhub/vfs4j/branch/master/graph/badge.svg)](https://codecov.io/gh/abarhub/vfs4j)


# VFS4J
C'est une librairie pour virtualiser le systeme de fichier.

Pour ajouter la dépendance, il faut ajouter dans le pom de maven :

```xml
<dependency>
  <groupId>org.vfs4j</groupId>
  <artifactId>vfs4j-parent</artifactId>
  <version>0.2.0-SNAPSHOT</version>
</dependency>
```

## Guide d'utilisation

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
VFS4JFiles.createFile(new PathName("dir1", "file01.txt"));
VFS4JFiles.createDirectories(new PathName("dir2", "direcory1/dir2/dir3"));
VFS4JFiles.copy(new PathName("dir1", "file01.txt"), new PathName("dir2", "file01.txt"));
```

## Liens

-   [GitHub project](https://github.com/abarhub/vfs4j)
-   [Issue tracker: Report a defect or feature request](https://github.com/abarhub/vfs4j/issues/new)


