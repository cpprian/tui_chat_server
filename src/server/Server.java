package server;

public interface Server {
    void broadcast(String message, String messageType, ClientInfo clientInfo);
    void listener();
    void send(String message, String name, ClientInfo client);
}
