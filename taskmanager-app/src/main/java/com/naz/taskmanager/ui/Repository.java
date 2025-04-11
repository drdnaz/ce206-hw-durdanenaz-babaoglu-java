package com.naz.taskmanager.ui;

import java.util.List;

/**
 * Interface for UI repositories.
 * Defines standard CRUD operations for UI-related data persistence.
 * 
 * @param <T> Type of entity to store
 * @author TaskManager Team
 * @version 1.0
 */
public interface Repository<T> {
    /**
     * Saves an entity.
     * 
     * @param item Entity to save
     */
    void save(T item);
    
    /**
     * Retrieves an entity by its ID.
     * 
     * @param id Entity ID
     * @return Entity with matching ID
     */
    T getById(String id);
    
    /**
     * Retrieves all entities.
     * 
     * @return List of all entities
     */
    List<T> getAll();
    
    /**
     * Updates an entity.
     * 
     * @param item Entity to update
     */
    void update(T item);
    
    /**
     * Deletes an entity.
     * 
     * @param id Entity ID to delete
     */
    void delete(String id);
}