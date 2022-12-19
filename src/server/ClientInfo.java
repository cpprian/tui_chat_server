package server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientInfo {
    private String name;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientInfo(String name, Socket socket, PrintWriter out, BufferedReader in) {
        this.name = name;
        this.socket = socket;
        this.out = out;
        this.in = in;
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public String getName() {
        return name;
    }
}
