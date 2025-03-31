
package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Application configuration class
 */
public class AppConfig {
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties = new Properties();
    private static AppConfig instance;
    
    // Default configuration values
    private static final String DEFAULT_DB_PATH = "data/taskmanager.db";
    private static final String DEFAULT_USER_DATA_PATH = "data/users.bin";
    private static final String DEFAULT_LOG_LEVEL = "INFO";
    
    /**
     * Private constructor for Singleton pattern
     */
    private AppConfig() {
        loadProperties();
    }
    
    /**
     * Get singleton instance
     * @return Singleton instance
     */
    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }
    
    /**
     * Load properties from config file
     */
    private void loadProperties() {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            System.out.println("Configuration file not found. Using default configuration.");
            setDefaultProperties();
        }
    }
    
    /**
     * Set default properties
     */
    private void setDefaultProperties() {
        properties.setProperty("db.path", DEFAULT_DB_PATH);
        properties.setProperty("user.data.path", DEFAULT_USER_DATA_PATH);
        properties.setProperty("log.level", DEFAULT_LOG_LEVEL);
    }
    
    /**
     * Get property value
     * @param key Property key
     * @param defaultValue Default value if property not found
     * @return Property value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get database path
     * @return Database file path
     */
    public String getDatabasePath() {
        return getProperty("db.path", DEFAULT_DB_PATH);
    }
    
    /**
     * Get user data path
     * @return User data file path
     */
    public String getUserDataPath() {
        return getProperty("user.data.path", DEFAULT_USER_DATA_PATH);
    }
    
    /**
     * Get log level
     * @return Log level
     */
    public String getLogLevel() {
        return getProperty("log.level", DEFAULT_LOG_LEVEL);
    }
    
    /**
     * Get data directory path
     * @return Data directory path
     */
    public String getDataDirPath() {
        return getProperty("data.dir", "data");
    }
    
    /**
     * Get application name
     * @return Application name
     */
    public String getAppName() {
        return getProperty("app.name", "TaskManager");
    }
    
    /**
     * Get application version
     * @return Application version
     */
    public String getAppVersion() {
        return getProperty("app.version", "1.0");
    }
    
    /**
     * Check if email notifications are enabled
     * @return true if email notifications enabled
     */
    public boolean isEmailNotificationsEnabled() {
        return Boolean.parseBoolean(getProperty("email.notifications.enabled", "false"));
    }
    
    /**
     * Get email configuration
     * @return Email configuration properties
     */
    public Properties getEmailConfig() {
        Properties emailProps = new Properties();
        emailProps.setProperty("email.smtp.host", getProperty("email.smtp.host", ""));
        emailProps.setProperty("email.smtp.port", getProperty("email.smtp.port", "587"));
        emailProps.setProperty("email.username", getProperty("email.username", ""));
        emailProps.setProperty("email.password", getProperty("email.password", ""));
        return emailProps;
    }
}