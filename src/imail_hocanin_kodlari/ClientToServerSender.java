package imail_hocanin_kodlari;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientToServerSender extends Thread {
    private String host;
    private int port;
    private String messageType;
    private List<Boolean> data;

    public ClientToServerSender(String host, int port, String messageType, List<Boolean> data) {
        this.host = host;
        this.port = port;
        this.messageType = messageType;
        this.data = data;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            // Sunucuya mesaj tipini ve veriyi gönder
            objectOutputStream.writeObject(messageType);
            objectOutputStream.writeObject(data);

            System.out.println("Sent update to server on port " + port);
        } catch (IOException e) {
            System.out.println("Error sending update to server on port " + port + ": " + e.getMessage());
        }
    }
}

