package com.naz.taskmanager.ui;

import com.naz.taskmanager.Taskmanager;
import com.naz.taskmanager.service.*;
import com.naz.taskmanager.ui.menu.*;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Handler for menu navigation.
 * Coordinates different menu screens and manages navigation between them.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class MenuHandler {
    /** Scanner for user input */
    private final Scanner scanner;
    
    /** PrintStream for output */
    private final PrintStream out;
    
    /** Task service */
    private final TaskService taskService;
    
    /** Reminder service */
    private final ReminderService reminderService;
    
    /** User service */
    private final UserService userService;
    
    /** Taskmanager instance */
    private final Taskmanager taskManager;
    
    /**
     * Constructor for MenuHandler
     * 
     * @param scanner Scanner for user input
     * @param out PrintStream for output
     * @param taskManager Taskmanager instance
     * @param userService User service
     * @param taskService Task service
     * @param reminderService Reminder service
     */
    public MenuHandler(Scanner scanner, PrintStream out, Taskmanager taskManager, 
                      UserService userService, TaskService taskService, 
                      ReminderService reminderService) {
        this.scanner = scanner;
        this.out = out;
        this.taskManager = taskManager;
        this.userService = userService;
        this.taskService = taskService;
        this.reminderService = reminderService;
    }
    
    /**
     * Start main menu flow
     */
    public void startMainMenu() {
        taskManager.mainMenu();
    }
    
    /**
     * Navigate to task management menu
     */
    public void navigateToTaskMenu() {
        taskManager.createTaskmanagerMenu();
    }
    
    /**
     * Navigate to deadline settings menu
     */
    public void navigateToDeadlineMenu() {
        taskManager.deadlineSettingsMenu();
    }
    
    /**
     * Navigate to reminder system menu
     */
    public void navigateToReminderMenu() {
        taskManager.reminderSystemMenu();
    }
    
    /**
     * Navigate to task prioritization menu
     */
    public void navigateToPriorityMenu() {
        taskManager.TaskmanagerPrioritizationMenu();
    }
    
    /**
     * Handle invalid menu choice
     */
    public void handleInvalidChoice() {
        taskManager.clearScreen();
        out.println("Invalid choice. Please try again.");
        taskManager.enterToContinue();
    }
}