package com.naz.taskmanager;

import com.naz.taskmanager.repository.DatabaseConnection;
import java.io.PrintStream;

/**
 * Alternative entry point for the application.
 * Focuses on initializing the database.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class Main {
    /**
     * Main method - application entry point
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Get database connection and initialize tables
        DatabaseConnection dbConnection = DatabaseConnection.getInstance(System.out);
        dbConnection.initializeDatabase();
    }
}