import com.naz.taskmanager.model.Category;
import com.naz.taskmanager.model.TaskmanagerItem;
import com.naz.taskmanager.repository.DatabaseConnection;
import com.naz.taskmanager.repository.TaskRepository;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Date;
import java.util.UUID;

public class TaskRepositoryTest {
    private TaskRepository repository;
    private static final String TEST_USERNAME = "test_user_" + System.currentTimeMillis();
    
    @Before
    public void setUp() {
        // Her test için yeni bir repository oluştur
        repository = new TaskRepository(TEST_USERNAME);
        // Temiz başlangıç için mevcut test görevleri temizlenebilir
        cleanupTestTasks();
    }
    
    @After
    public void tearDown() {
        // Test sonrası temizlik
        cleanupTestTasks();
    }
    
    private void cleanupTestTasks() {
        List<TaskmanagerItem> tasks = repository.getAll();
        for (TaskmanagerItem task : tasks) {
            if (task.getTitle().startsWith("Test Task")) {
                repository.delete(task.getId());
            }
        }
    }
    
    @Test
    public void testSaveAndGetById() {
        // Test görevi oluştur
        TaskmanagerItem task = createTestTask("Test Task Save");
        
        // Görevi kaydet
        repository.save(task);
        
        // ID'ye göre görevi al ve kontrol et
        TaskmanagerItem retrievedTask = repository.getById(task.getId());
        assertNotNull("Kaydedilen görev null olmamalı", retrievedTask);
        assertEquals("Görev başlıkları eşleşmeli", task.getTitle(), retrievedTask.getTitle());
        assertEquals("Görev açıklamaları eşleşmeli", task.getDescription(), retrievedTask.getDescription());
    }
    
    @Test
    public void testGetAll() {
        // Başlangıçta kaç görev var
        int initialCount = repository.getAll().size();
        
        // Test görevleri oluştur ve kaydet
        TaskmanagerItem task1 = createTestTask("Test Task GetAll 1");
        TaskmanagerItem task2 = createTestTask("Test Task GetAll 2");
        repository.save(task1);
        repository.save(task2);
        
        // Tüm görevleri al ve kontrol et
        List<TaskmanagerItem> allTasks = repository.getAll();
        assertNotNull("Görev listesi null olmamalı", allTasks);
        assertEquals("Görev sayısı beklendiği gibi artmalı", initialCount + 2, allTasks.size());
        
        // Görevlerin listede olduğunu kontrol et
        boolean found1 = false, found2 = false;
        for (TaskmanagerItem task : allTasks) {
            if (task.getId().equals(task1.getId())) found1 = true;
            if (task.getId().equals(task2.getId())) found2 = true;
        }
        assertTrue("İlk görev listede olmalı", found1);
        assertTrue("İkinci görev listede olmalı", found2);
    }
    
    @Test
    public void testUpdate() {
        // Test görevi oluştur ve kaydet
        TaskmanagerItem task = createTestTask("Test Task Update");
        repository.save(task);
        
        // Görevi güncelle
        task.setTitle("Updated Test Task");
        task.setDescription("Updated Description");
        repository.update(task);
        
        // Güncellenmiş görevi al ve kontrol et
        TaskmanagerItem updatedTask = repository.getById(task.getId());
        assertNotNull("Güncellenmiş görev null olmamalı", updatedTask);
        assertEquals("Görev başlığı güncellenmiş olmalı", "Updated Test Task", updatedTask.getTitle());
        assertEquals("Görev açıklaması güncellenmiş olmalı", "Updated Description", updatedTask.getDescription());
    }
    
    @Test
    public void testDelete() {
        // Test görevi oluştur ve kaydet
        TaskmanagerItem task = createTestTask("Test Task Delete");
        repository.save(task);
        
        // Görevin var olduğunu kontrol et
        assertNotNull("Kaydedilen görev getById ile alınabilmeli", repository.getById(task.getId()));
        
        // Görevi sil
        repository.delete(task.getId());
        
        // Görevin silindiğini kontrol et
        assertNull("Silinen görev null olmalı", repository.getById(task.getId()));
    }
    
    private TaskmanagerItem createTestTask(String title) {
        Category category = new Category("Test Category");
        TaskmanagerItem task = new TaskmanagerItem(title, "Test Description", category);
        task.setDeadline(new Date()); // Şu anki tarih
        task.setPriority("Medium");
        task.setStatus("Not Started");
        // Benzersiz bir ID atayalım
        task.setId(UUID.randomUUID().toString());
        return task;
    }
} 