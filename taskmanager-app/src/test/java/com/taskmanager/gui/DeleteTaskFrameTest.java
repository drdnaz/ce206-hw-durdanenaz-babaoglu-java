package com.taskmanager.gui;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import com.taskmanager.model.Task;
import java.util.Date;

public class DeleteTaskFrameTest {
    private DeleteTaskFrame deleteTaskFrame;
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
        deleteTaskFrame = new DeleteTaskFrame(mainMenuFrame);
        deleteTaskFrame.setVisible(true); // Frame'i görünür yap
    }

    @Test
    public void testFrameInitialization() {
        assertNotNull("DeleteTaskFrame should not be null", deleteTaskFrame);
        assertTrue("DeleteTaskFrame should be visible", deleteTaskFrame.isVisible());
        assertEquals("Frame title should be 'Delete Tasks'", "Delete Tasks", deleteTaskFrame.getTitle());
    }

    @Test
    public void testComponentsInitialization() {
        // Test if all required components are initialized
        Component[] components = deleteTaskFrame.getContentPane().getComponents();
        assertTrue("Frame should have components", components.length > 0);
        
        // Test if the frame has the correct size
        assertEquals("Frame width should be 700", 700, deleteTaskFrame.getWidth());
        assertEquals("Frame height should be 500", 500, deleteTaskFrame.getHeight());
    }

    @Test
    public void testTableInitialization() {
        // Get the table from the frame using the getter method
        JTable taskTable = deleteTaskFrame.getTaskTable();
        
        assertNotNull("Task table should be initialized", taskTable);
        assertEquals("Table should have 6 columns", 6, taskTable.getColumnCount());
    }

    @Test
    public void testFrameDisposal() {
        deleteTaskFrame.dispose();
        assertFalse("Frame should not be visible after disposal", deleteTaskFrame.isVisible());
    }
}