// import java.io.*;
// import java.net.*;
// import java.util.ArrayList;

// public class Sunucu {
//     private static final int PORT = 5001;
//     private static final String SERVER_HOST = "localhost";

//     private static Abone aboneler;

//     public static void main(String[] args) throws IOException {
//         aboneler = new Abone(100);
//         aboneler.setAboneDurumu(new ArrayList<>()); // Initialize aboneler list
//         aboneler.getAboneDurumu().add(false); // Örnek olarak bir abone ekledik, gerektiğiniz sayıda ekleyebilirsiniz

//         ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(SERVER_HOST));
//         System.out.println("Server1 is running on port " + PORT);

//         // new PingThread(SERVER_ID, "localhost", 5002).start(); // Ping Server2
//         // new PingThread(SERVER_ID, "localhost", 5003).start(); // Ping Server3

//         try {
//             while (true) {
//                 Socket clientSocket = serverSocket.accept();
//                 new ClientHandler(clientSocket).start();
//             }
//         } finally {
//             serverSocket.close();
//         }
//     }

//     private static class ClientHandler extends Thread {
//         private Socket clientSocket;

//         public ClientHandler(Socket socket) {
//             this.clientSocket = socket;

//         }

//         public void run() {
//             BufferedReader in = null;
//             String message = null;
//             PrintWriter out = null;

//             try {
//                 in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                 out = new PrintWriter(clientSocket.getOutputStream(), true);

//                 message = in.readLine();

//                 String[] parts = message.split(" ");
//                 if (parts.length < 3) {
//                     out.println("HATALI İSTEK");
//                     return;
//                 }

//                 String action = parts[0];
//                 int istemciNo = Integer.parseInt(parts[2]);




//                 switch (action) {
//                     case "ABONOL":

//                         String abonolResponse = aboneOl(istemciNo);
//                         out.println(abonolResponse);
//                         break;
//                     case "ABONPTAL":

//                         String iptalResponse = aboneIptal(istemciNo);
//                         out.println(iptalResponse);
//                         break;
//                     case "GIRIS":

//                         String girisResponse = girisYap(istemciNo);
//                         out.println(girisResponse);
//                         break;
//                     case "CIKIS":

//                         String cikisResponse = cikisYap(istemciNo);
//                         out.println(cikisResponse);
//                         break;

//                     default:
//                         out.println("HATALI İSTEK");
//                         break;
//                 }

//             } catch (IOException e) {
//                 throw new RuntimeException(e);
//             } finally {
//                 try {
//                     clientSocket.close();
//                 } catch (IOException e) {
//                     e.printStackTrace();
//                 }
//             }
//             System.out.println("Received message on Server1("+currentThread().getId() +") from client: " + message);


//         }

//         private String aboneOl(int istemciNo) {
//             if (!aboneler.getAboneDurumu().get(istemciNo)) {
//                 aboneler.getAboneDurumu().set(istemciNo, true);
//                 // Güncel aboneler nesnesini diğer sunuculara gönderme işlemi eklenebilir.
//                 return "55 TAMM";
//             } else {
//                 return "50 HATA";
//             }
//         }

//         private String aboneIptal(int istemciNo) {
//             if (aboneler.getAboneDurumu().get(istemciNo)) {
//                 aboneler.getAboneDurumu().set(istemciNo, false);
//                 // Güncel aboneler nesnesini diğer sunuculara gönderme işlemi eklenebilir.
//                 return "55 TAMM";
//             } else {
//                 return "50 HATA";
//             }
//         }

//         private String girisYap(int istemciNo) {
//             if (aboneler.getAboneDurumu().get(istemciNo)) {
//                 aboneler.getGirisYapanlar().set(istemciNo, true);
//                 // Güncel aboneler nesnesini diğer sunuculara gönderme işlemi eklenebilir.
//                 return "55 TAMM";
//             } else {
//                 return "50 HATA";
//             }
//         }

//         private String cikisYap(int istemciNo) {
//             if (aboneler.getGirisYapanlar().get(istemciNo)) {
//                 aboneler.getGirisYapanlar().set(istemciNo, false);
//                 // Güncel aboneler nesnesini diğer sunuculara gönderme işlemi eklenebilir.
//                 return "55 TAMM";
//             } else {
//                 return "50 HATA";
//             }
//         }

//     }

// }
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Sunucu {
    private Abone aboneler;

    public Sunucu(int aboneSayisi) {
        aboneler = new Abone();

        for (int i = 0; i < aboneSayisi; i++) {
            aboneler.getAboneler().add(false);
            aboneler.getGirisYapanlarListesi().add(false);
        }
    }

    public String handleIstek(String istek) {
        String[] istekParcalari = istek.split(" ");
        String cevap;

        if (istekParcalari.length < 3) {
            return "HATALI ISTEK FORMATI";
        }

        String tip = istekParcalari[0];
        int indis = Integer.parseInt(istekParcalari[2]);

        switch (tip) {
            case "ABONOL":
                cevap = aboneOl(indis);
                break;
            case "ABONPTAL":
                cevap = aboneligiIptalEt(indis);
                break;
            case "GIRIS":
                cevap = girisYap(indis);
                break;
            case "CIKIS":
                cevap = cikisYap(indis);
                break;
            default:
                cevap = "HATALI ISTEK TIP";
        }

        return cevap;
    }

    private String aboneOl(int indis) {
        if (aboneler.getAboneler().get(indis)) {
            return "50 HATA: Zaten abone olma durumu";
        } else {
            aboneler.getAboneler().set(indis, true);
            return "55 TAMM\n" + abonelerToString();
        }
    }

    private String aboneligiIptalEt(int indis) {
        if (!aboneler.getAboneler().get(indis)) {
            return "50 HATA: Zaten abone değil durumu";
        } else {
            aboneler.getAboneler().set(indis, false);
            return "55 TAMM\n" + abonelerToString();
        }
    }

    private String girisYap(int indis) {
        if (!aboneler.getAboneler().get(indis)) {
            return "50 HATA: Abone değil durumu";
        } else {
            aboneler.getGirisYapanlarListesi().set(indis, true);
            return "55 TAMM\n" + abonelerToString();
        }
    }

    private String cikisYap(int indis) {
        if (!aboneler.getGirisYapanlarListesi().get(indis)) {
            return "50 HATA: Giriş yapmamış olma durumu";
        } else {
            aboneler.getGirisYapanlarListesi().set(indis, false);
            return "55 TAMM\n" + abonelerToString();
        }
    }

    private String abonelerToString() {
        return "Güncellenen Aboneler Listesi: " + aboneler.getAboneler() +
               "\nGüncellenen Giriş Yapanlar Listesi: " + aboneler.getGirisYapanlarListesi();
    }
}
