package ru.gb.simplechat.server;

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
        clients = new HashMap<>();
        authService = new InMemoryAuthService();
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
    }

    public void unsubscribe(ClientHandler client) {

        clients.remove(client.getNick());
    }

    public void broadcast(String message) {
        for (ClientHandler client : clients.values()) {
            client.sendMessage(message);
        }
    }

    public void sendMessageToClient(ClientHandler from, String nickTo, String message) {
        ClientHandler clientTo = clients.get(nickTo);
        if (clientTo != null) {
            clientTo.sendMessage("От " + from.getNick() + ":" + message);
            from.sendMessage("Участнику " + nickTo + ": " + message);
            return;
        }
        from.sendMessage("Участника с ником " + nickTo + " нет в чате");
    }


    public void broadcastClientList() {
        String message = clients.values().stream()
                .map(ClientHandler::getNick)
                .collect(Collectors.joining(" ", "/clients", ""));

//        StringBuilder message = new StringBuilder("/clients ");
//        clients.values().forEach(client -> message.append(client.getNick()).append(" "));
        broadcast(message);

    }
}
