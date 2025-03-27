package com.naz.taskmanager;

import java.util.Scanner;

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
        
        // Get Taskmanager singleton instance
        Taskmanager taskManager = Taskmanager.getInstance(scanner, System.out);
        
        // Start the application
        System.out.println("Starting TaskManager application...");
        
        // Path to user data file
        String userDataPath = "users.bin";
        
        // Run the main menu
        taskManager.mainMenu(userDataPath);
        
        // Close the scanner
        scanner.close();
        
        System.out.println("TaskManager application closed. Goodbye!");
    }
}