package com.taskmanager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model class for Task objects
 */
public class Task {
    private static int nextId = 1;
    private int id;
    private String title;
    private String description;
    private Date dueDate;
    private String category;
    private String priority;
    private String status;
    
    // Static list to store all tasks
    private static final List<Task> allTasks = new ArrayList<>();
    
    /**
     * Creates a new task
     * @param title Task title
     * @param description Task description
     * @param dueDate Due date
     * @param category Category
     * @param priority Priority
     */
    public Task(String title, String description, Date dueDate, String category, String priority) {
        this.id = nextId++;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.category = category;
        this.priority = priority;
        this.status = "Not Started";
    }
    
    /**
     * Get all tasks
     * @return List of all tasks
     */
    public static List<Task> getAllTasks() {
        return allTasks;
    }
    
    /**
     * Add a task to the static list
     * @param task Task to add
     */
    public static void addTask(Task task) {
        allTasks.add(task);
    }
    
    /**
     * Delete a task by its ID
     * @param taskId ID of the task to delete
     * @return true if task was found and deleted, false otherwise
     */
    public static boolean deleteTask(int taskId) {
        for (int i = 0; i < allTasks.size(); i++) {
            if (allTasks.get(i).getId() == taskId) {
                allTasks.remove(i);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Find a task by its ID
     * @param taskId ID of the task to find
     * @return the task if found, null otherwise
     */
    public static Task findTaskById(int taskId) {
        for (Task task : allTasks) {
            if (task.getId() == taskId) {
                return task;
            }
        }
        return null;
    }
    
    /**
     * Update an existing task
     * @param taskId ID of the task to update
     * @param title New title
     * @param description New description
     * @param dueDate New due date
     * @param category New category
     * @param priority New priority
     * @param status New status
     * @return true if task was found and updated, false otherwise
     */
    public static boolean updateTask(int taskId, String title, String description, 
                                    Date dueDate, String category, String priority, String status) {
        Task task = findTaskById(taskId);
        if (task != null) {
            task.setTitle(title);
            task.setDescription(description);
            task.setDueDate(dueDate);
            task.setCategory(category);
            task.setPriority(priority);
            task.setStatus(status);
            return true;
        }
        return false;
    }
    
    /**
     * Get available task statuses
     * @return Array of status options
     */
    public static String[] getStatusOptions() {
        return new String[] {"Not Started", "In Progress", "Completed", "Deferred", "Cancelled"};
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 