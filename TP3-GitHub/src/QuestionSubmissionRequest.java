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
    private final String content;
    private final String category;
    private final String tags;
    private final String type;
    private final String difficulty;
    
    /**
     * Constructor for TP3 demonstration with type and difficulty.
     */
    public QuestionSubmissionRequest(String title, String content, String category, String difficulty) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.difficulty = difficulty;
        this.type = "multiple-choice";
        this.tags = "";
        this.description = content;
        this.username = "testuser";
        this.userId = "1";
        this.userRole = "STUDENT";
    }
    
    /**
     * Constructor for TP3 demonstration.
     */
    public QuestionSubmissionRequest(String content, String category, String tags) {
        this.content = content;
        this.category = category;
        this.tags = tags;
        this.type = "multiple-choice";
        this.difficulty = "medium";
        this.title = extractTitle(content);
        this.description = content;
        this.username = "testuser";
        this.userId = "1";
        this.userRole = "STUDENT";
    }
    
    public QuestionSubmissionRequest(String title, String description, String username, String userId, String userRole) {
        this.title = title;
        this.description = description;
        this.username = username;
        this.userId = userId;
        this.userRole = userRole;
        this.content = description;
        this.category = "general";
        this.tags = "";
        this.type = "multiple-choice";
        this.difficulty = "medium";
    }
    
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getUsername() { return username; }
    public String getUserId() { return userId; }
    public String getUserRole() { return userRole; }
    
    // TP3 additional methods
    public String getContent() { return content; }
    public String getCategory() { return category; }
    public String getTags() { return tags; }
    public String getType() { return type; }
    public String getDifficulty() { return difficulty; }
    
    /**
     * Extract title from content for TP3.
     */
    private String extractTitle(String content) {
        if (content == null || content.trim().isEmpty()) {
            return "Untitled Question";
        }
        return content.length() > 50 ? content.substring(0, 47) + "..." : content;
    }
}