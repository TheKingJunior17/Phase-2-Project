package edu.asu.cse360.hw3;

/**
 * Question validation service for verifying submission requests.
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public class QuestionValidationService {
    
    /**
     * Validates a question submission request.
     */
    public ValidationResult validateQuestionSubmission(QuestionSubmissionRequest request) {
        if (request == null) {
            return ValidationResult.invalid("Request cannot be null");
        }
        
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            return ValidationResult.invalid("Question title is required");
        }
        
        if (request.getTitle().length() < 5 || request.getTitle().length() > 100) {
            return ValidationResult.invalid("Question title must be between 5 and 100 characters");
        }
        
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            return ValidationResult.invalid("Question description is required");
        }
        
        if (request.getDescription().length() < 10 || request.getDescription().length() > 1000) {
            return ValidationResult.invalid("Question description must be between 10 and 1000 characters");
        }
        
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return ValidationResult.invalid("Username is required");
        }
        
        if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
            return ValidationResult.invalid("User ID is required");
        }
        
        return ValidationResult.valid();
    }
}