package imail_hocanin_kodlari;
// import java.io.*;

// import java.net.*;

// public class Server2_ex {
//     private static final int PORT = 5002; // Server2's port
//     private static final String SERVER_HOST = "localhost"; // Sunucunun gerçek IP adresi

//     public static void main(String[] args) throws IOException {
//         ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(SERVER_HOST));
//         System.out.println("Server2 is running on port " + PORT);

//         // Ping other servers
//         new PingThread("localhost", 5001).start(); // Ping Server1
//         new PingThread("localhost", 5003).start(); // Ping Server3

//         // Listen for client connections
//         try {
//             while (true) {
//                 new ClientHandler(serverSocket.accept()).start();
//             }
//         } finally {
//             serverSocket.close();
//         }
//     }

//     // Thread to handle client requests
//     private static class ClientHandler extends Thread {
//         private Socket clientSocket;

//         public ClientHandler(Socket socket) {
//             this.clientSocket = socket;
//         }

//         public void run() {
//             // Implement client handling logic here
//             BufferedReader in = null;
//             String message = null;
//             PrintWriter out = null;
//             try {
//                 in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                 out = new PrintWriter(clientSocket.getOutputStream(), true);

//                 message = in.readLine();

//                 // Send a response back to the client
//                 out.println("55 TAMM");
//             } catch (IOException e) {
//                 throw new RuntimeException(e);
//             }

//             System.out.println("Received message on Server2("+currentThread().getId() +") from client: " + message);

//         }
//     }

//     // Thread to ping other servers
//     private static class PingThread extends Thread {
//         private String host;
//         private int port;

//         public PingThread(String host, int port) {
//             this.host = host;
//             this.port = port;
//         }

//         public void run() {

//             try {
//                 while (true) {
//                     try (Socket socket = new Socket(host, port)) {
//                         System.out.println("Pinged " + host + " on port " + port);
//                     } catch (IOException e) {
//                         System.out.println("Ping to " + host + " on port " + port + " failed, retrying...");
//                     }

//                     try {
//                         Thread.sleep(10000); // Wait for 10 seconds before retrying
//                     } catch (InterruptedException ie) {
//                         System.out.println("Ping thread interrupted: " + ie.getMessage());
//                         break; // Optional: exit the loop if the thread is interrupted
//                     }
//                 }
//             } catch (Exception e) {
//                 System.out.println("Unexpected error: " + e.getMessage());
//             }

//         }
//     }
// }
import java.io.*;
import java.net.*;
import java.util.*;

public class Server2_ex {
    private static final int SERVER2_PORT = 5002; // Server1's port
    private static final String SERVER2_HOST = "localhost"; // Sunucunun gerçek IP adresi
    private static Aboneler_ex aboneler;

    public static void main(String[] args) throws IOException {
        aboneler = new Aboneler_ex(100);
        aboneler.setAboneDurumu(new ArrayList<>(100)); // Initialize aboneler list
        aboneler.getAboneDurumu().add(false); // Örnek olarak bir abone ekledik, gerektiğiniz sayıda ekleyebilirsiniz

        ServerSocket serverSocket = new ServerSocket(SERVER2_PORT, 50, InetAddress.getByName(SERVER2_HOST));
        System.out.println("Server1 is running on port " + SERVER2_PORT);

        new PingThread("localhost", 5001).start(); // Ping Server2
        new PingThread("localhost", 5003).start(); // Ping Server3

        // Listen for client connections
        try {
            while (true) {
                // Ping other servers
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();

            }
        } finally {
            serverSocket.close();
        }
    }

    // Thread to handle client requests
    private static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            // Implement client handling logic here
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
                    case "UPDATE_ABONELER":
                        sendAbonelerToOtherServers(aboneler.getAboneDurumu());
                        out.println("Aboneler listesi güncellendi");
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
            System.out.println("Received message on Server1(" + currentThread().getId() + ") from client: " + message);

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

        private static void sendAbonelerToOtherServers(List<Boolean> aboneDurumu) {
            // Diğer sunuculara aboneler listesini göndermek için kullanılacak kod
            // Bu kısım size ait sistem gereksinimlerine ve iletişim protokolüne göre
            // özelleştirilmelidir

            // Örnek olarak:
            new ClientToServerSender("localhost", 5002, "UPDATE_ABONELER", aboneDurumu).start();
            new ClientToServerSender("localhost", 5003, "UPDATE_ABONELER", aboneDurumu).start();
        }

        // private void notifyAbonelerGuncellemesi() {
        // // Diğer sunuculara güncellenmiş aboneler listesini bildir
        // for (int i = 1; i <= 3; i++) {
        // if (i != serverId) {
        // try (Socket notifySocket = new Socket("localhost", 5000 + i)) {
        // ObjectOutputStream outputStream = new
        // ObjectOutputStream(notifySocket.getOutputStream());
        // outputStream.writeObject(aboneler);
        // aboneler.printAbonelerListesi();
        // System.out.println();
        // System.out.println("Sent updated aboneler list to Server" + i);
        // } catch (IOException e) {
        // System.out.println("Failed to send updated aboneler list to Server" + i);
        // }
        // }
        // }
        // }

        // Thread to ping other servers
        // private static class PingThread extends Thread {
        // private String host;
        // private int port;

        // public PingThread(String host, int port) {
        // this.host = host;
        // this.port = port;
        // }

        // public void run() {

        // try {
        // while (true) {
        // try (Socket socket = new Socket(host, port)) {
        // System.out.println("Pinged " + host + " on port " + port);
        // } catch (IOException e) {
        // System.out.println("Ping to " + host + " on port " + port + " failed,
        // retrying...");
        // }

        // try {
        // Thread.sleep(10000); // Wait for 10 seconds before retrying
        // } catch (InterruptedException ie) {
        // System.out.println("Ping thread interrupted: " + ie.getMessage());
        // break; // Optional: exit the loop if the thread is interrupted
        // }
        // }
        // } catch (Exception e) {
        // System.out.println("Unexpected error: " + e.getMessage());
        // }

        // }
        // }
    }
}
