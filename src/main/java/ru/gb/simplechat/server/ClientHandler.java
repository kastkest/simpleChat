package ru.gb.simplechat.server;

import java.net.Socket;

public class ClientHandler {
    private final Socket socket;
    private final ChatServer chatServer;

    public ClientHandler(Socket socket, ChatServer chatServer) {
        this.socket = socket;
        this.chatServer = chatServer;
    }
}
