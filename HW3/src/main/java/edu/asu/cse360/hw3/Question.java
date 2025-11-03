package edu.asu.cse360.hw3;

/**
 * Question entity for database operations.
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public class Question {
    private Long id;
    private String title;
    private String description;
    private String content;
    private String type;
    private String difficulty;
    private String authorId;
    private String authorUsername;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
    
    public Question() {}
    
    public Question(String title, String description, String authorId, String authorUsername) {
        this.title = title;
        this.description = description;
        this.content = description; // For compatibility
        this.authorId = authorId;
        this.authorUsername = authorUsername;
        this.createdAt = java.time.LocalDateTime.now();
        this.updatedAt = java.time.LocalDateTime.now();
    }
    
    public Question(Long id, String title, String content, String type, String difficulty) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.description = content; // For compatibility
        this.type = type;
        this.difficulty = difficulty;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        this.content = description; // Keep in sync
    }
    
    public String getContent() { return content != null ? content : description; }
    public void setContent(String content) { 
        this.content = content;
        this.description = content; // Keep in sync
    }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }
    
    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }
    
    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}