// AbstractMenu.java
package com.naz.taskmanager.ui.menu;

import com.naz.taskmanager.Taskmanager;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Abstract base class for all menus
 */
public abstract class AbstractMenu implements Menu {
    protected final Scanner scanner;
    protected final PrintStream out;
    protected final Taskmanager taskManager;
    
    /**
     * Constructor for AbstractMenu
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
     * Show the menu and handle user input
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
     * Print menu header
     */
    protected void printHeader() {
        out.println("========================================");
        out.println("           " + getMenuTitle() + "           ");
        out.println("========================================");
    }
    
    /**
     * Print menu footer
     */
    protected void printFooter() {
        out.println("========================================");
        out.print("Please enter your choice: ");
    }
    
    /**
     * Get menu title
     * @return Menu title string
     */
    protected abstract String getMenuTitle();
}