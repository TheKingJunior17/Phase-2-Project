package edu.asu.cse360.hw3;

/**
 * User entity for database operations.
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public class UserEntity {
    private Integer id;
    private String username;
    private String email;
    private String passwordHash;
    private UserRole role;
    private boolean active;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
    
    public UserEntity() {}
    
    public UserEntity(String username, String email, String passwordHash, UserRole role) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.active = true;
        this.createdAt = java.time.LocalDateTime.now();
        this.updatedAt = java.time.LocalDateTime.now();
    }
    
    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    
    // Helper method for test compatibility
    public void setRole(String roleName) { 
        this.role = UserRole.valueOf(roleName); 
    }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}