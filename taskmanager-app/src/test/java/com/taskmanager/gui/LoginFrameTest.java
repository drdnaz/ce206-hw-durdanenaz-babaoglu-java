package com.taskmanager.gui;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import com.naz.taskmanager.repository.UserRepository;

public class LoginFrameTest {
    private LoginFrame loginFrame;
    private UserRepository userRepository;

    @Before
    public void setUp() {
        // Örnek kategori oluştur
        com.naz.taskmanager.model.Category category = new com.naz.taskmanager.model.Category("Test Category");
        // Test kullanıcısı ile TaskService oluştur
        com.naz.taskmanager.service.TaskService taskService = new com.naz.taskmanager.service.TaskService("testUser");
        // En az bir görev ekle
        taskService.createTask("Test Task", "Test Description", category);

        userRepository = new UserRepository(System.out);
        loginFrame = new LoginFrame();
        loginFrame.setVisible(true); // Frame'i görünür yap
    }

    @After
    public void tearDown() {
        if (loginFrame != null) loginFrame.dispose();
        closeAllDialogs();
    }

    @Test
    public void testFrameInitialization() {
        assertNotNull("LoginFrame should not be null", loginFrame);
        assertTrue("LoginFrame should be visible", loginFrame.isVisible());
        assertEquals("Frame title should be 'Task Manager - Login'", "Task Manager - Login", loginFrame.getTitle());
    }

    @Test
    public void testComponentsInitialization() {
        // Test if all required components are initialized
        Component[] components = loginFrame.getContentPane().getComponents();
        assertTrue("Frame should have components", components.length > 0);
        
        // Test if the frame has the correct size
        assertEquals("Frame width should be 400", 400, loginFrame.getWidth());
        assertEquals("Frame height should be 500", 500, loginFrame.getHeight());
    }

    @Test
    public void testLoginFields() {
        // Doğrudan getter ile erişim
        JTextField usernameField = null;
        JPasswordField passwordField = null;
        try {
            java.lang.reflect.Method getUsernameField = loginFrame.getClass().getDeclaredMethod("getUsernameField");
            getUsernameField.setAccessible(true);
            usernameField = (JTextField) getUsernameField.invoke(loginFrame);
            java.lang.reflect.Method getPasswordField = loginFrame.getClass().getDeclaredMethod("getPasswordField");
            getPasswordField.setAccessible(true);
            passwordField = (JPasswordField) getPasswordField.invoke(loginFrame);
        } catch (Exception e) {
            // ignore
        }
        assertNotNull("Username field should be initialized", usernameField);
        assertNotNull("Password field should be initialized", passwordField);
    }

    @Test
    public void testFrameDisposal() {
        loginFrame.dispose();
        assertFalse("Frame should not be visible after disposal", loginFrame.isVisible());
    }

    // Yardımcı metod: Tüm açık JOptionPane dialoglarını kapat
    private void closeAllDialogs() {
        for (Window window : Window.getWindows()) {
            if (window instanceof JDialog) {
                window.dispose();
            }
        }
    }
} 