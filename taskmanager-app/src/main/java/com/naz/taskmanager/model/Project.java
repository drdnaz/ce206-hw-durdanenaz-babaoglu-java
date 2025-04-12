package com.naz.taskmanager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a project that can contain multiple taskmanager items.
 * This class demonstrates inheritance (extends BaseItem) and composition
 * (contains TaskmanagerItem objects).
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class Project extends BaseItem {
    /** Serial version UID for serialization */
    private static final long serialVersionUID = 1L;
    
    /** List of tasks in this project */
    private List<TaskmanagerItem> tasks;
    
    /** Start date of the project */
    private Date startDate;
    
    /** End date of the project */
    private Date endDate;
    
    /**
     * Constructor for creating a new Project
     * 
     * @param name Name of the project
     * @param description Description of the project
     */
    public Project(String name, String description) {
        super(name, description);
        this.tasks = new ArrayList<>();
    }
    
    /**
     * Gets the start date of the project
     * 
     * @return Start date as a defensive copy
     */
    public Date getStartDate() {
        return startDate != null ? (Date) startDate.clone() : null;
    }
    
    /**
     * Sets the start date of the project
     * 
     * @param startDate New start date
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate != null ? (Date) startDate.clone() : null;
    }
    
    /**
     * Gets the end date of the project
     * 
     * @return End date as a defensive copy
     */
    public Date getEndDate() {
        return endDate != null ? (Date) endDate.clone() : null;
    }
    
    /**
     * Sets the end date of the project
     * 
     * @param endDate New end date
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate != null ? (Date) endDate.clone() : null;
    }
    
    /**
     * Gets all tasks in this project
     * 
     * @return List of tasks as a defensive copy
     */
    public List<TaskmanagerItem> getTasks() {
        return new ArrayList<>(tasks); // Return a copy for encapsulation
    }
    
    /**
     * Adds a task to the project.
     * Demonstrates method overloading for polymorphism.
     * 
     * @param task The task to add
     * @throws IllegalArgumentException if task is null
     */
    public void addTask(TaskmanagerItem task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        tasks.add(task);
    }
    
    /**
     * Creates and adds a new task to the project.
     * Demonstrates method overloading for polymorphism.
     * 
     * @param name Name of the task
     * @param description Description of the task
     * @param category Category of the task
     * @return The created task
     */
    public TaskmanagerItem addTask(String name, String description, Category category) {
        TaskmanagerItem task = new TaskmanagerItem(name, description, category);
        tasks.add(task);
        return task;
    }
    
    /**
     * Creates and adds a new task with deadline and priority.
     * Demonstrates method overloading for polymorphism.
     * 
     * @param name Name of the task
     * @param description Description of the task
     * @param category Category of the task
     * @param deadline Deadline for the task
     * @param priority Priority of the task
     * @return The created task
     */
    public TaskmanagerItem addTask(String name, String description, Category category, 
                                  Date deadline, Priority priority) {
        TaskmanagerItem task = new TaskmanagerItem(name, description, category);
        task.setDeadline(deadline);
        task.setPriority(priority);
        tasks.add(task);
        return task;
    }
    
    /**
     * Removes a task from the project
     * 
     * @param task The task to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeTask(TaskmanagerItem task) {
        return tasks.remove(task);
    }
    
    /**
     * Calculates the completion percentage of the project
     * 
     * @return Completion percentage (0-100)
     */
    public double getCompletionPercentage() {
        if (tasks.isEmpty()) {
            return 0;
        }
        
        int completedTasks = 0;
        for (TaskmanagerItem task : tasks) {
            if (task.isCompleted()) {
                completedTasks++;
            }
        }
        
        return ((double) completedTasks * 100) / tasks.size();
    }
    
    /**
     * Displays project details.
     * Implements polymorphism through method overriding.
     */
    @Override
    public void display() {
        System.out.println("Project: " + name);
        System.out.println("Description: " + description);
        System.out.println("Start date: " + (startDate != null ? startDate : "Not set"));
        System.out.println("End date: " + (endDate != null ? endDate : "Not set"));
        System.out.println("Number of tasks: " + tasks.size());
        System.out.println("Completion: " + getCompletionPercentage() + "%");
        
        if (!tasks.isEmpty()) {
            System.out.println("\nTasks in this project:");
            for (int i = 0; i < tasks.size(); i++) {
                TaskmanagerItem task = tasks.get(i);
                System.out.println((i+1) + ". " + task.getName() + 
                                   " [" + (task.isCompleted() ? "Completed" : "Pending") + "]");
            }
        }
    }
    
    /**
     * Gets the item type
     * 
     * @return The string "Project"
     */
    @Override
    public String getItemType() {
        return "Project";
    }
    
    /**
     * Sets the ID for the project
     * 
     * @param id New ID
     */
    public void setId(String id) {
        this.id = id;
    }
}