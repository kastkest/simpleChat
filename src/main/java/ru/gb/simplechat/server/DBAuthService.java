package ru.gb.simplechat.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBAuthService implements AuthService {

    private Connection connection;
    private Statement statement;

    public DBAuthService() {
    }


    public static void main(String[] args) {
        DBAuthService dbAuth = new DBAuthService();
        try {
            dbAuth.connect();
            dbAuth.createTable();
        } finally {
            dbAuth.disconnect();
        }
    }

    private void createTable() {
        try {
            statement.executeUpdate("" +
                    "create table if not exists user (" +
                    "   id integer primary key autoincrement, " +
                    "   nick text, " +
                    "   login text, " +
                    "   password text" +
                    ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:java.db");
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
