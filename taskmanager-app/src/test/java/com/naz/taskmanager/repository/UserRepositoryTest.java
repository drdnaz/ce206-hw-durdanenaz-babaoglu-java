package com.naz.taskmanager.repository;

import com.naz.taskmanager.User;
import org.junit.*;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.UUID;

public class UserRepositoryTest {
    private UserRepository repository;
    private static final String TEST_USERNAME = "user_test_" + UUID.randomUUID().toString().substring(0, 8);
    private static final String TEST_PASSWORD = "test_password";
    private static final String TEST_EMAIL = "test@example.com";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream testOut = new PrintStream(outContent);
    
    @Before
    public void setUp() {
        // Özel bir PrintStream ile repository oluştur
        repository = new UserRepository(testOut);
        System.setOut(testOut);
        
        // Test veritabanı için bir kullanıcı oluştur, varsa temizle
        try {
            repository.delete(TEST_USERNAME);
        } catch (Exception e) {
            // İlk kez çalıştırılıyorsa kullanıcı olmayabilir, sorun değil
        }
    }
    
    @After
    public void tearDown() {
        // Test kullanıcısını temizle
        try {
            repository.delete(TEST_USERNAME);
        } catch (Exception e) {
            // Silinemeyen kullanıcı olabilir, sorun değil
        }
        
        // System.out'u eski haline getir
        System.setOut(originalOut);
    }
    
