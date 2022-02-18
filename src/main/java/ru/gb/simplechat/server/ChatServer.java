package ru.gb.simplechat.server;

import ru.gb.simplechat.Command;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ChatServer {
    private final Map<String, ClientHandler> clients;
    private final AuthService authService;

    public ChatServer() {
        authService = new DBAuthService();
        clients = new HashMap<>();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(8190)) {
            while (true) {
                System.out.println("Ожидание подключения клиента...");
                Socket socket = serverSocket.accept();
                new ClientHandler(socket, this);
                System.out.println("Клиент подключился");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AuthService getAuthService() {
        return authService;
    }

    public boolean isNickBusy(String nick) {
        return clients.containsKey(nick);

    }

    public void subscribe(ClientHandler client) {

        clients.put(client.getNick(), client);
        broadcastClientList();
    }

    public void unsubscribe(ClientHandler client) {

        clients.remove(client.getNick());
        broadcastClientList();
    }

    public void broadcast(String message) {
        for (ClientHandler client : clients.values()) {
            client.sendMessage(message);
        }
    }

    public void sendMessageToClient(ClientHandler from, String nickTo, String message) {
        ClientHandler clientTo = clients.get(nickTo);
        if (clientTo != null) {
            clientTo.sendMessage("От " + from.getNick() + " : " + message);
            from.sendMessage("Участнику " + nickTo + ": " + message);
            return;
        }
        from.sendMessage("Участника с ником " + nickTo + " нет в чате");
    }


    public void broadcastClientList() {
        String message = clients.values().stream()
                .map(ClientHandler::getNick)
                .collect(Collectors.joining(" "));
        broadcast(Command.CLIENTS, message);

    }

    private void broadcast(Command command, String message) {
        for (ClientHandler client : clients.values()) {
            client.sendMessage(command, message);
        }
    }
}
