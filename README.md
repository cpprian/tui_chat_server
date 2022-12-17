# tui_chat_server

- [tui\_chat\_server](#tui_chat_server)
  - [Description](#description)
## Description

This is a simple chat server written in Java. Architecture is a client-server model with a single server and multiple clients. The server is a TCP server that accepts connections from clients using sockets.

Clients can send messages to the server, which will then broadcast the message to all connected clients. Clients can also send private messages to other clients.

- Write 'ALL' at the beginning of a message to send it to all clients. 
- Write name of the user at the beginning of a message to send it to a specific client. 
- Write 'list' to get a list of all connected clients. 
- Write 'bye' to disconnect from the server.
