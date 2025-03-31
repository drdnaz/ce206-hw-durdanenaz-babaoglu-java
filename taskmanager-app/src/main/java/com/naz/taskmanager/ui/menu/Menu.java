// Menu.java
package com.naz.taskmanager.ui.menu;

/**
 * Interface for menu classes
 */
public interface Menu {
    /**
     * Print menu options
     */
    void printMenuOptions();
    
    /**
     * Handle menu selection
     * @param choice User's menu choice
     * @return true if should continue showing menu, false if should exit
     */
    boolean handleSelection(int choice);
}