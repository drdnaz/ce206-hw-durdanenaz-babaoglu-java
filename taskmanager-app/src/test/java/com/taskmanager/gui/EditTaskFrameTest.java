package com.taskmanager.gui;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import com.taskmanager.model.Task;
import java.util.Date;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EditTaskFrameTest {
    private EditTaskFrame editTaskFrame;
    private MainMenuFrame mainMenuFrame;

    @Before
    public void setUp() {
        // Örnek kategori oluştur
        com.naz.taskmanager.model.Category category = new com.naz.taskmanager.model.Category("Test Category");
        // Test kullanıcısı ile TaskService oluştur
        com.naz.taskmanager.service.TaskService taskService = new com.naz.taskmanager.service.TaskService("testUser");
        // En az bir görev ekle
        taskService.createTask("Test Task", "Test Description", category);

        mainMenuFrame = new MainMenuFrame("testUser");
        editTaskFrame = new EditTaskFrame(mainMenuFrame);
        editTaskFrame.setVisible(true);
    }

    @After
    public void tearDown() {
        if (editTaskFrame != null) editTaskFrame.dispose();
        closeAllDialogs();
    }

    @Test
    public void testFrameInitialization() {
        assertNotNull("EditTaskFrame should not be null", editTaskFrame);
        assertTrue("EditTaskFrame should be visible", editTaskFrame.isVisible());
        assertEquals("Frame title should be 'Edit Tasks'", "Edit Tasks", editTaskFrame.getTitle());
    }

    @Test
    public void testComponentsInitialization() {
        Component[] components = editTaskFrame.getContentPane().getComponents();
        assertTrue("Frame should have components", components.length > 0);
        assertEquals("Frame width should be 800", 800, editTaskFrame.getWidth());
        assertEquals("Frame height should be 600", 600, editTaskFrame.getHeight());
    }

    @Test
    public void testTableInitialization() {
        JTable taskTable = editTaskFrame.getTaskTable();
        assertNotNull("Task table should be initialized", taskTable);
        assertEquals("Table should have 6 columns", 6, taskTable.getColumnCount());
    }

    @Test
    public void testFrameDisposal() {
        editTaskFrame.dispose();
        assertFalse("Frame should not be visible after disposal", editTaskFrame.isVisible());
    }

    @Test
    public void testEditButtonAction() {
        JButton editButton = (JButton) getPrivateField(editTaskFrame, "editButton");
        assertNotNull(editButton);
        for (ActionListener al : editButton.getActionListeners()) {
            al.actionPerformed(new java.awt.event.ActionEvent(editButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        closeAllDialogs();
    }

    @Test
    public void testRefreshButtonAction() {
        JButton refreshButton = (JButton) getPrivateField(editTaskFrame, "refreshButton");
        assertNotNull(refreshButton);
        for (ActionListener al : refreshButton.getActionListeners()) {
            al.actionPerformed(new java.awt.event.ActionEvent(refreshButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        closeAllDialogs();
    }

    @Test
    public void testCloseButtonAction() {
        JButton closeButton = (JButton) getPrivateField(editTaskFrame, "closeButton");
        assertNotNull(closeButton);
        for (ActionListener al : closeButton.getActionListeners()) {
            al.actionPerformed(new java.awt.event.ActionEvent(closeButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        closeAllDialogs();
    }

    @Test
    public void testEditFormFields() {
        assertNotNull(getPrivateField(editTaskFrame, "taskTitleField"));
        assertNotNull(getPrivateField(editTaskFrame, "taskDescriptionArea"));
        assertNotNull(getPrivateField(editTaskFrame, "dateSpinner"));
        assertNotNull(getPrivateField(editTaskFrame, "categoryComboBox"));
        assertNotNull(getPrivateField(editTaskFrame, "priorityComboBox"));
        assertNotNull(getPrivateField(editTaskFrame, "statusComboBox"));
    }

    // Yardımcı: private alanlara erişim
    private Object getPrivateField(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            return null;
        }
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