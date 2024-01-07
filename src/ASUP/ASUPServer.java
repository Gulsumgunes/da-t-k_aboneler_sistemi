// package ASUP;

// import java.io.*;
// import java.net.ServerSocket;
// import java.net.Socket;

// public class ASUPServer {
//     private static final int PORT = 12344;
//     private static Abone aboneler = new Abone(100); // Örnek olarak 100 abone

//     public static void main(String[] args) {
//         try {
//             try (ServerSocket serverSocket = new ServerSocket(PORT)) {
//                 System.out.println("ASUP Server is running on port " + PORT);

//                 while (true) {
//                     Socket clientSocket = serverSocket.accept();
//                     System.out.println("New client connected: " + clientSocket);

//                     ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
//                     ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

//                     Object request = objectInputStream.readObject();
//                     processRequest(request);

//                     objectOutputStream.writeObject(aboneler);
//                     objectInputStream.close();
//                     objectOutputStream.close();
//                     clientSocket.close();
//                 }
//             }
//         } catch (IOException | ClassNotFoundException e) {
//             e.printStackTrace();
//         }
//     }

//     private static void processRequest(Object request) {
//         if (request instanceof String) {
//             String strRequest = (String) request;
//             String[] parts = strRequest.split(" ");

//             if (parts.length >= 3) {
//                 int clientIndex = Integer.parseInt(parts[2]);

//                 switch (parts[0]) {
//                     case "ABONOL":
//                         aboneler.setAboneDurumu(clientIndex - 1, true);
//                         break;
//                     case "ABONPTAL":
//                         aboneler.setAboneDurumu(clientIndex - 1, false);
//                         break;
//                     case "GIRIS":
//                         aboneler.setGirisDurumu(clientIndex - 1, true);
//                         break;
//                     case "CIKIS":
//                         aboneler.setGirisDurumu(clientIndex - 1, false);
//                         break;
//                     default:
//                         // Hatalı istek
//                         break;
//                 }
//             }
//         }
//     }
// }
package ASUP;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ASUPServer {
    private static final int PORT = 12344;
    private static final String SERVER_HOST = "localhost";;

    public static void main(String[] args)throws IOException {
            ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(SERVER_HOST));
            System.out.println("ASUP Server 1 is running on port " + PORT);

            new PingThread("localhost", 12345).start(); // Ping Server2
            new PingThread("localhost", 12346).start();

        try {
                while (true) {
                    new ClientHandler(serverSocket.accept()).start();


                    // ObjectInputStream objectInputStream = new ObjectInputStream(ClientHandler.getInputStream());

                    // Object receivedObject = objectInputStream.readObject();

                    // // if (receivedObject instanceof Abone) {
                    // //     Abone receivedAboneler = (Abone) receivedObject;
                    // //     // Sunucu 2'nin abone listesini güncelleme
                    // //     aboneler = receivedAboneler;
                    // //     System.out.println("Server 3 updated its aboneler list: " + aboneler.getAbonelerList());
                    // // }
                    // if (receivedObject instanceof String) {
                    //     // Gelen istek bir metin isteği ise
                    //     processTextRequest((String) receivedObject);
                    // } else if (receivedObject instanceof Abone) {
                    //     // Gelen istek bir nesne isteği ise
                    //     processObjectRequest((Abone) receivedObject);
                    // }


                    // objectInputStream.close();
                    // ClientHandler.close();
                }

        } finally{
            serverSocket.close();
        }

    }

    private static class ClientHandler extends Thread{
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            String message = null;
            BufferedReader in = null;
            PrintWriter out = null;
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());


                Object receivedObject = objectInputStream.readObject();
                out.println("55 TAMM");


                // if (receivedObject instanceof Abone) {
                //     Abone receivedAboneler = (Abone) receivedObject;
                //     // Sunucu 2'nin abone listesini güncelleme
                //     aboneler = receivedAboneler;
                //     System.out.println("Server 3 updated its aboneler list: " + aboneler.getAbonelerList());
                // }

                if (receivedObject instanceof String) {
                    // Gelen istek bir metin isteği ise
                    processTextRequest((String) receivedObject);
                    System.out.println("1");
                } else if (receivedObject instanceof Abone) {
                    // Gelen istek bir nesne isteği ise
                    processObjectRequest((Abone) receivedObject);
                    System.out.println("0");
                }
                sendAbonelerToOtherServer();


                    objectOutputStream.writeObject(aboneler);
                    objectInputStream.close();
                    objectOutputStream.close();
                    clientSocket.close();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();

            }


            //Implement client handling logic here



            System.out.println("Received message on Server1("+currentThread().getId() +") from client: " + message);

        }

        private void sendAbonelerToOtherServer() {
        }




    }


    private static void processTextRequest(String textRequest) {
        String[] parts = textRequest.split(" ");

        if (parts.length >= 3) {
            int clientIndex = Integer.parseInt(parts[2]);

            switch (parts[0]) {
                case "ABONOL":
                    abonelerList.setAboneler();
                    break;
                case "ABONPTAL":
                    aboneler.setAboneler(clientIndex - 1, false);
                    break;
                case "GIRIS":
                    aboneler.setGirisYapanlarList(clientIndex - 1, true);
                    break;
                case "CIKIS":
                    aboneler.setGirisYapanlarList(clientIndex - 1, false);
                    break;
                default:
                    // Hatalı istek
                    break;
            }
        }
    }

    private static void processObjectRequest(Abone receivedAboneler) {
        // Nesne isteği ile ilgili işlemler burada yapılabilir
        System.out.println("Server 1 received object request: " + receivedAboneler.getAbonelerList());
    }

    private static class PingThread extends Thread {
        private String host;
        private int port;

        public PingThread(String host, int port) {
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
    private static void sendAbonelerToOtherServer() {
        try {
            Socket socket = new Socket("127.0.0.1", 12346); // Diğer sunucunun adresi ve portu
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(aboneler);
            objectOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


