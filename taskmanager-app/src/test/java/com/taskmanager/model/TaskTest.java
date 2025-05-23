package com.taskmanager.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

public class TaskTest {
    
    private Task testTask;
    private Date testDate;
    
    @Before
    public void setUp() {
        // Test için bir tarih oluştur
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7); // 7 gün sonrası
        testDate = calendar.getTime();
        
        // Test görevi oluştur
        testTask = new Task(
            "Test Task",
            "Test Description",
            testDate,
            "Work",
            "High"
        );
    }
    
    @Test
    public void testConstructorAndGetters() {
        assertEquals("Test Task", testTask.getTitle());
        assertEquals("Test Description", testTask.getDescription());
        assertEquals(testDate, testTask.getDueDate());
        assertEquals("Work", testTask.getCategory());
        assertEquals("High", testTask.getPriority());
        assertEquals("Not Started", testTask.getStatus());
        assertTrue(testTask.getId() > 0);
    }
    
    @Test
    public void testSetters() {
        Date newDate = new Date();
        testTask.setTitle("New Title");
        testTask.setDescription("New Description");
        testTask.setDueDate(newDate);
        testTask.setCategory("Personal");
        testTask.setPriority("Medium");
        testTask.setStatus("In Progress");
        
        assertEquals("New Title", testTask.getTitle());
        assertEquals("New Description", testTask.getDescription());
        assertEquals(newDate, testTask.getDueDate());
        assertEquals("Personal", testTask.getCategory());
        assertEquals("Medium", testTask.getPriority());
        assertEquals("In Progress", testTask.getStatus());
    }
    
    @Test
    public void testGetAllTasks() {
        List<Task> tasks = Task.getAllTasks();
        assertNotNull(tasks);
    }
    
    @Test
    public void testAddTask() {
        int initialSize = Task.getAllTasks().size();
        Task.addTask(testTask);
        
        assertEquals(initialSize + 1, Task.getAllTasks().size());
        assertTrue(Task.getAllTasks().contains(testTask));
    }
    
    @Test
    public void testDeleteTask() {
        Task.addTask(testTask);
        int taskId = testTask.getId();
        
        assertTrue(Task.deleteTask(taskId));
        assertNull(Task.findTaskById(taskId));
    }
    
    @Test
    public void testDeleteNonExistentTask() {
        assertFalse(Task.deleteTask(999)); // Var olmayan bir ID
    }
    
    @Test
    public void testFindTaskById() {
        Task.addTask(testTask);
        Task found = Task.findTaskById(testTask.getId());
        
        assertNotNull(found);
        assertEquals(testTask.getId(), found.getId());
        assertEquals(testTask.getTitle(), found.getTitle());
    }
    
    @Test
    public void testFindNonExistentTaskById() {
        assertNull(Task.findTaskById(999)); // Var olmayan bir ID
    }
    
    @Test
    public void testUpdateTask() {
        Task.addTask(testTask);
        Date newDate = new Date();
        
        boolean updated = Task.updateTask(
            testTask.getId(),
            "Updated Title",
            "Updated Description",
            newDate,
            "Study",
            "Low",
            "Completed"
        );
        
        assertTrue(updated);
        Task found = Task.findTaskById(testTask.getId());
        assertEquals("Updated Title", found.getTitle());
        assertEquals("Updated Description", found.getDescription());
        assertEquals(newDate, found.getDueDate());
        assertEquals("Study", found.getCategory());
        assertEquals("Low", found.getPriority());
        assertEquals("Completed", found.getStatus());
    }
    
    @Test
    public void testUpdateNonExistentTask() {
        Date newDate = new Date();
        boolean updated = Task.updateTask(
            999, // Var olmayan bir ID
            "Updated Title",
            "Updated Description",
            newDate,
            "Study",
            "Low",
            "Completed"
        );
        
        assertFalse(updated);
    }
    
    @Test
    public void testGetStatusOptions() {
        String[] statusOptions = Task.getStatusOptions();
        assertNotNull(statusOptions);
        assertEquals(5, statusOptions.length);
        assertArrayEquals(
            new String[] {"Not Started", "In Progress", "Completed", "Deferred", "Cancelled"},
            statusOptions
        );
    }
    
    @Test
    public void testMultipleTasks() {
        // Birden fazla görev oluştur ve test et
        Task task1 = new Task("Task 1", "Description 1", testDate, "Work", "High");
        Task task2 = new Task("Task 2", "Description 2", testDate, "Personal", "Medium");
        Task task3 = new Task("Task 3", "Description 3", testDate, "Study", "Low");
        
        Task.addTask(task1);
        Task.addTask(task2);
        Task.addTask(task3);
        
        List<Task> tasks = Task.getAllTasks();
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
        assertTrue(tasks.contains(task3));
        
        // Görevleri güncelle
        assertTrue(Task.updateTask(task1.getId(), "Updated Task 1", "New Desc 1", testDate, "Work", "High", "In Progress"));
        assertTrue(Task.updateTask(task2.getId(), "Updated Task 2", "New Desc 2", testDate, "Personal", "Medium", "Completed"));
        
        // Görevleri sil
        assertTrue(Task.deleteTask(task1.getId()));
        assertTrue(Task.deleteTask(task2.getId()));
        assertTrue(Task.deleteTask(task3.getId()));
        
        // Silinen görevlerin bulunamadığını kontrol et
        assertNull(Task.findTaskById(task1.getId()));
        assertNull(Task.findTaskById(task2.getId()));
        assertNull(Task.findTaskById(task3.getId()));
    }
} 