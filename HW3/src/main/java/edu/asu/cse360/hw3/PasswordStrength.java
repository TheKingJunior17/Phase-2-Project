package edu.asu.cse360.hw3;

/**
 * Enumeration representing different levels of password strength.
 * 
 * <p>This enum is used by the {@link PasswordStrengthValidator} to categorize
 * passwords based on their complexity and security characteristics. Each level
 * represents increasing security requirements and resistance to common attacks.</p>
 * 
 * <p>The strength levels are determined by factors including:</p>
 * <ul>
 *   <li>Password length</li>
 *   <li>Character diversity (uppercase, lowercase, numbers, symbols)</li>
 *   <li>Absence of common patterns</li>
 *   <li>Resistance to dictionary attacks</li>
 * </ul>
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 * 
 * @see PasswordStrengthValidator
 */
public enum PasswordStrength {
    
    /**
     * Indicates a very weak password that fails basic security requirements.
     * 
     * <p>Characteristics of VERY_WEAK passwords:</p>
     * <ul>
     *   <li>Length less than 6 characters</li>
     *   <li>Contains only lowercase letters or only numbers</li>
     *   <li>Common patterns like "123456" or "password"</li>
     *   <li>Easily guessable or in common dictionaries</li>
     * </ul>
     */
    VERY_WEAK,
    
    /**
     * Indicates a weak password with minimal security characteristics.
     * 
     * <p>Characteristics of WEAK passwords:</p>
     * <ul>
     *   <li>Length 6-7 characters</li>
     *   <li>Contains only two character types (e.g., letters and numbers)</li>
     *   <li>May contain simple patterns</li>
     *   <li>Susceptible to basic attacks</li>
     * </ul>
     */
    WEAK,
    
    /**
     * Indicates a medium strength password with moderate security.
     * 
     * <p>Characteristics of MEDIUM passwords:</p>
     * <ul>
     *   <li>Length 8-10 characters</li>
     *   <li>Contains three character types</li>
     *   <li>No obvious patterns</li>
     *   <li>Provides reasonable protection for most use cases</li>
     * </ul>
     */
    MEDIUM,
    
    /**
     * Indicates a strong password with good security characteristics.
     * 
     * <p>Characteristics of STRONG passwords:</p>
     * <ul>
     *   <li>Length 11-14 characters</li>
     *   <li>Contains all four character types</li>
     *   <li>No common patterns or dictionary words</li>
     *   <li>Resistant to most automated attacks</li>
     * </ul>
     */
    STRONG,
    
    /**
     * Indicates a very strong password with excellent security.
     * 
     * <p>Characteristics of VERY_STRONG passwords:</p>
     * <ul>
     *   <li>Length 15+ characters</li>
     *   <li>Contains all character types with good distribution</li>
     *   <li>No recognizable patterns</li>
     *   <li>Highly resistant to all common attack methods</li>
     * </ul>
     */
    VERY_STRONG
}