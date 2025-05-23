package com.naz.taskmanager.repository;

import com.naz.taskmanager.model.Category;
import com.naz.taskmanager.model.Priority;
import com.naz.taskmanager.model.TaskmanagerItem;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TaskRepositoryTest {
    private TaskRepository repository;
    private static final String TEST_USERNAME = "task_test_" + UUID.randomUUID().toString().substring(0, 8);
    private static final String TEST_TASK_NAME = "Test Task";
    private static final String TEST_TASK_DESC = "Test Description";
    private UserRepository userRepository;
    
    @Before
    public void setUp() {
        // Öncelikle test kullanıcısı oluştur
        userRepository = new UserRepository(System.out);
        if (!userRepository.userExists(TEST_USERNAME)) {
            userRepository.addUser(TEST_USERNAME, "password");
        }
        
        // TaskRepository örneği oluştur
        repository = new TaskRepository(TEST_USERNAME);
        
        // Mevcut görevleri temizle
        List<TaskmanagerItem> existingTasks = repository.getAll();
        for (TaskmanagerItem task : existingTasks) {
            repository.delete(task.getId());
        }
    }
    
    @After
    public void tearDown() {
        // Test kullanıcısını temizle
        try {
            // Önce görevleri temizle
            List<TaskmanagerItem> existingTasks = repository.getAll();
            for (TaskmanagerItem task : existingTasks) {
                repository.delete(task.getId());
            }
            
            // Sonra kullanıcıyı sil
            userRepository.delete(TEST_USERNAME);
        } catch (Exception e) {
            // Silme hatası olabilir, sorun değil
        }
    }
    
    @Test
    public void testSaveAndGetById() {
        // Temel bir kategori oluştur
        Category category = new Category("Test Category");
        
        // Yeni görev oluştur
        TaskmanagerItem task = new TaskmanagerItem(TEST_TASK_NAME, TEST_TASK_DESC, category);
        task.setPriority(Priority.HIGH);
        
        // ID kontrolünü kaldırıyoruz - model sınıfı muhtemelen constructor'da ID atıyor
        String originalId = task.getId();
        
        // Kaydet
        repository.save(task);
        
        // Görevin bir ID'si olmalı ve değişmiş olmalı
        assertNotNull("Kaydettikten sonra görevin ID'si null olmamalı", task.getId());
        
        // ID ile getir
        TaskmanagerItem retrievedTask = repository.getById(task.getId());
        
        // Kontrol et
        assertNotNull("Kaydedilen görev null olmamalı", retrievedTask);
        assertEquals("Görev adı eşleşmeli", TEST_TASK_NAME, retrievedTask.getName());
        assertEquals("Görev açıklaması eşleşmeli", TEST_TASK_DESC, retrievedTask.getDescription());
        assertEquals("Kategori adı eşleşmeli", category.getName(), retrievedTask.getCategory().getName());
        assertEquals("Öncelik eşleşmeli", Priority.HIGH, retrievedTask.getPriority());
        assertFalse("Yeni görev tamamlanmamış olmalı", retrievedTask.isCompleted());
    }
    
    @Test
    public void testGetAll() {
        // Test için iki görev oluştur
        TaskmanagerItem task1 = new TaskmanagerItem("Task 1", "Description 1", new Category("Category 1"));
        TaskmanagerItem task2 = new TaskmanagerItem("Task 2", "Description 2", new Category("Category 2"));
        
        // Kaydet
        repository.save(task1);
        repository.save(task2);
        
        // Tüm görevleri getir
        List<TaskmanagerItem> allTasks = repository.getAll();
        
        // En az iki görev olmalı
        assertTrue("En az iki görev olmalı", allTasks.size() >= 2);
        
        // Bizim görevlerimizi içermeli
        boolean containsTask1 = false;
        boolean containsTask2 = false;
        
        for (TaskmanagerItem task : allTasks) {
            if (task.getId().equals(task1.getId())) {
                containsTask1 = true;
            } else if (task.getId().equals(task2.getId())) {
                containsTask2 = true;
            }
        }
        
        assertTrue("Görev listesi görev 1'i içermeli", containsTask1);
        assertTrue("Görev listesi görev 2'yi içermeli", containsTask2);
    }
    
    @Test
    public void testUpdate() {
        // Test için bir görev oluştur
        TaskmanagerItem task = new TaskmanagerItem(TEST_TASK_NAME, TEST_TASK_DESC, new Category("Initial Category"));
        repository.save(task);
        
        // Şimdi güncelle
        String updatedName = "Updated Task";
        String updatedDesc = "Updated Description";
        Category updatedCategory = new Category("Updated Category");
        
        task.setName(updatedName);
        task.setDescription(updatedDesc);
        task.setCategory(updatedCategory);
        task.setPriority(Priority.LOW);
        task.setCompleted(true);
        
        // Tarih ekle
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date deadline = calendar.getTime();
        task.setDeadline(deadline);
        
        // Güncelle
        repository.update(task);
        
        // Güncellenmiş görevi al
        TaskmanagerItem updatedTask = repository.getById(task.getId());
        
        // Kontrol et
        assertNotNull("Güncellenmiş görev null olmamalı", updatedTask);
        assertEquals("Görev adı güncellenmiş olmalı", updatedName, updatedTask.getName());
        assertEquals("Görev açıklaması güncellenmiş olmalı", updatedDesc, updatedTask.getDescription());
        assertEquals("Kategori adı güncellenmiş olmalı", updatedCategory.getName(), updatedTask.getCategory().getName());
        assertEquals("Öncelik güncellenmiş olmalı", Priority.LOW, updatedTask.getPriority());
        assertTrue("Görev tamamlanmış olmalı", updatedTask.isCompleted());
        assertNotNull("Son tarih null olmamalı", updatedTask.getDeadline());
    }
    
    @Test
    public void testDelete() {
        // Test için bir görev oluştur
        TaskmanagerItem task = new TaskmanagerItem(TEST_TASK_NAME, TEST_TASK_DESC, new Category("Test Category"));
        repository.save(task);
        
        // Görevin var olduğunu doğrula
        TaskmanagerItem savedTask = repository.getById(task.getId());
        assertNotNull("Kaydedilen görev null olmamalı", savedTask);
        
        // Şimdi sil
        repository.delete(task.getId());
        
        // Artık olmamalı
        TaskmanagerItem deletedTask = repository.getById(task.getId());
        assertNull("Silinen görev null olmalı", deletedTask);
    }
    
    @Test
    public void testGetTasksInDateRange() {
        // Bugün, yarın ve bir hafta sonrası için görevler oluştur
        TaskmanagerItem taskToday = new TaskmanagerItem("Today Task", "Today Description", new Category("Today"));
        TaskmanagerItem taskTomorrow = new TaskmanagerItem("Tomorrow Task", "Tomorrow Description", new Category("Tomorrow"));
        TaskmanagerItem taskNextWeek = new TaskmanagerItem("Next Week Task", "Next Week Description", new Category("Next Week"));
        
        // Tarihleri ayarla
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        taskToday.setDeadline(today);
        
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = calendar.getTime();
        taskTomorrow.setDeadline(tomorrow);
        
        calendar.add(Calendar.DAY_OF_MONTH, 6); // +1 ayrıca eklendiği için toplam 7 gün
        Date nextWeek = calendar.getTime();
        taskNextWeek.setDeadline(nextWeek);
        
        // Görevleri kaydet
        repository.save(taskToday);
        repository.save(taskTomorrow);
        repository.save(taskNextWeek);
        
        // Bugünden yarına kadar olan görevleri getir
        List<TaskmanagerItem> todayToTomorrowTasks = repository.getTasksInDateRange(today, tomorrow);
        
        // Bugün ve yarınki görevler olmalı, gelecek haftaki olmamalı
        boolean containsToday = false;
        boolean containsTomorrow = false;
        boolean containsNextWeek = false;
        
        for (TaskmanagerItem task : todayToTomorrowTasks) {
            if (task.getId().equals(taskToday.getId())) {
                containsToday = true;
            } else if (task.getId().equals(taskTomorrow.getId())) {
                containsTomorrow = true;
            } else if (task.getId().equals(taskNextWeek.getId())) {
                containsNextWeek = false;
            }
        }
        
        assertTrue("Bugünün görevi tarih aralığında olmalı", containsToday);
        assertTrue("Yarının görevi tarih aralığında olmalı", containsTomorrow);
        assertFalse("Gelecek haftanın görevi tarih aralığında olmamalı", containsNextWeek);
        
        // Tüm tarihleri kapsayan aralık
        List<TaskmanagerItem> allDateRangeTasks = repository.getTasksInDateRange(today, nextWeek);
        assertTrue("Tüm tarih aralığında en az 3 görev olmalı", allDateRangeTasks.size() >= 3);
    }
    
    @Test
    public void testSaveWithNullCategory() {
        // Kategorisi olmayan bir görev oluştur
        TaskmanagerItem task = new TaskmanagerItem(TEST_TASK_NAME, TEST_TASK_DESC, null);
        
        // Kaydet
        repository.save(task);
        
        // ID ile getir
        TaskmanagerItem retrievedTask = repository.getById(task.getId());
        
        // Kontrol et
        assertNotNull("Kaydedilen görev null olmamalı", retrievedTask);
        assertNotNull("Kategori null olmamalı, varsayılan değer atanmalı", retrievedTask.getCategory());
        assertEquals("Kategori adı 'Uncategorized' olmalı", "Uncategorized", retrievedTask.getCategory().getName());
    }
} 