# vfs4j

[![Build Status](https://travis-ci.com/abarhub/vfs4j.svg?branch=master)](https://travis-ci.com/abarhub/vfs4j)


[![codecov](https://codecov.io/gh/abarhub/vfs4j/branch/master/graph/badge.svg)](https://codecov.io/gh/abarhub/vfs4j)


# VFS4J
C'est une librairie pour virtualiser le systeme de fichier.

Pour ajouter la dépendance, il faut ajouter dans le pom de maven :

```xml

<dependency>
  <groupId>io.github.abarhub.vfs4j</groupId>
  <artifactId>vfs4j-core</artifactId>
  <version>0.3.0-SNAPSHOT</version>
</dependency>
```

## Fonctionnalitées

* Java 8 minimum. Fonctionne avec Java 11
* Limitation des répertoires accéssibles par l'application
* Le paramétrage est défini dans un fichier de configuration hors de l'application
* mécanisme de plugin pour modifier les comportements
  * Audit pour logger les opérations sir le filesystem. Possibilité de n'auditer qu'une partie des opérations en
    fonction du type d'opération (lecture, écriture, copie, etc...) et/ou un filtre glob (**/*.jpg)
* Dépendance avec SLF4J uniquement
* Les répertoires configurés peuvent être soit un répertoire, soit le classpath, soit un répertoire temporaire, soit un
  répertoire avec un filesystem déclaré.
* possibilité de limiter en lecture l'accès aux répertoires

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
VFS4JFiles.createFile(new VFS4JPathName("dir1","file01.txt"));
        VFS4JFiles.createDirectories(new VFS4JPathName("dir2","direcory1/dir2/dir3"));
        VFS4JFiles.copy(new VFS4JPathName("dir1","file01.txt"),new PathName("dir2","file01.txt"));
```

Il y a d'autres exemples [ici](./doc/guide_utilisation.md).

## Liens

- [GitHub project](https://github.com/abarhub/vfs4j)
- [Issue tracker: Report a defect or feature request](https://github.com/abarhub/vfs4j/issues/new)
- [Guide d'utilisation](./doc/guide_utilisation.md)
- [Documentation de référence](./doc/doc_reference.md)


