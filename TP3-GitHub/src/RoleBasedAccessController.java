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
    
    // Permission matrix for TP3 demonstration
    private static final java.util.Map<UserRole, java.util.Set<AccessPermission>> ROLE_PERMISSIONS = 
        new java.util.HashMap<UserRole, java.util.Set<AccessPermission>>() {{
            put(UserRole.STUDENT, java.util.EnumSet.of(
                AccessPermission.SUBMIT_QUESTION, AccessPermission.VIEW_QUESTION, 
                AccessPermission.VIEW_QUESTIONS, AccessPermission.VIEW_ANSWERS, AccessPermission.SUBMIT_ANSWERS
            ));
            put(UserRole.REVIEWER, java.util.EnumSet.of(
                AccessPermission.SUBMIT_QUESTION, AccessPermission.VIEW_QUESTION, AccessPermission.VIEW_QUESTIONS,
                AccessPermission.VIEW_ALL_QUESTIONS, AccessPermission.VIEW_ANSWERS, AccessPermission.SUBMIT_ANSWERS,
                AccessPermission.EDIT_ANSWERS, AccessPermission.APPROVE_CONTENT, AccessPermission.MODERATE_CONTENT
            ));
            put(UserRole.INSTRUCTOR, java.util.EnumSet.allOf(AccessPermission.class));
        }};
    
    // TP3 Permission mapping for demo
    private static final java.util.Map<UserRole, java.util.Set<Permission>> TP3_ROLE_PERMISSIONS = 
        new java.util.HashMap<UserRole, java.util.Set<Permission>>() {{
            put(UserRole.STUDENT, java.util.EnumSet.of(Permission.SUBMIT_QUESTION));
            put(UserRole.REVIEWER, java.util.EnumSet.of(Permission.SUBMIT_QUESTION, Permission.VIEW_ALL_QUESTIONS));
            put(UserRole.INSTRUCTOR, java.util.EnumSet.allOf(Permission.class));
        }};
    
    /**
     * Default constructor for TP3 demonstration.
     */
    public RoleBasedAccessController() {
        this.permissionRepository = null; // Mock mode
    }
    
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
    
    /**
     * Validate access for TP3 demonstration.
     */
    /**
     * Validates access for TP3 demo - simplified version.
     */
    public AccessResult validateAccess(UserRole userRole, Permission permission) {
        java.util.Set<Permission> rolePermissions = TP3_ROLE_PERMISSIONS.get(userRole);
        if (rolePermissions != null && rolePermissions.contains(permission)) {
            return new AccessResult(true, "Access granted");
        } else {
            return new AccessResult(false, "Role " + userRole + " does not have permission " + permission);
        }
    }
    
    public AccessResult validateAccess(String userId, UserRole userRole, String resourceId, AccessPermission permission) {
        if (userId == null || userRole == null || permission == null) {
            return AccessResult.denied("Invalid access parameters");
        }
        
        java.util.Set<AccessPermission> rolePermissions = ROLE_PERMISSIONS.get(userRole);
        if (rolePermissions != null && rolePermissions.contains(permission)) {
            return AccessResult.granted();
        }
        
        return AccessResult.denied("Role " + userRole + " does not have permission " + permission);
    }
}