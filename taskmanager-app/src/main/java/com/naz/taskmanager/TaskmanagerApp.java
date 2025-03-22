package com.naz.taskmanager;

import java.util.Scanner;

/**
 * Main application class for the Task Scheduler
 * This is the entry point of the application
 */
public class TaskmanagerApp {
    /**
     * Main method
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        /** Path to the user data file. */
        String pathFileUsers = "users.bin"; // File to store user data
        
        /** Scanner for user input. */
        Scanner inputScanner = new Scanner(System.in);
        
        /** Create an instance of the Taskmanager class. */
        Taskmanager taskmanager = new Taskmanager(inputScanner, System.out);
        
        /** Start the main menu of the Taskmanager class. */
        taskmanager.mainMenu(pathFileUsers);
        
        /** Close the scanner when done. */
        inputScanner.close();
        
        System.out.println("Thank you for using Task Manager!");
    }
}