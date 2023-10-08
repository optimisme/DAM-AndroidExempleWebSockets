package com.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

// Compilar amb:
// javac -cp "lib/*:." WsServidor.java
// java -cp "lib/*:." WsServidor

// Tutorials: http://tootallnate.github.io/Java-WebSocket/

public class WsServidor extends WebSocketServer {

    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws InterruptedException, IOException {
        int port = 8888; 
        boolean running = true;

        // Deshabilitar SSLv3 per clients Android
        java.lang.System.setProperty("jdk.tls.client.protocols", "TLSv1,TLSv1.1,TLSv1.2");

        WsServidor socket = new WsServidor(port);
        socket.start();
        System.out.println("WsServidor funciona al port: " + socket.getPort());

        while (running) {
            String line = in.readLine();
            socket.broadcast(line);
            if (line.equals("exit")) {
                running = false;
            }
        }    

        System.out.println("Aturant WsServidor");
        socket.stop(1000);
    }

    public WsServidor(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public WsServidor(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

        // Saludem personalment al nou client
        conn.send("Benvingut a WsServer"); 

        // Enviem la direcció URI del nou client a tothom 
        broadcast("Nova connexió: " + handshake.getResourceDescriptor());

        // Mostrem per pantalla (servidor) la nova connexió
        String host = conn.getRemoteSocketAddress().getAddress().getHostAddress();
        System.out.println(host + " s'ha connectat");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

        // Informem a tothom que el client s'ha desconnectat
        broadcast(conn + " s'ha desconnectat");

        // Mostrem per pantalla (servidor) la desconnexió
        System.out.println(conn + " s'ha desconnectat");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {

        if (message.equalsIgnoreCase("list")) {
            // Enviar la llsita de connexions al client
            System.out.println("Llista de connexions:");
            String strList = "";
            for (WebSocket ws : this.getConnections()) {
                strList += " " + getConnectionId(ws);
                
            }
            conn.send(strList);

        } else if (message.contains("to(")) {
            // Missatge privat
        
            // Trobar el client amb aquest identificador
            String strDesti = message.substring(message.indexOf("(") + 1, message.indexOf(")"));
            WebSocket desti = null;
            for (WebSocket ws : this.getConnections()) {
                if (strDesti.compareTo(getConnectionId(ws)) == 0) {
                    desti = ws;
                    break;
                }                
            }

            // Enviar el missatge si s'ha trobat el client o retornar un error en cas contrari
            if (desti != null) {
                String idOrigen = getConnectionId(conn);
                String idDesti = getConnectionId(desti);
                desti.send("Missatge privat de " + idOrigen + ": " + message);
                conn.send("S'ha entregat el missatge privat a: " + idDesti);
                System.out.println("Missatge privat entre " + idOrigen + " i " + idDesti + ": " + message);
            } else {
                conn.send("No s'ha trobat el destí " + strDesti);
            }

        } else {
            // Enviem el missatge del client a tothom
            broadcast(getConnectionId(conn) + " ha dit: " + message);

            // Mostrem per pantalla (servidor) el missatge
            System.out.println("Broadcast de " + getConnectionId(conn) + ": " + message);
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {

        // Enviem el missatge del client a tothom
        broadcast(message.array());

        // Mostrem per pantalla (servidor) el missatge
        System.out.println(conn + ": " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        // S'inicia el servidor
        System.out.println("Escriu 'exit' per aturar el servidor");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

    public String getConnectionId (WebSocket connection) {
        String name = connection.toString();
        return name.replaceAll("org.java_websocket.WebSocketImpl@", "").substring(0, 3);
    }
}