package ru.gb.simplechat.server;

import ru.gb.simplechat.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


import static ru.gb.simplechat.Command.*;

public class ClientHandler {

    private Socket socket;
    private ChatServer chatServer;
    private DataInputStream in;
    private DataOutputStream out;


    private String nick;

    public ClientHandler(Socket socket, ChatServer chatServer) {
        try {
            this.nick = "";
            this.socket = socket;
            this.chatServer = chatServer;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    authenticate();
                    readMessage();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
                chatServer.unsubscribe(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readMessage() {
        try {
            while (true) {
                String message = in.readUTF();
                if ((Command.isCommand(message))) {
                    if (getCommandByText(message) == END) {
                        break;
                    }
                    if (getCommandByText(message) == PM) {
                        String[] split = message.split(" ");
                        String nickTo = split[1];
                        String msg = split[2];
                        chatServer.sendPM(this, nickTo, message.substring(PM.getCommand().length() + 2 + nickTo.length()));
                    }
                    continue;
                }
                chatServer.broadcast(nick + ": " + message);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void authenticate() {
        while (true) {
            try {
                String message = in.readUTF(); // /auth login password
                if (getCommandByText(message) == AUTH) {
                    String[] split = message.split(" ");
                    String login = split[1];
                    String password = split[2];
                    String nick = chatServer.getAuthService().getNickByLoginAndPassword(login, password);
                    if (nick != null) {
                        if (chatServer.isNickBusy(nick)) {
                            sendMessage("Пользователь уже авторизован");
                            continue;
                        }
                        sendMessage(Command.AUTHOK, nick);
                        this.nick = nick;
                        chatServer.broadcast("Пользователь " + nick + " зашел в чат");
                        chatServer.subscribe(this);
                        break;
                    } else {
                        sendMessage("Неверные логин и пароль!");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(Command command, String message) {
        try {
            out.writeUTF(command.getCommand() + " " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNick() {
        return nick == null ? "" : nick;
    }
}