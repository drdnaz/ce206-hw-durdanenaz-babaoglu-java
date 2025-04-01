package com.naz.taskmanager.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Base abstract class for all items in the task manager application
 * Implements abstraction through an abstract class
 */
public abstract class BaseItem {
    private static final long serialVersionUID = 1L;
    
    protected String id;
    protected String name;
    protected String description;
    protected Date creationDate;
    protected boolean isCompleted;
    
    public BaseItem(String name, String description) {
        this.id = generateId();
        this.name = name;
        this.description = description;
        this.creationDate = new Date();
        this.isCompleted = false;
    }
    
    protected String generateId() {
        return String.valueOf(System.currentTimeMillis()) + Math.random();
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Date getCreationDate() {
        return creationDate != null ? (Date) creationDate.clone() : null;
    }
    
    public boolean isCompleted() {
        return isCompleted;
    }
    
    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }
    
    // Abstract methods that must be implemented by subclasses
    public abstract void display();
    public abstract String getItemType();
}