import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;

// Compilar amb:
// javac -cp "lib/*:." WsClient.java
// java -cp "lib/*:." WsClient

public class WsClient  extends WebSocketClient {

    private boolean running = true;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int port = 8888;
        String host = "localhost";
        String location = "ws://" + host + ":" + port;
        String text = "";

        WsClient client = connecta(location);
        
        while (client.running) {
            text = sc.nextLine();

            try {
                client.send(text);
            } catch (WebsocketNotConnectedException e) {
                System.out.println("Connexió perduda, reconnectant ...");
                client = connecta(location);
            }

            if (text.compareTo("exit") == 0) {
                client.running = false;
            }
        }

        if (client != null) { client.close(); }
    }

    public WsClient (URI uri, Draft draft) {
        super (uri, draft);
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Has rebut un missatge: " + message);
        if (message.compareTo("exit") == 0) {
            System.out.println("El servidor s'ha aturat");
        }
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("T'has connectat a: " + getURI());
        System.out.println("list per veure la llista de ids");
        System.out.println("to(id)missatge per enviar missatges privats");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("T'has desconnectat de: " + getURI());
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Error amb la connexió del socket");
    }

    static public WsClient connecta (String location) {
        WsClient client = null;

        try {
            client = new WsClient(new URI(location), (Draft) new Draft_6455());
            client.connect();
        } catch (URISyntaxException e) { 
            e.printStackTrace(); 
            System.out.println("Error: " + location + " no és una direcció URI de WebSocket vàlida");
        }

        return client;
    }
}