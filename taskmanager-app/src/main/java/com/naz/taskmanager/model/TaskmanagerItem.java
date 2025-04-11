package com.naz.taskmanager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

/**
 * @file TaskmanagerItem.java
 * @brief Core class for managing tasks
 *
 * This class defines the main structure for task objects within the Taskmanager application. 
 * It contains fields such as category, priority, deadline, and reminders. It also implements
 * Schedulable and Categorizable interfaces and inherits from BaseItem.
 *
 * @author
 * Durdane Naz Babaoğlu
 *
 * @version 1.0
 * @date 2025-04-11
 */
public class TaskmanagerItem extends BaseItem implements Schedulable, Categorizable {
    private static final long serialVersionUID = 1L;

    /** @brief Task category */
    private Category category;

    /** @brief Task deadline */
    private Date deadline;

    /** @brief Task priority */
    private Priority priority = Priority.MEDIUM;

    /** @brief List of reminders */
    private List<Reminder> reminders;

    /**
     * @brief Constructor for TaskmanagerItem
     * @param name Task name
     * @param description Task description
     * @param category Task category
     */
    public TaskmanagerItem(String name, String description, Category category) {
        super(name, description);
        this.category = category;
        this.reminders = new ArrayList<>();
    }

    /** @return Returns the task category */
    @Override
    public Category getCategory() {
        return category;
    }

    /**
     * @brief Sets the category of the task
     * @param category New category
     */
    @Override
    public void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        this.category = category;
    }

    /** @return Returns the deadline of the task */
    @Override
    public Date getDeadline() {
        return deadline != null ? (Date) deadline.clone() : null;
    }

    /**
     * @brief Sets the deadline of the task
     * @param deadline New deadline
     */
    @Override
    public void setDeadline(Date deadline) {
        this.deadline = deadline != null ? (Date) deadline.clone() : null;
    }

    /** @return Returns the priority of the task */
    public Priority getPriority() {
        return priority;
    }

    /**
     * @brief Sets the priority of the task
     * @param priority New priority
     */
    public void setPriority(Priority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }
        this.priority = priority;
    }

    /**
     * @brief Adds a reminder to the task
     * @param reminder Reminder object
     */
    public void addReminder(Reminder reminder) {
        if (reminder == null) {
            throw new IllegalArgumentException("Reminder cannot be null");
        }
        reminders.add(reminder);
    }

    /** @return Returns a copy of the reminders list */
    public List<Reminder> getReminders() {
        return new ArrayList<>(reminders);
    }

    /**
     * @brief Removes a reminder from the task
     * @param reminder Reminder to be removed
     * @return true if removed successfully
     */
    public boolean removeReminder(Reminder reminder) {
        return reminders.remove(reminder);
    }

    /**
     * @brief Checks whether the task is overdue
     * @return true if deadline has passed, false otherwise
     */
    @Override
    public boolean isOverdue() {
        if (deadline == null || isCompleted) {
            return false;
        }
        return deadline.before(new Date());
    }

    /**
     * @brief Calculates days left until deadline
     * @return Number of days remaining or -1 if no deadline is set
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
     * @brief Displays task information to console
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

    /** @return Returns the item type */
    @Override
    public String getItemType() {
        return "Task";
    }

    /**
     * @brief Sets the ID for the task
     * @param id New ID
     */
    public void setId(String id) {
        this.id = id;
    }
}
