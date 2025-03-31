// PriorityMenu.java (güncellendi)
package com.naz.taskmanager.ui.menu;

import com.naz.taskmanager.Taskmanager;
import com.naz.taskmanager.service.TaskService;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Task prioritization menu implementation
 */
public class PriorityMenu implements Menu {
    private final Scanner scanner;
    private final PrintStream out;
    private final Taskmanager taskManager;
    private final TaskService taskService;
    
    /**
     * Constructor for PriorityMenu
     * @param scanner Scanner for user input
     * @param out PrintStream for output
     * @param taskManager Taskmanager instance
     * @param taskService Task service
     */
    public PriorityMenu(Scanner scanner, PrintStream out, Taskmanager taskManager, TaskService taskService) {
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
        taskManager.printTaskmanagerPrioritizationMenu();
    }
    
    /**
     * Handle menu selection
     * @param choice User's menu choice
     * @return true if should continue, false to exit
     */
    @Override
    public boolean handleSelection(int choice) {
        switch (choice) {
            case 1:
                taskManager.markTaskPriority();
                break;
            case 2:
                taskManager.viewTasksByPriority();
                taskManager.enterToContinue();
                break;
            case 3:
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
     * Show priority menu and handle user input
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