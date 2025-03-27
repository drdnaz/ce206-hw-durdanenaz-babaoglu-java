package com.naz.taskmanager.repository;

import java.util.List;

/**
 * Generic interface for repositories
 * Implements Dependency Inversion principle
 * @param <T> Type of entity to store
 */
public interface Repository<T> {
    void save(T item);
    T getById(String id);
    List<T> getAll();
    void update(T item);
    void delete(String id);
}