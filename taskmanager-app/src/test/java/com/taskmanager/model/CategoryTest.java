package com.taskmanager.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.lang.reflect.Field;
import org.junit.*;

public class CategoryTest {
    
    private Category testCategory;
    
    @Before
    public void setUp() throws Exception {
        resetCategoryStatics();
        // Her test öncesi yeni bir kategori oluştur
        testCategory = new Category("Test Category", "Test Description", "#FF0000");
    }
    
    @After
    public void tearDown() throws Exception {
        resetCategoryStatics();
    }
    
    private void resetCategoryStatics() throws Exception {
        // allCategories listesini temizle
        Field allCategoriesField = Category.class.getDeclaredField("allCategories");
        allCategoriesField.setAccessible(true);
        List<Category> allCategories = (List<Category>) allCategoriesField.get(null);
        allCategories.clear();

        // nextId'yi sıfırla
        Field nextIdField = Category.class.getDeclaredField("nextId");
        nextIdField.setAccessible(true);
        nextIdField.setInt(null, 1);

        // Varsayılan kategorileri tekrar ekle
        Category.addCategory(new Category("Work", "Work related tasks", "#FF5733"));
        Category.addCategory(new Category("Personal", "Personal tasks", "#33FF57"));
        Category.addCategory(new Category("Study", "Study related tasks", "#3357FF"));
        Category.addCategory(new Category("Health", "Health and fitness", "#FF33A8"));
        Category.addCategory(new Category("Other", "Other tasks", "#33FFF5"));
    }
    
    @Test
    public void testConstructorAndGetters() {
        assertEquals("Test Category", testCategory.getName());
        assertEquals("Test Description", testCategory.getDescription());
        assertEquals("#FF0000", testCategory.getColor());
        assertTrue(testCategory.getId() > 0);
    }
    
    @Test
    public void testSetters() {
        testCategory.setName("New Name");
        testCategory.setDescription("New Description");
        testCategory.setColor("#00FF00");
        
        assertEquals("New Name", testCategory.getName());
        assertEquals("New Description", testCategory.getDescription());
        assertEquals("#00FF00", testCategory.getColor());
    }
    
    @Test
    public void testGetAllCategories() {
        List<Category> categories = Category.getAllCategories();
        assertNotNull(categories);
        assertTrue(categories.size() >= 5); // En az 5 varsayılan kategori olmalı
    }
    
    @Test
    public void testGetCategoryNames() {
        String[] names = Category.getCategoryNames();
        assertNotNull(names);
        assertTrue(names.length >= 5); // En az 5 varsayılan kategori ismi olmalı
        assertTrue(containsIgnoreCase(names, "Work"));
        assertTrue(containsIgnoreCase(names, "Personal"));
        assertTrue(containsIgnoreCase(names, "Study"));
    }
    
    @Test
    public void testAddCategory() {
        int initialSize = Category.getAllCategories().size();
        Category newCategory = new Category("New Category", "New Description", "#0000FF");
        Category.addCategory(newCategory);
        
        assertEquals(initialSize + 1, Category.getAllCategories().size());
        assertTrue(Category.getAllCategories().contains(newCategory));
    }
    
    @Test
    public void testAddDuplicateCategory() {
        int initialSize = Category.getAllCategories().size();
        Category.addCategory(testCategory);
        Category.addCategory(testCategory); // Aynı kategoriyi tekrar ekle
        
        assertEquals(initialSize + 1, Category.getAllCategories().size());
    }
    
    @Test
    public void testDeleteCategory() {
        Category newCategory = new Category("To Delete", "Will be deleted", "#FF00FF");
        Category.addCategory(newCategory);
        int categoryId = newCategory.getId();
        
        assertTrue(Category.deleteCategory(categoryId));
        assertNull(Category.findCategoryById(categoryId));
    }
    
    @Test
    public void testDeleteDefaultCategory() {
        // Varsayılan kategorileri silmeye çalış (ID 1-5)
        assertFalse(Category.deleteCategory(1));
        assertFalse(Category.deleteCategory(2));
        assertFalse(Category.deleteCategory(3));
        assertFalse(Category.deleteCategory(4));
        assertFalse(Category.deleteCategory(5));
    }
    
    @Test
    public void testFindCategoryById() {
        Category.addCategory(testCategory);
        Category found = Category.findCategoryById(testCategory.getId());
        assertNotNull(found);
        assertEquals(testCategory.getId(), found.getId());
        assertEquals(testCategory.getName(), found.getName());
    }
    
    @Test
    public void testFindCategoryByName() {
        Category.addCategory(testCategory);
        Category found = Category.findCategoryByName("Test Category");
        assertNotNull(found);
        assertEquals(testCategory.getName(), found.getName());
    }
    
    @Test
    public void testFindCategoryByNameCaseInsensitive() {
        Category.addCategory(testCategory);
        Category found = Category.findCategoryByName("test category");
        assertNotNull(found);
        assertEquals(testCategory.getName(), found.getName());
    }
    
    @Test
    public void testUpdateCategory() {
        Category.addCategory(testCategory);
        boolean updated = Category.updateCategory(
            testCategory.getId(),
            "Updated Name",
            "Updated Description",
            "#00FF00"
        );
        
        assertTrue(updated);
        Category found = Category.findCategoryById(testCategory.getId());
        assertEquals("Updated Name", found.getName());
        assertEquals("Updated Description", found.getDescription());
        assertEquals("#00FF00", found.getColor());
    }
    
    @Test
    public void testUpdateCategoryWithExistingName() {
        Category category1 = new Category("Category 1", "Description 1", "#FF0000");
        Category category2 = new Category("Category 2", "Description 2", "#00FF00");
        Category.addCategory(category1);
        Category.addCategory(category2);
        
        boolean updated = Category.updateCategory(
            category1.getId(),
            "Category 2", // Mevcut bir isim
            "New Description",
            "#0000FF"
        );
        
        assertFalse(updated);
        Category found = Category.findCategoryById(category1.getId());
        assertEquals("Category 1", found.getName()); // İsim değişmemeli
    }
    
    // Yardımcı metod
    private boolean containsIgnoreCase(String[] array, String value) {
        for (String item : array) {
            if (item.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
} 