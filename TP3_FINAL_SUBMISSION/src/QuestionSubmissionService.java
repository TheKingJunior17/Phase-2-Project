package edu.asu.cse360.hw3;

/**
 * Service for handling question submission validation and processing.
 * 
 * <p>This service provides comprehensive question submission functionality including
 * input validation, content sanitization, and database persistence operations.</p>
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public class QuestionSubmissionService {
    
    private java.sql.Connection connection;
    private DatabaseCRUDService databaseService;
    private int nextQuestionId = 1;
    
    /**
     * Default constructor for TP3 demonstration.
     */
    public QuestionSubmissionService() {
        this.connection = null; // Mock mode
        this.databaseService = new DatabaseCRUDService();
    }
    
    public QuestionSubmissionService(java.sql.Connection connection) {
        this.connection = connection;
        this.databaseService = new DatabaseCRUDService(connection);
    }
    
    public ValidationResult submitQuestion(QuestionSubmissionRequest request) {
        try {
            // Validate authorization
            if (!isAuthorized(request.getUserRole())) {
                return ValidationResult.invalid("Insufficient permissions to submit questions");
            }
            
            // Validate input
            java.util.List<String> errors = validateInput(request);
            if (!errors.isEmpty()) {
                return ValidationResult.invalid(errors);
            }
            
            // Sanitize content
            String sanitizedTitle = sanitizeContent(request.getTitle());
            String sanitizedDescription = sanitizeContent(request.getDescription());
            
            // Persist to database (simulated)
            Integer questionId = persistQuestion(sanitizedTitle, sanitizedDescription, request);
            
            return ValidationResult.valid(questionId);
            
        } catch (Exception e) {
            return ValidationResult.invalid("System error occurred during submission");
        }
    }
    
    private boolean isAuthorized(String role) {
        return !"GUEST".equals(role) && !"SUSPENDED".equals(role) && !"BANNED".equals(role);
    }
    
    private java.util.List<String> validateInput(QuestionSubmissionRequest request) {
        java.util.List<String> errors = new java.util.ArrayList<>();
        
        // Title validation
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            errors.add("Title cannot be empty");
        } else if (request.getTitle().trim().length() < 5) {
            errors.add("Title must be at least 5 characters");
        } else if (request.getTitle().length() > 200) {
            errors.add("Title exceeds maximum length");
        } else if (containsInvalidCharacters(request.getTitle())) {
            errors.add("Title contains invalid characters");
        }
        
        // Description validation
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            errors.add("Description cannot be empty");
        } else if (request.getDescription().trim().length() < 10) {
            errors.add("Description must be at least 10 characters");
        } else if (request.getDescription().length() > 2000) {
            errors.add("Description exceeds maximum length");
        } else if (containsInvalidCharacters(request.getDescription())) {
            errors.add("Description contains invalid characters");
        }
        
        return errors;
    }
    
    private boolean containsInvalidCharacters(String content) {
        return content.contains("<script>") || content.contains("javascript:") || 
               content.contains("onerror=") || content.contains("onclick=");
    }
    
    private String sanitizeContent(String content) {
        if (content == null) return null;
        return content.replaceAll("<[^>]*>", "").trim();
    }
    
    private Integer persistQuestion(String title, String description, QuestionSubmissionRequest request) 
            throws java.sql.SQLException {
        // Simulate database insertion
        return 1001; // Mock generated ID
    }
    
    /**
     * Validate a question submission request for TP3.
     */
    public ValidationResult validateQuestion(QuestionSubmissionRequest request) {
        java.util.List<String> errors = validateInput(request);
        return new ValidationResult(errors.isEmpty(), errors);
    }
    
    /**
     * Submit a question and return the created entity for TP3.
     */
    public QuestionEntity submitQuestion(QuestionSubmissionRequest request, String submitterId) {
        ValidationResult validation = validateQuestion(request);
        if (!validation.isValid()) {
            throw new IllegalArgumentException("Question validation failed");
        }
        
        QuestionEntity question = new QuestionEntity();
        question.setId(nextQuestionId++);
        question.setContent(request.getContent());
        question.setCategory(request.getCategory() != null ? request.getCategory() : "general");
        question.setAuthorId(Integer.parseInt(submitterId.replaceAll("\\D", "1")));
        question.setCreatedAt(java.time.LocalDateTime.now());
        question.setUpdatedAt(java.time.LocalDateTime.now());
        
        return databaseService.createQuestion(question);
    }
}