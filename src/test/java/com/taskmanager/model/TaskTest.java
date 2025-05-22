import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class TaskTest {
    @Before
    public void setUp() {
        Task.getAllTasks().clear();
    }

    @Test
    public void testTaskConstructorAndGetters() {
        Date now = new Date();
        Task t = new Task("Başlık", "Açıklama", now, "Kategori", "Yüksek");
        assertEquals("Başlık", t.getTitle());
        assertEquals("Açıklama", t.getDescription());
        assertEquals(now, t.getDueDate());
        assertEquals("Kategori", t.getCategory());
        assertEquals("Yüksek", t.getPriority());
        assertEquals("Not Started", t.getStatus());
    }

    @Test
    public void testSetters() {
        Task t = new Task("a", "b", new Date(), "c", "d");
        t.setTitle("x");
        t.setDescription("y");
        Date d = new Date();
        t.setDueDate(d);
        t.setCategory("z");
        t.setPriority("orta");
        t.setStatus("Completed");
        assertEquals("x", t.getTitle());
        assertEquals("y", t.getDescription());
        assertEquals(d, t.getDueDate());
        assertEquals("z", t.getCategory());
        assertEquals("orta", t.getPriority());
        assertEquals("Completed", t.getStatus());
    }

    @Test
    public void testAddAndFindTask() {
        Task t = new Task("a", "b", new Date(), "c", "d");
        Task.addTask(t);
        assertTrue(Task.getAllTasks().contains(t));
        Task found = Task.findTaskById(t.getId());
        assertEquals(t, found);
    }

    @Test
    public void testDeleteTask() {
        Task t = new Task("a", "b", new Date(), "c", "d");
        Task.addTask(t);
        boolean deleted = Task.deleteTask(t.getId());
        assertTrue(deleted);
        assertNull(Task.findTaskById(t.getId()));
        assertFalse(Task.deleteTask(9999));
    }

    @Test
    public void testUpdateTask() {
        Task t = new Task("a", "b", new Date(), "c", "d");
        Task.addTask(t);
        Date newDate = new Date();
        boolean updated = Task.updateTask(t.getId(), "yeni", "desc", newDate, "cat", "prio", "Completed");
        assertTrue(updated);
        Task updatedTask = Task.findTaskById(t.getId());
        assertEquals("yeni", updatedTask.getTitle());
        assertEquals("desc", updatedTask.getDescription());
        assertEquals(newDate, updatedTask.getDueDate());
        assertEquals("cat", updatedTask.getCategory());
        assertEquals("prio", updatedTask.getPriority());
        assertEquals("Completed", updatedTask.getStatus());
        assertFalse(Task.updateTask(9999, "a", "b", newDate, "c", "d", "e"));
    }

    @Test
    public void testStatusOptions() {
        String[] options = Task.getStatusOptions();
        assertArrayEquals(new String[]{"Not Started", "In Progress", "Completed", "Deferred", "Cancelled"}, options);
    }
    
    @Test
    public void testTaskIdGeneration() {
        // Test ID oluşturma mekanizmasını
        Task t1 = new Task("Task 1", "Description 1", new Date(), "Category", "High");
        Task t2 = new Task("Task 2", "Description 2", new Date(), "Category", "Medium");
        
        // İkinci task ID'si ilk task ID'sinden büyük olmalı
        assertTrue("Task ID'ler artan sırada olmalıdır", t2.getId() > t1.getId());
    }
    
    @Test
    public void testFindTaskByIdWithNonExistentId() {
        // Olmayan bir ID ile görev arama
        Task.getAllTasks().clear(); // Önceki görevleri temizle
        
        // Olmayan ID ile arama yap
        Task result = Task.findTaskById(99999);
        
        // Sonuç null olmalı
        assertNull("Olmayan ID ile arama yapıldığında null dönmelidir", result);
    }
    
    @Test
    public void testGetAllTasksWithEmptyList() {
        // Tüm görevleri temizle
        Task.getAllTasks().clear();
        
        // Tüm görevleri al
        List<Task> tasks = Task.getAllTasks();
        
        // Liste boş olmalı
        assertNotNull("getAllTasks null dönmemelidir", tasks);
        assertTrue("getAllTasks boş bir liste dönmelidir", tasks.isEmpty());
    }
    
    @Test
    public void testGetAllTasksWithMultipleTasks() {
        // Tüm görevleri temizle
        Task.getAllTasks().clear();
        
        // 3 görev ekle
        Task t1 = new Task("Task 1", "Description 1", new Date(), "Category 1", "High");
        Task t2 = new Task("Task 2", "Description 2", new Date(), "Category 2", "Medium");
        Task t3 = new Task("Task 3", "Description 3", new Date(), "Category 3", "Low");
        
        Task.addTask(t1);
        Task.addTask(t2);
        Task.addTask(t3);
        
        // Tüm görevleri al
        List<Task> tasks = Task.getAllTasks();
        
        // 3 görev olmalı
        assertEquals("getAllTasks 3 görev içermelidir", 3, tasks.size());
        
        // Her görev listede olmalı
        assertTrue("Liste task1 içermelidir", tasks.contains(t1));
        assertTrue("Liste task2 içermelidir", tasks.contains(t2));
        assertTrue("Liste task3 içermelidir", tasks.contains(t3));
    }
    
    @Test
    public void testDeleteTaskWithEmptyList() {
        // Tüm görevleri temizle
        Task.getAllTasks().clear();
        
        // Olmayan bir görevi silmeye çalış
        boolean result = Task.deleteTask(1);
        
        // Sonuç false olmalı
        assertFalse("Boş listeden görev silme false dönmelidir", result);
    }
    
    @Test
    public void testUpdateTaskWithNullValues() {
        // Bir görev oluştur ve ekle
        Task task = new Task("Original", "Original Desc", new Date(), "Original Cat", "Original Pri");
        Task.addTask(task);
        int taskId = task.getId();
        
        // Görevi null değerlerle güncelle
        boolean result = Task.updateTask(taskId, null, null, null, null, null, null);
        
        // Güncelleme başarılı olmalı
        assertTrue("Null değerlerle güncelleme başarılı olmalıdır", result);
        
        // Güncellenmiş görevi al
        Task updatedTask = Task.findTaskById(taskId);
        
        // Null değerler atanmış olmalı
        assertNull("Title null olmalıdır", updatedTask.getTitle());
        assertNull("Description null olmalıdır", updatedTask.getDescription());
        assertNull("DueDate null olmalıdır", updatedTask.getDueDate());
        assertNull("Category null olmalıdır", updatedTask.getCategory());
        assertNull("Priority null olmalıdır", updatedTask.getPriority());
        assertNull("Status null olmalıdır", updatedTask.getStatus());
    }
} 