package com.naz.taskmanager.model;

/**
 * Interface for items that can be categorized
 * Implements interface segregation principle
 */
public interface Categorizable {
    Category getCategory();
    void setCategory(Category category);
}