package com.taskmanager.gui;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import com.naz.taskmanager.repository.UserRepository;

public class RegisterFrameTest {
    private RegisterFrame registerFrame;
    private UserRepository userRepository;

    @Before
    public void setUp() {
        // Örnek kategori oluştur
        com.naz.taskmanager.model.Category category = new com.naz.taskmanager.model.Category("Test Category");
        // Test kullanıcısı ile TaskService oluştur
        com.naz.taskmanager.service.TaskService taskService = new com.naz.taskmanager.service.TaskService("testUser");
        // En az bir görev ekle
        taskService.createTask("Test Task", "Test Description", category);

        registerFrame = new RegisterFrame();
        registerFrame.setVisible(true);
    }

    @Test
    public void testFrameInitialization() {
        assertNotNull("RegisterFrame should not be null", registerFrame);
        assertTrue("RegisterFrame should be visible", registerFrame.isVisible());
        assertEquals("Frame title should be 'Register'", "Register", registerFrame.getTitle());
    }

    @Test
    public void testComponentsInitialization() {
        // Test if all required components are initialized
        Component[] components = registerFrame.getContentPane().getComponents();
        assertTrue("Frame should have components", components.length > 0);
        
        // Test if the frame has the correct size
        assertEquals("Frame width should be 400", 400, registerFrame.getWidth());
        assertEquals("Frame height should be 400", 400, registerFrame.getHeight());
    }

    @Test
    public void testRegistrationFields() {
        assertNotNull("Username field should be initialized", registerFrame.getUsernameField());
        assertNotNull("Password field should be initialized", registerFrame.getPasswordField());
        assertNotNull("Confirm password field should be initialized", registerFrame.getConfirmField());
    }

    @Test
    public void testFrameDisposal() {
        registerFrame.dispose();
        assertFalse("Frame should not be visible after disposal", registerFrame.isVisible());
    }
}