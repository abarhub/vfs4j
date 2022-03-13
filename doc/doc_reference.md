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

Le chemin d'accès se fait avec la méthode `VFS4JPaths.get(String name, String... paths)` qui retourne
l'interface `VFS4JPathName`. Le 1er paramètre est le nom du path. Les autres paramètres sont le chemin à partir de ce
nom. Il est conseillé d'utiliser toujours le / pour que le chemin fonctionne sous Linux et Windows. On peut aussi
utiliser la classe utilitaire `VFS4JPaths`. Sous windows, le séparateur chemin `/` sera convertie par `\`.

Exemples :

```java
VFS4JPathName path=VFS4JPaths.get("app","messages.properties");
        VFS4JPathName path2=VFS4JPaths.get("app","directory","config/messages.properties");
        VFS4JPathName path3=VFS4JPaths.get("dir1","mydirectory","file01.txt");
        VFS4JPathName path4=VFS4JPaths.parsePath("dir1:mydirectory/file02.txt");
```

Ensuite, une fois l'objet créé, il faut utiliser les méthodes de la classe `VFS4JFiles`. Les méthodes correspondent à
celles de la classe `Files`.

Exemples :

```java
VFS4JFiles.createFile(path);
VFS4JFiles.createDirectories(directory);
VFS4JFiles.copy(file1,file2);
```

L'interface ```VFS4JPathName``` à des méthodes pour manipuler les fichiers.


| Nom de la méthode  | Type de retour | Description                                         |
|--------------------|----------------|-----------------------------------------------------|
| getParent()        | VFS4JPathName  | Retourne le répertoire parent                       | 
| resolve(String)    | VFS4JPathName  | Ajoute à la fin du chemin un autre chemin           | 
| relativize(String) | VFS4JPathName  | Retourne le chemin relatif                          |  
| normalize()        | VFS4JPathName  | Normalise le chemin en enlevant les "." et les ".." | 
| getNameCount()     | int            | Retourne le nombre délément.                        | 
| getName(int)       | String         | Retourne le ieme élément. I commence à 1.           | 


Exemples :
```java
VFS4JPathName path=VFS4JPaths.get("app","dir1/dir2/dir3");

assertEquals(VFS4JPaths.get("app","dir1/dir2"),path.getParent());

