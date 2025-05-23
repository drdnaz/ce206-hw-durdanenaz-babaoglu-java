package com.taskmanager.gui;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import com.taskmanager.model.Category;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ManageCategoriesFrameTest {
    private ManageCatagoriesFrame manageCategoriesFrame;
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
        manageCategoriesFrame = new ManageCatagoriesFrame(mainMenuFrame);
        manageCategoriesFrame.setVisible(true);
    }

    @After
    public void tearDown() {
        if (manageCategoriesFrame != null) manageCategoriesFrame.dispose();
        closeAllDialogs();
    }

    @Test
    public void testFrameInitialization() {
        assertNotNull("ManageCategoriesFrame should not be null", manageCategoriesFrame);
        assertTrue("ManageCategoriesFrame should be visible", manageCategoriesFrame.isVisible());
        assertEquals("Frame title should be 'Manage Categories'", "Manage Categories", manageCategoriesFrame.getTitle());
    }

    @Test
    public void testComponentsInitialization() {
        Component[] components = manageCategoriesFrame.getContentPane().getComponents();
        assertTrue("Frame should have components", components.length > 0);
        assertEquals("Frame width should be 800", 800, manageCategoriesFrame.getWidth());
        assertEquals("Frame height should be 500", 500, manageCategoriesFrame.getHeight());
    }

    @Test
    public void testTableInitialization() {
        JTable categoryTable = manageCategoriesFrame.getCategoryTable();
        assertNotNull("Category table should be initialized", categoryTable);
        assertEquals("Table should have 4 columns", 4, categoryTable.getColumnCount());
    }

    @Test
    public void testAddButtonAction() {
        JButton addButton = (JButton) getPrivateField(manageCategoriesFrame, "addButton");
        assertNotNull(addButton);
        for (ActionListener al : addButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(addButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        closeAllDialogs();
    }

    @Test
    public void testEditButtonAction() {
        JButton editButton = (JButton) getPrivateField(manageCategoriesFrame, "editButton");
        assertNotNull(editButton);
        for (ActionListener al : editButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(editButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        closeAllDialogs();
    }

    @Test
    public void testDeleteButtonAction() {
        JButton deleteButton = (JButton) getPrivateField(manageCategoriesFrame, "deleteButton");
        assertNotNull(deleteButton);
        for (ActionListener al : deleteButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(deleteButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        closeAllDialogs();
    }

    @Test
    public void testCloseButtonAction() {
        JButton closeButton = (JButton) getPrivateField(manageCategoriesFrame, "closeButton");
        assertNotNull(closeButton);
        for (ActionListener al : closeButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(closeButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        closeAllDialogs();
    }

    @Test
    public void testFormFields() {
        assertNotNull(getPrivateField(manageCategoriesFrame, "nameField"));
        assertNotNull(getPrivateField(manageCategoriesFrame, "descriptionArea"));
        assertNotNull(getPrivateField(manageCategoriesFrame, "colorField"));
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