package com.naz.taskmanager.ui.menu;

/**
 * Interface for menu classes.
 * Defines common methods that all menu implementations should provide.
 * Part of the Strategy pattern for menu handling.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public interface Menu {
    /**
     * Print menu options to the console
     */
    void printMenuOptions();
    
    /**
     * Handle user's menu selection
     * 
     * @param choice User's menu choice
     * @return true if should continue showing menu, false if should exit
     */
    boolean handleSelection(int choice);
}