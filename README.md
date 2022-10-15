# AndroidWebSockets

Exemple de connexió amb WebSockets a través de android.

- Fer anar el servidor de la carpeta 'Server' amb:
```
javac -cp "lib/*:." WsServidor.java
java -cp "lib/*:." WsServidor
```

- Fer anar el client Java
```
javac -cp "lib/*:." WsClient.java
java -cp "lib/*:." WsClient
```

- Fer anar el client Java amb GUI
```
javac -cp "lib/*:." WsGuiClient.java
java -cp "lib/*:." WsGuiClient
```

- Amb android studio obrir el projecte i arrencar l'emulador.

# Opcions

Mostrar els clients connectats
```
list
```

Enviar missatge privat
```
to(client)Missatge
```