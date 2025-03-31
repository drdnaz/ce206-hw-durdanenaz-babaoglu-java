package com.naz.taskmanager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

/**
 * Represents a single taskmanager item in the application
 * Implements inheritance, polymorphism, and multiple interfaces
 */
public class TaskmanagerItem extends BaseItem implements Schedulable, Categorizable {
    private static final long serialVersionUID = 1L;
    
    private Category category;
    private Date deadline;
    private Priority priority = Priority.MEDIUM;
    private List<Reminder> reminders;

    /**
     * Constructor for TaskmanagerItem
     * @param name Name of the taskmanager item
     * @param description Description of the taskmanager item
     * @param category Category of the taskmanager item
     */
    public TaskmanagerItem(String name, String description, Category category) {
        super(name, description);
        this.category = category;
        this.reminders = new ArrayList<>();
    }

    /**
     * Get the category of the taskmanager item
     * @return category
     */
    @Override
    public Category getCategory() {
        return category;
    }

    /**
     * Set the category of the taskmanager item
     * @param category new category
     */
    @Override
    public void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        this.category = category;
    }

    /**
     * Get the deadline of the taskmanager item
     * @return deadline
     */
    @Override
    public Date getDeadline() {
        return deadline != null ? (Date) deadline.clone() : null;
    }

    /**
     * Set the deadline of the taskmanager item
     * @param deadline new deadline
     */
    @Override
    public void setDeadline(Date deadline) {
        this.deadline = deadline != null ? (Date) deadline.clone() : null;
    }

    /**
     * Get the priority of the taskmanager item
     * @return priority
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Set the priority of the taskmanager item
     * @param priority new priority
     */
    public void setPriority(Priority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }
        this.priority = priority;
    }
    
    /**
     * Add a reminder to this taskmanager item
     * @param reminder The reminder to add
     */
    public void addReminder(Reminder reminder) {
        if (reminder == null) {
            throw new IllegalArgumentException("Reminder cannot be null");
        }
        reminders.add(reminder);
    }
    
    /**
     * Get all reminders for this taskmanager item
     * @return List of reminders
     */
    public List<Reminder> getReminders() {
        return new ArrayList<>(reminders); // Return a copy for encapsulation
    }
    
    /**
     * Remove a reminder from this taskmanager item
     * @param reminder The reminder to remove
     * @return true if removed successfully
     */
    public boolean removeReminder(Reminder reminder) {
        return reminders.remove(reminder);
    }

    /**
     * Check if the taskmanager item is overdue
     * @return true if overdue
     */
    @Override
    public boolean isOverdue() {
        if (deadline == null || isCompleted) {
            return false;
        }
        return deadline.before(new Date());
    }
    
    /**
     * Get the number of days until the deadline
     * @return days until deadline, or -1 if no deadline
     */
    @Override
    public int getDaysUntilDeadline() {
        if (deadline == null) {
            return -1;
        }
        
        long diffTime = deadline.getTime() - new Date().getTime();
        return (int) (diffTime / (24 * 60 * 60 * 1000));
    }
    
    /**
     * Display the taskmanager item details
     * Implements polymorphism through method overriding
     */
    @Override
    public void display() {
        System.out.println("Task: " + name);
        System.out.println("Description: " + description);
        System.out.println("Category: " + category.getName());
        System.out.println("Priority: " + priority);
        
        if (deadline != null) {
            System.out.println("Deadline: " + deadline);
            if (isOverdue()) {
                System.out.println("Status: OVERDUE");
            } else {
                System.out.println("Days until deadline: " + getDaysUntilDeadline());
            }
        }
        
        System.out.println("Status: " + (isCompleted ? "Completed" : "Pending"));
    }
    
    /**
     * Get the item type
     * @return The item type as a string
     */
    @Override
    public String getItemType() {
        return "Task";
    }
    /**
     * Set the ID of the taskmanager item
     * @param id new ID
     */
    public void setId(String id) {
        this.id = id;
    }
    
}