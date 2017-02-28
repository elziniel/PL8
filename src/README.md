

## libGDX
libGDX est un framework basé sur OpenGL pour le développement de jeu en Java.
[libGDX](https://github.com/libgdx/libgdx)

## Executer un module

Le projet est facilement executable avec Gradle depuis le répertoire `./tower_defense`
Par exemple, la version desktop se lance avec la commande:
```
./gradlew desktop:run
```

Pour un serveur de jeu:
```
./gradlew game_server:run
```
[Gradle on the Commandeline](https://github.com/libgdx/libgdx/wiki/Gradle-on-the-Commandline)


## Outils utilisé

##### libGDX Texture Packer GUI
Permet de rassembler un ensemble d'image dans une seule.
Solution plus performante que d'avoir plusieurs images, car une seule image est
chargée par la machine.

[libGDX Texture Packer GUI](https://code.google.com/p/libgdx-texturepacker-gui/)

##### libGDX Skin Editor
Editeur de skin pour tout ce qui est boutons, label, textArea, etc de Scene2d.ui de libGDX


[libGDX Skin Editor](https://github.com/cobolfoo/gdx-skineditor)

[Scene2d.ui](https://github.com/libgdx/libgdx/wiki/Scene2d.ui)
