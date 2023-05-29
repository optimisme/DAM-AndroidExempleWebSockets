import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

public class WsGuiClient extends JFrame implements ActionListener {

  private final JTextField uriField;
  private final JButton connect;
  private final JButton close;
  private final JTextArea ta;
  private final JTextField chatField;
  private WebSocketClient cc;

    public static void main(String[] args) {
        int port = 8888;
        String location = "localhost";

        location = "ws://" + location + ":" + port;
        System.out.println("Default server url not specified: defaulting to \'" + location + "\'");

        new WsGuiClient(location);
    }

    public WsGuiClient(String defaultlocation) {

        super("WebSocket Chat Client");
        Container container = getContentPane();
        GridLayout layout = new GridLayout();

        layout.setColumns(1);
        layout.setRows(6);
        container.setLayout(layout);

        uriField = new JTextField();
        uriField.setText(defaultlocation);
        container.add(uriField);

        Container containerButtons = new Container();
        GridLayout layoutButtons = new GridLayout();
        layoutButtons.setColumns(2);
        layoutButtons.setRows(1);
        containerButtons.setLayout(layoutButtons);

        connect = new JButton("Connecta");
        connect.addActionListener(this);
        containerButtons.add(connect);

        close = new JButton("Desconnecta");
        close.addActionListener(this);
        close.setEnabled(false);
        containerButtons.add(close);

        container.add(containerButtons);

        JLabel labelArea = new JLabel("Consola:");
        container.add(labelArea);

        JScrollPane scroll = new JScrollPane();
        ta = new JTextArea();
        scroll.setViewportView(ta);
        container.add(scroll);

        JLabel labelEscriu = new JLabel("Escriu un missatge:");
        container.add(labelEscriu);

        chatField = new JTextField();
        chatField.setText("");
        chatField.addActionListener(this);
        container.add(chatField);

        java.awt.Dimension d = new java.awt.Dimension(300, 600);
        setPreferredSize(d);
        setSize(d);

        addWindowListener(new java.awt.event.WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            if (cc != null) {
              cc.close();
            }
            dispose();
          }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

      if (e.getSource() == chatField) {
          if (cc != null) {
            cc.send(chatField.getText());
            chatField.setText("");
            chatField.requestFocus();
          }
      } else if (e.getSource() == connect) {
          try {
              cc = new WebSocketClient(new URI(uriField.getText()), (Draft) new Draft_6455()) {
                  @Override
                  public void onMessage(String message) {
                      ta.append("got: " + message + "\n");
                      ta.setCaretPosition(ta.getDocument().getLength());
                  }

                  @Override
                  public void onOpen(ServerHandshake handshake) {
                    String text = "T'has connectat a: " + getURI()
                              + "\nlist per veure la llista de ids"
                              + "\nto(id)missatge per enviar missatges privats";

                      ta.append(text);
                      ta.setCaretPosition(ta.getDocument().getLength());
                  }

                  @Override
                  public void onClose(int code, String reason, boolean remote) {
                      ta.append("T'has desconnectat de: " + getURI() + "\n");
                      ta.setCaretPosition(ta.getDocument().getLength());
                      connect.setEnabled(true);
                      uriField.setEditable(true);
                      close.setEnabled(false);
                  }

                  @Override
                  public void onError(Exception ex) {
                      ta.append("Error amb la connexió del socket\n");
                      ta.setCaretPosition(ta.getDocument().getLength());
                      connect.setEnabled(true);
                      uriField.setEditable(true);
                      close.setEnabled(false);
                  }
              };

              close.setEnabled(true);
              connect.setEnabled(false);
              uriField.setEditable(false);
              cc.connect();
            } catch (URISyntaxException ex) {
              ta.append(uriField.getText() + " no és una direcció URI de WebSocket vàlida\n");
            }
        } else if (e.getSource() == close) {
            cc.close();
        }
    }
}