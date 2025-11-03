package edu.asu.cse360.hw3;

/**
 * Role-based access control system for managing user permissions.
 * 
 * <p>This controller enforces security policies by validating user permissions
 * based on their roles and session state. It provides centralized access control
 * for all application features and operations.</p>
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public class RoleBasedAccessController {
    
    private PermissionRepository permissionRepository;
    
    public RoleBasedAccessController(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }
    
    public AccessResult checkAccess(UserSession session, AccessPermission permission) {
        // Check authentication
        if (!session.isAuthenticated()) {
            return AccessResult.denied("Authentication required. Please login to continue.");
        }
        
        // Check session expiration
        if (session.isExpired()) {
            return AccessResult.denied("Session has expired. Please login again.");
        }
        
        // Check role-based permissions
        UserRole role = session.getUserRole();
        if (role == UserRole.ADMIN) {
            // Admins have access to everything
            return AccessResult.granted();
        }
        
        if (permissionRepository.hasPermission(role, permission)) {
            return AccessResult.granted();
        }
        
        return AccessResult.denied("Insufficient permissions for this operation.");
    }
}