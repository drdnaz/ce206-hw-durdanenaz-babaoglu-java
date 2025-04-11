package com.naz.taskmanager.ui.menu;

import com.naz.taskmanager.Taskmanager;
import com.naz.taskmanager.service.UserService;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Main menu implementation.
 * Provides options for login, registration, and exiting the application.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class MainMenu implements Menu {
    /** Scanner for user input */
    private final Scanner scanner;
    
    /** PrintStream for output */
    private final PrintStream out;
    
    /** Taskmanager instance */
    private final Taskmanager taskManager;
    
    /** User service for authentication */
    private final UserService userService;
    
    /**
     * Constructor for MainMenu
     * 
     * @param scanner Scanner for user input
     * @param out PrintStream for output
     * @param taskManager Taskmanager instance
     * @param userService User service
     */
    public MainMenu(Scanner scanner, PrintStream out, Taskmanager taskManager, UserService userService) {
        this.scanner = scanner;
        this.out = out;
        this.taskManager = taskManager;
        this.userService = userService;
    }
    
    /**
     * Print menu options
     */
    @Override
    public void printMenuOptions() {
        taskManager.openingScreenMenu();
    }
    
    /**
     * Handle menu selection
     * 
     * @param choice User's menu choice
     * @return true if should continue, false to exit
     */
    @Override
    public boolean handleSelection(int choice) {
        switch (choice) {
            case 1:
                taskManager.clearScreen();
                if (taskManager.loginUserMenu()) {
                    taskManager.userOptionsMenu();
                }
                break;
            case 2:
                taskManager.clearScreen();
                taskManager.registerUserMenu();
                break;
            case 3:
                out.println("Exit Program");
                return false;
            default:
                out.println("Invalid choice. Please try again.");
                taskManager.enterToContinue();
                break;
        }
        return true;
    }
    
    /**
     * Show main menu and handle user input
     */
    public void showMenu() {
        boolean continueMenu = true;
        while (continueMenu) {
            printMenuOptions();
            int choice = taskManager.getInput();
            if (choice == -2) {
                taskManager.handleInputError();
                taskManager.enterToContinue();
                continue;
            }
            
            continueMenu = handleSelection(choice);
        }
    }
}