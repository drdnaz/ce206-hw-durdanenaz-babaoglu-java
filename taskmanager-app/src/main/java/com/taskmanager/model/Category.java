package com.taskmanager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class for Category objects
 */
public class Category {
    private static int nextId = 1;
    private int id;
    private String name;
    private String description;
    private String color;
    
    // Static list to store all categories
    private static final List<Category> allCategories = new ArrayList<>();
    
    // Add default categories
    static {
        addCategory(new Category("Work", "Work related tasks", "#FF5733"));
        addCategory(new Category("Personal", "Personal tasks", "#33FF57"));
        addCategory(new Category("Study", "Study related tasks", "#3357FF"));
        addCategory(new Category("Health", "Health and fitness", "#FF33A8"));
        addCategory(new Category("Other", "Other tasks", "#33FFF5"));
    }
    
    /**
     * Creates a new category
     * @param name Category name
     * @param description Category description
     * @param color Color code in hex format (e.g. #FF5733)
     */
    public Category(String name, String description, String color) {
        this.id = nextId++;
        this.name = name;
        this.description = description;
        this.color = color;
    }
    
    /**
     * Get all categories
     * @return List of all categories
     */
    public static List<Category> getAllCategories() {
        return allCategories;
    }
    
    /**
     * Get category names as an array
     * @return Array of category names
     */
    public static String[] getCategoryNames() {
        String[] names = new String[allCategories.size()];
        for (int i = 0; i < allCategories.size(); i++) {
            names[i] = allCategories.get(i).getName();
        }
        return names;
    }
    
    /**
     * Add a category to the static list
     * @param category Category to add
     */
    public static void addCategory(Category category) {
        // Check if a category with the same name already exists
        for (Category existingCategory : allCategories) {
            if (existingCategory.getName().equalsIgnoreCase(category.getName())) {
                return; // Don't add duplicates
            }
        }
        allCategories.add(category);
    }
    
    /**
     * Delete a category by its ID
     * @param categoryId ID of the category to delete
     * @return true if category was found and deleted, false otherwise
     */
    public static boolean deleteCategory(int categoryId) {
        for (int i = 0; i < allCategories.size(); i++) {
            if (allCategories.get(i).getId() == categoryId) {
                // Don't delete if it's one of the default categories (ID 1-5)
                if (categoryId <= 5) {
                    return false;
                }
                allCategories.remove(i);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Find a category by its ID
     * @param categoryId ID of the category to find
     * @return the category if found, null otherwise
     */
    public static Category findCategoryById(int categoryId) {
        for (Category category : allCategories) {
            if (category.getId() == categoryId) {
                return category;
            }
        }
        return null;
    }
    
    /**
     * Find a category by its name
     * @param name Name of the category to find
     * @return the category if found, null otherwise
     */
    public static Category findCategoryByName(String name) {
        for (Category category : allCategories) {
            if (category.getName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }
    
    /**
     * Update an existing category
     * @param categoryId ID of the category to update
     * @param name New name
     * @param description New description
     * @param color New color
     * @return true if category was found and updated, false otherwise
     */
    public static boolean updateCategory(int categoryId, String name, String description, String color) {
        Category category = findCategoryById(categoryId);
        if (category != null) {
            // Check if new name conflicts with existing category
            for (Category existingCategory : allCategories) {
                if (existingCategory.getId() != categoryId && 
                    existingCategory.getName().equalsIgnoreCase(name)) {
                    return false; // Name already exists
                }
            }
            
            category.setName(name);
            category.setDescription(description);
            category.setColor(color);
            return true;
        }
        return false;
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
} 