package edu.asu.cse360.hw3;

import java.time.LocalDateTime;

/**
 * Default implementation of UserSession for TP3.
 * 
 * @author Jose Mendoza
 * @version 3.0.0 (TP3 Implementation)
 * @since 2025-11-09
 */
public class DefaultUserSession implements UserSession {
    
    private final String username;
    private final String role;
    private final String sessionToken;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;
    private boolean authenticated;
    
    /**
     * Create a new user session.
     */
    public DefaultUserSession(String username, String role, String sessionToken) {
        this.username = username;
        this.role = role;
        this.sessionToken = sessionToken;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = createdAt.plusHours(8); // 8 hour session
        this.authenticated = true;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    /**
     * Get the user role as string.
     */
    public String getRole() {
        return role;
    }
    
    @Override
    public UserRole getUserRole() {
        try {
            return UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UserRole.STUDENT; // Default fallback
        }
    }
    
    /**
     * Get the session token.
     */
    public String getSessionToken() {
        return sessionToken;
    }
    
    @Override
    public boolean isAuthenticated() {
        return authenticated && !isExpired();
    }
    
    @Override
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    /**
     * Get session creation time.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Get session expiration time.
     */
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    /**
     * Invalidate this session.
     */
    public void invalidate() {
        this.authenticated = false;
    }
}