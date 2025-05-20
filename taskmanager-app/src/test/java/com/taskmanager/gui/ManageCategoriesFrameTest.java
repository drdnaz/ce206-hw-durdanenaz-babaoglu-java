package com.taskmanager.gui;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import com.taskmanager.model.Category;

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

    @Test
    public void testFrameInitialization() {
        assertNotNull("ManageCategoriesFrame should not be null", manageCategoriesFrame);
        assertTrue("ManageCategoriesFrame should be visible", manageCategoriesFrame.isVisible());
        assertEquals("Frame title should be 'Manage Categories'", "Manage Categories", manageCategoriesFrame.getTitle());
    }

    @Test
    public void testComponentsInitialization() {
        // Test if all required components are initialized
        Component[] components = manageCategoriesFrame.getContentPane().getComponents();
        assertTrue("Frame should have components", components.length > 0);
        
        // Test if the frame has the correct size
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
    public void testFrameDisposal() {
        manageCategoriesFrame.dispose();
        assertFalse("Frame should not be visible after disposal", manageCategoriesFrame.isVisible());
    }
} 