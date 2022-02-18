package ru.gb.simplechat.server;

import java.sql.*;

public class DBAuthService implements AuthService {

    private Connection connection;
    private Statement statement;


    public DBAuthService() {
        connect();
    }


    public static void main(String[] args) {
        DBAuthService dbAuth = new DBAuthService();
        try {
            dbAuth.connect();
            dbAuth.createTable();

                dbAuth.dropData();
            System.out.println(dbAuth.getNickByLoginAndPassword("login1", "pass1"));
        } finally {
            dbAuth.disconnect();
        }
    }

    private void insert(String login, String pass, String nick) {
        try (PreparedStatement ps = connection.prepareStatement("insert into user(login, password, nick) values (?, ?, ?)")) {
            ps.setString(1, login);
            ps.setString(2, pass);
            ps.setString(3, nick);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

    private void dropData() {
        try {
            statement.executeUpdate("delete from user");
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
        try (PreparedStatement ps = connection.prepareStatement("select nick from user where login = ? and password = ?")) {
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nick = rs.getString(1);
                return nick;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
