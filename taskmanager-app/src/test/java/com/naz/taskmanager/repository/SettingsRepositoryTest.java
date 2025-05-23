package com.naz.taskmanager.repository;

import com.naz.taskmanager.model.NotificationSettings;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.UUID;

public class SettingsRepositoryTest {
    private SettingsRepository repository;
    private UserRepository userRepository;
    private static final String TEST_USERNAME = "settings_test_" + UUID.randomUUID().toString().substring(0, 8);
    
    @Before
    public void setUp() {
        // Öncelikle test kullanıcısı oluştur
        userRepository = new UserRepository(System.out);
        if (!userRepository.userExists(TEST_USERNAME)) {
            userRepository.addUser(TEST_USERNAME, "password");
        }
        
        // Her test için yeni bir repository oluştur
        repository = new SettingsRepository(TEST_USERNAME);
    }
    
    @After
    public void tearDown() {
        // Test kullanıcısını temizle
        try {
            userRepository.delete(TEST_USERNAME);
        } catch (Exception e) {
            // Silme hatası olabilir, sorun değil
        }
    }
    
    @Test
    public void testSaveAndGetSettings() {
        // Varsayılan ayarları al
        NotificationSettings defaultSettings = repository.getSettings();
        assertNotNull("Varsayılan ayarlar null olmamalı", defaultSettings);
        
        // Varsayılan ayarları kontrol et
        assertTrue("Varsayılan olarak email bildirimleri açık olmalı", defaultSettings.isEmailEnabled());
        assertTrue("Varsayılan olarak uygulama bildirimleri açık olmalı", defaultSettings.isAppNotificationsEnabled());
        assertEquals("Varsayılan hatırlatıcı süresi 30 dakika olmalı", 30, defaultSettings.getDefaultReminderMinutes());
        
        // Özel ayarlar oluştur
        NotificationSettings customSettings = new NotificationSettings();
        customSettings.setEmailEnabled(false);
        customSettings.setAppNotificationsEnabled(false);
        customSettings.setDefaultReminderMinutes(45);
        
        // Ayarları kaydet
        repository.saveSettings(customSettings);
        
        // Ayarları yeniden yükle
        NotificationSettings retrievedSettings = repository.getSettings();
        assertNotNull("Alınan ayarlar null olmamalı", retrievedSettings);
        
        // Ayarların doğru kaydedildiğini kontrol et
        assertFalse("Email bildirimleri kapalı olmalı", retrievedSettings.isEmailEnabled());
        assertFalse("Uygulama bildirimleri kapalı olmalı", retrievedSettings.isAppNotificationsEnabled());
        assertEquals("Hatırlatıcı süresi 45 dakika olmalı", 45, retrievedSettings.getDefaultReminderMinutes());
    }
    
    @Test
    public void testGetSettingsForNewUser() {
        // Yeni bir kullanıcı için benzersiz bir kullanıcı adı oluştur
        String newUsername = "new_settings_user_" + UUID.randomUUID().toString().substring(0, 8);
        
        try {
            // Yeni kullanıcı ekle
            userRepository.addUser(newUsername, "password");
            
            // Bu kullanıcı için settings repository oluştur
            SettingsRepository newUserRepo = new SettingsRepository(newUsername);
            
            // Ayarları getir - hiç kaydedilmemiş olsa da varsayılan değerler dönmeli
            NotificationSettings settings = newUserRepo.getSettings();
            
            // Varsayılan değerleri kontrol et
            assertNotNull("Varsayılan ayarlar null olmamalı", settings);
            assertTrue("Varsayılan olarak email bildirimleri açık olmalı", settings.isEmailEnabled());
            assertTrue("Varsayılan olarak uygulama bildirimleri açık olmalı", settings.isAppNotificationsEnabled());
            assertEquals("Varsayılan hatırlatıcı süresi 30 dakika olmalı", 30, settings.getDefaultReminderMinutes());
        } finally {
            // Temizlik
            try {
                userRepository.delete(newUsername);
            } catch (Exception e) {
                // Sorun değil
            }
        }
    }
    
    @Test
    public void testSaveSettingsMultipleTimes() {
        // İlk ayarlar
        NotificationSettings settings1 = new NotificationSettings();
        settings1.setEmailEnabled(true);
        settings1.setAppNotificationsEnabled(false);
        settings1.setDefaultReminderMinutes(15);
        repository.saveSettings(settings1);
        
        // İlk ayarları doğrula
        NotificationSettings retrieved1 = repository.getSettings();
        assertTrue("Email bildirimleri açık olmalı", retrieved1.isEmailEnabled());
        assertFalse("Uygulama bildirimleri kapalı olmalı", retrieved1.isAppNotificationsEnabled());
        assertEquals("Hatırlatıcı süresi 15 dakika olmalı", 15, retrieved1.getDefaultReminderMinutes());
        
        // İkinci ayarlar
        NotificationSettings settings2 = new NotificationSettings();
        settings2.setEmailEnabled(false);
        settings2.setAppNotificationsEnabled(true);
        settings2.setDefaultReminderMinutes(60);
        repository.saveSettings(settings2);
        
        // İkinci ayarları doğrula
        NotificationSettings retrieved2 = repository.getSettings();
        assertFalse("Email bildirimleri kapalı olmalı", retrieved2.isEmailEnabled());
        assertTrue("Uygulama bildirimleri açık olmalı", retrieved2.isAppNotificationsEnabled());
        assertEquals("Hatırlatıcı süresi 60 dakika olmalı", 60, retrieved2.getDefaultReminderMinutes());
    }
} 