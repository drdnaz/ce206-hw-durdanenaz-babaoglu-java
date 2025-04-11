package com.naz.taskmanager.model;

import java.util.Date;

/**
 * Interface for items that can have deadlines.
 * Implements the Interface Segregation Principle by separating the scheduling
 * behavior from other item behaviors.
 * 
 * @author TaskManager Team
 * @version 1.0
 */
public interface Schedulable {
    /**
     * Gets the deadline of the item
     * 
     * @return Deadline date
     */
    Date getDeadline();
    
    /**
     * Sets the deadline of the item
     * 
     * @param deadline New deadline date
     */
    void setDeadline(Date deadline);
    
    /**
     * Checks if the item is overdue based on its deadline
     * 
     * @return true if item is overdue
     */
    boolean isOverdue();
    
    /**
     * Calculates the number of days until the deadline
     * 
     * @return Days until deadline, or a special value if no deadline
     */
    int getDaysUntilDeadline();
}