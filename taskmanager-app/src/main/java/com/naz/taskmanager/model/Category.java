package com.naz.taskmanager.model;

import java.io.Serializable;

/**
 * Represents a category for Taskmanager items.
 * Categories help organize tasks by grouping them into logical collections.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class Category {
    /** Unique identifier for serialization */
    private static final long serialVersionUID = 1L;

    /** Name of the category */
    private String name;

    /**
     * Constructor for creating a new Category
     * 
     * @param name Name of the category
     */
    public Category(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the category
     * 
     * @return Category name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the category
     * 
     * @param name New category name
     */
    public void setName(String name) {
        this.name = name;
    }
}