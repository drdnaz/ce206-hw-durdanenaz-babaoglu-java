package com.naz.taskmanager.ui.menu;

import com.naz.taskmanager.Taskmanager;
import com.naz.taskmanager.service.TaskService;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Deadline settings menu implementation.
 * Provides options for managing task deadlines.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class DeadLineMenu implements Menu {
    /** Scanner for user input */
    private final Scanner scanner;
    
    /** PrintStream for output */
    private final PrintStream out;
    
    /** Taskmanager instance */
    private final Taskmanager taskManager;
    
    /** Task service */
    private final TaskService taskService;
    
    /**
     * Constructor for DeadLineMenu
     * 
     * @param scanner Scanner for user input
     * @param out PrintStream for output
     * @param taskManager Taskmanager instance
     * @param taskService Task service
     */
    public DeadLineMenu(Scanner scanner, PrintStream out, Taskmanager taskManager, TaskService taskService) {
        this.scanner = scanner;
        this.out = out;
        this.taskManager = taskManager;
        this.taskService = taskService;
    }
    
    /**
     * Print menu options
     */
    @Override
    public void printMenuOptions() {
        taskManager.printDeadlineSettingsMenu();
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
                taskManager.assignDeadline();
                break;
            case 2:
                taskManager.viewDeadlines();
                taskManager.enterToContinue();
                break;
            case 3:
                taskManager.viewDeadlinesInRange();
                taskManager.enterToContinue();
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
     * Show deadline menu and handle user input
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