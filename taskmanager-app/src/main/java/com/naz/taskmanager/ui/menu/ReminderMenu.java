package com.naz.taskmanager.ui.menu;

import com.naz.taskmanager.Taskmanager;
import com.naz.taskmanager.service.ReminderService;
import com.naz.taskmanager.service.TaskService;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Reminder system menu implementation.
 * Provides options for managing task reminders and notifications.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class ReminderMenu implements Menu {
    /** Scanner for user input */
    private final Scanner scanner;
    
    /** PrintStream for output */
    private final PrintStream out;
    
    /** Taskmanager instance */
    private final Taskmanager taskManager;
    
    /** Reminder service */
    private final ReminderService reminderService;
    
    /** Task service */
    private final TaskService taskService;
    
    /**
     * Constructor for ReminderMenu
     * 
     * @param scanner Scanner for user input
     * @param out PrintStream for output
     * @param taskManager Taskmanager instance
     * @param reminderService Reminder service
     * @param taskService Task service
     */
    public ReminderMenu(Scanner scanner, PrintStream out, Taskmanager taskManager, 
                       ReminderService reminderService, TaskService taskService) {
        this.scanner = scanner;
        this.out = out;
        this.taskManager = taskManager;
        this.reminderService = reminderService;
        this.taskService = taskService;
    }
    
    /**
     * Print menu options
     */
    @Override
    public void printMenuOptions() {
        taskManager.printReminderSystemMenu();
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
                taskManager.setReminders();
                break;
            case 2:
                taskManager.viewReminders();
                taskManager.enterToContinue();
                break;
            case 3:
                taskManager.notificationSettings();
                break;
            case 4:
                return false;
            default:
                taskManager.clearScreen();
                out.println("Invalid choice. Please try again.");
                taskManager.enterToContinue();
                break;
        }
        return true;
    }
    
    /**
     * Show reminder menu and handle user input
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