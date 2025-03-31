// Repository.java (UI Paketi içinde)
package com.naz.taskmanager.ui;

import java.util.List;

/**
 * Interface for UI repositories
 * @param <T> Type of entity to store
 */
public interface Repository<T> {
    /**
     * Save entity
     * @param item Entity to save
     */
    void save(T item);
    
    /**
     * Get entity by ID
     * @param id Entity ID
     * @return Entity with matching ID
     */
    T getById(String id);
    
    /**
     * Get all entities
     * @return List of all entities
     */
    List<T> getAll();
    
    /**
     * Update entity
     * @param item Entity to update
     */
    void update(T item);
    
    /**
     * Delete entity
     * @param id Entity ID to delete
     */
    void delete(String id);
}