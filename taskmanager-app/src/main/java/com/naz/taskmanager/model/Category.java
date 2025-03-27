package com.naz.taskmanager.model;

import java.io.Serializable;

/**
 * Represents a category for Taskmanager items
 */
public class Category implements Serializable {
    /** Unique identifier for serialization */
    private static final long serialVersionUID = 1L;

    /** Name of the category */
    private String name;

    /**
     * Constructor for Category
     * @param name Name of the category
     */
    public Category(String name) {
        this.name = name;
    }

    /**
     * Get the name of the category
     * @return category name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the category
     * @param name new category name
     */
    public void setName(String name) {
        this.name = name;
    }
}