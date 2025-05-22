import com.naz.taskmanager.model.Reminder;
import com.naz.taskmanager.repository.ReminderRepository;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ReminderRepositoryTest {
    private ReminderRepository repository;
    private static final String TEST_USERNAME = "reminder_test_" + System.currentTimeMillis();
    
    @Before
    public void setUp() {
        // Her test için yeni bir repository oluştur
        repository = new ReminderRepository(TEST_USERNAME);
        // Temiz başlangıç için mevcut test hatırlatıcıları temizlenebilir
        cleanupTestReminders();
    }
    
    @After
    public void tearDown() {
        // Test sonrası temizlik
        cleanupTestReminders();
    }
    
    private void cleanupTestReminders() {
        List<Reminder> reminders = repository.getAll();
        for (Reminder reminder : reminders) {
            if (reminder.getMessage().startsWith("Test Reminder")) {
                repository.delete(reminder.getId());
            }
        }
    }
    
    @Test
    public void testSaveAndGetById() {
        // Test hatırlatıcısı oluştur
        Reminder reminder = createTestReminder("Test Reminder Save");
        
        // Hatırlatıcıyı kaydet
        repository.save(reminder);
        
        // ID'ye göre hatırlatıcıyı al ve kontrol et
        Reminder retrievedReminder = repository.getById(reminder.getId());
        assertNotNull("Kaydedilen hatırlatıcı null olmamalı", retrievedReminder);
        assertEquals("Hatırlatıcı mesajları eşleşmeli", reminder.getMessage(), retrievedReminder.getMessage());
        assertEquals("Hatırlatıcı tarihleri yaklaşık eşleşmeli", reminder.getDate().getTime()/1000, retrievedReminder.getDate().getTime()/1000);
    }
    
    @Test
    public void testGetAll() {
        // Başlangıçta kaç hatırlatıcı var
        int initialCount = repository.getAll().size();
        
        // Test hatırlatıcıları oluştur ve kaydet
        Reminder reminder1 = createTestReminder("Test Reminder GetAll 1");
        Reminder reminder2 = createTestReminder("Test Reminder GetAll 2");
        repository.save(reminder1);
        repository.save(reminder2);
        
        // Tüm hatırlatıcıları al ve kontrol et
        List<Reminder> allReminders = repository.getAll();
        assertNotNull("Hatırlatıcı listesi null olmamalı", allReminders);
        assertEquals("Hatırlatıcı sayısı beklendiği gibi artmalı", initialCount + 2, allReminders.size());
        
        // Hatırlatıcıların listede olduğunu kontrol et
        boolean found1 = false, found2 = false;
        for (Reminder reminder : allReminders) {
            if (reminder.getId().equals(reminder1.getId())) found1 = true;
            if (reminder.getId().equals(reminder2.getId())) found2 = true;
        }
        assertTrue("İlk hatırlatıcı listede olmalı", found1);
        assertTrue("İkinci hatırlatıcı listede olmalı", found2);
    }
    
    @Test
    public void testUpdate() {
        // Test hatırlatıcısı oluştur ve kaydet
        Reminder reminder = createTestReminder("Test Reminder Update");
        repository.save(reminder);
        
        // Hatırlatıcıyı güncelle
        reminder.setMessage("Updated Test Reminder");
        Date newDate = new Date(System.currentTimeMillis() + 86400000); // 1 gün sonrası
        reminder.setDate(newDate);
        repository.update(reminder);
        
        // Güncellenmiş hatırlatıcıyı al ve kontrol et
        Reminder updatedReminder = repository.getById(reminder.getId());
        assertNotNull("Güncellenmiş hatırlatıcı null olmamalı", updatedReminder);
        assertEquals("Hatırlatıcı mesajı güncellenmiş olmalı", "Updated Test Reminder", updatedReminder.getMessage());
        assertEquals("Hatırlatıcı tarihi güncellenmiş olmalı", newDate.getTime()/1000, updatedReminder.getDate().getTime()/1000);
    }
    
    @Test
    public void testDelete() {
        // Test hatırlatıcısı oluştur ve kaydet
        Reminder reminder = createTestReminder("Test Reminder Delete");
        repository.save(reminder);
        
        // Hatırlatıcının var olduğunu kontrol et
        assertNotNull("Kaydedilen hatırlatıcı getById ile alınabilmeli", repository.getById(reminder.getId()));
        
        // Hatırlatıcıyı sil
        repository.delete(reminder.getId());
        
        // Hatırlatıcının silindiğini kontrol et
        assertNull("Silinen hatırlatıcı null olmalı", repository.getById(reminder.getId()));
    }
    
    @Test
    public void testGetDueReminders() {
        // Şimdi ve gelecekteki bir zaman için hatırlatıcılar oluştur
        Date now = new Date();
        Date future = new Date(now.getTime() + 86400000); // 1 gün sonrası
        
        Reminder dueReminder = createTestReminder("Test Reminder Due Now");
        dueReminder.setDate(now);
        repository.save(dueReminder);
        
        Reminder futureReminder = createTestReminder("Test Reminder Future");
        futureReminder.setDate(future);
        repository.save(futureReminder);
        
        // Şu an gelmesi gereken hatırlatıcıları al
        List<Reminder> dueReminders = repository.getDueReminders();
        
        // En az bir hatırlatıcı gelmeli (şimdi olanı)
        assertTrue("En az bir hatırlatıcı gelmeli", dueReminders.size() >= 1);
        
        // Şimdiki hatırlatıcının listede olduğunu kontrol et
        boolean foundDueReminder = false;
        for (Reminder reminder : dueReminders) {
            if (reminder.getId().equals(dueReminder.getId())) {
                foundDueReminder = true;
                break;
            }
        }
        assertTrue("Vadesi gelen hatırlatıcı listede olmalı", foundDueReminder);
    }
    
    private Reminder createTestReminder(String message) {
        // Benzersiz bir ID oluştur
        String id = UUID.randomUUID().toString();
        
        // Şimdiden 1 saat sonrası için hatırlatıcı oluştur
        Date reminderDate = new Date(System.currentTimeMillis() + 3600000);
        
        // Test hatırlatıcısı oluştur
        Reminder reminder = new Reminder();
        reminder.setId(id);
        reminder.setTaskId("task-" + id.substring(0, 8));
        reminder.setMessage(message);
        reminder.setDate(reminderDate);
        
        return reminder;
    }
} 