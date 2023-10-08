package com.project;

import java.io.IOException;
import java.util.*;

public class Main {

    static Scanner in = new Scanner(System.in); // System.in és global, Scanner també ho a de ser

    // Main
    public static void main(String[] args) throws InterruptedException, IOException {
        
        boolean running = true;

        while (running) {

            String menu = "Escull una opció:";
            menu = menu + "\n 0) Servidor";
            menu = menu + "\n 1) Client GUI";
            menu = menu + "\n 2) Client Terminal";
            menu = menu + "\n 3) Sortir";
            System.out.println(menu);

            int opcio = Integer.valueOf(llegirLinia("Opció:"));
            
            switch (opcio) {
                case 0: WsServidor.main(args);      break;
                case 1: WsGuiClient.main(args);     break;
                case 2: WsClient.main(args);        break;
                case 3: running = false;            break;
                default: break;
            }
        }

		in.close();
    }

    static public String llegirLinia (String text) {
        System.out.print(text);
        return in.nextLine();
    }
}