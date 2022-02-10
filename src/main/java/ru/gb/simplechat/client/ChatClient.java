package ru.gb.simplechat.client;

import ru.gb.simplechat.ClientСontroller;
import ru.gb.simplechat.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import static ru.gb.simplechat.Command.*;

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
            socket = new Socket("localhost", 8189);
            socket.setSoTimeout(10_000);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                while (true) {
                    try {
                        String authMessage = in.readUTF();
                        if (getCommandByText(authMessage) == AUTHOK) {
                            String nick = authMessage.split(" ")[1];
                            controller.addMessage("Успешная авторизация под ником " + nick);
                            controller.setAuth(true);
                            break;
                        }

                        while (true) {
                            String message = in.readUTF();
                            if (Command.isCommand(message)) {
                                Command command = getCommandByText(message);
                                if (command == END) {
                                    controller.setAuth(false);
                                    break;
                                }
                                if (command == CLIENTS) {
                                    String[] clients = message.replace(CLIENTS.getCommand() + " ", "").split(" ");
                                    controller.updateClientsList(clients);
                                }
                            }
                            controller.addMessage(message);
                        }
                    } catch (SocketTimeoutException e) {
                        System.out.println("Время на авторизацию вышло");
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        closeConnection();
                    }
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