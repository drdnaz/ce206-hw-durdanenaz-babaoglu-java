package com.naz.taskmanager.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a category for Taskmanager items.
 * Categories help organize tasks by grouping them into logical collections.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class Category implements Serializable {
    /** Unique identifier for serialization */
    private static final long serialVersionUID = 1L;

    // Static alanlar
    private static int nextId = 1;
    private static final List<Category> allCategories = new ArrayList<>();

    private int id;
    private String name;
    private String description;
    private String color;

    // Varsayılan kategoriler
    static {
        addCategory(new Category("Work", "Work related tasks", "#FF5733"));
        addCategory(new Category("Personal", "Personal tasks", "#33FF57"));
        addCategory(new Category("Study", "Study related tasks", "#3357FF"));
        addCategory(new Category("Health", "Health and fitness", "#FF33A8"));
        addCategory(new Category("Other", "Other tasks", "#33FFF5"));
    }

    /**
     * Constructor for creating a new Category
     * 
     * @param name Name of the category
     * @param description Description of the category
     * @param color Color of the category
     */
    public Category(String name, String description, String color) {
        this.id = nextId++;
        this.name = name;
        this.description = description;
        this.color = color;
    }

    /**
     * Constructor for creating a new Category with only a name
     * 
     * @param name Name of the category
     */
    public Category(String name) {
        this(name, "", "#FFFFFF");
    }

    /**
     * Gets the name of the category
     * 
     * @return Category name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the category
     * 
     * @param name New category name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the category
     * 
     * @return Category description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the category
     * 
     * @param description New category description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the color of the category
     * 
     * @return Category color
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the color of the category
     * 
     * @param color New category color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Gets the ID of the category
     * 
     * @return Category ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns a list of all categories
     * 
     * @return List of all categories
     */
    public static List<Category> getAllCategories() {
        return allCategories;
    }

    /**
     * Adds a new category to the list of all categories
     * 
     * @param category Category to add
     */
    public static void addCategory(Category category) {
        for (Category existing : allCategories) {
            if (existing.getName().equalsIgnoreCase(category.getName())) {
                return;
            }
        }
        allCategories.add(category);
    }

    /**
     * Deletes a category from the list of all categories
     * 
     * @param categoryId ID of the category to delete
     * @return true if the category was deleted, false otherwise
     */
    public static boolean deleteCategory(int categoryId) {
        for (int i = 0; i < allCategories.size(); i++) {
            if (allCategories.get(i).getId() == categoryId) {
                if (categoryId <= 5) return false;
                allCategories.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Finds a category by its ID
     * 
     * @param categoryId ID of the category to find
     * @return Category with the specified ID, or null if not found
     */
    public static Category findCategoryById(int categoryId) {
        for (Category category : allCategories) {
            if (category.getId() == categoryId) return category;
        }
        return null;
    }

    /**
     * Finds a category by its name
     * 
     * @param name Name of the category to find
     * @return Category with the specified name, or null if not found
     */
    public static Category findCategoryByName(String name) {
        for (Category category : allCategories) {
            if (category.getName().equalsIgnoreCase(name)) return category;
        }
        return null;
    }

    /**
     * Updates a category in the list of all categories
     * 
     * @param categoryId ID of the category to update
     * @param name New name for the category
     * @param description New description for the category
     * @param color New color for the category
     * @return true if the category was updated, false otherwise
     */
    public static boolean updateCategory(int categoryId, String name, String description, String color) {
        Category category = findCategoryById(categoryId);
        if (category != null) {
            for (Category existing : allCategories) {
                if (existing.getId() != categoryId && existing.getName().equalsIgnoreCase(name)) {
                    return false;
                }
            }
            category.setName(name);
            category.setDescription(description);
            category.setColor(color);
            return true;
        }
        return false;
    }

    /**
     * Compares this category with another object for equality.
     * Two categories are equal if they have the same name.
     * 
     * @param obj Object to compare with
     * @return true if the categories are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category category = (Category) obj;
        return name.equalsIgnoreCase(category.name);
    }

    /**
     * Returns a hash code value for the category.
     * 
     * @return Hash code value
     */
    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    /**
     * Returns a string representation of the category.
     * 
     * @return String representation of the category
     */
    @Override
    public String toString() {
        return name;
    }
}