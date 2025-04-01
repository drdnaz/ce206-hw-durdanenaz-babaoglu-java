package com.naz.taskmanager.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents a reminder for a task
 */
public class Reminder {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String taskId;
    private Date reminderTime;
    private boolean triggered;
    private String message;
    
    /**
     * Default constructor
     */
    public Reminder() {
        this.id = generateId();
        this.triggered = false;
    }
    
    /**
     * Constructor with taskId and reminderTime
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
     * Generate a unique ID
     * @return Unique ID string
     */
    private String generateId() {
        return String.valueOf(System.currentTimeMillis()) + Math.random();
    }
    
    /**
     * Get the ID of the reminder
     * @return Reminder ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Get the task ID associated with this reminder
     * @return Task ID
     */
    public String getTaskId() {
        return taskId;
    }
    
    /**
     * Set the task ID for this reminder
     * @param taskId New task ID
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    
    /**
     * Get the reminder time
     * @return Reminder time
     */
    public Date getReminderTime() {
        return reminderTime != null ? (Date) reminderTime.clone() : null;
    }
    
    /**
     * Set the reminder time
     * @param reminderTime New reminder time
     */
    public void setReminderTime(Date reminderTime) {
        this.reminderTime = reminderTime != null ? (Date) reminderTime.clone() : null;
    }
    
    /**
     * Check if the reminder has been triggered
     * @return true if triggered
     */
    public boolean isTriggered() {
        return triggered;
    }
    
    /**
     * Set whether the reminder has been triggered
     * @param triggered New triggered status
     */
    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }
    
    /**
     * Get the reminder message
     * @return Reminder message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Set the reminder message
     * @param message New message
     */
    public void setMessage(String message) {
        this.message = message;
    }
    /**
     * Set the ID of the reminder
     * @param id new ID
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * Check if the reminder is due (time has passed)
     * @return true if due
     */
    public boolean isDue() {
        if (reminderTime == null || triggered) {
            return false;
        }
        return reminderTime.before(new Date());
    }
    
    
    
}