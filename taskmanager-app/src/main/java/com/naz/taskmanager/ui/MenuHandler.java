// MenuHandler.java
package com.naz.taskmanager.ui;

import com.naz.taskmanager.Taskmanager;
import com.naz.taskmanager.service.*;
import com.naz.taskmanager.ui.menu.*;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Handler for menu navigation
 */
public class MenuHandler {
    private final Scanner scanner;
    private final PrintStream out;
    private final TaskService taskService;
    private final ReminderService reminderService;
    private final UserService userService;
    private final Taskmanager taskManager;
    
    /**
     * Constructor for MenuHandler
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
        taskManager.mainMenu("users.db");
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