package edu.asu.cse360.hw3;

/**
 * Result container for access control operations.
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public class AccessResult {
    private final boolean granted;
    private final String reason;
    
    public AccessResult(boolean granted, String reason) {
        this.granted = granted;
        this.reason = reason;
    }
    
    public boolean isGranted() { return granted; }
    public String getReason() { return reason; }
    public String getDenialReason() { return reason; } // Alias for test compatibility
    
    /**
     * Creates a granted access result.
     */
    public static AccessResult granted() {
        return new AccessResult(true, null);
    }
    
    /**
     * Creates a denied access result with a reason.
     */
    public static AccessResult denied(String reason) {
        return new AccessResult(false, reason);
    }
}