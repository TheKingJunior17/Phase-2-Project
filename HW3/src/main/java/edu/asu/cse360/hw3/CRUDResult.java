package edu.asu.cse360.hw3;

/**
 * Result wrapper for CRUD operations.
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public class CRUDResult<T> {
    private final boolean success;
    private final T data;
    private final String errorMessage;
    
    private CRUDResult(boolean success, T data, String errorMessage) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
    }
    
    public static <T> CRUDResult<T> success(T data) {
        return new CRUDResult<>(true, data, null);
    }
    
    public static <T> CRUDResult<T> failure(String errorMessage) {
        return new CRUDResult<>(false, null, errorMessage);
    }
    
    public boolean isSuccess() { return success; }
    public T getData() { return data; }
    public String getErrorMessage() { return errorMessage; }
}