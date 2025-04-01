package com.naz.taskmanager.service;

import com.naz.taskmanager.model.Category;
import com.naz.taskmanager.model.Priority;
import com.naz.taskmanager.model.TaskmanagerItem;
import com.naz.taskmanager.repository.TaskRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for task management
 * Implements Strategy Pattern for sorting tasks
 */
public class TaskService {
    private final TaskRepository taskRepository;
    
    /**
     * Constructor for TaskService
     * @param username Username for repository creation
     */
    public TaskService(String username) {
        this.taskRepository = new TaskRepository(username);
    }
    
    /**
     * Create a new task
     * @param name Task name
     * @param description Task description
     * @param category Task category
     * @return The created task
     */
    public TaskmanagerItem createTask(String name, String description, Category category) {
        TaskmanagerItem task = new TaskmanagerItem(name, description, category);
        taskRepository.save(task);
        return task;
    }
    
    /**
     * Get a task by ID
     * @param id Task ID
     * @return Task with matching ID
     */
    public TaskmanagerItem getTask(String id) {
        return taskRepository.getById(id);
    }
    
    /**
     * Get all tasks
     * @return List of all tasks
     */
    public List<TaskmanagerItem> getAllTasks() {
        return taskRepository.getAll();
    }
    
    /**
     * Update an existing task
     * @param task Task to update
     */
    public void updateTask(TaskmanagerItem task) {
        taskRepository.update(task);
    }
    
    /**
     * Delete a task
     * @param id ID of task to delete
     */
    public void deleteTask(String id) {
        taskRepository.delete(id);
    }
    
    /**
     * Mark a task as completed
     * @param id Task ID
     */
    public void markTaskCompleted(String id) {
        TaskmanagerItem task = getTask(id);
        if (task != null) {
            task.setCompleted(true);
            updateTask(task);
        }
    }
    
    /**
     * Get tasks by category
     * @param category Category to filter by
     * @return List of tasks in the category
     */
    public List<TaskmanagerItem> getTasksByCategory(Category category) {
        return getAllTasks().stream()
                .filter(task -> task.getCategory().equals(category))
                .collect(Collectors.toList());
    }
    
    /**
     * Get tasks by priority
     * @param priority Priority to filter by
     * @return List of tasks with the priority
     */
    public List<TaskmanagerItem> getTasksByPriority(Priority priority) {
        return getAllTasks().stream()
                .filter(task -> task.getPriority().equals(priority))
                .collect(Collectors.toList());
    }
    
    /**
     * Get overdue tasks
     * @return List of overdue tasks
     */
    public List<TaskmanagerItem> getOverdueTasks() {
        return getAllTasks().stream()
                .filter(TaskmanagerItem::isOverdue)
                .collect(Collectors.toList());
    }
    
    /**
     * Get tasks with deadlines in a date range
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return List of tasks in the date range
     */
    public List<TaskmanagerItem> getTasksInDateRange(Date startDate, Date endDate) {
        return taskRepository.getTasksInDateRange(startDate, endDate);
    }
    /**
     * Strategy Pattern: Interface for task sorting strategies
     */
    public interface SortStrategy {
        List<TaskmanagerItem> sort(List<TaskmanagerItem> tasks);
    }
    
    /**
     * Strategy Pattern: Sort tasks using the provided strategy
     * @param strategy Sorting strategy to use
     * @return Sorted list of tasks
     */
    public List<TaskmanagerItem> sortTasks(SortStrategy strategy) {
        return strategy.sort(getAllTasks());
    }
    
    /**
     * Strategy Pattern: Sort tasks by deadline
     * @return Sorted list of tasks
     */
    public List<TaskmanagerItem> sortTasksByDeadline() {
        return sortTasks(new DeadlineSortStrategy());
    }
    
    /**
     * Strategy Pattern: Sort tasks by priority
     * @return Sorted list of tasks
     */
    public List<TaskmanagerItem> sortTasksByPriority() {
        return sortTasks(new PrioritySortStrategy());
    }
    
    /**
     * Strategy Pattern: Implementation for deadline sorting
     */
    private class DeadlineSortStrategy implements SortStrategy {
        @Override
        public List<TaskmanagerItem> sort(List<TaskmanagerItem> tasks) {
            List<TaskmanagerItem> result = new ArrayList<>(tasks);
            result.sort((t1, t2) -> {
                Date d1 = t1.getDeadline();
                Date d2 = t2.getDeadline();
                
                if (d1 == null && d2 == null) {
                    return 0;
                } else if (d1 == null) {
                    return 1; // Tasks without deadlines come last
                } else if (d2 == null) {
                    return -1;
                } else {
                    return d1.compareTo(d2);
                }
            });
            return result;
        }
    }
    
    /**
     * Strategy Pattern: Implementation for priority sorting
     */
    private class PrioritySortStrategy implements SortStrategy {
        @Override
        public List<TaskmanagerItem> sort(List<TaskmanagerItem> tasks) {
            List<TaskmanagerItem> result = new ArrayList<>(tasks);
            result.sort((t1, t2) -> {
                // Sort HIGH to LOW: HIGH(0), MEDIUM(1), LOW(2)
                return t1.getPriority().compareTo(t2.getPriority());
            });
            return result;
        }
    }
}