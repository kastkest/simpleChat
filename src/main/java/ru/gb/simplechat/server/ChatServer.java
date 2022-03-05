package ru.gb.simplechat.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gb.simplechat.Command;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ChatServer {
    private final Map<String, ClientHandler> clients;
    private final AuthService authService;
    private static final Logger logger = LogManager.getLogger(ChatServer.class);

    public ChatServer() {
        authService = new DBAuthService();
        clients = new HashMap<>();
    }

    public void start() {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        executorService.execute(() -> {
            try (ServerSocket serverSocket = new ServerSocket(8190)) {
                while (true) {
                    logger.info("Запущен");
                    System.out.println("Ожидание подключения клиента...");
                    Socket socket = serverSocket.accept();
                    new ClientHandler(socket, this);
                    logger.info("Клиент подключился");
                    System.out.println("Клиент подключился");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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

    public void saveHistory(String message) {
        for (ClientHandler client : clients.values()) {
            try (FileOutputStream fos = new FileOutputStream("history_" + client.getNick() + ".txt", true);
                 BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fos);
                 PrintStream ps = new PrintStream(bufferedOutputStream)) {
                ps.println(message);
            } catch (IOException e) {
                logger.warn("Произошла ошибка" + e);
                e.printStackTrace();
            }
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
        logger.info("Клиент отправил команду/сообщение" + message);
        for (ClientHandler client : clients.values()) {
            client.sendMessage(command, message);
        }
    }
}
