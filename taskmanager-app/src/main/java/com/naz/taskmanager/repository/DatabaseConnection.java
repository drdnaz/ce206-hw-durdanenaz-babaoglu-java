package com.naz.taskmanager.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.PrintStream;
import java.io.File;

/**
 * Singleton class for database connection management.
 * Handles SQLite database connectivity, initialization, and schema creation.
 * Implements the Singleton design pattern to ensure a single connection instance.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class DatabaseConnection {
    /** Singleton instance */
    private static DatabaseConnection instance;
    
    /** Database connection */
    private Connection connection = null;
    
    /** Database URL for SQLite */
    private static final String DB_URL = "jdbc:sqlite:data/taskmanager.db";
    
    /** PrintStream for output messages */
    private final PrintStream out;
    
    /** Flag indicating if the database has been initialized */
    private boolean initialized = false;

    /**
     * Private constructor for Singleton pattern.
     * 
     * @param out PrintStream for output messages
     */
    private DatabaseConnection(PrintStream out) {
        this.out = out;
    }
    
    /**
     * Gets the singleton instance.
     * Creates a new instance if one doesn't exist yet.
     * 
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
     * Gets the active database connection.
     * Opens a new connection if needed.
     * 
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
     * Opens a new database connection.
     * Creates the data directory if it doesn't exist.
     */
    private void openConnection() {
        File dbFile = new File("data/taskmanager.db");
        out.println("Database file full path: " + dbFile.getAbsolutePath());
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
     * Closes the database connection.
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
     * Releases the connection back to the pool.
     * In a real connection pool, this would return the connection to the pool.
     */
    public synchronized void releaseConnection() {
        // In a real connection pool, you would return the connection to the pool here
        // For now, we'll do nothing and let the connection remain open
    }

    /**
     * Initializes the database tables.
     * Creates all required tables if they don't exist yet.
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