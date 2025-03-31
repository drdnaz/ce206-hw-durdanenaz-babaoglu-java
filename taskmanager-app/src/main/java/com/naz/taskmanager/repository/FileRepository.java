// FileRepository.java
package com.naz.taskmanager.repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract file repository class for serializable objects
 * @param <T> Type of entity to store
 */
public abstract class FileRepository<T> implements Repository<T> {
    protected final String dataFolder;
    protected final String fileName;
    
    /**
     * Constructor for FileRepository
     * @param dataFolder Folder to store data
     * @param fileName File name for data storage
     */
    public FileRepository(String dataFolder, String fileName) {
        this.dataFolder = dataFolder;
        this.fileName = fileName;
        
        // Ensure data folder exists
        File folder = new File(dataFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
    
    /**
     * Get full file path
     * @return Full file path
     */
    protected String getFilePath() {
        return dataFolder + File.separator + fileName;
    }
    
    /**
     * Save list of items to file
     * @param items Items to save
     * @return true if successful
     */
    protected boolean saveToFile(List<T> items) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getFilePath()))) {
            oos.writeObject(items);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Load list of items from file
     * @return Loaded items list
     */
    @SuppressWarnings("unchecked")
    protected List<T> loadFromFile() {
        File file = new File(getFilePath());
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Get entity by ID
     * @param id Entity ID
     * @return Entity with matching ID
     */
    @Override
    public abstract T getById(String id);
    
    /**
     * Get all entities
     * @return List of all entities
     */
    @Override
    public abstract List<T> getAll();
    
    /**
     * Save new entity
     * @param item Entity to save
     */
    @Override
    public abstract void save(T item);
    
    /**
     * Update existing entity
     * @param item Entity to update
     */
    @Override
    public abstract void update(T item);
    
    /**
     * Delete entity by ID
     * @param id ID of entity to delete
     */
    @Override
    public abstract void delete(String id);
}