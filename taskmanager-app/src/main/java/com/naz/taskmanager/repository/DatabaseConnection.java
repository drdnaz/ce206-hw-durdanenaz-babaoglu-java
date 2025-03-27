package com.naz.taskmanager.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.PrintStream;

public class DatabaseConnection {
    private Connection connection = null;
    private static final String DB_URL = "jdbc:sqlite:taskmanager.db";
    private final PrintStream out;

    public DatabaseConnection(PrintStream out) {
        this.out = out;
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL);
                out.println("Database connection established successfully.");
            } catch (SQLException e) {
                out.println("Error connecting to database: " + e.getMessage());
            }
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
                connection = null;
                out.println("Database connection closed successfully.");
            } catch (SQLException e) {
                out.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    public void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Kullanıcılar tablosu
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Users (" +
                "username TEXT PRIMARY KEY, " +
                "password TEXT NOT NULL, " +
                "email TEXT" +
                ")"
            );
            
            // Kategoriler tablosu
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL UNIQUE" +
                ")"
            );
            
            // Görevler tablosu
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "category_id INTEGER, " +
                "deadline TEXT, " +
                "priority INTEGER DEFAULT 1, " +
                "completed INTEGER DEFAULT 0, " +
                "creation_date TEXT NOT NULL, " +
                "FOREIGN KEY(username) REFERENCES Users(username), " +
                "FOREIGN KEY(category_id) REFERENCES Categories(id)" +
                ")"
            );
            
            out.println("Veritabanı başarıyla initialize edildi.");
        } catch (SQLException e) {
            out.println("Veritabanı başlatılırken hata oluştu: " + e.getMessage());
        }
    }
}