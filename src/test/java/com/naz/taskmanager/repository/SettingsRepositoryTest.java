import com.naz.taskmanager.model.NotificationSettings;
import com.naz.taskmanager.repository.SettingsRepository;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.UUID;

public class SettingsRepositoryTest {
    private SettingsRepository repository;
    private static final String TEST_USERNAME = "settings_test_" + UUID.randomUUID().toString().substring(0, 8);
    
    @Before
    public void setUp() {
        // Her test için yeni bir repository oluştur
        repository = new SettingsRepository(TEST_USERNAME);
    }
    
    @Test
    public void testSaveAndGetSettings() {
        // Varsayılan ayarları al
        NotificationSettings defaultSettings = repository.getSettings();
        assertNotNull("Default settings should not be null", defaultSettings);
        
        // Varsayılan ayarları kontrol et
        assertTrue("Email notifications should be enabled by default", defaultSettings.isEmailEnabled());
        assertTrue("App notifications should be enabled by default", defaultSettings.isAppNotificationsEnabled());
        assertEquals("Default reminder time should be 30 minutes", 30, defaultSettings.getDefaultReminderMinutes());
        
        // Özel ayarlar oluştur
        NotificationSettings customSettings = new NotificationSettings();
        customSettings.setEmailEnabled(false);
        customSettings.setAppNotificationsEnabled(false);
        customSettings.setDefaultReminderMinutes(45);
        
        // Ayarları kaydet
        repository.saveSettings(customSettings);
        
        // Ayarları yeniden yükle
        NotificationSettings retrievedSettings = repository.getSettings();
        assertNotNull("Retrieved settings should not be null", retrievedSettings);
        
        // Ayarların doğru kaydedildiğini kontrol et
        assertFalse("Email notifications should be disabled", retrievedSettings.isEmailEnabled());
        assertFalse("App notifications should be disabled", retrievedSettings.isAppNotificationsEnabled());
        assertEquals("Reminder time should be updated to 45 minutes", 45, retrievedSettings.getDefaultReminderMinutes());
    }
    
    @Test
    public void testUpdateSettings() {
        // Varsayılan ayarları al
        NotificationSettings settings = repository.getSettings();
        
        // Ayarları değiştir
        boolean originalEmailEnabled = settings.isEmailEnabled();
        settings.setEmailEnabled(!originalEmailEnabled);
        int newReminderTime = 60;
        settings.setDefaultReminderMinutes(newReminderTime);
        
        // Değişiklikleri kaydet
        repository.saveSettings(settings);
        
        // Ayarları yeniden yükle
        NotificationSettings updatedSettings = repository.getSettings();
        
        // Değişikliklerin kaydedildiğini kontrol et
        assertEquals("Email notification setting should be updated", !originalEmailEnabled, updatedSettings.isEmailEnabled());
        assertEquals("Reminder time should be updated", newReminderTime, updatedSettings.getDefaultReminderMinutes());
    }
} 