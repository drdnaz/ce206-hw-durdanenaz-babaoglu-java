package com.naz.taskmanager.ui.menu;

import com.naz.taskmanager.Taskmanager;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * @brief Abstract base class for all menus.
 * 
 * @details Implements common menu behavior while allowing subclasses to customize specific aspects.
 * Part of the Template Method design pattern for menu implementation.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public abstract class AbstractMenu implements Menu {
    /** @brief Scanner for user input */
    protected final Scanner scanner;
    
    /** @brief PrintStream for output */
    protected final PrintStream out;
    
    /** @brief Taskmanager instance */
    protected final Taskmanager taskManager;
    
    /**
     * @brief Constructor for AbstractMenu
     * 
     * @param scanner Scanner for user input
     * @param out PrintStream for output
     * @param taskManager Taskmanager instance
     */
    public AbstractMenu(Scanner scanner, PrintStream out, Taskmanager taskManager) {
        this.scanner = scanner;
        this.out = out;
        this.taskManager = taskManager;
    }
    
    /**
     * @brief Template method for showing the menu and handling user input.
     * 
     * @details Defines the structure of the algorithm while allowing subclasses
     * to override specific steps.
     */
    public void showMenu() {
        taskManager.clearScreen();
        printHeader();
        printMenuOptions();
        printFooter();
        
        int choice = taskManager.getInput();
        if (choice == -2) {
            taskManager.handleInputError();
            taskManager.enterToContinue();
            return;
        }
        
        if (!handleSelection(choice)) {
            return; // Exit menu if requested
        }
    }
    
    /**
     * @brief Prints menu header.
     * 
     * @details Can be overridden by subclasses for custom header formatting.
     */
    protected void printHeader() {
        out.println("========================================");
        out.println("           " + getMenuTitle() + "           ");
        out.println("========================================");
    }
    
    /**
     * @brief Prints menu footer.
     * 
     * @details Can be overridden by subclasses for custom footer formatting.
     */
    protected void printFooter() {
        out.println("========================================");
        out.print("Please enter your choice: ");
    }
    
    /**
     * @brief Abstract method to get the menu title.
     * 
     * @details Must be implemented by concrete menu classes.
     * 
     * @return Menu title string
     */
    protected abstract String getMenuTitle();
}