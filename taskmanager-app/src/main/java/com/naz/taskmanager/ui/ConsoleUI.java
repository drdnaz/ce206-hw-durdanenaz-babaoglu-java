// ConsoleUI.java
package com.naz.taskmanager.ui;

import com.naz.taskmanager.Taskmanager;
import com.naz.taskmanager.model.*;
import com.naz.taskmanager.service.*;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

/**
 * Console user interface class
 */
public class ConsoleUI {
    private final Scanner scanner;
    private final PrintStream out;
    private final InputHandler inputHandler;
    private final TaskService taskService;
    private final ReminderService reminderService;
    private final Taskmanager taskManager;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    /**
     * Constructor for ConsoleUI
     * @param scanner Scanner for user input
     * @param out PrintStream for output
     * @param taskManager Taskmanager instance
     * @param taskService Task service
     * @param reminderService Reminder service
     */
    public ConsoleUI(Scanner scanner, PrintStream out, Taskmanager taskManager, 
                    TaskService taskService, ReminderService reminderService) {
        this.scanner = scanner;
        this.out = out;
        this.taskManager = taskManager;
        this.inputHandler = new InputHandler(scanner);
        this.taskService = taskService;
        this.reminderService = reminderService;
    }
    
    /**
     * Display all tasks
     */
    public void displayAllTasks() {
        List<TaskmanagerItem> tasks = taskService.getAllTasks();
        
        if (tasks.isEmpty()) {
            out.println("No tasks found.");
            return;
        }
        
        out.println("===== ALL TASKS =====");
        
        for (int i = 0; i < tasks.size(); i++) {
            TaskmanagerItem task = tasks.get(i);
            displayTask(i + 1, task);
            out.println("--------------------");
        }
    }
    
    /**
     * Display task details
     * @param index Task index
     * @param task Task to display
     */
    public void displayTask(int index, TaskmanagerItem task) {
        out.println(index + ". " + task.getName() + " [" + task.getCategory().getName() + "]");
        out.println("   Description: " + task.getDescription());
        
        if (task.getDeadline() != null) {
            out.println("   Deadline: " + dateFormat.format(task.getDeadline()));
            if (task.isOverdue()) {
                out.println("   Status: OVERDUE");
            } else {
                out.println("   Days until deadline: " + task.getDaysUntilDeadline());
            }
        }
        
        out.println("   Priority: " + task.getPriority());
        out.println("   Completed: " + (task.isCompleted() ? "Yes" : "No"));
    }
    
    /**
     * Display tasks by priority
     */
    public void displayTasksByPriority() {
        List<TaskmanagerItem> tasks = taskService.sortTasksByPriority();
        
        if (tasks.isEmpty()) {
            out.println("No tasks found.");
            return;
        }
        
        out.println("--- HIGH PRIORITY TASKS ---");
        displayTasksWithPriority(tasks, Priority.HIGH);
        
        out.println("\n--- MEDIUM PRIORITY TASKS ---");
        displayTasksWithPriority(tasks, Priority.MEDIUM);
        
        out.println("\n--- LOW PRIORITY TASKS ---");
        displayTasksWithPriority(tasks, Priority.LOW);
    }
    
    /**
     * Display tasks with specific priority
     * @param tasks List of all tasks
     * @param priority Priority to filter by
     */
    private void displayTasksWithPriority(List<TaskmanagerItem> tasks, Priority priority) {
        boolean found = false;
        
        for (int i = 0; i < tasks.size(); i++) {
            TaskmanagerItem task = tasks.get(i);
            if (task.getPriority() == priority) {
                found = true;
                displayTask(i + 1, task);
                out.println("--------------------");
            }
        }
        
        if (!found) {
            out.println("No tasks with " + priority + " priority.");
        }
    }
    
