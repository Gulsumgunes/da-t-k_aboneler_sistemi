import java.io.*;
import java.net.Socket;

public class Istemci {
    private static final String SERVER1_HOST = "localhost";
    private static final int SERVER1_PORT = 5001;


    public static void main(String[] args) {
        try{

            Socket socket = new Socket("localhost", 5001); // Bağlanılacak server portunu değiştirin

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "ABONOL");

            sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "GIRIS ISTMC 33");
            sendAndReceiveMessage(SERVER1_HOST, SERVER1_PORT, "CIKIS ISTMC 99");


            out.println("GIRIS ISTMC 33"); // Örnek bir isteği sunucuya gönder

            // Yazma işlemi tamamlandıktan sonra
            // Tamponu temizle, veriyi gönder
            out.close(); // PrintWriter'ı kapat, kaynakları serbest bırak

            // Sunucu yanıtlarını kontrol etme:
            receiveResponse(in);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private static void sendRequest(PrintWriter out, String request) {
        out.println(request);
    }


    private static void receiveResponse(BufferedReader in) throws IOException {



        // Sunucu yanıtını kontrol etme ve uygun aksiyonları almak gerekir
    }

    private static void sendAndReceiveMessage(String host, int port, String message) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader/*Scanner*/ in = new BufferedReader /*Scanner*/ (new InputStreamReader(socket.getInputStream()))) {

            // Send a message to the server
            out.println(message);
            sendRequest(out, message);

            // Receive the response from the server
            String response = in.readLine();
            System.out.println("Response from server on port " + port + ": " + response);
        } catch (IOException e) {
            System.out.println("Error connecting to server on port " + port + ": " + e.getMessage());
        }
    }



}


