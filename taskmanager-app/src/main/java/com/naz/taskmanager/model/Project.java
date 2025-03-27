package com.naz.taskmanager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a project that can contain multiple taskmanager items
 * Demonstrates inheritance and composition
 */
public class Project extends BaseItem {
    private static final long serialVersionUID = 1L;
    
    private List<TaskmanagerItem> tasks;
    private Date startDate;
    private Date endDate;
    
    /**
     * Constructor for Project
     * @param name Name of the project
     * @param description Description of the project
     */
    public Project(String name, String description) {
        super(name, description);
        this.tasks = new ArrayList<>();
    }
    
    /**
     * Get the start date of the project
     * @return start date
     */
    public Date getStartDate() {
        return startDate != null ? (Date) startDate.clone() : null;
    }
    
    /**
     * Set the start date of the project
     * @param startDate new start date
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate != null ? (Date) startDate.clone() : null;
    }
    
    /**
     * Get the end date of the project
     * @return end date
     */
    public Date getEndDate() {
        return endDate != null ? (Date) endDate.clone() : null;
    }
    
    /**
     * Set the end date of the project
     * @param endDate new end date
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate != null ? (Date) endDate.clone() : null;
    }
    
    /**
     * Get all tasks in this project
     * @return List of tasks
     */
    public List<TaskmanagerItem> getTasks() {
        return new ArrayList<>(tasks); // Return a copy for encapsulation
    }
    
    /**
     * Method overloading for polymorphism
     * Add a task to the project
     * @param task The task to add
     */
    public void addTask(TaskmanagerItem task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        tasks.add(task);
    }
    
    /**
     * Method overloading for polymorphism
     * Create and add a new task to the project
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
     * Method overloading for polymorphism
     * Create and add a new task with deadline and priority
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
     * Remove a task from the project
     * @param task The task to remove
     * @return true if removed successfully
     */
    public boolean removeTask(TaskmanagerItem task) {
        return tasks.remove(task);
    }
    
    /**
     * Calculate the completion percentage of the project
     * @return Completion percentage (0-100)
     */
    public int getCompletionPercentage() {
        if (tasks.isEmpty()) {
            return 0;
        }
        
        int completedTasks = 0;
        for (TaskmanagerItem task : tasks) {
            if (task.isCompleted()) {
                completedTasks++;
            }
        }
        
        return (completedTasks * 100) / tasks.size();
    }
    
    /**
     * Display project details
     * Implements polymorphism through method overriding
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
     * Get the item type
     * @return The item type as a string
     */
    @Override
    public String getItemType() {
        return "Project";
    }
}