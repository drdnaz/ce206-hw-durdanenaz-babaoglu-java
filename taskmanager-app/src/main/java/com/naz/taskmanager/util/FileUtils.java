// FileUtils.java
package com.naz.taskmanager.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for file operations
 */
public class FileUtils {
    
    /**
     * Write object to file
     * @param obj Object to write
     * @param filePath File path
     * @return true if successful
     */
    public static boolean writeObjectToFile(Object obj, String filePath) {
        File dir = new File(filePath).getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(obj);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Read object from file
     * @param filePath File path
     * @return Read object
     */
    public static Object readObjectFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading from file: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Write object list to file
     * @param objList List of objects to write
     * @param filePath File path
     * @return true if successful
     */
    public static boolean writeListToFile(List<?> objList, String filePath) {
        return writeObjectToFile(objList, filePath);
    }
    
    /**
     * Read object list from file
     * @param filePath File path
     * @return Read list of objects
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> readListFromFile(String filePath) {
        Object obj = readObjectFromFile(filePath);
        if (obj instanceof List<?>) {
            try {
                return (List<T>) obj;
            } catch (ClassCastException e) {
                System.err.println("Error casting to List: " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }
    
    /**
     * Ensure directory exists
     * @param dirPath Directory path
     * @return true if directory exists or was created
     */
    public static boolean ensureDirectoryExists(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return dir.isDirectory();
    }
    
    /**
     * Delete file
     * @param filePath File path
     * @return true if file was deleted or doesn't exist
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return true;
        }
        return file.delete();
    }
}