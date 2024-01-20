package imail_hocanin_kodlari;

import java.io.*;
import java.net.Socket;

public class Client_ex {
    private static final String SERVER1_HOST = "localhost";
    private static final int SERVER1_PORT = 5001;
    private static final String SERVER2_HOST = "localhost";
    private static final int SERVER2_PORT = 5002;
    private static final String SERVER3_HOST = "localhost";
    private static final int SERVER3_PORT = 5003;

    public static void main(String[] args) {
            sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "ABONOL 1");
            sendAndReceiveMessage(SERVER2_HOST, SERVER2_PORT, "ABONPTAL 2");
            sendAndReceiveMessage(SERVER3_HOST, SERVER3_PORT, "GIRIS ISTMC 33");
            sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "CIKIS ISTMC 99");
            sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "ABONOL 1");
            sendAndReceiveMessage(SERVER2_HOST, SERVER2_PORT, "ABONPTAL 2");
            sendAndReceiveMessage(SERVER3_HOST, SERVER3_PORT, "GIRIS ISTMC 33");
            sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "CIKIS ISTMC 99");
            sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "ABONOL 1");
            sendAndReceiveMessage(SERVER2_HOST, SERVER2_PORT, "ABONPTAL 2");
            sendAndReceiveMessage(SERVER3_HOST, SERVER3_PORT, "GIRIS ISTMC 33");
            sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "CIKIS ISTMC 99");
            sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "ABONOL 1");
            sendAndReceiveMessage(SERVER2_HOST, SERVER2_PORT, "ABONPTAL 2");
            sendAndReceiveMessage(SERVER3_HOST, SERVER3_PORT, "GIRIS ISTMC 33");
            sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "CIKIS ISTMC 99");
    }


    private static void sendAndReceiveMessage(String host, int port, String message) {
        try (
                Socket socket = new Socket(host, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send a message to the server
            out.println(message);

            // Receive the response from the server
            String response = in.readLine();
            System.out.println("Response from server on port " + port + ": " + response);


        } catch (IOException e) {
            System.out.println("Error connecting to server on port " + port + ": " + e.getMessage());
        }
    }
}


