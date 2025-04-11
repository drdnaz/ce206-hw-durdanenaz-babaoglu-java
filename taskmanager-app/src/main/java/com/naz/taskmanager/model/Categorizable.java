package com.naz.taskmanager.model;

/**
 * Interface for items that can be categorized.
 * Implements the Interface Segregation Principle by separating the categorization
 * behavior from other item behaviors.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public interface Categorizable {
    /**
     * Gets the category of the item
     * 
     * @return Category of the item
     */
    Category getCategory();
    
    /**
     * Sets the category of the item
     * 
     * @param category New category for the item
     */
    void setCategory(Category category);
}