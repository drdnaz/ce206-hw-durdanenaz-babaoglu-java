package com.naz.taskmanager.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Base abstract class for all items in the task manager application.
 * This class implements the abstraction principle through an abstract class.
 * It provides common properties and behaviors for all item types in the system.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public abstract class BaseItem {
    /** Serial version UID for serialization */
    private static final long serialVersionUID = 1L;
    
    /** Unique identifier for the item */
    protected String id;
    
    /** Name of the item */
    protected String name;
    
    /** Description of the item */
    protected String description;
    
    /** Creation date of the item */
    protected Date creationDate;
    
    /** Completion status of the item */
    protected boolean isCompleted;
    
    /**
     * Constructor for creating a new BaseItem
     * 
     * @param name Name of the item
     * @param description Description of the item
     */
    public BaseItem(String name, String description) {
        this.id = generateId();
        this.name = name;
        this.description = description;
        this.creationDate = new Date();
        this.isCompleted = false;
    }
    
    /**
     * Generates a unique ID for the item
     * 
     * @return Unique ID string based on timestamp and random value
     */
    protected String generateId() {
        return String.valueOf(System.currentTimeMillis()) + Math.random();
    }
    
    /**
     * Gets the unique ID of the item
     * 
     * @return ID of the item
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the name of the item
     * 
     * @return Name of the item
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name of the item
     * 
     * @param name New name for the item
     * @throws IllegalArgumentException if name is null or empty
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }
    
    /**
     * Gets the description of the item
     * 
     * @return Description of the item
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the description of the item
     * 
     * @param description New description for the item
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Gets the creation date of the item
     * 
     * @return Creation date of the item as a defensive copy
     */
    public Date getCreationDate() {
        return creationDate != null ? (Date) creationDate.clone() : null;
    }
    
    /**
     * Checks if the item is completed
     * 
     * @return true if completed, false otherwise
     */
    public boolean isCompleted() {
        return isCompleted;
    }
    
    /**
     * Sets the completion status of the item
     * 
     * @param completed New completion status
     */
    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }
    
    /**
     * Abstract method to display item details
     * Must be implemented by subclasses to show item-specific information
     */
    public abstract void display();
    
    /**
     * Abstract method to get the type of the item
     * 
     * @return String representing the item type
     */
    public abstract String getItemType();
}