    @Test
    public void testSaveAndGetById() {
        // Yeni kullanıcı oluştur
        User user = new User(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
        
        // Kaydet
        repository.save(user);
        
        // Getir
        User retrievedUser = repository.getById(TEST_USERNAME);
        
        // Kontrol et
        assertNotNull("Kaydedilen kullanıcı null olmamalı", retrievedUser);
        assertEquals("Kullanıcı adı eşleşmeli", TEST_USERNAME, retrievedUser.getUsername());
        assertEquals("Şifre eşleşmeli", TEST_PASSWORD, retrievedUser.getPassword());
        assertEquals("Email eşleşmeli", TEST_EMAIL, retrievedUser.getEmail());
    }
    
    @Test
    public void testUpdate() {
        // Önce kullanıcıyı kaydet
        User user = new User(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
        repository.save(user);
        
        // Şimdi güncelle
        String updatedPassword = "new_password";
        String updatedEmail = "new_email@example.com";
        user.setPassword(updatedPassword);
        user.setEmail(updatedEmail);
        
        repository.update(user);
        
        // Güncellenmiş kullanıcıyı al
        User updatedUser = repository.getById(TEST_USERNAME);
        
        // Kontrol et
        assertNotNull("Güncellenmiş kullanıcı null olmamalı", updatedUser);
        assertEquals("Kullanıcı adı değişmemeli", TEST_USERNAME, updatedUser.getUsername());
        assertEquals("Şifre güncellenmiş olmalı", updatedPassword, updatedUser.getPassword());
        assertEquals("Email güncellenmiş olmalı", updatedEmail, updatedUser.getEmail());
    }
    
    @Test
    public void testSaveExistingUser() {
        // İlk olarak kullanıcı oluştur
        User user = new User(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
        repository.save(user);
        
        // Şimdi aynı kullanıcı adıyla farklı bilgiler içeren kullanıcı kaydet
        String updatedPassword = "updated_by_save";
        String updatedEmail = "updated_by_save@example.com";
        User updatedUser = new User(TEST_USERNAME, updatedPassword, updatedEmail);
        
        repository.save(updatedUser);
        
        // Kullanıcıyı al ve kontrol et
        User retrievedUser = repository.getById(TEST_USERNAME);
        assertNotNull("Kullanıcı null olmamalı", retrievedUser);
        assertEquals("Şifre güncellenmiş olmalı", updatedPassword, retrievedUser.getPassword());
        assertEquals("Email güncellenmiş olmalı", updatedEmail, retrievedUser.getEmail());
    }
    
    @Test
    public void testGetAll() {
        // Önce bir test kullanıcısı oluştur
        User user = new User(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
        repository.save(user);
        
        // Tüm kullanıcıları al
        List<User> allUsers = repository.getAll();
        
        // En az bir kullanıcı olmalı (bizim eklediğimiz)
        assertFalse("Kullanıcı listesi boş olmamalı", allUsers.isEmpty());
        
        // Bizim kullanıcımızı içermeli
        boolean containsTestUser = false;
        for (User u : allUsers) {
            if (u.getUsername().equals(TEST_USERNAME)) {
                containsTestUser = true;
                break;
            }
        }
        assertTrue("Kullanıcı listesi test kullanıcısını içermeli", containsTestUser);
    }
    
    @Test
    public void testDelete() {
        // Önce kullanıcıyı kaydet
        User user = new User(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
        repository.save(user);
        
        // Kullanıcının var olduğunu doğrula
        User savedUser = repository.getById(TEST_USERNAME);
        assertNotNull("Kaydedilen kullanıcı null olmamalı", savedUser);
        
        // Şimdi sil
        repository.delete(TEST_USERNAME);
        
        // Artık olmamalı
        User deletedUser = repository.getById(TEST_USERNAME);
        assertNull("Silinen kullanıcı null olmalı", deletedUser);
    }
    
    @Test
    public void testAuthenticateUser() {
        // Önce kullanıcıyı kaydet
        User user = new User(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
        repository.save(user);
        
        // Doğru kimlik bilgileriyle doğrula
        User authenticatedUser = repository.authenticateUser(TEST_USERNAME, TEST_PASSWORD);
        assertNotNull("Doğru kimlik bilgileriyle kullanıcı doğrulanmalı", authenticatedUser);
        
        // Yanlış şifreyle doğrulama başarısız olmalı
        User invalidUser = repository.authenticateUser(TEST_USERNAME, "wrong_password");
        assertNull("Yanlış şifreyle kullanıcı doğrulanmamalı", invalidUser);
        
        // Yanlış kullanıcı adıyla doğrulama başarısız olmalı
        User nonexistentUser = repository.authenticateUser("nonexistent_user", TEST_PASSWORD);
        assertNull("Yanlış kullanıcı adıyla kullanıcı doğrulanmamalı", nonexistentUser);
    }
    
    @Test
    public void testUserExists() {
        // Önce kullanıcı yok olmalı
        assertFalse("Başlangıçta kullanıcı olmamalı", repository.userExists(TEST_USERNAME));
        
        // Kullanıcıyı kaydet
        User user = new User(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
        repository.save(user);
        
        // Şimdi var olmalı
        assertTrue("Kaydettikten sonra kullanıcı olmalı", repository.userExists(TEST_USERNAME));
    }
    
    @Test
    public void testAddUser() {
        // Kullanıcı ekle
        String newUsername = TEST_USERNAME + "_add";
        String newPassword = "add_password";
        
        // Önce sil (varsa)
        try {
            repository.delete(newUsername);
        } catch (Exception e) {
            // İlk çalıştırmada kullanıcı olmayabilir
        }
        
        // Ekle
        boolean result = repository.addUser(newUsername, newPassword);
        assertTrue("Kullanıcı ekleme başarılı olmalı", result);
        
        // Kullanıcının eklendiğini doğrula
        assertTrue("Kullanıcı var olmalı", repository.userExists(newUsername));
        
        // Temizlik
        repository.delete(newUsername);
    }
    
    @Test
    public void testValidateUser() {
        // Kullanıcı ekle
        String validateUsername = TEST_USERNAME + "_validate";
        String validatePassword = "validate_password";
        
        // Önce sil (varsa)
        try {
            repository.delete(validateUsername);
        } catch (Exception e) {
            // İlk çalıştırmada kullanıcı olmayabilir
        }
        
        // Ekle
        repository.addUser(validateUsername, validatePassword);
        
        // Doğrula
        boolean validResult = repository.validateUser(validateUsername, validatePassword);
        assertTrue("Doğru kimlik bilgileriyle doğrulama başarılı olmalı", validResult);
        
        // Yanlış şifreyle başarısız olmalı
        boolean invalidPasswordResult = repository.validateUser(validateUsername, "wrong_password");
        assertFalse("Yanlış şifreyle doğrulama başarısız olmalı", invalidPasswordResult);
        
        // Yanlış kullanıcı adıyla başarısız olmalı
        boolean invalidUsernameResult = repository.validateUser("wrong_username", validatePassword);
        assertFalse("Yanlış kullanıcı adıyla doğrulama başarısız olmalı", invalidUsernameResult);
        
        // Temizlik
        repository.delete(validateUsername);
    }
    
    @Test(expected = RuntimeException.class)
    public void testUpdateNonexistentUser() {
        // Olmayan bir kullanıcıyı güncellemeye çalış
        String nonexistentUsername = "nonexistent_" + UUID.randomUUID();
        User user = new User(nonexistentUsername, "password", "email@example.com");
        
        // Bu işlem exception fırlatmalı
        repository.update(user);
    }
} 