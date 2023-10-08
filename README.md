# AndroidWebSockets

Exemple de connexió amb WebSockets a través de android.

### Compilació i funcionament ###

Cal el 'Maven' per compilar el projecte
```bash
cd JavaServer
mvn clean
mvn compile
```

Per executar el projecte a Windows cal
```bash
cd JavaServer
.\run.ps1 com.project.Main
```

Per executar el projecte a Linux/macOS cal
```bash
cd JavaServer
./run.sh com.project.Main
```

- Fer anar el servidor de la carpeta 'Server' amb:
```
cd JavaServer
./run.sh com.project.WsServidor
```

- Fer anar el client Java
```
cd JavaServer
./run.sh com.project.WsClient
```

- Fer anar el client Java amb GUI
```
cd JavaServer
./run.sh com.project.WsGuiClient
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