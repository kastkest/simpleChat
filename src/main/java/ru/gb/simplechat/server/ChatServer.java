package ru.gb.simplechat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

    public ChatServer() {
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true){
                System.out.println("Ожидание подключения клиента...");
                Socket socket = serverSocket.accept();
                System.out.println("Клиент подключился");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
