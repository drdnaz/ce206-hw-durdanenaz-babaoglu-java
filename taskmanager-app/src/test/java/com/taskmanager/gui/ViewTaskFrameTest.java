package com.taskmanager.gui;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import com.taskmanager.model.Task;
import java.util.Date;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ViewTaskFrameTest {
    private ViewTaskFrame viewTaskFrame;
    private MainMenuFrame mainMenuFrame;
    private static boolean warningShown = false;

    @Before
    public void setUp() {
        // Create a clean state for testing
        mainMenuFrame = new MainMenuFrame("testUser");
        viewTaskFrame = new ViewTaskFrame(mainMenuFrame);
        viewTaskFrame.setVisible(true);
        
        // Clear any existing data
        DefaultTableModel model = (DefaultTableModel) viewTaskFrame.getTaskTable().getModel();
        model.setRowCount(0);
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

    @Test
    public void testRefreshButtonAction() {
        JButton refreshButton = (JButton) getPrivateField(viewTaskFrame, "refreshButton");
        assertNotNull(refreshButton);
        for (ActionListener al : refreshButton.getActionListeners()) {
            al.actionPerformed(new java.awt.event.ActionEvent(refreshButton, ActionEvent.ACTION_PERFORMED, ""));
        }
    }

    @Test
    public void testCloseButtonAction() {
        JButton closeButton = (JButton) getPrivateField(viewTaskFrame, "closeButton");
        assertNotNull(closeButton);
        for (ActionListener al : closeButton.getActionListeners()) {
            al.actionPerformed(new java.awt.event.ActionEvent(closeButton, ActionEvent.ACTION_PERFORMED, ""));
        }
    }

    @Test
    public void testDetailsButtonAction() {
        JButton detailsButton = (JButton) getPrivateField(viewTaskFrame, "detailsButton");
        assertNotNull(detailsButton);
        for (ActionListener al : detailsButton.getActionListeners()) {
            al.actionPerformed(new java.awt.event.ActionEvent(detailsButton, ActionEvent.ACTION_PERFORMED, ""));
        }
    }

    @Test
    public void testFilterTasks() {
        // Get the table and filter components
        JTable taskTable = viewTaskFrame.getTaskTable();
        JTextField searchField = viewTaskFrame.getSearchField();
        JComboBox<String> categoryFilter = viewTaskFrame.getCategoryFilterComboBox();
        JComboBox<String> priorityFilter = viewTaskFrame.getPriorityFilterComboBox();
        
        // Clear existing data and add test data
        DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
        model.setRowCount(0); // Clear all rows
        model.addRow(new Object[]{1, "Test Task 1", "Description 1", "01/01/2024", "Work", "High", "Pending"});
        model.addRow(new Object[]{2, "Test Task 2", "Description 2", "02/01/2024", "Personal", "Low", "Completed"});
        
        try {
            // Get the filterTasks method using reflection
            java.lang.reflect.Method filterTasksMethod = ViewTaskFrame.class.getDeclaredMethod("filterTasks");
            filterTasksMethod.setAccessible(true);
            
            // Test search filter
            searchField.setText("Test Task 1");
            filterTasksMethod.invoke(viewTaskFrame);
            Thread.sleep(100); // Give time for the table to update
            assertEquals("Should filter to 1 row", 1, taskTable.getRowSorter().getViewRowCount());
            
            // Test category filter
            searchField.setText("");
            categoryFilter.setSelectedItem("Work");
            filterTasksMethod.invoke(viewTaskFrame);
            Thread.sleep(100); // Give time for the table to update
            assertEquals("Should filter to 1 row", 1, taskTable.getRowSorter().getViewRowCount());
            
            // Test priority filter
            categoryFilter.setSelectedItem("All");
            priorityFilter.setSelectedItem("High");
            filterTasksMethod.invoke(viewTaskFrame);
            Thread.sleep(100); // Give time for the table to update
            assertEquals("Should filter to 1 row", 1, taskTable.getRowSorter().getViewRowCount());
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testShowTaskDetails() {
        // Get the table and add test data
        JTable taskTable = viewTaskFrame.getTaskTable();
        DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
        model.addRow(new Object[]{1, "Test Task", "Test Description", "01/01/2024", "Work", "High", "Pending"});
        
        // Select the first row
        taskTable.setRowSelectionInterval(0, 0);
        
        // Get the details button and trigger its action
        JButton detailsButton = (JButton) getPrivateField(viewTaskFrame, "detailsButton");
        assertNotNull("Details button should exist", detailsButton);
        
        // Trigger the action listener
        for (ActionListener al : detailsButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(detailsButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        
        // Verify that a dialog was shown (we can't directly test the dialog content)
        boolean dialogShown = false;
        for (Window window : Window.getWindows()) {
            if (window instanceof JDialog) {
                dialogShown = true;
                window.dispose();
                break;
            }
        }
        assertTrue("Task details dialog should be shown", dialogShown);
        
        // Test with no selection
        taskTable.clearSelection();
        for (ActionListener al : detailsButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(detailsButton, ActionEvent.ACTION_PERFORMED, ""));
        }
        
        // Verify that a warning dialog was shown
        boolean warningShown = false;
        for (Window window : Window.getWindows()) {
            if (window instanceof JDialog) {
                warningShown = true;
                window.dispose();
                break;
            }
        }
        assertTrue("Warning dialog should be shown when no task is selected", warningShown);
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

    public static void closeAllDialogs() {
        for (Window window : Window.getWindows()) {
            if (window instanceof JDialog) {
                window.dispose();
            }
        }
    }
} 