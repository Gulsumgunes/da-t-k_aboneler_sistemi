// package gonderilen_mesaj_yanlıs;

package gonderilen_mesaj_yanlıs;
import java.io.*;
import java.net.*;
import java.util.ArrayList;




public class Server1 {
    private static final int PORT = 5001;
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_ID = 1;

    private static Aboneler aboneler;

    public static void main(String[] args) throws IOException {
        aboneler = new Aboneler(100);
        aboneler.setAboneler(new ArrayList<>(100)); // Initialize aboneler list
        aboneler.getAboneler().add(false); // Örnek olarak bir abone ekledik, gerektiğiniz sayıda ekleyebilirsiniz

        ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(SERVER_HOST));
        System.out.println("Server1 is running on port " + PORT);

        new PingThread(SERVER_ID, "localhost", 5002).start(); // Ping Server2
        new PingThread(SERVER_ID, "localhost", 5003).start(); // Ping Server3

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
            try {
                // İstemciden alınan serileştirilmiş nesneyi işle
                // Eğer işlem başarılıysa "55 TAMM" döndür, aksi takdirde uygun bir hata mesajı

                // Örnek olarak sadece receivedAboneler'ı print ediyoruz, gerçek işlemleri buraya ekleyin
                System.out.println("Received serialized object from client: " + receivedAboneler);

                // Gerçek işlemleri buraya ekleyin, örneğin:
                // aboneler.handleReceivedAboneler(receivedAboneler);

                return "55 TAMM";
            } catch (Exception e) {
                e.printStackTrace();
                return "99 HATA"; // Hata durumunda uygun bir hata mesajı döndür
            }
        }

        private void notifyAbonelerGuncellemesi() {
        // Diğer sunuculara güncellenmiş aboneler listesini bildir
        for (int i = 1; i <=3; i++) {
            if (i != serverId) {
                try (Socket notifySocket = new Socket("localhost", 5000 + i);
                     ObjectOutputStream outputStream = new ObjectOutputStream(notifySocket.getOutputStream())) {

                    outputStream.writeObject(aboneler);
                    aboneler.printAbonelerListesi();
                    System.out.println();
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
                        Thread.sleep(10000); // 10 saniye bekleyin, tekrar denemeden önce
                    } catch (InterruptedException ie) {
                        System.out.println("Ping thread interrupted: " + ie.getMessage());
                        break; // İsteğe bağlı: eğer thread kesilirse döngüden çıkın
                    }
                }

            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }
}

    // private static class PingThread extends Thread {
    //     private int serverId;
    //     private String host;
    //     private int port;

    //     public PingThread(int serverId, String host, int port) {
    //         this.serverId = serverId;
    //         this.host = host;
    //         this.port = port;
    //     }

    //     public void run() {
    //         try {
    //             while (true) {
    //                 // Ping diğer sunucuya
    //                 try (Socket socket = new Socket(host, port)) {
    //                     // Pong mesajını bekler
    //                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    //                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

    //                     out.println("PING");
    //                     String pongResponse = in.readLine();

    //                     if ("PONG".equals(pongResponse)) {
    //                         System.out.println("Received PONG from Server" + (serverId % 3 + 1));
    //                     }

    //                     // Güncellenmiş aboneler listesini diğer sunuculara bildir
    //                     for (int i = 1; i <= 3; i++) {
    //                         if (i != serverId) {
    //                             try (Socket notifySocket = new Socket("localhost", 5000 + i)) {
    //                                 ObjectOutputStream outputStream = new ObjectOutputStream(
    //                                         notifySocket.getOutputStream());
    //                                 outputStream.writeObject(aboneler);
    //                                 System.out.println("Sent updated aboneler list to Server" + i);
    //                             } catch (IOException e) {
    //                                 System.out.println("Failed to send updated aboneler list to Server" + i);
    //                             }
    //                         }
    //                     }

    //                 } catch (IOException e) {
    //                     System.out.println("Ping to " + host + " on port " + port + " failed, retrying...");
    //                 }

    //                 try {
    //                     Thread.sleep(10000);
    //                 } catch (InterruptedException ie) {
    //                     System.out.println("Ping thread interrupted: " + ie.getMessage());
    //                     break;
    //                 }
    //             }
    //         } catch (Exception e) {
    //             System.out.println("Unexpected error: " + e.getMessage());
    //         }
    //     }
    // }









