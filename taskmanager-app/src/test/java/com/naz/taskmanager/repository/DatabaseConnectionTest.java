package com.naz.taskmanager.repository;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.io.File;

public class DatabaseConnectionTest {
    private DatabaseConnection dbConnection;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream testOut = new PrintStream(outContent);
    private final PrintStream originalOut = System.out;
    
    @Before
    public void setUp() {
        // Gerçek System.out'u kaydet ve test çıktı akışıyla değiştir
        System.setOut(testOut);
        
        // Her test için benzersiz bir örnek
        dbConnection = DatabaseConnection.getInstance(testOut);
    }
    
    @After
    public void tearDown() {
        // Test sonrası bağlantıyı kapat
        dbConnection.closeConnection();
        
        // Orijinal System.out'u geri yükle
        System.setOut(originalOut);
    }
    
    @Test
    public void testGetInstance() {
        // Singleton modeli doğrula
        DatabaseConnection instance1 = DatabaseConnection.getInstance(testOut);
        DatabaseConnection instance2 = DatabaseConnection.getInstance(testOut);
        
        // Aynı örnek olmalı
        assertSame("İki örnek aynı olmalı (singleton)", instance1, instance2);
        assertNotNull("Örnek null olmamalı", instance1);
    }
    
    @Test
    public void testGetConnection() {
        // Çıktı tamponunu temizle
        outContent.reset();
        
        // Bağlantıyı al
        Connection connection = dbConnection.getConnection();
        
        // Bağlantı başarılı olmalı
        assertNotNull("Veritabanı bağlantısı null olmamalı", connection);
    }
    
    @Test
    public void testCloseConnection() {
        // Çıktı tamponunu temizle
        outContent.reset();
        
        // Bağlantıyı al
        Connection connection = dbConnection.getConnection();
        assertNotNull("Veritabanı bağlantısı null olmamalı", connection);
        
        // Bağlantıyı kapat
        dbConnection.closeConnection();
        
        try {
            // Kapanan bağlantıyı kontrol et
            assertTrue("Bağlantı kapalı olmalı", connection.isClosed());
        } catch (SQLException e) {
            fail("Bağlantı durumunu kontrol ederken hata: " + e.getMessage());
        }
    }
    
    @Test
    public void testReleaseConnection() {
        // Bağlantıyı al
        Connection connection = dbConnection.getConnection();
        assertNotNull("Veritabanı bağlantısı null olmamalı", connection);
        
        // Bağlantıyı serbest bırak
        dbConnection.releaseConnection();
        
        try {
            // Bağlantı hala açık olmalı (bu yöntem şu anda bir şey yapmıyor)
            assertFalse("Bağlantı hala açık olmalı", connection.isClosed());
        } catch (SQLException e) {
            fail("Bağlantı durumunu kontrol ederken hata: " + e.getMessage());
        }
    }
    
    @Test
    public void testInitializeDatabase() {
        // Çıktı tamponunu temizle
        outContent.reset();
        
        // Veritabanını başlat
        dbConnection.initializeDatabase();
        
        // Tablolar oluşturuldu mu kontrol et
        Connection conn = dbConnection.getConnection();
        
        try (Statement stmt = conn.createStatement()) {
            // Users tablosunu kontrol et
            ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Users'");
            assertTrue("Users tablosu oluşturulmuş olmalı", rs.next());
            
            // Tasks tablosunu kontrol et
            rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Tasks'");
            assertTrue("Tasks tablosu oluşturulmuş olmalı", rs.next());
            
            // Categories tablosunu kontrol et
            rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Categories'");
            assertTrue("Categories tablosu oluşturulmuş olmalı", rs.next());
            
            // Reminders tablosunu kontrol et
            rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Reminders'");
            assertTrue("Reminders tablosu oluşturulmuş olmalı", rs.next());
            
            // Projects tablosunu kontrol et
            rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Projects'");
            assertTrue("Projects tablosu oluşturulmuş olmalı", rs.next());
            
            // Project_Tasks tablosunu kontrol et
            rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Project_Tasks'");
            assertTrue("Project_Tasks tablosu oluşturulmuş olmalı", rs.next());
            
            // Settings tablosunu kontrol et
            rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Settings'");
            assertTrue("Settings tablosu oluşturulmuş olmalı", rs.next());
        } catch (SQLException e) {
            fail("Tabloları kontrol ederken hata: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetConnectionMultipleTimes() {
        // İlk bağlantıyı al
        Connection connection1 = dbConnection.getConnection();
        assertNotNull("İlk veritabanı bağlantısı null olmamalı", connection1);
        
        // İkinci bağlantıyı al
        Connection connection2 = dbConnection.getConnection();
        assertNotNull("İkinci veritabanı bağlantısı null olmamalı", connection2);
        
        // Aynı bağlantı nesnesi olmalı
        assertSame("İki bağlantı aynı olmalı", connection1, connection2);
    }
    
    @Test
    public void testDataDirectoryCreation() {
        // Veritabanı dizini
        File dataDir = new File("data");
        
        // Bağlantıyı al - bu dizini oluşturmalı
        dbConnection.getConnection();
        
        // Dizin var olmalı
        assertTrue("Veri dizini oluşturulmuş olmalı", dataDir.exists());
        assertTrue("Veri dizini bir dizin olmalı", dataDir.isDirectory());
    }
    
    @Test
    public void testGetClosedConnection() {
        // Bağlantıyı al
        Connection connection1 = dbConnection.getConnection();
        assertNotNull("Veritabanı bağlantısı null olmamalı", connection1);
        
        // Bağlantıyı kapat
        dbConnection.closeConnection();
        
        try {
            // Kapanan bağlantıyı kontrol et
            assertTrue("Bağlantı kapalı olmalı", connection1.isClosed());
        } catch (SQLException e) {
            fail("Bağlantı durumunu kontrol ederken hata: " + e.getMessage());
        }
        
        // Yeni bağlantı al
        Connection connection2 = dbConnection.getConnection();
        assertNotNull("Yeni veritabanı bağlantısı null olmamalı", connection2);
        
        try {
            // Yeni bağlantı açık olmalı
            assertFalse("Yeni bağlantı açık olmalı", connection2.isClosed());
        } catch (SQLException e) {
            fail("Bağlantı durumunu kontrol ederken hata: " + e.getMessage());
        }
    }
} 