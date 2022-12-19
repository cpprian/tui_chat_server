package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientImpl implements Client {
    private static final int PORT = 8080;
    private static final String HOST = "localhost";
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(ip + ":" + port + " is not available");
            System.exit(1);
        }
    }

    @Override
    public void sendMessage() {
        try {
            BufferedReader inServer = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String message = inServer.readLine();
                if (message == null) {
                    System.out.println("sendMessage: message is null");
                    break;
                }
                if (message.equals("bye")) {
                    break;
                }
                out.println(message);
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveMessage() {
        try {
            while (true) {
                String message = in.readLine();
                if (message == null) {
                    break;
                }
                System.out.println(message);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClientImpl client = new ClientImpl();
        client.startConnection(HOST, PORT);

        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                client.sendMessage();
            }
        });
        sender.start();

        Thread receiver = new Thread(new Runnable() {
            @Override
            public void run() {
                client.receiveMessage();
            }
        });
        receiver.start();

        try {
            sender.join();
            receiver.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        client.stopConnection();
        System.out.println("Client stopped");
    }
}
