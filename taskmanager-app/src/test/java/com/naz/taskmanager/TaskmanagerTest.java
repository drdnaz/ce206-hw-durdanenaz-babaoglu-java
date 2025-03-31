// TaskmanagerTest.java (tamamlanmış)
package com.naz.taskmanager;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.naz.taskmanager.model.Category;
import com.naz.taskmanager.model.Priority;
import com.naz.taskmanager.model.TaskmanagerItem;
import com.naz.taskmanager.service.TaskService;

public class TaskmanagerTest {

    private Taskmanager taskManager;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    private Scanner scanner;
    private TaskService taskService;

    @Before
    public void setUp() throws Exception {
        // Redirect standard output to capture it
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        // Setup input for testing
        String input = "1\ntest\ntest123\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        scanner = new Scanner(System.in);
        taskManager = Taskmanager.getInstance(scanner, System.out);
        
        // Initialize test services
        taskService = new TaskService("test_user");
    }

    @After
    public void tearDown() throws Exception {
        // Restore original standard output
        System.setOut(originalOut);
        
        // Close resources
        scanner.close();
    }

    @Test
    public void testSingletonPattern() {
        // Test that getInstance returns the same instance
        Taskmanager instance1 = Taskmanager.getInstance(scanner, System.out);
        Taskmanager instance2 = Taskmanager.getInstance(scanner, System.out);
        
        assertSame("getInstance should return the same instance", instance1, instance2);
    }
    
    @Test
    public void testClearScreen() {
        taskManager.clearScreen();
        // Clear screen should output something (ANSI sequence or newlines)
        assertTrue(outContent.toString().length() > 0);
    }
    
    @Test
    public void testGetInputValid() {
        // Setup test input
        String input = "42\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);
        
        // Replace taskManager's scanner
        Taskmanager testManager = new Taskmanager(scanner, System.out);
        
        // Test valid input
        assertEquals(42, testManager.getInput());
        
        scanner.close();
    }
    
    @Test
    public void testGetInputInvalid() {
        // Setup test input
        String input = "not_a_number\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);
        
        // Replace taskManager's scanner
        Taskmanager testManager = new Taskmanager(scanner, System.out);
        
        // Test invalid input (should return -2)
        assertEquals(-2, testManager.getInput());
        
        scanner.close();
    }
    
    @Test
    public void testHandleInputError() {
        taskManager.handleInputError();
        assertTrue(outContent.toString().contains("Invalid input"));
    }
    
    @Test
    public void testTaskManagerInitialization() {
        assertNotNull("taskManager should not be null", taskManager);
    }
    
    @Test
    public void testTaskCreation() {
        // Create a task through the task service
        Category category = new Category("Test Category");
        TaskmanagerItem task = taskService.createTask("Test Task", "Test Description", category);
        
        // Verify task properties
        assertEquals("Test Task", task.getName());
        assertEquals("Test Description", task.getDescription());
        assertEquals(category, task.getCategory());
        assertEquals(Priority.MEDIUM, task.getPriority()); // Default priority
        assertFalse(task.isCompleted());
    }
    
    @Test
    public void testTaskCompletion() {
        // Create a task through the task service
        Category category = new Category("Test Category");
        TaskmanagerItem task = taskService.createTask("Test Task", "Test Description", category);
        String taskId = task.getId();
        
        // Mark the task as completed
        taskService.markTaskCompleted(taskId);
        
        // Fetch the task and verify it's completed
        TaskmanagerItem completedTask = taskService.getTask(taskId);
        assertTrue(completedTask.isCompleted());
    }
    
    @Test
    public void testTaskDeadline() {
        // Create a task through the task service
        Category category = new Category("Test Category");
        TaskmanagerItem task = taskService.createTask("Test Task", "Test Description", category);
        
        // Set deadline
        Date deadline = new Date(System.currentTimeMillis() + 86400000); // 1 day in future
        task.setDeadline(deadline);
        taskService.updateTask(task);
        
        // Fetch the task and verify deadline
        TaskmanagerItem updatedTask = taskService.getTask(task.getId());
        assertNotNull(updatedTask.getDeadline());
        assertEquals(deadline.getTime() / 1000, updatedTask.getDeadline().getTime() / 1000); // Compare seconds
    }
    
    @Test
    public void testTaskOverdue() {
        // Create a task through the task service
        Category category = new Category("Test Category");
        TaskmanagerItem task = taskService.createTask("Test Task", "Test Description", category);
        
        // Set deadline in the past
        Date pastDeadline = new Date(System.currentTimeMillis() - 86400000); // 1 day ago
        task.setDeadline(pastDeadline);
        taskService.updateTask(task);
        
        // Fetch the task and verify it's overdue
        TaskmanagerItem updatedTask = taskService.getTask(task.getId());
        assertTrue(updatedTask.isOverdue());
    }
    
    @Test
    public void testTaskSorting() {
        // Create test tasks with different priorities
        Category category = new Category("Test Category");
        TaskmanagerItem highTask = taskService.createTask("High Priority", "Description", category);
        highTask.setPriority(Priority.HIGH);
        taskService.updateTask(highTask);
        
        TaskmanagerItem mediumTask = taskService.createTask("Medium Priority", "Description", category);
        mediumTask.setPriority(Priority.MEDIUM);
        taskService.updateTask(mediumTask);
        
        TaskmanagerItem lowTask = taskService.createTask("Low Priority", "Description", category);
        lowTask.setPriority(Priority.LOW);
        taskService.updateTask(lowTask);
        
        // Sort by priority and verify order
        List<TaskmanagerItem> sortedTasks = taskService.sortTasksByPriority();
        assertEquals(3, sortedTasks.size());
        assertEquals(Priority.HIGH, sortedTasks.get(0).getPriority());
        assertEquals(Priority.MEDIUM, sortedTasks.get(1).getPriority());
        assertEquals(Priority.LOW, sortedTasks.get(2).getPriority());
    }
    
    @Test
    public void testTaskFiltering() {
        // Create test tasks with different categories
        Category workCategory = new Category("Work");
        Category personalCategory = new Category("Personal");
        
        taskService.createTask("Work Task", "Description", workCategory);
        taskService.createTask("Personal Task", "Description", personalCategory);
        
        // Filter by category and verify counts
        List<TaskmanagerItem> workTasks = taskService.getTasksByCategory(workCategory);
        List<TaskmanagerItem> personalTasks = taskService.getTasksByCategory(personalCategory);
        
        assertEquals(1, workTasks.size());
        assertEquals(1, personalTasks.size());
        assertEquals("Work Task", workTasks.get(0).getName());
        assertEquals("Personal Task", personalTasks.get(0).getName());
    }
}