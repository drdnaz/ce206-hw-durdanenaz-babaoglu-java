package com.naz.taskmanager.model;

import java.io.Serializable;

/**
 * Represents user notification preferences.
 * Controls how and when users receive notifications about tasks and reminders.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class NotificationSettings {
    /** Unique identifier for serialization */
    private static final long serialVersionUID = 1L;

    /** Flag for email notifications */
    private boolean emailEnabled = true;

    /** Flag for app notifications */
    private boolean appNotificationsEnabled = true;

    /** Default reminder time in minutes before deadline */
    private int defaultReminderMinutes = 30;

    /**
     * Checks if email notifications are enabled
     * 
     * @return Email notification status
     */
    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    /**
     * Sets email notification status
     * 
     * @param emailEnabled New email notification status
     */
    public void setEmailEnabled(boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    /**
     * Checks if app notifications are enabled
     * 
     * @return App notification status
     */
    public boolean isAppNotificationsEnabled() {
        return appNotificationsEnabled;
    }

    /**
     * Sets app notification status
     * 
     * @param appNotificationsEnabled New app notification status
     */
    public void setAppNotificationsEnabled(boolean appNotificationsEnabled) {
        this.appNotificationsEnabled = appNotificationsEnabled;
    }

    /**
     * Gets default reminder time
     * 
     * @return Default reminder time in minutes
     */
    public int getDefaultReminderMinutes() {
        return defaultReminderMinutes;
    }

    /**
     * Sets default reminder time
     * 
     * @param defaultReminderMinutes New default reminder time in minutes
     */
    public void setDefaultReminderMinutes(int defaultReminderMinutes) {
        this.defaultReminderMinutes = defaultReminderMinutes;
    }
}