// import java.io.*;
// import java.net.ServerSocket;
// import java.net.Socket;
// import java.util.ArrayList;
// import java.util.List;

// public class Server1 {
//     private List<Boolean> aboneler;
//     private List<Boolean> girisYapanlarListesi;

//     public Server1(int aboneSayisi) {
//         aboneler = new ArrayList<>(aboneSayisi);
//         girisYapanlarListesi = new ArrayList<>(aboneSayisi);

//         for (int i = 0; i < aboneSayisi; i++) {
//             aboneler.add(false);
//             girisYapanlarListesi.add(false);
//         }
//     }

//     public String handleIstek(String istek) {
//         String[] istekParcalari = istek.split(" ");
//         String cevap;

//         if (istekParcalari.length < 3) {
//             return "HATALI ISTEK FORMATI";
//         }

//         String tip = istekParcalari[0];
//         int indis = Integer.parseInt(istekParcalari[2]);

//         if (indis < 0 || indis >= aboneler.size()) {
//             return "HATALI INDIS";
//         }

//         switch (tip) {
//             case "ABONOL":
//                 cevap = aboneOl(indis);
//                 break;
//             case "ABONPTAL":
//                 cevap = aboneligiIptalEt(indis);
//                 break;
//             case "GIRIS":
//                 cevap = girisYap(indis);
//                 break;
//             case "CIKIS":
//                 cevap = cikisYap(indis);
//                 break;
//             default:
//                 cevap = "HATALI ISTEK TIP";
//         }

//         return cevap;
//     }

//     private String aboneOl(int indis) {
//         if (aboneler.get(indis)) {
//             return "50 HATA: Zaten abone olma durumu";
//         } else {
//             aboneler.set(indis, true);
//             return "55 TAMM\n" + abonelerToString();
//         }
//     }

//     private String aboneligiIptalEt(int indis) {
//         if (!aboneler.get(indis)) {
//             return "50 HATA: Zaten abone değil durumu";
//         } else {
//             aboneler.set(indis, false);
//             return "55 TAMM\n" + abonelerToString();
//         }
//     }

//     private String girisYap(int indis) {
//         if (!aboneler.get(indis)) {
//             return "50 HATA: Abone değil durumu";
//         } else {
//             girisYapanlarListesi.set(indis, true);
//             return "55 TAMM\n" + abonelerToString();
//         }
//     }

//     private String cikisYap(int indis) {
//         if (!girisYapanlarListesi.get(indis)) {
//             return "50 HATA: Giriş yapmamış olma durumu";
//         } else {
//             girisYapanlarListesi.set(indis, false);
//             return "55 TAMM\n" + abonelerToString();
//         }
//     }

//     private String abonelerToString() {
//         return "Güncellenen Aboneler Listesi: " + aboneler +
//                 "\nGüncellenen Giriş Yapanlar Listesi: " + girisYapanlarListesi;
//     }

//     public static void main(String[] args) {
//         try {
//             ServerSocket serverSocket = new ServerSocket(5001);
//             System.out.println("Sunucu başlatıldı. Port: 5001");

//             Socket clientSocket = serverSocket.accept();
//             System.out.println("İstemci bağlandı. IP: " + clientSocket.getInetAddress());

//             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

//             Server1 sunucu = new Server1(100);

//             while (true) {
//                 String istek = in.readLine();
//                 if (istek.equals("EXIT")) {
//                     break;
//                 }

//                 String cevap = sunucu.handleIstek(istek);
//                 out.println(cevap);
//             }

//             in.close();
//             out.close();
//             clientSocket.close();
//             serverSocket.close();

//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
// }
