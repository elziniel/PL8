## Serveur d'authentification avec un compte Twitch

### Twitch API

[Twitch API](https://github.com/justintv/Twitch-API)

[Twitch Authentification](https://github.com/justintv/Twitch-API/blob/master/authentication.md#authorization-code-flow)

### Docker
Le serveur écoute sur le port 8080 dans le container de Docker.
Mais il doit être accessible depuis l'extérieur via le port 49170.
Il faut donc faire une redirection lors du lancement du container via Docker


```
docker run -d -p 49170:8080 [image]
```

[Docker Cheat Sheet](https://github.com/wsargent/docker-cheat-sheet)
