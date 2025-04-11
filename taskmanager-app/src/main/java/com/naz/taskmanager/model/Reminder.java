package com.naz.taskmanager.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents a reminder for a task.
 * Reminders notify users about upcoming or overdue tasks at specified times.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class Reminder {
    /** Serial version UID for serialization */
    private static final long serialVersionUID = 1L;
    
    /** Unique identifier for the reminder */
    private String id;
    
    /** ID of the associated task */
    private String taskId;
    
    /** Time when the reminder should trigger */
    private Date reminderTime;
    
    /** Flag indicating if the reminder has been triggered */
    private boolean triggered;
    
    /** Optional message for the reminder */
    private String message;
    
    /**
     * Default constructor for creating a new Reminder
     */
    public Reminder() {
        this.id = generateId();
        this.triggered = false;
    }
    
    /**
     * Constructor with taskId and reminderTime
     * 
     * @param taskId ID of the task
     * @param reminderTime Time for the reminder
     */
    public Reminder(String taskId, Date reminderTime) {
        this.id = generateId();
        this.taskId = taskId;
        this.reminderTime = reminderTime != null ? (Date) reminderTime.clone() : null;
        this.triggered = false;
    }
    
    /**
     * Generates a unique ID based on timestamp and random value
     * 
     * @return Unique ID string
     */
    private String generateId() {
        return String.valueOf(System.currentTimeMillis()) + Math.random();
    }
    
    /**
     * Gets the ID of the reminder
     * 
     * @return Reminder ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the task ID associated with this reminder
     * 
     * @return Task ID
     */
    public String getTaskId() {
        return taskId;
    }
    
    /**
     * Sets the task ID for this reminder
     * 
     * @param taskId New task ID
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    
    /**
     * Gets the reminder time
     * 
     * @return Reminder time as a defensive copy
     */
    public Date getReminderTime() {
        return reminderTime != null ? (Date) reminderTime.clone() : null;
    }
    
    /**
     * Sets the reminder time
     * 
     * @param reminderTime New reminder time
     */
    public void setReminderTime(Date reminderTime) {
        this.reminderTime = reminderTime != null ? (Date) reminderTime.clone() : null;
    }
    
    /**
     * Checks if the reminder has been triggered
     * 
     * @return true if triggered, false otherwise
     */
    public boolean isTriggered() {
        return triggered;
    }
    
    /**
     * Sets the triggered status of the reminder
     * 
     * @param triggered New triggered status
     */
    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }
    
    /**
     * Gets the reminder message
     * 
     * @return Reminder message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Sets the reminder message
     * 
     * @param message New message
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Sets the ID of the reminder
     * 
     * @param id New ID
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Checks if the reminder is due (time has passed)
     * 
     * @return true if due and not yet triggered
     */
    public boolean isDue() {
        if (reminderTime == null || triggered) {
            return false;
        }
        return reminderTime.before(new Date());
    }
}