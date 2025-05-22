import com.naz.taskmanager.User;
import com.naz.taskmanager.repository.UserRepository;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class UserRepositoryTest {
    private UserRepository repository;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    
    @Before
    public void setUp() {
        // Çıktıları yakalamak için PrintStream yönlendirme
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(outContent);
        
        // Her test için yeni bir repository oluştur
        repository = new UserRepository(ps);
        
        // Temiz başlangıç için mevcut test kullanıcıları temizlenebilir
        cleanupTestUsers();
    }
    
    @After
    public void tearDown() {
        // Test sonrası temizlik
        cleanupTestUsers();
        
        // Çıktı akışını geri yükle
        System.setOut(originalOut);
    }
    
    private void cleanupTestUsers() {
        List<User> users = repository.getAll();
        for (User user : users) {
            if (user.getUsername().startsWith("test_")) {
                repository.delete(user.getUsername());
            }
        }
    }
    
    @Test
    public void testSaveAndGetById() {
        // Test kullanıcısı oluştur
        User user = createTestUser("test_save");
        
        // Kullanıcıyı kaydet
        repository.save(user);
        
        // ID'ye göre kullanıcıyı al ve kontrol et
        User retrievedUser = repository.getById(user.getUsername());
        assertNotNull("Kaydedilen kullanıcı null olmamalı", retrievedUser);
        assertEquals("Kullanıcı adları eşleşmeli", user.getUsername(), retrievedUser.getUsername());
        assertEquals("Şifreler eşleşmeli", user.getPassword(), retrievedUser.getPassword());
        assertEquals("E-postalar eşleşmeli", user.getEmail(), retrievedUser.getEmail());
    }
    
    @Test
    public void testUserExists() {
        // Test kullanıcısı oluştur
        User user = createTestUser("test_exists");
        
        // Kullanıcıyı kaydet
        repository.save(user);
        
        // Kullanıcının var olduğunu kontrol et
        assertTrue("Kullanıcı var olmalı", repository.userExists(user.getUsername()));
        
        // Olmayan kullanıcıyı kontrol et
        assertFalse("Olmayan kullanıcı false dönmeli", repository.userExists("nonexistent_user"));
    }
    
    @Test
    public void testFindByUsername() {
        // Test kullanıcısı oluştur
        User user = createTestUser("test_find");
        
        // Kullanıcıyı kaydet
        repository.save(user);
        
        // Kullanıcı adına göre bul
        User foundUser = repository.findByUsername(user.getUsername());
        assertNotNull("Bulunan kullanıcı null olmamalı", foundUser);
        assertEquals("Kullanıcı adları eşleşmeli", user.getUsername(), foundUser.getUsername());
        
        // Olmayan kullanıcıyı arama
        assertNull("Olmayan kullanıcı null dönmeli", repository.findByUsername("nonexistent_user"));
    }
    
    @Test
    public void testGetAll() {
        // Başlangıçta kaç kullanıcı var
        int initialCount = repository.getAll().size();
        
        // Test kullanıcıları oluştur ve kaydet
        User user1 = createTestUser("test_getall1");
        User user2 = createTestUser("test_getall2");
        repository.save(user1);
        repository.save(user2);
        
        // Tüm kullanıcıları al ve kontrol et
        List<User> allUsers = repository.getAll();
        assertNotNull("Kullanıcı listesi null olmamalı", allUsers);
        assertTrue("Kullanıcı sayısı beklendiği gibi artmalı", allUsers.size() >= initialCount + 2);
        
        // Kullanıcıların listede olduğunu kontrol et
        boolean found1 = false, found2 = false;
        for (User user : allUsers) {
            if (user.getUsername().equals(user1.getUsername())) found1 = true;
            if (user.getUsername().equals(user2.getUsername())) found2 = true;
        }
        assertTrue("İlk kullanıcı listede olmalı", found1);
        assertTrue("İkinci kullanıcı listede olmalı", found2);
    }
    
    @Test
    public void testUpdate() {
        // Test kullanıcısı oluştur ve kaydet
        User user = createTestUser("test_update");
        repository.save(user);
        
        // Kullanıcıyı güncelle
        user.setEmail("updated@example.com");
        user.setPassword("updatedPass");
        repository.update(user);
        
        // Güncellenmiş kullanıcıyı al ve kontrol et
        User updatedUser = repository.getById(user.getUsername());
        assertNotNull("Güncellenmiş kullanıcı null olmamalı", updatedUser);
        assertEquals("E-posta güncellenmiş olmalı", "updated@example.com", updatedUser.getEmail());
        assertEquals("Şifre güncellenmiş olmalı", "updatedPass", updatedUser.getPassword());
    }
    
    @Test
    public void testDelete() {
        // Test kullanıcısı oluştur ve kaydet
        User user = createTestUser("test_delete");
        repository.save(user);
        
        // Kullanıcının var olduğunu kontrol et
        assertNotNull("Kaydedilen kullanıcı getById ile alınabilmeli", repository.getById(user.getUsername()));
        
        // Kullanıcıyı sil
        repository.delete(user.getUsername());
        
        // Kullanıcının silindiğini kontrol et
        assertNull("Silinen kullanıcı null olmalı", repository.getById(user.getUsername()));
    }
    
    @Test
    public void testLoginUser() {
        // Test kullanıcısı oluştur ve kaydet
        User user = createTestUser("test_login");
        repository.save(user);
        
        // Doğru kimlik bilgileri ile giriş
        User loggedInUser = repository.loginUser(user.getUsername(), "password123");
        assertNotNull("Doğru kimlik bilgileri ile kullanıcı null olmamalı", loggedInUser);
        assertEquals("Doğru kullanıcı dönmeli", user.getUsername(), loggedInUser.getUsername());
        
        // Yanlış şifre ile giriş
        User wrongPassUser = repository.loginUser(user.getUsername(), "wrongpass");
        assertNull("Yanlış şifre ile kullanıcı null olmalı", wrongPassUser);
        
        // Olmayan kullanıcı ile giriş
        User nonexistentUser = repository.loginUser("nonexistent_user", "password123");
        assertNull("Olmayan kullanıcı null olmalı", nonexistentUser);
    }
    
    @Test
    public void testSaveWithExistingUser() {
        // Test kullanıcısı oluştur ve kaydet
        User user = createTestUser("test_save_existing");
        repository.save(user);
        
        // Aynı kullanıcı adı ile başka bir kullanıcı oluştur
        User duplicateUser = new User(user.getUsername(), "anotherpass", "another@example.com");
        
        // Kaydetmeyi dene (başarısız olmalı)
        boolean result = repository.save(duplicateUser);
        assertFalse("Mevcut kullanıcıyı kaydetme false dönmeli", result);
    }
    
    @Test
    public void testUpdateNonexistentUser() {
        // Var olmayan bir kullanıcıyı güncellemeyi dene
        User nonexistentUser = new User("nonexistent_user", "password", "email@example.com");
        boolean result = repository.update(nonexistentUser);
        
        // Güncelleme başarısız olmalı
        assertFalse("Olmayan kullanıcıyı güncelleme false dönmeli", result);
    }
    
    @Test
    public void testDeleteNonexistentUser() {
        // Var olmayan bir kullanıcıyı silmeyi dene
        boolean result = repository.delete("nonexistent_user");
        
        // Silme başarısız olmalı
        assertFalse("Olmayan kullanıcıyı silme false dönmeli", result);
    }
    
    @Test
    public void testGetByIdWithNullId() {
        // Null ID ile kullanıcı almayı dene
        User user = repository.getById(null);
        
        // Sonuç null olmalı
        assertNull("Null ID ile kullanıcı null olmalı", user);
    }
    
    @Test
    public void testGetByIdWithEmptyId() {
        // Boş ID ile kullanıcı almayı dene
        User user = repository.getById("");
        
        // Sonuç null olmalı
        assertNull("Boş ID ile kullanıcı null olmalı", user);
    }
    
    @Test
    public void testGetByIdWithNonexistentId() {
        // Var olmayan ID ile kullanıcı almayı dene
        User user = repository.getById("nonexistent_id");
        
        // Sonuç null olmalı
        assertNull("Olmayan ID ile kullanıcı null olmalı", user);
    }
    
    private User createTestUser(String username) {
        // Benzersiz bir kullanıcı oluştur
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return new User(
            username + "_" + uniqueId,
            "password123",
            username + "_" + uniqueId + "@example.com"
        );
    }
} 