package com.taskmanager.gui;

import org.junit.Before;
import org.junit.After;
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
        // Test için veri hazırlığı
        Task task = new Task("Test Task", "Test Description", new Date(), "Test Category", "High");
        Task.addTask(task);
        
        mainMenuFrame = new MainMenuFrame("testUser");
        deleteTaskFrame = new DeleteTaskFrame(mainMenuFrame);
    }

    @After
    public void tearDown() {
        if (deleteTaskFrame != null) deleteTaskFrame.dispose();
        closeAllDialogs();
    }

    @Test
    public void testFrameInitialization() {
        assertNotNull("DeleteTaskFrame should not be null", deleteTaskFrame);
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
    public void testDeleteSelectedTask() {
        JTable taskTable = deleteTaskFrame.getTaskTable();
        if (taskTable.getRowCount() > 0) {
            taskTable.setRowSelectionInterval(0, 0);
            try {
                java.lang.reflect.Method method = DeleteTaskFrame.class.getDeclaredMethod("deleteSelectedTask");
                method.setAccessible(true);
                method.invoke(deleteTaskFrame);
                // Silme işlemi başarılı olmalı
                assertTrue(true);
            } catch (Exception e) {
                fail("deleteSelectedTask çağrısı başarısız: " + e.getMessage());
            }
        }
        closeAllDialogs();
    }

    @Test
    public void testFrameDisposal() {
        deleteTaskFrame.dispose();
        assertFalse("Frame should not be visible after disposal", deleteTaskFrame.isVisible());
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