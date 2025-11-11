package edu.asu.cse360.hw3;

/**
 * Answer entity for database operations.
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public class AnswerEntity {
    private Integer id;
    private String content;
    private Integer questionId;
    private Integer authorId;
    private boolean isCorrect;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
    
    public AnswerEntity() {}
    
    public AnswerEntity(String content, Integer questionId, Integer authorId, boolean isCorrect) {
        this.content = content;
        this.questionId = questionId;
        this.authorId = authorId;
        this.isCorrect = isCorrect;
        this.createdAt = java.time.LocalDateTime.now();
        this.updatedAt = java.time.LocalDateTime.now();
    }
    
    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    // Alias methods for test compatibility
    public String getAnswerText() { return content; }
    public void setAnswerText(String answerText) { this.content = answerText; }
    
    public boolean isAccepted() { return isCorrect; }
    public void setAccepted(boolean accepted) { this.isCorrect = accepted; }
    
    public Integer getQuestionId() { return questionId; }
    public void setQuestionId(Integer questionId) { this.questionId = questionId; }
    
    public Integer getAuthorId() { return authorId; }
    public void setAuthorId(Integer authorId) { this.authorId = authorId; }
    
    public boolean isCorrect() { return isCorrect; }
    public void setCorrect(boolean correct) { isCorrect = correct; }
    
    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}