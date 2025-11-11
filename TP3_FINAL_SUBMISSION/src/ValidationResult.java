package edu.asu.cse360.hw3;

/**
 * Validation result container for form submission operations.
 * 
 * <p>This class encapsulates validation results including success status,
 * generated IDs, and detailed error messages. It provides comprehensive
 * feedback for form validation and submission processes.</p>
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public class ValidationResult {
    private final boolean valid;
    private final Integer questionId;
    private final java.util.List<String> errors;
    private final String errorMessage;
    
    public ValidationResult(boolean valid, Integer questionId, java.util.List<String> errors) {
        this.valid = valid;
        this.questionId = questionId;
        this.errors = errors != null ? errors : new java.util.ArrayList<>();
        this.errorMessage = errors != null && !errors.isEmpty() ? errors.get(0) : null;
    }
    
    /**
     * Constructor for TP3 (boolean, List<String>).
     */
    public ValidationResult(boolean valid, java.util.List<String> errors) {
        this.valid = valid;
        this.questionId = null;
        this.errors = errors != null ? errors : new java.util.ArrayList<>();
        this.errorMessage = errors != null && !errors.isEmpty() ? errors.get(0) : null;
    }
    
    public ValidationResult(boolean valid, String errorMessage) {
        this.valid = valid;
        this.questionId = null;
        this.errors = new java.util.ArrayList<>();
        this.errorMessage = errorMessage;
        if (errorMessage != null) {
            this.errors.add(errorMessage);
        }
    }
    
    public boolean isValid() { return valid; }
    public Integer getQuestionId() { return questionId; }
    public java.util.List<String> getErrors() { return errors; }
    public String getErrorMessage() { return errorMessage; }
    
    /**
     * Creates a valid validation result.
     */
    public static ValidationResult valid() {
        return new ValidationResult(true, (String) null);
    }
    
    /**
     * Creates a valid validation result with question ID.
     */
    public static ValidationResult valid(Integer questionId) {
        return new ValidationResult(true, questionId, new java.util.ArrayList<>());
    }
    
    /**
     * Creates an invalid validation result with an error message.
     */
    public static ValidationResult invalid(String errorMessage) {
        return new ValidationResult(false, errorMessage);
    }
    
    /**
     * Creates an invalid validation result with multiple errors.
     */
    public static ValidationResult invalid(java.util.List<String> errors) {
        return new ValidationResult(false, null, errors);
    }
}