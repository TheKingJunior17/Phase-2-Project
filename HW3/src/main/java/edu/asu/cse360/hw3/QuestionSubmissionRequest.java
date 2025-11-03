package edu.asu.cse360.hw3;

/**
 * Request container for question submission data.
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public class QuestionSubmissionRequest {
    private final String title;
    private final String description;
    private final String username;
    private final String userId;
    private final String userRole;
    
    public QuestionSubmissionRequest(String title, String description, String username, String userId, String userRole) {
        this.title = title;
        this.description = description;
        this.username = username;
        this.userId = userId;
        this.userRole = userRole;
    }
    
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getUsername() { return username; }
    public String getUserId() { return userId; }
    public String getUserRole() { return userRole; }
}