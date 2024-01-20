package gonderilen_mesaj_yanlıs;
import java.io.*;

import java.net.Socket;

public class Client {
    private static final String SERVER1_HOST = "localhost";
    private static final int SERVER1_PORT = 5001;
    private static final String SERVER2_HOST = "localhost";
    private static final int SERVER2_PORT = 5002;
    private static final String SERVER3_HOST = "localhost";
    private static final int SERVER3_PORT = 5003;

    public static void main(String[] args) throws IOException {
        Aboneler aboneler = new Aboneler(100);
        aboneler.updateAboneDurumu(1, true);
        aboneler.updateAboneDurumu(2, true);

        // Socket socket = new Socket("localhost", 5001); // Bağlanılacak server portunu değiştirin

        // BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "ABONOL 1", aboneler);
        sendAndReceiveMessage(SERVER2_HOST, SERVER2_PORT, "ABONPTAL 2", aboneler);
        sendAndReceiveMessage(SERVER3_HOST, SERVER3_PORT, "GIRIS ISTMC 33", aboneler);
        sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "CIKIS ISTMC 99", aboneler);
        sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "ABONOL 1", aboneler);
        sendAndReceiveMessage(SERVER2_HOST, SERVER2_PORT, "ABONPTAL 2", aboneler);
        sendAndReceiveMessage(SERVER3_HOST, SERVER3_PORT, "GIRIS ISTMC 33", aboneler);
        sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "CIKIS ISTMC 99", aboneler);
        sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "ABONOL 1", aboneler);
        sendAndReceiveMessage(SERVER2_HOST, SERVER2_PORT, "ABONPTAL 2", aboneler);
        sendAndReceiveMessage(SERVER3_HOST, SERVER3_PORT, "GIRIS ISTMC 33", aboneler);
        sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "CIKIS ISTMC 99", aboneler);
        sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "ABONOL 1", aboneler);
        sendAndReceiveMessage(SERVER2_HOST, SERVER2_PORT, "ABONPTAL 2", aboneler);
        sendAndReceiveMessage(SERVER3_HOST, SERVER3_PORT, "GIRIS ISTMC 33", aboneler);
        sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "CIKIS ISTMC 99", aboneler);
        // Örnek serileştirilmiş nesne gönderimi:
        // sendSerializedObject(socket, aboneler);

        // // // Sunucu yanıtlarını kontrol etme:
        // receiveResponse(in);


    }
    private static void sendRequest(PrintWriter out, String request) {
        out.println(request);
    }

    private static void sendSerializedObject(Socket socket, Aboneler aboneler) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        // Güncellenmiş Aboneler nesnesini gönder
        objectOutputStream.writeObject(aboneler);
    }

    private static void receiveResponse(BufferedReader in) throws IOException {
        // Sunucudan cevabı oku ve uygun işlemleri yap
        String response = in.readLine();
        System.out.println("Response from server: " + response);

        // Diğer işlemleri gerçekleştirin...
    }

    private static void sendAndReceiveMessage(String host, int port, String message, Aboneler aboneler) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Sunucuya mesajı gönder
            out.println(message);

            // Aboneler nesnesini gönder
            sendSerializedObject(socket, aboneler);

            // Sunucudan cevabı al
            receiveResponse(in);

        } catch (IOException e) {
            System.out.println("Error connecting to server on port " + port + ": " + e.getMessage());
        }
    }




}

