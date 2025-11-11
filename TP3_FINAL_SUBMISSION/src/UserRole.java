package edu.asu.cse360.hw3;

/**
 * Enumeration for user roles in the system.
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public enum UserRole {
    ADMIN("ADMIN", "System Administrator"),
    INSTRUCTOR("INSTRUCTOR", "Instructor"),
    STUDENT("STUDENT", "Student"),
    REVIEWER("REVIEWER", "Reviewer"),
    STAFF("STAFF", "Staff Member"),
    GUEST("GUEST", "Guest User"),
    SUSPENDED("SUSPENDED", "Suspended User"),
    BANNED("BANNED", "Banned User");
    
    private final String code;
    private final String displayName;
    
    UserRole(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
    
    public String getCode() { return code; }
    public String getDisplayName() { return displayName; }
}