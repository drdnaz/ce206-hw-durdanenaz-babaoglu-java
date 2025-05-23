package com.naz.taskmanager.service;

import com.naz.taskmanager.model.Category;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class CategoryServiceTest {
    
    private CategoryService categoryService;
    private Category testCategory;
    
    @Before
    public void setUp() {
        categoryService = new CategoryService();
        testCategory = new Category("Test Category");
    }
    
    @Test
    public void testConstructor() {
        List<Category> categories = categoryService.getAllCategories();
        assertNotNull(categories);
        assertEquals(4, categories.size()); // 4 varsayılan kategori
        
        // Varsayılan kategorilerin varlığını kontrol et
        assertTrue(containsCategoryWithName(categories, "Work"));
        assertTrue(containsCategoryWithName(categories, "Personal"));
        assertTrue(containsCategoryWithName(categories, "Study"));
        assertTrue(containsCategoryWithName(categories, "Health"));
    }
    
    @Test
    public void testAddCategory() {
        int initialSize = categoryService.getAllCategories().size();
        categoryService.addCategory(testCategory);
        assertEquals(initialSize + 1, categoryService.getAllCategories().size());
        assertTrue(containsCategoryWithName(categoryService.getAllCategories(), "Test Category"));
    }
    
    @Test
    public void testAddDuplicateCategory() {
        categoryService.addCategory(testCategory);
        int sizeAfterFirstAdd = categoryService.getAllCategories().size();
        categoryService.addCategory(testCategory);
        assertEquals(sizeAfterFirstAdd, categoryService.getAllCategories().size());
    }
    
    @Test
    public void testUpdateCategory() {
        categoryService.addCategory(testCategory);
        testCategory.setName("Updated Category");
        categoryService.updateCategory(testCategory);
        assertTrue(containsCategoryWithName(categoryService.getAllCategories(), "Updated Category"));
        assertFalse(containsCategoryWithName(categoryService.getAllCategories(), "Test Category"));
    }
    
    @Test
    public void testDeleteCategory() {
        categoryService.addCategory(testCategory);
        int sizeAfterAdd = categoryService.getAllCategories().size();
        categoryService.deleteCategory(testCategory);
        assertEquals(sizeAfterAdd - 1, categoryService.getAllCategories().size());
        assertFalse(containsCategoryWithName(categoryService.getAllCategories(), "Test Category"));
    }
    
    // Yardımcı metod
    private boolean containsCategoryWithName(List<Category> categories, String name) {
        for (Category category : categories) {
            if (category.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
} 