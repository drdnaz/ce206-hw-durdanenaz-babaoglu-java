package com.taskmanager.gui;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import com.taskmanager.model.Task;
import java.util.Date;

public class ViewTaskFrameTest {
    private ViewTaskFrame viewTaskFrame;
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
        viewTaskFrame = new ViewTaskFrame(mainMenuFrame);
         viewTaskFrame.setVisible(true); // Frame'i görünür yap
    }

    @Test
    public void testFrameInitialization() {
        assertNotNull("ViewTaskFrame should not be null", viewTaskFrame);
        assertTrue("ViewTaskFrame should be visible", viewTaskFrame.isVisible());
        assertEquals("Frame title should be 'View Tasks'", "View Tasks", viewTaskFrame.getTitle());
    }

    @Test
    public void testComponentsInitialization() {
        // Test if all required components are initialized
        Component[] components = viewTaskFrame.getContentPane().getComponents();
        assertTrue("Frame should have components", components.length > 0);
        
        // Test if the frame has the correct size
        assertEquals("Frame width should be 800", 800, viewTaskFrame.getWidth());
        assertEquals("Frame height should be 600", 600, viewTaskFrame.getHeight());
    }

    @Test
    public void testTableInitialization() {
        // Get the table from the frame using the getter method
        JTable taskTable = viewTaskFrame.getTaskTable();
        
        assertNotNull("Task table should be initialized", taskTable);
        assertEquals("Table should have 7 columns", 7, taskTable.getColumnCount());
    }

    @Test
    public void testFilterComponents() {
        // Get filter components using the getter methods
        JTextField searchField = viewTaskFrame.getSearchField();
        JComboBox<String> categoryFilter = viewTaskFrame.getCategoryFilterComboBox();
        JComboBox<String> priorityFilter = viewTaskFrame.getPriorityFilterComboBox();
        
        assertNotNull("Search field should be initialized", searchField);
        assertNotNull("Category filter should be initialized", categoryFilter);
        assertNotNull("Priority filter should be initialized", priorityFilter);
    }

    @Test
    public void testFrameDisposal() {
        viewTaskFrame.dispose();
        assertFalse("Frame should not be visible after disposal", viewTaskFrame.isVisible());
    }
} 