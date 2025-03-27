package com.naz.taskmanager.model;

import java.io.Serializable;

/**
 * Represents user notification preferences
 */
public class NotificationSettings implements Serializable {
    /** Unique identifier for serialization */
    private static final long serialVersionUID = 1L;

    /** Flag for email notifications */
    private boolean emailEnabled = true;

    /** Flag for app notifications */
    private boolean appNotificationsEnabled = true;

    /** Default reminder time in minutes before deadline */
    private int defaultReminderMinutes = 30;

    /**
     * Check if email notifications are enabled
     * @return email notification status
     */
    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    /**
     * Set email notification status
     * @param emailEnabled new email notification status
     */
    public void setEmailEnabled(boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    /**
     * Check if app notifications are enabled
     * @return app notification status
     */
    public boolean isAppNotificationsEnabled() {
        return appNotificationsEnabled;
    }

    /**
     * Set app notification status
     * @param appNotificationsEnabled new app notification status
     */
    public void setAppNotificationsEnabled(boolean appNotificationsEnabled) {
        this.appNotificationsEnabled = appNotificationsEnabled;
    }

    /**
     * Get default reminder time
     * @return default reminder time in minutes
     */
    public int getDefaultReminderMinutes() {
        return defaultReminderMinutes;
    }

    /**
     * Set default reminder time
     * @param defaultReminderMinutes new default reminder time in minutes
     */
    public void setDefaultReminderMinutes(int defaultReminderMinutes) {
        this.defaultReminderMinutes = defaultReminderMinutes;
    }
}