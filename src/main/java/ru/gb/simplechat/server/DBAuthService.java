package ru.gb.simplechat.server;

import java.sql.Connection;

public class DBAuthService implements AuthService {

    private Connection connection;

    public DBAuthService() {
    }

    @Override
    public String getNickByLoginAndPassword(String login, String password) {
//        "select nick from user where login = ? and password = ?"
//        create table if not exist user ( +
//                id integer prymary key autoincrement,
//        nick text,
//        login text,
//        password text)
        return null;
    }
}
