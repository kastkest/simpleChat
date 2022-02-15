package ru.gb.simplechat.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcApp {
    private Connection connection;
    private Statement statement;

    public static void main(String[] args) {
        JdbcApp jdbcApp = new JdbcApp();
        try {
            jdbcApp.connect();
            jdbcApp.createTable();
            jdbcApp.dropData();
            jdbcApp.insert("Bob", 95);
        } finally {
            jdbcApp.disconnect();
        }
    }

    private void dropData() {
        try {
            statement.executeUpdate("delete from students");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insert(String name , int score) {
        try {
            statement.executeUpdate("insert into students(name, score) values ('" + name +
                    "', " + score + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        try {
            statement.executeUpdate("" +
                    "create table if not exists students (" +
                    "   id integer primary key autoincrement, " +
                    "   name text, " +
                    "   score integer" +
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

    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:java.db");
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
