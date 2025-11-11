package edu.asu.cse360.hw3;

/**
 * Repository interface for checking role-based permissions.
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public interface PermissionRepository {
    boolean hasPermission(UserRole role, AccessPermission permission);
}