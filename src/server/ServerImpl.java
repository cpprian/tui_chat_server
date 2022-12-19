package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerImpl implements Server {
    private static final int PORT = 8080;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private boolean isRunning = true;
    private ArrayList<ClientInfo> clients = new ArrayList<ClientInfo>();

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started");

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    isRunning = false;
                    stop();
                }
            }));

            Thread listener = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if (!isRunning) {
                            return;
                        }
                        listener();
                    }
                }
            });
            listener.start();
            listener.join();
        } catch (IOException e) {
            System.out.println("start: Server failed");
        } catch (InterruptedException e) {
            System.out.println("start: Interrupted");
        }
    }

    public void stop() {
        try {
            for (ClientInfo client : clients) {
                client.getIn().close();
                client.getOut().close();
                client.getSocket().close();
            }

            serverSocket.close();
        } catch (IOException e) {
            System.out.println("stop: Close failed");
        }
    }

    public static void main(String[] args) {
        ServerImpl server = new ServerImpl();
        server.start(PORT);
        System.out.println("Server is closed");
    }

    @Override
    public void broadcast(String message, String messageType, ClientInfo clientInfo) {
        for (ClientInfo client : clients) {
            if (messageType.equals("ALL")) {
                if (!client.equals(clientInfo)) {
                    send(message, clientInfo.getName(), client);
                }
            } else {
                if (client.getName().equals(messageType)) {
                    send(message, clientInfo.getName(), client);
                    return;
                }
            }
        }
    }

    @Override
    public void listener() {
        try {
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String name = in.readLine();
            if (name == null) {
                System.out.println("listener: name is null");
                return;
            }

            final ClientInfo clientInfo = new ClientInfo(name, clientSocket, out, in);
            clients.add(clientInfo);
            
            Thread receiver = new Thread(new Runnable() {
                @Override
                public void run() {
                    ClientInfo client = clientInfo;
                    while (true) {
                        if (!isRunning) {
                            return;
                        }

                        try {
                            String message = client.getIn().readLine();
                            if (message == null) {
                                System.out.println("receiver: " + client.getName() + " disconnected");
                                return;
                            }
                            String[] messageParts = message.split(" ");
                            // join rest of message
                            for (int i = 2; i < messageParts.length; i++) {
                                messageParts[1] += " " + messageParts[i];
                            }
                            if (messageParts[0].equals("ALL")) {
                                broadcast(messageParts[1], "ALL", client);
                            } else {
                                broadcast(messageParts[1], messageParts[0], client);
                            }
                        } catch (IOException e) {
                            System.out.println("receiver: Read failed");
                        }
                    }
                }
            });
            receiver.start();

            System.out.println("listener: " + name + " connected");
        } catch (IOException e) {
            System.out.println("listener: Accept failed 8080");
        }
    }

    @Override
    public void send(String message, String name, ClientInfo clientInfo) {
        clientInfo.getOut().println("[" + name + "]: " + message);
    }
}
