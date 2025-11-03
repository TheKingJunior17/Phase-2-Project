package edu.asu.cse360.hw3;

/**
 * Authentication result container for user login operations.
 * 
 * <p>This class encapsulates the result of authentication attempts including
 * success status, user information, session tokens, and error messages. It
 * provides a standardized way to handle authentication responses across the
 * application.</p>
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public class AuthenticationResult {
    private final boolean success;
    private final String username;
    private final String userRole;
    private final String sessionToken;
    private final String errorMessage;
    private final boolean requiresPasswordChange;
    
    private AuthenticationResult(Builder builder) {
        this.success = builder.success;
        this.username = builder.username;
        this.userRole = builder.userRole;
        this.sessionToken = builder.sessionToken;
        this.errorMessage = builder.errorMessage;
        this.requiresPasswordChange = builder.requiresPasswordChange;
    }
    
    public boolean isSuccess() { return success; }
    public String getUsername() { return username; }
    public String getUserRole() { return userRole; }
    public String getSessionToken() { return sessionToken; }
    public String getErrorMessage() { return errorMessage; }
    public boolean requiresPasswordChange() { return requiresPasswordChange; }
    
    public static Builder builder() { return new Builder(); }
    
    /**
     * Creates a successful authentication result.
     */
    public static AuthenticationResult success(String username, String role, String token) {
        return builder()
            .success(true)
            .username(username)
            .userRole(role)
            .sessionToken(token)
            .build();
    }
    
    /**
     * Creates a failed authentication result.
     */
    public static AuthenticationResult failure(String errorMessage) {
        return builder()
            .success(false)
            .errorMessage(errorMessage)
            .build();
    }
    
    public static class Builder {
        private boolean success;
        private String username;
        private String userRole;
        private String sessionToken;
        private String errorMessage;
        private boolean requiresPasswordChange;
        
        public Builder success(boolean success) { this.success = success; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder userRole(String userRole) { this.userRole = userRole; return this; }
        public Builder sessionToken(String sessionToken) { this.sessionToken = sessionToken; return this; }
        public Builder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }
        public Builder requiresPasswordChange(boolean requiresPasswordChange) { this.requiresPasswordChange = requiresPasswordChange; return this; }
        
        public AuthenticationResult build() { return new AuthenticationResult(this); }
    }
}