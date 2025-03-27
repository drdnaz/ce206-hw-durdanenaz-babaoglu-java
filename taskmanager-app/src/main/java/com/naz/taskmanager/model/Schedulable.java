package com.naz.taskmanager.model;

import java.util.Date;

/**
 * Interface for items that can have deadlines
 * Implements interface segregation principle
 */
public interface Schedulable {
    Date getDeadline();
    void setDeadline(Date deadline);
    boolean isOverdue();
    int getDaysUntilDeadline();
}