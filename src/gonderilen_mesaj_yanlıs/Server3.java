package gonderilen_mesaj_yanlıs;
import java.io.*;
import java.net.*;
import java.util.ArrayList;




public class Server3 {
    private static final int PORT = 5003;
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_ID = 3;

    private static Aboneler aboneler;

    public static void main(String[] args) throws IOException {
        aboneler = new Aboneler(100);
        aboneler.setAboneler(new ArrayList<>()); // Initialize aboneler list
        aboneler.getAboneler().add(false); // Örnek olarak bir abone ekledik, gerektiğiniz sayıda ekleyebilirsiniz

        ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(SERVER_HOST));
        System.out.println("Server2 is running on port " + PORT);

        new PingThread(SERVER_ID, "localhost", 5001).start(); // Ping Server2
        new PingThread(SERVER_ID, "localhost", 5002).start(); // Ping Server3

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket, SERVER_ID).start();
            }
        } finally {
            serverSocket.close();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private int serverId;

        public ClientHandler(Socket socket, int serverId) {
            this.clientSocket = socket;
            this.serverId = serverId;
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
                String action = parts[0];

                switch (action) {
                    case "ABONOL":
                        int aboneNum = Integer.parseInt(parts[1]);
                        String abonolResponse = aboneOl(aboneNum);
                        out.println(abonolResponse);
                        break;
                    case "ABONPTAL":
                        int iptalNum = Integer.parseInt(parts[1]);
                        String iptalResponse = aboneIptal(iptalNum);
                        out.println(iptalResponse);
                        break;
                    case "GIRIS":
                        int girisNum = Integer.parseInt(parts[2]);
                        String girisResponse = girisYap(girisNum);
                        out.println(girisResponse);
                        break;
                    case "CIKIS":
                        int cikisNum = Integer.parseInt(parts[2]);
                        String cikisResponse = cikisYap(cikisNum);
                        out.println(cikisResponse);
                        break;
                    case "SERILESTIRILMIS_NESNE":
                        try {
                            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                            Aboneler receivedAboneler = (Aboneler) objectInputStream.readObject();
                            String serilestirilmisResponse = handleSerilestirilmisNesne(receivedAboneler);
                            out.println(serilestirilmisResponse);
                        } catch (ClassNotFoundException e) {
                            out.println("99 HATA");
                        }
                        break;
                    default:
                        out.println("99 HATA");
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

            System.out.println("Received message on Server" + serverId + "(" + currentThread().getId()
                    + ") from client: " + message);
        }

        private String aboneOl(int aboneNum) {
            if (aboneler.getAboneler().get(aboneNum - 1)) {
                return "50 HATA"; // Zaten abone durumu
            }
            aboneler.updateAboneDurumu(aboneNum, true);
            notifyAbonelerGuncellemesi();
            return "55 TAMM";
        }

        private String aboneIptal(int iptalNum) {
            if (!aboneler.getAboneler().get(iptalNum - 1)) {
                return "50 HATA"; // Zaten abone değil durumu
            }
            aboneler.updateAboneDurumu(iptalNum, false);
            notifyAbonelerGuncellemesi();
            return "55 TAMM";
        }

        private String girisYap(int girisNum) {
            if (!aboneler.getAboneler().get(girisNum - 1)) {
                return "50 HATA"; // Abone değil durumu
            }
            aboneler.updateGirisDurumu(girisNum, true);
            notifyAbonelerGuncellemesi();
            return "55 TAMM";
        }

        private String cikisYap(int cikisNum) {
            if (cikisNum <= 0 || cikisNum > aboneler.getGirisYapanlarListesi().size()) {
                return "50 HATA"; // Geçersiz indeks
            }

            if (!aboneler.getGirisYapanlarListesi().get(cikisNum - 1)) {
                return "50 HATA"; // Giriş yapmamış olma durumu
            }

            aboneler.updateGirisDurumu(cikisNum, false);
            notifyAbonelerGuncellemesi();
            return "55 TAMM";
        }


        private String handleSerilestirilmisNesne(Aboneler receivedAboneler) {

            // Yapılacak işlemleri buraya ekleyin
            // receivedAboneler nesnesini kontrol edin ve güncellemeleri yapın
            // Eğer işlem başarılıysa "55 TAMM" döndürün, aksi takdirde uygun bir hata
            // mesajı
            return "55 TAMM";
        }

        private void notifyAbonelerGuncellemesi() {
            // Diğer sunuculara güncellenmiş aboneler listesini bildir
            for (int i = 1; i <= 3; i++) {
                if (i != serverId) {
                    try (Socket notifySocket = new Socket("localhost", 5000 + i)) {
                        ObjectOutputStream outputStream = new ObjectOutputStream(notifySocket.getOutputStream());
                        outputStream.writeObject(aboneler);
                        aboneler.printAbonelerListesi();

                        System.out.println("Sent updated aboneler list to Server" + i);
                    } catch (IOException e) {
                        System.out.println("Failed to send updated aboneler list to Server" + i);
                    }
                }
            }
        }

    }


    private static class PingThread extends Thread {

        private String host;
        private int port;

        public PingThread(int serverId, String host, int port) {

            this.host = host;
            this.port = port;
        }

        public void run() {
            try {
                while (true) {
                    try (Socket socket = new Socket(host, port)) {
                        System.out.println("Pinged " + host + " on port " + port);
                    } catch (IOException e) {
                        System.out.println("Ping to " + host + " on port " + port + " failed, retrying...");
                    }

                    try {
                        Thread.sleep(10000); // Wait for 10 seconds before retrying
                    } catch (InterruptedException ie) {
                        System.out.println("Ping thread interrupted: " + ie.getMessage());
                        break; // Optional: exit the loop if the thread is interrupted
                    }
                }

            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }
}
