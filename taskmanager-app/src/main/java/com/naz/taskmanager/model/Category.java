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
    
    /**
     * Compares this category with another object for equality.
     * Two categories are equal if they have the same name.
     * 
     * @param obj Object to compare with
     * @return true if the categories are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Category other = (Category) obj;
        return name != null ? name.equals(other.name) : other.name == null;
    }
    
    /**
     * Returns a hash code value for the category.
     * 
     * @return Hash code value
     */
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
    
    /**
     * Returns a string representation of the category.
     * 
     * @return String representation of the category
     */
    @Override
    public String toString() {
        return name;
    }
}