    /**
     * Create a new task with user input
     * @return Created task
     */
    public TaskmanagerItem createTaskWithUserInput() {
        taskManager.clearScreen();
        out.println("========================================");
        out.println("               ADD TASK                ");
        out.println("========================================");
        
        out.print("Enter task name: ");
        String name = scanner.nextLine().trim();
        
        out.print("Enter task description: ");
        String description = scanner.nextLine().trim();
        
        // Select category
        Category category = selectCategory();
        
        // Create task
        TaskmanagerItem task = taskService.createTask(name, description, category);
        
        // Set deadline if user wants
        out.print("Do you want to add a deadline for this task? (y/n): ");
        String addDeadline = scanner.nextLine().trim().toLowerCase();
        if (addDeadline.equals("y") || addDeadline.equals("yes")) {
            assignDeadlineToTask(task);
        }
        
        // Set priority
        out.println("Select priority:");
        out.println("1. HIGH");
        out.println("2. MEDIUM");
        out.println("3. LOW");
        
        int priorityChoice;
        while (true) {
            out.print("Enter your choice (1-3): ");
            priorityChoice = taskManager.getInput();
            
            if (priorityChoice >= 1 && priorityChoice <= 3) {
                break;
            }
            out.println("Invalid choice. Please enter a number between 1 and 3.");
        }
        
        switch (priorityChoice) {
            case 1:
                task.setPriority(Priority.HIGH);
                break;
            case 2:
                task.setPriority(Priority.MEDIUM);
                break;
            case 3:
                task.setPriority(Priority.LOW);
                break;
        }
        
        // Update task
        taskService.updateTask(task);
        
        out.println("Task created successfully!");
        return task;
    }
    
    /**
     * Assign deadline to a task
     * @param task Task to assign deadline to
     */
    private void assignDeadlineToTask(TaskmanagerItem task) {
        out.println("Current task: " + task.getName());
        if (task.getDeadline() != null) {
            out.println("Current deadline: " + dateFormat.format(task.getDeadline()));
        }
        
        out.println("Enter new deadline (format: dd/MM/yyyy HH:mm): ");
        String deadlineStr = scanner.nextLine().trim();
        
        try {
            Date deadline = dateFormat.parse(deadlineStr);
            task.setDeadline(deadline);
            
            // Update task
            taskService.updateTask(task);
            
            out.println("Deadline assigned successfully!");
            
            // Ask if user wants to set a reminder
            out.print("Do you want to set a reminder for this task? (y/n): ");
            String setReminder = scanner.nextLine().trim().toLowerCase();
            if (setReminder.equals("y") || setReminder.equals("yes")) {
                createReminderForTask(task);
            }
            
        } catch (Exception e) {
            out.println("Invalid date format. Please use dd/MM/yyyy HH:mm");
        }
    }
    
    /**
     * Select a category from existing ones or create new
     * @return Selected or created category
     */
    private Category selectCategory() {
        List<Category> categories = List.of(
            new Category("Work"),
            new Category("Personal"),
            new Category("Study"),
            new Category("Health"),
            new Category("Other")
        );
        
        out.println("Select category:");
        for (int i = 0; i < categories.size(); i++) {
            out.println((i + 1) + ". " + categories.get(i).getName());
        }
        out.println((categories.size() + 1) + ". Create new category");
        
        int choice;
        while (true) {
            out.print("Enter your choice: ");
            choice = taskManager.getInput();
            
            if (choice >= 1 && choice <= categories.size() + 1) {
                break;
            }
            out.println("Invalid choice. Please try again.");
        }
        
        if (choice <= categories.size()) {
            return categories.get(choice - 1);
        } else {
            out.print("Enter new category name: ");
            String categoryName = scanner.nextLine().trim();
            return new Category(categoryName);
        }
    }
    
