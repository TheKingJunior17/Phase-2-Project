package edu.asu.cse360.hw3;

/**
 * User credentials for authentication purposes.
 * Contains username and password for login validation.
 * 
 * @author Jose Mendoza
 * @version 1.0.0
 * @since 2025-11-09
 */
public class UserCredentials {
    private String username;
    private String password;
    
    /**
     * Creates new user credentials with username and password.
     * 
     * @param username the username for authentication
     * @param password the password for authentication
     */
    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    /**
     * Gets the username.
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Gets the password.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the username.
     * 
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Sets the password.
     * 
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}