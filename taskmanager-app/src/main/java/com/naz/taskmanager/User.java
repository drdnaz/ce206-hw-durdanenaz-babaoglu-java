package com.naz.taskmanager;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * User class for authentication
 */
class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private UUID id;
    private String username;
    private String password; // In a real application, this should be hashed
    private String email;
    
    /**
     * Constructor for User
     * @param username Username
     * @param password Password
     * @param email Email address
     */
    public User(String username, String password, String email) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.email = email;
    }
    
    // Getters and setters
    public UUID getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}

/**
 * Category class for task categorization
 */
class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private UUID id;
    private String name;
    
    /**
     * Constructor for Category
     * @param name Category name
     */
    public Category(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }
    
    // Getters and setters
    public UUID getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category category = (Category) obj;
        return id.equals(category.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

/**
 * Priority enum for task prioritization
 */
enum Priority {
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low");
    
    private final String displayName;
    
    /**
     * Constructor for Priority
     * @param displayName Display name
     */
    Priority(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Get display name
     * @return Display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}

/**
 * TaskmanagerItem class for task representation
 */
class TaskmanagerItem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private UUID id;
    private String name;
    private String description;
    private Category category;
    private Date deadline;
    private Priority priority;
    private boolean completed;
    
    /**
     * Constructor for TaskItem
     * @param name Task name
     * @param description Task description
     * @param category Task category
     */
    public TaskmanagerItem(String name, String description, Category category) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.category = category;
        this.priority = Priority.MEDIUM; // Default priority
        this.completed = false;
    }
    
    // Getters and setters
    public UUID getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public Date getDeadline() {
        return deadline;
    }
    
    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
    
    public Priority getPriority() {
        return priority;
    }
    
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

/**
 * Reminder class for task reminders
 */
class Reminder implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private UUID id;
    private int taskId;
    private Date reminderTime;
    private boolean triggered;
    
    /**
     * Constructor for Reminder
     * @param taskId Task ID
     * @param reminderTime Reminder time
     */
    public Reminder(int taskId, Date reminderTime) {
        this.id = UUID.randomUUID();
        this.taskId = taskId;
        this.reminderTime = reminderTime;
        this.triggered = false;
    }
    
    // Getters and setters
    public UUID getId() {
        return id;
    }
    
    public int getTaskmanagerId() {
        return taskId;
    }
    
    public Date getReminderTime() {
        return reminderTime;
    }
    
    public void setReminderTime(Date reminderTime) {
        this.reminderTime = reminderTime;
    }
    
    public boolean isTriggered() {
        return triggered;
    }
    
    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }
}

/**
 * NotificationSettings class for user notification preferences
 */
class NotificationSettings implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private boolean emailEnabled;
    private boolean appNotificationsEnabled;
    private int defaultReminderMinutes;
    
    /**
     * Constructor for NotificationSettings
     */
    public NotificationSettings() {
        this.emailEnabled = false;
        this.appNotificationsEnabled = true;
        this.defaultReminderMinutes = 30; // Default: 30 minutes before
    }
    
    // Getters and setters
    public boolean isEmailEnabled() {
        return emailEnabled;
    }
    
    public void setEmailEnabled(boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }
    
    public boolean isAppNotificationsEnabled() {
        return appNotificationsEnabled;
    }
    
    public void setAppNotificationsEnabled(boolean appNotificationsEnabled) {
        this.appNotificationsEnabled = appNotificationsEnabled;
    }
    
    public int getDefaultReminderMinutes() {
        return defaultReminderMinutes;
    }
    
    public void setDefaultReminderMinutes(int defaultReminderMinutes) {
        this.defaultReminderMinutes = defaultReminderMinutes;
    }
}