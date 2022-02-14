package ru.gb.simplechat.client;

import ru.gb.simplechat.ClientСontroller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class ChatClient {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ClientСontroller controller;

    public ChatClient(ClientСontroller controller) {
        this.controller = controller;
        openConnection();

    }

    private void openConnection() {
        try {
            InetAddress dstAddress;
            socket = new Socket("localhost", 8190);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    while (true) {
                        String authMessage = in.readUTF();
                        if (authMessage.startsWith("/authok")) {
                            String nick = authMessage.split(" ")[1];
                            controller.addMessage("Успешная авторизация под ником " + nick);
                            controller.setAuth(true);
                            break;
                        }
                    }
                    while (true) {
                        String message = in.readUTF();
                        if ("/end".equals(message)) {
                            controller.setAuth(false);
                            break;
                        }
                        if (message.startsWith("/clients")){
                            String[] clients = message.replace("/clients ", "").split(" ");
                            controller.updateClientsList(clients);
                        }
                        controller.addMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String text) {
        try {
            out.writeUTF(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
