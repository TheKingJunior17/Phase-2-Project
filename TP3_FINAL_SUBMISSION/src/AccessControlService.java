package edu.asu.cse360.hw3;

/**
 * Service for enforcing role-based access control.
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public class AccessControlService {
    
    /**
     * Checks if a user has permission for a specific action.
     */
    public boolean hasPermission(String userId, UserRole userRole, AccessPermission permission) {
        if (userId == null || userRole == null || permission == null) {
            return false;
        }
        
        switch (permission) {
            case DELETE_QUESTION:
                return userRole == UserRole.ADMIN || userRole == UserRole.INSTRUCTOR;
            case MANAGE_USERS:
                return userRole == UserRole.ADMIN;
            case VIEW_ALL_QUESTIONS:
                return userRole == UserRole.ADMIN || userRole == UserRole.INSTRUCTOR;
            case SUBMIT_QUESTION:
                return userRole != UserRole.GUEST;
            case VIEW_QUESTION:
                return true; // All users can view questions
            default:
                return false;
        }
    }
    
    /**
     * Validates access to a specific resource.
     */
    public AccessResult validateAccess(String userId, UserRole userRole, String resourceId, AccessPermission permission) {
        if (!hasPermission(userId, userRole, permission)) {
            return AccessResult.denied("Access denied for permission: " + permission);
        }
        
        // Additional resource-specific checks could go here
        return AccessResult.granted();
    }
}