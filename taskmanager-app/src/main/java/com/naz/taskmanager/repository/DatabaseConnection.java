package com.naz.taskmanager.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.PrintStream;
import java.io.File;

/**
 * Singleton class for database connection management
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection = null;
    private static final String DB_URL = "jdbc:sqlite:data/taskmanager.db";
    private final PrintStream out;
    private boolean initialized = false;

    /**
     * Private constructor for Singleton pattern
     * @param out PrintStream for output messages
     */
    private DatabaseConnection(PrintStream out) {
        this.out = out;
    }
    
    /**
     * Get singleton instance
     * @param out PrintStream for output messages
     * @return Singleton instance
     */
    public static synchronized DatabaseConnection getInstance(PrintStream out) {
        if (instance == null) {
            instance = new DatabaseConnection(out);
        }
        return instance;
    }

    /**
     * Get database connection - opens connection if closed
     * @return Active database connection
     */
    public synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                openConnection();
            }
            return connection;
        } catch (SQLException e) {
            out.println("Error checking connection state: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Open a new database connection
     */
    private void openConnection() {
        try {
            // Load SQLite driver
            Class.forName("org.sqlite.JDBC");
            
            // Create data directory if it doesn't exist
            File dbDir = new File("data");
            if (!dbDir.exists()) {
                boolean created = dbDir.mkdirs();
                System.out.println("Created data directory: " + created);
            }
            
            // Open connection
            connection = DriverManager.getConnection(DB_URL);
            
            // Enable foreign keys if not initialized yet
            if (!initialized) {
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON");
                }
                initialized = true;
            }
            
            out.println("Database connection established");
        } catch (SQLException e) {
            out.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            out.println("SQLite JDBC driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Close database connection
     */
    public synchronized void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    out.println("Database connection closed");
                }
                connection = null;
            } catch (SQLException e) {
                out.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Release connection back to the pool (doesn't actually close it in this implementation)
     * In a real connection pool, this would return the connection to the pool
     */
    public synchronized void releaseConnection() {
        // In a real connection pool, you would return the connection to the pool here
        // For now, we'll do nothing and let the connection remain open
    }

    /**
     * Initialize database tables
     */
    public void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Enable foreign keys
            stmt.execute("PRAGMA foreign_keys = ON");
            
            // Users table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Users (" +
                "username TEXT PRIMARY KEY, " +
                "password TEXT NOT NULL, " +
                "email TEXT" +
                ")"
            );
            
            // Categories table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL UNIQUE" +
                ")"
            );
            
            // Tasks table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "category_id INTEGER, " +
                "deadline TEXT, " +
                "priority INTEGER DEFAULT 1, " + // 0: HIGH, 1: MEDIUM, 2: LOW
                "completed INTEGER DEFAULT 0, " +
                "creation_date TEXT NOT NULL, " +
                "FOREIGN KEY(username) REFERENCES Users(username) ON DELETE CASCADE, " +
                "FOREIGN KEY(category_id) REFERENCES Categories(id) ON DELETE CASCADE" +
                ")"
            );
            
            // Reminders table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Reminders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "task_id INTEGER NOT NULL, " +
                "reminder_time TEXT NOT NULL, " +
                "triggered INTEGER DEFAULT 0, " +
                "message TEXT, " +
                "FOREIGN KEY(task_id) REFERENCES Tasks(id) ON DELETE CASCADE" +
                ")"
            );
            
            // Projects table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Projects (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "start_date TEXT, " +
                "end_date TEXT, " +
                "creation_date TEXT NOT NULL, " +
                "completed INTEGER DEFAULT 0, " +
                "FOREIGN KEY(username) REFERENCES Users(username) ON DELETE CASCADE" +
                ")"
            );
            
            // Project_Tasks relationship table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Project_Tasks (" +
                "project_id INTEGER NOT NULL, " +
                "task_id INTEGER NOT NULL, " +
                "PRIMARY KEY (project_id, task_id), " +
                "FOREIGN KEY(project_id) REFERENCES Projects(id) ON DELETE CASCADE, " +
                "FOREIGN KEY(task_id) REFERENCES Tasks(id) ON DELETE CASCADE" +
                ")"
            );
            
            // Settings table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Settings (" +
                "username TEXT PRIMARY KEY, " +
                "email_enabled INTEGER DEFAULT 1, " +
                "app_notifications_enabled INTEGER DEFAULT 1, " +
                "default_reminder_minutes INTEGER DEFAULT 30, " +
                "FOREIGN KEY(username) REFERENCES Users(username) ON DELETE CASCADE" +
                ")"
            );
            
            out.println("Database initialized successfully.");
        } catch (SQLException e) {
            out.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}