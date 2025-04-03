package com.naz.taskmanager;

import java.util.Scanner;
import com.naz.taskmanager.repository.DatabaseConnection;

/**
 * Main application class that creates and starts the Taskmanager
 */
public class TaskmanagerApp {
    /**
     * Application entry point
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Create scanner for user input
        Scanner scanner = new Scanner(System.in);
        Taskmanager taskmanagerApp = new Taskmanager(scanner, System.out);
        
        // Initialize database
        System.out.println("Initializing database...");
        DatabaseConnection.getInstance(System.out).initializeDatabase();
        
        // Get Taskmanager singleton instance
        Taskmanager taskManager = Taskmanager.getInstance(scanner, System.out);
        
        // Start the application
        System.out.println("Starting TaskManager application...");
        
        // Run the main menu
        taskManager.mainMenu();
        
        // Close the scanner
        scanner.close();
        
        // Close database connection
        DatabaseConnection.getInstance(System.out).closeConnection();
        
        System.out.println("TaskManager application closed. Goodbye!");
    }
}