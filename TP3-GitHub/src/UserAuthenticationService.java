package edu.asu.cse360.hw3;

/**
 * Authentication service for validating user credentials and managing sessions.
 * 
 * <p>This service provides comprehensive user authentication functionality including
 * credential validation, session management, and security features such as rate
 * limiting and password strength enforcement.</p>
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public class UserAuthenticationService {
    
    private java.sql.Connection connection;
    private java.util.Map<String, Integer> loginAttempts = new java.util.concurrent.ConcurrentHashMap<>();
    private final PasswordStrengthValidator passwordValidator;
    
    // Mock user database for TP3 demonstration
    private static final java.util.Map<String, UserCredentials> USER_DATABASE = 
        new java.util.HashMap<String, UserCredentials>() {{
            put("student1", new UserCredentials("student1", "password123", UserRole.STUDENT, false));
            put("reviewer1", new UserCredentials("reviewer1", "reviewer_pass", UserRole.REVIEWER, false));
            put("instructor1", new UserCredentials("instructor1", "admin_pass", UserRole.INSTRUCTOR, false));
            put("admin", new UserCredentials("admin", "AdminPass000!", UserRole.INSTRUCTOR, true));
        }};
    
    /**
     * Default constructor for TP3 demonstration.
     */
    public UserAuthenticationService() {
        this.connection = null; // Mock mode for TP3
        this.passwordValidator = new PasswordStrengthValidator();
    }
    
    public UserAuthenticationService(java.sql.Connection connection) {
        this.connection = connection;
        this.passwordValidator = new PasswordStrengthValidator();
    }
    
    public AuthenticationResult authenticate(String username, String password) {
        // Input validation
        if (username == null || username.trim().isEmpty()) {
            return AuthenticationResult.failure("Username cannot be empty");
        }
        
        if (password == null || password.trim().isEmpty()) {
            return AuthenticationResult.failure("Password cannot be empty");
        }
        
        // Check rate limiting
        if (isRateLimited(username)) {
            return AuthenticationResult.failure("Too many attempts. Please try again later.");
        }
        
        try {
            // Use mock database for TP3 demonstration
            UserCredentials credentials = USER_DATABASE.get(username.toLowerCase());
            if (credentials == null || !credentials.getPassword().equals(password)) {
                recordFailedAttempt(username);
                return AuthenticationResult.failure("Invalid username or password");
            }
            
            // Check password strength for TP3 enhancement
            PasswordStrength strength = passwordValidator.evaluatePassword(password);
            boolean requiresChange = strength == PasswordStrength.WEAK || credentials.isPasswordChangeRequired();
            
            String token = generateSessionToken();
            return AuthenticationResult.builder()
                .success(true)
                .username(credentials.getUsername())
                .userRole(credentials.getRole().name())
                .sessionToken(token)
                .requiresPasswordChange(requiresChange)
                .build();
                
        } catch (Exception e) {
            return AuthenticationResult.failure("System temporarily unavailable");
        }
    }
    
    private boolean validateCredentials(String username, String password) {
        // Simulate successful authentication for test purposes
        return username != null && password != null && !username.isEmpty() && !password.isEmpty();
    }
    
    /**
     * Generate a secure session token for TP3.
     */
    public String generateSessionToken() {
        return "TP3_SESSION_" + java.util.UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * Validate an existing session token.
     */
    public boolean validateSession(String token) {
        if (token == null || token.length() < 16) {
            return false;
        }
        return token.startsWith("TP3_SESSION_") && token.length() == 48;
    }
    
    /**
     * Check if a user requires password change.
     */
    public boolean requirePasswordChange(String username) {
        UserCredentials credentials = USER_DATABASE.get(username.toLowerCase());
        if (credentials == null) {
            return false;
        }
        
        PasswordStrength strength = passwordValidator.evaluatePassword(credentials.getPassword());
        return strength == PasswordStrength.WEAK || credentials.isPasswordChangeRequired();
    }
    
    private boolean isRateLimited(String username) {
        return loginAttempts.getOrDefault(username, 0) >= 5;
    }
    
    private void recordFailedAttempt(String username) {
        loginAttempts.merge(username, 1, Integer::sum);
    }
    
    /**
     * Internal class to represent user credentials for TP3 demonstration.
     */
    private static class UserCredentials {
        private final String username;
        private final String password;
        private final UserRole role;
        private final boolean passwordChangeRequired;

        public UserCredentials(String username, String password, UserRole role, boolean passwordChangeRequired) {
            this.username = username;
            this.password = password;
            this.role = role;
            this.passwordChangeRequired = passwordChangeRequired;
        }

        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public UserRole getRole() { return role; }
        public boolean isPasswordChangeRequired() { return passwordChangeRequired; }
    }
}