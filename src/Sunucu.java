import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Sunucu {
    private static final int PORT = 5001;
    private static final String SERVER_HOST = "localhost";

    private static Abone aboneler;

    public static void main(String[] args) throws IOException {
        aboneler = new Abone(100);
        aboneler.setAboneDurumu(new ArrayList<>()); // Initialize aboneler list
        aboneler.getAboneDurumu().add(false); // Örnek olarak bir abone ekledik, gerektiğiniz sayıda ekleyebilirsiniz

        ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(SERVER_HOST));
        System.out.println("Server1 is running on port " + PORT);

        // new PingThread(SERVER_ID, "localhost", 5002).start(); // Ping Server2
        // new PingThread(SERVER_ID, "localhost", 5003).start(); // Ping Server3

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } finally {
            serverSocket.close();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;

        }

        public void run() {
            BufferedReader in = null;
            String message = null;
            PrintWriter out = null;

            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                message = in.readLine();

                String[] parts = message.split(" ");
                if (parts.length < 3) {
                    out.println("HATALI İSTEK");
                    return;
                }

                String action = parts[0];
                int istemciNo = Integer.parseInt(parts[2]);

                switch (action) {
                    case "ABONOL":

                        String abonolResponse = aboneOl(istemciNo);
                        out.println(abonolResponse);
                        break;
                    case "ABONPTAL":

                        String iptalResponse = aboneIptal(istemciNo);
                        out.println(iptalResponse);
                        break;
                    case "GIRIS":

                        String girisResponse = girisYap(istemciNo);
                        out.println(girisResponse);
                        break;
                    case "CIKIS":

                        String cikisResponse = cikisYap(istemciNo);
                        out.println(cikisResponse);
                        break;

                    default:
                        out.println("HATALI İSTEK");
                        break;
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Received message on Server1("+currentThread().getId() +") from client: " + message);

        }

        private String aboneOl(int istemciNo) {
            if (!aboneler.getAboneDurumu().get(istemciNo)) {
                aboneler.getAboneDurumu().set(istemciNo, true);
                // Güncel aboneler nesnesini diğer sunuculara gönderme işlemi eklenebilir.
                return "55 TAMM";
            } else {
                return "50 HATA";
            }
        }

        private String aboneIptal(int istemciNo) {
            if (aboneler.getAboneDurumu().get(istemciNo)) {
                aboneler.getAboneDurumu().set(istemciNo, false);
                // Güncel aboneler nesnesini diğer sunuculara gönderme işlemi eklenebilir.
                return "55 TAMM";
            } else {
                return "50 HATA";
            }
        }

        private String girisYap(int istemciNo) {
            if (aboneler.getAboneDurumu().get(istemciNo)) {
                aboneler.getGirisYapanlar().set(istemciNo, true);
                // Güncel aboneler nesnesini diğer sunuculara gönderme işlemi eklenebilir.
                return "55 TAMM";
            } else {
                return "50 HATA";
            }
        }

        private String cikisYap(int istemciNo) {
            if (aboneler.getGirisYapanlar().get(istemciNo)) {
                aboneler.getGirisYapanlar().set(istemciNo, false);
                // Güncel aboneler nesnesini diğer sunuculara gönderme işlemi eklenebilir.
                return "55 TAMM";
            } else {
                return "50 HATA";
            }
        }

    }

}
