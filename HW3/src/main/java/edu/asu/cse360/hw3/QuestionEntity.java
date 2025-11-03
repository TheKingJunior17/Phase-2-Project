package edu.asu.cse360.hw3;

/**
 * Question entity for database operations.
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public class QuestionEntity {
    private Integer id;
    private String title;
    private String content;
    private String type;
    private String difficulty;
    private Integer authorId;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
    
    public QuestionEntity() {}
    
    public QuestionEntity(String title, String content, String type, String difficulty, Integer authorId) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.difficulty = difficulty;
        this.authorId = authorId;
        this.createdAt = java.time.LocalDateTime.now();
        this.updatedAt = java.time.LocalDateTime.now();
    }
    
    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    // Alias methods for test compatibility
    public String getQuestionText() { return content; }
    public void setQuestionText(String questionText) { this.content = questionText; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public Integer getAuthorId() { return authorId; }
    public void setAuthorId(Integer authorId) { this.authorId = authorId; }
    
    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}