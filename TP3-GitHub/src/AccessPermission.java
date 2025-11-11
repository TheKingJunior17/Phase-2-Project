package edu.asu.cse360.hw3;

/**
 * Enumeration for access permissions in the system.
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public enum AccessPermission {
    DELETE_QUESTION("DELETE_QUESTION", "Delete Question"),
    MANAGE_USERS("MANAGE_USERS", "Manage Users"),
    VIEW_ALL_QUESTIONS("VIEW_ALL_QUESTIONS", "View All Questions"),
    SUBMIT_QUESTION("SUBMIT_QUESTION", "Submit Question"),
    VIEW_QUESTION("VIEW_QUESTION", "View Question"),
    VIEW_QUESTIONS("VIEW_QUESTIONS", "View Questions"),
    SUBMIT_QUESTIONS("SUBMIT_QUESTIONS", "Submit Questions"),
    VIEW_ANSWERS("VIEW_ANSWERS", "View Answers"),
    SUBMIT_ANSWERS("SUBMIT_ANSWERS", "Submit Answers"),
    EDIT_QUESTIONS("EDIT_QUESTIONS", "Edit Questions"),
    EDIT_ANSWERS("EDIT_ANSWERS", "Edit Answers"),
    DELETE_ANSWERS("DELETE_ANSWERS", "Delete Answers"),
    APPROVE_CONTENT("APPROVE_CONTENT", "Approve Content"),
    MODERATE_CONTENT("MODERATE_CONTENT", "Moderate Content"),
    ADMIN_PANEL("ADMIN_PANEL", "Admin Panel"),
    SYSTEM_SETTINGS("SYSTEM_SETTINGS", "System Settings");
    
    private final String code;
    private final String displayName;
    
    AccessPermission(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
    
    public String getCode() { return code; }
    public String getDisplayName() { return displayName; }
}