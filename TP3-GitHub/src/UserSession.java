package edu.asu.cse360.hw3;

/**
 * User session containing authentication and role information.
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public interface UserSession {
    boolean isAuthenticated();
    boolean isExpired();
    String getUsername();
    UserRole getUserRole();
}