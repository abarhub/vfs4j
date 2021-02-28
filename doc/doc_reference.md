# Documentation de référence

## Description générale

Il faut configurer les répertoires. Chaque répertoire a un nom. Ce nom peut être utilisé dans l'application pour accéder
aux fichiers.

## Configuration

La configuration peut être faites soit par un fichier de configuration, soit par programmation. Pour le fichier de
configuration, soit il est déclarré avec la propriété systeme 'VFS_CONFIG', soit le fichier 'vfs.properties' doit être
présent sur le classpath.

Le fichier de configuration doit être composé par des cles et des valeurs. Le format de la cle est :
vfs.paths.'nom'.'propriété'
par exemple :
vfs.paths.dir1.mode

Chaque répertoire doit avoir un mode. Il existe trois modes :

* STANDARD : Ce mode permet d'accèder aux fichiers et répertoires sur le disque dur. C'est le mode par défaut.
* TEMPORARY : Ce mode crée un répertoire temporaire. A chaque démarrage de l'application, ce répertoire est vide.
* CLASSPATH : Ce mode permet d'accèder au classpath. Il ne peut être qu'en lecture seule.

Les options :

* path : le répertoire
* readonly : permet d'indiquer si le répertoire est accéssible en lecture seule ou lecture/écriture. Les valeurs
  possibles sont true ou false.

## Manipulation des fichiers et répertoires

La création du chemin d'accès se fait avec la classe `org.vfs.core.api.VFS4JPathName`. Le 1er paramètre est le nom du
path. Les autres paramètres sont le chemin à partir de ce nom. Il est conséillé d'utiliser toujours le / pour que le
chemin fonctionne sous Linux et Windows. On peut aussi utiliser la classe utilitaire `org.vfs.core.api.VFS4JPaths`.

Exemples :

```java
VFS4JPathName path=VFS4JPaths.get("app","messages.properties");
        VFS4JPathName path2=VFS4JPaths.get("app","directory","config/messages.properties");
        VFS4JPathName path3=new VFS4JPathName("dir1","mydirectory","file01.txt");
```

Ensuite, une fois l'objet créé, il faut utiliser les methodes de la classe `org.vfs.core.api.VFS4JFiles`. Les methodes
correspondent à celles de la classe `Files`.

Exemples :

```java
VFS4JFiles.createFile(path);
        VFS4JFiles.createDirectories(directory);
        VFS4JFiles.copy(file1,file2);
```

## Plugins

## Création d'un plugin

Pour créer un plugin, il faut créer une classe qui implémente
l'interface `org.vfs.core.plugin.common.VFS4JPluginsFactory` et une autre pour
l'interface `org.vfs.core.plugin.common.VFS4JPlugins`. Ensuite, pour activer le plugin, il faut aller dans

### Plugins Audit

Il existe un plugin pour auditer les opérations. Cela permet de logger les actions qui sont faites sur le FS. 





