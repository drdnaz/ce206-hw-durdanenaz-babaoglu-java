package com.taskmanager.gui;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;

public class AddTaskFrameTest {
    private AddTaskFrame addTaskFrame;
    private MainMenuFrame mainMenuFrame;

@Before
public void setUp() {
    // Kategori veritabanına eklenmeli
    com.naz.taskmanager.model.Category category = new com.naz.taskmanager.model.Category("Test Category");
    com.naz.taskmanager.service.CategoryService categoryService = new com.naz.taskmanager.service.CategoryService();
    categoryService.addCategory(category);
    // Test kullanıcısı ile TaskService oluştur
    com.naz.taskmanager.service.TaskService taskService = new com.naz.taskmanager.service.TaskService("testUser");
    // En az bir görev ekle
    taskService.createTask("Test Task", "Test Description", category);

    mainMenuFrame = new MainMenuFrame("testUser");
    addTaskFrame = new AddTaskFrame(mainMenuFrame);
    // addTaskFrame.setVisible(true); // Headless modda pencere açma
}
    @Test
    public void testFrameInitialization() {
        assertNotNull("AddTaskFrame should not be null", addTaskFrame);
        assertEquals("Frame title should be 'Add New Task'", "Add New Task", addTaskFrame.getTitle());
    }

    @Test
    public void testComponentsInitialization() {
        // Test if all required components are initialized
        Component[] components = addTaskFrame.getContentPane().getComponents();
        assertTrue("Frame should have components", components.length > 0);
        
        // Test if the frame has the correct size
        assertEquals("Frame width should be 600", 600, addTaskFrame.getWidth());
        assertEquals("Frame height should be 500", 500, addTaskFrame.getHeight());
    }

    @Test
    public void testFormFields() {
        // Not: Bu testi çalıştırmak için AddTaskFrame sınıfında aşağıdaki getter metodları eklenmelidir:
        // getTaskTitleField(), getTaskDescriptionArea(), getDateSpinner(), getCategoryComboBox(), getPriorityComboBox()
        
        // Form alanlarının null olmaması gerekir
        assertNotNull("Task title field should not be null", addTaskFrame.getTaskTitleField());
        assertNotNull("Task description area should not be null", addTaskFrame.getTaskDescriptionArea());
        assertNotNull("Date spinner should not be null", addTaskFrame.getDateSpinner());
        assertNotNull("Category combo box should not be null", addTaskFrame.getCategoryComboBox());
        assertNotNull("Priority combo box should not be null", addTaskFrame.getPriorityComboBox());
        
        // ComboBox'ların doğru seçenekleri içermesi gerekir
        assertTrue("Category combo box should have items", addTaskFrame.getCategoryComboBox().getItemCount() > 0);
        assertTrue("Priority combo box should have items", addTaskFrame.getPriorityComboBox().getItemCount() > 0);
    }

    @Test
    public void testFrameDisposal() {
        addTaskFrame.dispose();
        assertFalse("Frame should not be visible after disposal", addTaskFrame.isVisible());
    }
} 