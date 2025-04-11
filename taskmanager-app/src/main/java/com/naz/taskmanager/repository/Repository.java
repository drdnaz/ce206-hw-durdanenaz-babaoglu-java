package com.naz.taskmanager.repository;

import java.util.List;

/**
 * Generic interface for repositories.
 * Implements the Dependency Inversion principle by defining a common interface
 * for all repository types.
 * 
 * @param <T> Type of entity to store
 * @author TaskManager Team
 * @version 1.0
 */
public interface Repository<T> {
    /**
     * Saves a new entity.
     * 
     * @param item Entity to save
     */
    void save(T item);
    
    /**
     * Retrieves an entity by its ID.
     * 
     * @param id Entity ID
     * @return Entity with matching ID, or null if not found
     */
    T getById(String id);
    
    /**
     * Retrieves all entities.
     * 
     * @return List of all entities
     */
    List<T> getAll();
    
    /**
     * Updates an existing entity.
     * 
     * @param item Entity to update
     */
    void update(T item);
    
    /**
     * Deletes an entity by its ID.
     * 
     * @param id ID of entity to delete
     */
    void delete(String id);
}