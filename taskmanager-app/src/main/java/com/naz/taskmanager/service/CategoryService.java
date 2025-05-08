package com.naz.taskmanager.service;

import com.naz.taskmanager.model.Category;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private final List<Category> categories;

    public CategoryService() {
        this.categories = new ArrayList<>();
        // Varsayılan kategoriler
        categories.add(new Category("Work"));
        categories.add(new Category("Personal"));
        categories.add(new Category("Study"));
        categories.add(new Category("Health"));
    }

    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }

    public void addCategory(Category category) {
        if (!categories.contains(category)) {
            categories.add(category);
        }
    }

    public void updateCategory(Category category) {
        // Kategori ismi güncellendiğinde listede güncelle
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).equals(category)) {
                categories.set(i, category);
                break;
            }
        }
    }

    public void deleteCategory(Category category) {
        categories.remove(category);
    }
} 