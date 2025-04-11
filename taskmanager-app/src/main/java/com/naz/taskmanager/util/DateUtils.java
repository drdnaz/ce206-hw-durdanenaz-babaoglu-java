package com.naz.taskmanager.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility class for date operations.
 * Provides common date manipulation and formatting operations used across the application.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public class DateUtils {
    /** Date format for database storage */
    public static final SimpleDateFormat DB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /** Date format for user interface display */
    public static final SimpleDateFormat UI_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    /**
     * Converts UI date format to database format
     * 
     * @param date Date to format
     * @return Formatted date string for database storage
     */
    public static String convertUIDateToDBFormat(Date date) {
        if (date == null) {
            return null;
        }
        return DB_DATE_FORMAT.format(date);
    }
    
    /**
     * Converts database date to UI format
     * 
     * @param date Date to convert
     * @return Formatted string for UI display
     */
    public static String convertDBDateToUIFormat(Date date) {
        if (date == null) {
            return "Not set";
        }
        return UI_DATE_FORMAT.format(date);
    }
    
    /**
     * Formats date to ISO 8601 format for SQLite
     * 
     * @param date Date to format
     * @return ISO 8601 formatted date string
     */
    public static String toSQLiteFormat(Date date) {
        if (date == null) {
            return null;
        }
        return DB_DATE_FORMAT.format(date);
    }
    
    /**
     * Formats a date using the UI date format
     * 
     * @param date Date to format
     * @return Formatted date string
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "Not set";
        }
        return UI_DATE_FORMAT.format(date);
    }
    
    /**
     * Parses date from string using UI date format
     * 
     * @param dateStr Date string in format dd/MM/yyyy HH:mm
     * @return Parsed date object
     * @throws ParseException if the string cannot be parsed
     */
    public static Date parseDate(String dateStr) throws ParseException {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        return UI_DATE_FORMAT.parse(dateStr);
    }
    
    /**
     * Adds minutes to a date
     * 
     * @param date Original date
     * @param minutes Minutes to add
     * @return New date with added minutes
     */
    public static Date addMinutes(Date date, int minutes) {
        if (date == null) {
            return null;
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }
    
    /**
     * Calculates days between two dates
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return Number of days between dates
     */
    public static int daysBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        
        long diffTime = endDate.getTime() - startDate.getTime();
        return (int) (diffTime / (24 * 60 * 60 * 1000));
    }
    
    /**
     * Checks if a date is in the past
     * 
     * @param date Date to check
     * @return true if date is in the past
     */
    public static boolean isInPast(Date date) {
        if (date == null) {
            return false;
        }
        return date.before(new Date());
    }
    
    /**
     * Gets a date with time set to end of day (23:59:59)
     * 
     * @param date Original date
     * @return Date with time set to end of day
     */
    public static Date getEndOfDay(Date date) {
        if (date == null) {
            return null;
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
    
    /**
     * Gets a date with time set to start of day (00:00:00)
     * 
     * @param date Original date
     * @return Date with time set to start of day
     */
    public static Date getStartOfDay(Date date) {
        if (date == null) {
            return null;
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    /**
     * Adds days to a date
     * 
     * @param date Original date
     * @param days Days to add
     * @return New date with added days
     */
    public static Date addDays(Date date, int days) {
        if (date == null) {
            return null;
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
    
    /**
     * Formats date with custom format
     * 
     * @param date Date to format
     * @param format Format pattern
     * @return Formatted date string
     */
    public static String formatWithPattern(Date date, String format) {
        if (date == null) {
            return "Not set";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}