package com.taskmanager.gui;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import com.taskmanager.model.Task;
import java.util.Date;

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

    @Test
    public void testFrameInitialization() {
        assertNotNull("EditTaskFrame should not be null", editTaskFrame);
        assertTrue("EditTaskFrame should be visible", editTaskFrame.isVisible());
        assertEquals("Frame title should be 'Edit Tasks'", "Edit Tasks", editTaskFrame.getTitle());
    }

    @Test
    public void testComponentsInitialization() {
        // Test if all required components are initialized
        Component[] components = editTaskFrame.getContentPane().getComponents();
        assertTrue("Frame should have components", components.length > 0);
        
        // Test if the frame has the correct size
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
} 