assertEquals(VFS4JPaths.get("app","dir1/dir2/dir3/directory/file.txt"), path.resolve("directory/file.txt");

assertEquals("dir4", path.relativize("dir1/dir2/dir3/dir4");

VFS4JPathName path2=VFS4JPaths.get("app","dir1/../dir3");
assertEquals(VFS4JPaths.get("app","dir1/dir3"),path.normalize());

assertEquals(3, path.getNameCount());
assertEquals("dir2",path.getName(2));
```

Pour le chemin dans le code, il est conseillé d'utiliser toujours le séparateur / à la unix. S'il y a des \\, la librairie peut 
les gérer, mais il y a des cas particuliers qui peuvent ne pas marcher.

La classe ```VFS4JFileManager``` gère la configuration. On peut y accéder avec la méthode ```VFS4JDefaultFileManager.get()```.
Par défaut, elle récupère le fichier de configuration. Il est possible de réinitialiser la configuration de la façon suivante :
```java
Properties properties=new Properties();

        properties.setProperty("vfs.paths.dir.path",directorySource.toString());
        properties.setProperty("vfs.paths.dir.readonly","false");

        VFS4JParseConfigFile parseConfigFile=new VFS4JParseConfigFile();
        VFS4JFileManagerBuilder fileManagerBuilder=parseConfigFile.parse(properties);
        VFS4JDefaultFileManager.get().setConfig(fileManagerBuilder.build());
        VFS4JFiles.reinit();
```

Cette classe contient la methode `matcher(String pattern)` pour faire des recherches glob (`*.txt`) ou regex (`[a-c]+`).
La syntaxe correspond à celle de ma
méthode [FileSystem.getPathMatcher(String syntaxAndPattern)](https://docs.oracle.com/javase/8/docs/api/java/nio/file/FileSystem.html#getPathMatcher-java.lang.String-)
de la librairie standard. Exemple d'utilisation :
`fileManager.matcher("glob:**/*.txt").matches(VFS4JPaths.get("path1", "dir/file1.txt"))`

## Plugins

## Création d'un plugin

Pour créer un plugin, il faut créer une classe qui implémente l'interface `VFS4JPluginsFactory` et une autre pour
l'interface `VFS4JPlugins`. Ensuite, pour activer le plugin, il faut aller dans

### Plugins Audit

Il existe un plugin pour auditer les opérations. Cela permet de logger les actions qui sont faites sur le FS. 

Pour l'activer, il faut ajouter le parametre :
```properties
vfs.plugins.XXX.class=io.github.abarhub.vfs.core.plugin.audit.VFS4JAuditPluginsFactory
```
en remplaçant XXX par le nom du plugin.

Voici un exemple d'utilisation :
```properties
vfs.plugins.plugins1.class=io.github.abarhub.vfs.core.plugin.audit.VFS4JAuditPluginsFactory
vfs.plugins.plugins1.operations=COMMAND
vfs.plugins.plugins1.filterPath=*.txt
```

La liste des paramètres est :

| Nom du Parametre | Valeurs possibles          | Description                                                                                                                                     |
|------------------|----------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| operations       | COMMAND, ...               | La liste des opérations ou des groupes d'opérations. Le séparateur est la virgule. Si la valeur est vide, toutes les opérations sontr traitées. |
| filterPath       | \*.txt, /etc/\*\*/\*.ini   | Un glob. Le séparateur est la virgule. Par défaut, tout est logué                                                                               |
| loglevel         | INFO, ERROR, DEBUG, etc... | Le niveau de log. Par défaut, le niveau de log est info                                                                                         |

Les logs sont associés à `io.github.abarhub.vfs.core.plugin.audit.VFS4JAuditPlugins`
La liste des opération défini plus bas.

Exemple de log :
```
16:56:51.409 [main] INFO  io.github.abarhub.vfs.core.plugin.audit.VFS4JAuditPlugins - createFile for file rep01:fichier01.txt
```

### Plugins Unclosed

Il existe un plugin pour détecter si un fichier ouvert n'a pas été fermé. Il détecte si un fichier n'est pas fermé 
alors qu'il n'est plus accéssible.

Voici un exemple de paramétrage :
```properties
vfs.plugins.plugins1.class=io.github.abarhub.vfs.core.plugin.audit.VFS4JUnclosedPluginsFactory
vfs.plugins.plugins1.operations=NEW_INPUT_STREAM,NEW_OUTPUT_STREAM
vfs.plugins.plugins1.loglevel=WARN
vfs.plugins.plugins1.filterPath=*.txt
vfs.plugins.plugins1.logopen=true
vfs.plugins.plugins1.logclose=true
vfs.plugins.plugins1.exceptionlogopen=true
vfs.plugins.plugins1.exceptionlogclose=true
vfs.plugins.plugins1.logIfNotClosedAfterMs=2500
```

La liste des paramètres est :

| Nom du Parametre      | Valeurs possibles          | Description                                                                                                                                     |
|-----------------------|----------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| operations            | COMMAND, ...               | La liste des opérations ou des groupes d'opérations. Le séparateur est la virgule. Si la valeur est vide, toutes les opérations sontr traitées. |
| filterPath            | \*.txt, /etc/\*\*/\*.ini   | Un glob. Le séparateur est la virgule. Par défaut, tout est logué                                                                               |
| loglevel              | INFO, ERROR, DEBUG, etc... | Le niveau de log. Par défaut, le niveau de log est info                                                                                         |
| logopen               | true/false                 | Si à true, les ouvertures sont loguées                                                                                                          |
| logclose              | true/false                 | Si à true, les fermetures sont loguées                                                                                                          |
| exceptionlogopen      | true/false                 | Si à true, les ouvertures sont loguées avec une exception ce qui permet de savoir quel code a fait l'ouverture                                  |
| exceptionlogclose     | true/false                 | Si à true, les fermetures sont loguées avec une exception ce qui permet de savoir quel code a fait l'ouverture                                  |
| logIfNotClosedAfterMs | true/false                 | Si à true, les ouvertures qui ne sont pas fermé au bout de ce temps sont loguées                                                                |

##  Les opérations


La liste des opérations :
* Les opération d'ouverture ou vermeture de flux (groupe d'opération COMMAND) : 
  * NEW_INPUT_STREAM, 
  * NEW_OUTPUT_STREAM, 
  * NEW_READER, 
  * NEW_WRITER, 
  * NEW_BYTE_CHANNEL, 
  * NEW_DIRECTORY_STREAM,
* Les opération de modification de fichiers (groupe d'opération ATTRIBUTE) :
  * CREATE_FILE, 
  * CREATE_DIRECTORY, 
  * CREATE_DIRECTORIES, 
  * DELETE, 
  * DELETE_IF_EXISTS, 
  * CREATE_LINK, 
  * CREATE_SYMBOLIC_LINK, 
  * COPY,   
  * MOVE, 
  * WRITE,
* Les opération sur les attributs de fichiers (groupe d'opération OPEN) :
  * SET_ATTRIBUTE, 
  * SET_LAST_MODIFIED_TIME, 
  * SET_OWNER, 
  * SET_POSIX_FILE_PERMISSIONS, 
  * GET_ATTRIBUTE, 
  * GET_FILE_ATTRIBUTE_VIEW,
  * GET_LAST_MODIFIED_TIME, 
  * GET_OWNER,
  * GET_POSIX_FILE_PERMISSIONS,
  * IS_EXECUTABLE,
  * IS_READABLE,
  * IS_HIDDEN,
  * IS_WRITABLE,
  * READ_ATTRIBUTES,
* Les opération de parcourt de fichier (groupe d'opération SEARCH) :
  * LIST, 
  * WALK,
  * FIND,
* Les opérations de lecture (groupe d'opération QUERY) :
  * EXISTS,
  * IS_DIRECTORY,
  * IS_REGULAR_FILE,
  * IS_SAME_FILE,
  * IS_SYMBOLIC_LINK,
  * LINES,
  * NOT_EXISTS,
  * READ_ALL_BYTES,
  * READ_ALL_LINES,
  * SIZE



