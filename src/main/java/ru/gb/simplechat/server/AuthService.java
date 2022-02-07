package ru.gb.simplechat.server;

public interface AuthService {

    String getNickByLoginAndPassword(String login, String password);
}
