package com.taskmanager.gui;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import com.naz.taskmanager.repository.UserRepository;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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

    @Test
    public void testValidateForm() {
        // Test empty username
        loginFrame.getUsernameField().setText("");
        loginFrame.getPasswordField().setText("password123");
        
        // Get the validateForm method using reflection
        try {
            java.lang.reflect.Method validateFormMethod = LoginFrame.class.getDeclaredMethod("validateForm");
            validateFormMethod.setAccessible(true);
            
            boolean result = (boolean) validateFormMethod.invoke(loginFrame);
            assertFalse("Form should not validate with empty username", result);
            
            // Verify error dialog was shown
            boolean dialogShown = false;
            for (Window window : Window.getWindows()) {
                if (window instanceof JDialog) {
                    dialogShown = true;
                    window.dispose();
                    break;
                }
            }
            assertTrue("Error dialog should be shown for empty username", dialogShown);
            
            // Test empty password
            loginFrame.getUsernameField().setText("testuser");
            loginFrame.getPasswordField().setText("");
            
            result = (boolean) validateFormMethod.invoke(loginFrame);
            assertFalse("Form should not validate with empty password", result);
            
            // Verify error dialog was shown
            dialogShown = false;
            for (Window window : Window.getWindows()) {
                if (window instanceof JDialog) {
                    dialogShown = true;
                    window.dispose();
                    break;
                }
            }
            assertTrue("Error dialog should be shown for empty password", dialogShown);
            
            // Test valid input
            loginFrame.getUsernameField().setText("testuser");
            loginFrame.getPasswordField().setText("password123");
            
            result = (boolean) validateFormMethod.invoke(loginFrame);
            assertTrue("Form should validate with valid input", result);
            
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testLogin() {
        // Test successful login
        loginFrame.getUsernameField().setText("testuser");
        loginFrame.getPasswordField().setText("password123");
        
        try {
            // Get the login method using reflection
            java.lang.reflect.Method loginMethod = LoginFrame.class.getDeclaredMethod("login");
            loginMethod.setAccessible(true);
            
            // Perform login
            loginMethod.invoke(loginFrame);
            
            // Verify login frame is disposed
            assertFalse("Login frame should be disposed after successful login", loginFrame.isVisible());
            
            // Verify main menu frame is shown
            boolean mainMenuShown = false;
            for (Window window : Window.getWindows()) {
                if (window instanceof MainMenuFrame) {
                    mainMenuShown = true;
                    window.dispose();
                    break;
                }
            }
            assertTrue("Main menu frame should be shown after successful login", mainMenuShown);
            
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testShowRegisterDialog() {
        try {
            // Get the showRegisterDialog method using reflection
            java.lang.reflect.Method showRegisterDialogMethod = LoginFrame.class.getDeclaredMethod("showRegisterDialog");
            showRegisterDialogMethod.setAccessible(true);
            
            // Show register dialog
            showRegisterDialogMethod.invoke(loginFrame);
            
            // Verify register dialog is shown
            boolean registerDialogShown = false;
            for (Window window : Window.getWindows()) {
                if (window instanceof RegisterFrame) {
                    registerDialogShown = true;
                    window.dispose();
                    break;
                }
            }
            assertTrue("Register dialog should be shown", registerDialogShown);
            
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testLoginButtonAction() {
        // Get the login button
        JButton loginButton = null;
        try {
            java.lang.reflect.Field loginButtonField = LoginFrame.class.getDeclaredField("loginButton");
            loginButtonField.setAccessible(true);
            loginButton = (JButton) loginButtonField.get(loginFrame);
        } catch (Exception e) {
            fail("Failed to get login button: " + e.getMessage());
        }
        
        assertNotNull("Login button should exist", loginButton);
        
        // Test login button action with valid input
        loginFrame.getUsernameField().setText("testuser");
        loginFrame.getPasswordField().setText("password123");
        
        // Trigger the action listener
        for (ActionListener al : loginButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(loginButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        
        // Verify login frame is disposed
        assertFalse("Login frame should be disposed after successful login", loginFrame.isVisible());
    }

    @Test
    public void testRegisterButtonAction() {
        // Get the register button
        JButton registerButton = null;
        try {
            java.lang.reflect.Field registerButtonField = LoginFrame.class.getDeclaredField("registerButton");
            registerButtonField.setAccessible(true);
            registerButton = (JButton) registerButtonField.get(loginFrame);
        } catch (Exception e) {
            fail("Failed to get register button: " + e.getMessage());
        }
        
        assertNotNull("Register button should exist", registerButton);
        
        // Trigger the action listener
        for (ActionListener al : registerButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(registerButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        
        // Verify register dialog is shown
        boolean registerDialogShown = false;
        for (Window window : Window.getWindows()) {
            if (window instanceof RegisterFrame) {
                registerDialogShown = true;
                window.dispose();
                break;
            }
        }
        assertTrue("Register dialog should be shown", registerDialogShown);
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