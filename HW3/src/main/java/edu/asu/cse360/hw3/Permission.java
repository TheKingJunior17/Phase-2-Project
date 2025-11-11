package edu.asu.cse360.hw3;

/**
 * Enumeration of permissions available in the system for role-based access control.
 * 
 * @author Jose Mendoza
 * @version 1.0.0
 * @since 2025-11-09
 */
public enum Permission {
    /**
     * Permission to submit new questions to the system.
     */
    SUBMIT_QUESTION,
    
    /**
     * Permission to view all questions in the system.
     */
    VIEW_ALL_QUESTIONS,
    
    /**
     * Permission to edit existing questions.
     */
    EDIT_QUESTIONS,
    
    /**
     * Permission to delete questions from the system.
     */
    DELETE_QUESTION,
    
    /**
     * Permission to manage user accounts and roles.
     */
    MANAGE_USERS,
    
    /**
     * Permission to access administrative panel and functions.
     */
    ADMIN_PANEL
}