    /**
     * Mark task as completed
     */
    public void markTaskAsCompleted() {
        taskManager.clearScreen();
        displayAllTasks();
        List<TaskmanagerItem> tasks = taskService.getAllTasks();
        
        if (tasks.isEmpty()) {
            return;
        }
        
        int taskIndex;
        while (true) {
            out.print("Enter task number to mark as completed (0 to cancel): ");
            taskIndex = taskManager.getInput();
            
            if (taskIndex >= 0 && taskIndex <= tasks.size()) {
                break;
            }
            out.println("Invalid choice. Please enter a number between 0 and " + tasks.size() + ".");
        }
        
        if (taskIndex == 0) {
            out.println("Operation cancelled.");
            return;
        }
        
        TaskmanagerItem task = tasks.get(taskIndex - 1);
        task.setCompleted(true);
        taskService.updateTask(task);
        
        out.println("Task marked as completed: " + task.getName());
    }
    
    /**
     * Create reminder for a task
     */
    public void createReminder() {
        taskManager.clearScreen();
        out.println("========================================");
        out.println("             SET REMINDERS             ");
        out.println("========================================");
        
        List<TaskmanagerItem> tasks = taskService.getAllTasks();
        List<TaskmanagerItem> tasksWithDeadlines = new java.util.ArrayList<>();
        
        for (TaskmanagerItem task : tasks) {
            if (task.getDeadline() != null) {
                tasksWithDeadlines.add(task);
            }
        }
        
        if (tasksWithDeadlines.isEmpty()) {
            out.println("No tasks with deadlines to set reminders for.");
            taskManager.enterToContinue();
            return;
        }
        
        // Display tasks with deadlines
        for (int i = 0; i < tasksWithDeadlines.size(); i++) {
            TaskmanagerItem task = tasksWithDeadlines.get(i);
            out.println((i + 1) + ". " + task.getName());
            out.println("   Deadline: " + dateFormat.format(task.getDeadline()));
            out.println("----------------------------------------");
        }
        
        int choice;
        while (true) {
            out.print("Enter task number to set reminder for (0 to cancel): ");
            choice = taskManager.getInput();
            
            if (choice >= 0 && choice <= tasksWithDeadlines.size()) {
                break;
            }
            out.println("Invalid choice. Please enter a number between 0 and " + tasksWithDeadlines.size() + ".");
        }
        
        if (choice == 0) {
            out.println("Operation cancelled.");
            taskManager.enterToContinue();
            return;
        }
        
        TaskmanagerItem selectedTask = tasksWithDeadlines.get(choice - 1);
        createReminderForTask(selectedTask);
        
        taskManager.enterToContinue();
    }
    
    /**
     * Create reminder for a specific task
     * @param task Task to create reminder for
     */
    private void createReminderForTask(TaskmanagerItem task) {
        Date deadline = task.getDeadline();
        
        if (deadline == null) {
            out.println("This task doesn't have a deadline set. Please set a deadline first.");
            return;
        }
        
        out.println("Task: " + task.getName());
        out.println("Deadline: " + dateFormat.format(deadline));
        
        out.println("\nHow many minutes before the deadline do you want to be reminded?");
        out.println("1. 15 minutes");
        out.println("2. 30 minutes");
        out.println("3. 1 hour");
        out.println("4. 1 day");
        out.println("5. Custom time");
        
        int choice = taskManager.getInput();
        int minutesBefore;
        
        switch (choice) {
            case 1:
                minutesBefore = 15;
                break;
            case 2:
                minutesBefore = 30;
                break;
            case 3:
                minutesBefore = 60;
                break;
            case 4:
                minutesBefore = 24 * 60; // 1 day
                break;
            case 5:
                out.print("Enter custom minutes before deadline: ");
                minutesBefore = taskManager.getInput();
                if (minutesBefore <= 0) {
                    out.println("Invalid input. Setting to 30 minutes by default.");
                    minutesBefore = 30;
                }
                break;
            default:
                out.println("Invalid choice. Setting to 30 minutes by default.");
                minutesBefore = 30;
        }
        
        // Create reminder using service
        try {
            Reminder reminder = reminderService.createReminderBeforeDeadline(task, minutesBefore);
            out.println("Reminder set successfully for " + dateFormat.format(reminder.getReminderTime()));
        } catch (Exception e) {
            out.println("Error setting reminder: " + e.getMessage());
        }
    }
}