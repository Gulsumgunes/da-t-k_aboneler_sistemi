import java.io.*;
import java.net.*;

public class Server1_ex {
    private static final int PORT = 5001; // Server1's port
    private static final String SERVER_HOST = "localhost"; // Sunucunun gerçek IP adresi




    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(SERVER_HOST));
        System.out.println("Server1 is running on port " + PORT);

        new PingThread("localhost", 5002).start(); // Ping Server2
        new PingThread("localhost", 5003).start(); // Ping Server3


        // Listen for client connections
        try {
            while (true) {
                // Ping other servers

                new ClientHandler(serverSocket.accept()).start();
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

                // Send a response back to the client
                out.println("55 TAMM");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Received message on Server1("+currentThread().getId() +") from client: " + message);

        }
    }

    // Thread to ping other servers
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
}

