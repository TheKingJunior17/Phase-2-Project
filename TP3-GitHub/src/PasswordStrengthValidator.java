package edu.asu.cse360.hw3;

import java.util.regex.Pattern;

/**
 * Password strength validation utility for evaluating password security.
 * 
 * <p>This class provides comprehensive password strength evaluation based on
 * industry-standard security criteria. It analyzes various aspects of password
 * complexity including length, character diversity, and common patterns to
 * determine an overall strength rating.</p>
 * 
 * <p>The validator considers the following factors:</p>
 * <ul>
 *   <li><strong>Length:</strong> Longer passwords are generally more secure</li>
 *   <li><strong>Character Types:</strong> Uppercase, lowercase, numbers, symbols</li>
 *   <li><strong>Patterns:</strong> Avoids common sequences and repetitions</li>
 *   <li><strong>Dictionary Words:</strong> Detects common passwords and words</li>
 * </ul>
 * 
 * <p>Usage example:</p>
 * <pre>{@code
 * PasswordStrengthValidator validator = new PasswordStrengthValidator();
 * PasswordStrength strength = validator.evaluatePassword("MySecurePass123!");
 * System.out.println("Password strength: " + strength); // Output: STRONG
 * }</pre>
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 * 
 * @see PasswordStrength
 */
public class PasswordStrengthValidator {
    
    /** Pattern for detecting uppercase letters */
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    
    /** Pattern for detecting lowercase letters */
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    
    /** Pattern for detecting numeric digits */
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("\\d");
    
    /** Pattern for detecting special symbols */
    private static final Pattern SYMBOL_PATTERN = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
    
    /** Pattern for detecting common weak patterns */
    private static final Pattern WEAK_PATTERNS = Pattern.compile(
        "(?i)(password|123456|qwerty|abc|111|000|admin|user|guest|test)"
    );
    
    /** Pattern for detecting sequential characters */
    private static final Pattern SEQUENTIAL_PATTERN = Pattern.compile(
        "(012|123|234|345|456|567|678|789|890|abc|bcd|cde|def|efg|fgh|ghi|hij|ijk|jkl|klm|lmn|mno|nop|opq|pqr|qrs|rst|stu|tuv|uvw|vwx|wxy|xyz)"
    );
    
    /**
     * Evaluates the strength of a given password.
     * 
     * <p>This method performs a comprehensive analysis of the password using
     * multiple criteria to determine its overall security strength. The evaluation
     * considers length, character diversity, common patterns, and other security
     * factors.</p>
     * 
     * @param password the password to evaluate (must not be null)
     * @return the {@link PasswordStrength} rating for the given password
     * @throws IllegalArgumentException if password is null
     * 
     * @testCase Test with various password types to ensure correct strength ratings
     * @precondition Password parameter is not null
     * @postcondition Returns appropriate PasswordStrength enum value
     * @expectedResult Passwords are rated according to security criteria:
     *                - Very weak: &lt;6 chars, simple patterns
     *                - Weak: 6-7 chars, limited character types
     *                - Medium: 8-10 chars, moderate complexity
     *                - Strong: 11-14 chars, high complexity
     *                - Very strong: 15+ chars, maximum complexity
     */
    public PasswordStrength evaluatePassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        
        int score = calculatePasswordScore(password);
        return mapScoreToStrength(score);
    }
    
    /**
     * Calculates a numerical score representing password strength.
     * 
     * <p>This method assigns points based on various password characteristics:
     * - Length (1 point per character up to 20)
     * - Character types (5 points each for uppercase, lowercase, numbers, symbols)
     * - Bonus for good length (10 points for 12+ characters)
     * - Penalties for weak patterns (-15 points each)
     * - Penalties for sequential characters (-10 points each)</p>
     * 
     * @param password the password to score
     * @return numerical score representing password strength (0-100+ range)
     * 
     * @testCase Verify score calculation accuracy for known password patterns
     * @precondition Password is not null (validated by caller)
     * @postcondition Returns score based on cumulative password characteristics
     * @expectedResult Higher scores for more complex passwords, penalties for weak patterns
     */
    private int calculatePasswordScore(String password) {
        int score = 0;
        
        // Length scoring (1 point per character, max 20)
        score += Math.min(password.length(), 20);
        
        // Character type diversity (5 points each)
        if (UPPERCASE_PATTERN.matcher(password).find()) score += 5;
        if (LOWERCASE_PATTERN.matcher(password).find()) score += 5;
        if (NUMERIC_PATTERN.matcher(password).find()) score += 5;
        if (SYMBOL_PATTERN.matcher(password).find()) score += 5;
        
        // Length bonus
        if (password.length() >= 12) score += 10;
        if (password.length() >= 16) score += 5;
        
        // Penalties for weak patterns
        if (WEAK_PATTERNS.matcher(password).find()) score -= 15;
        if (SEQUENTIAL_PATTERN.matcher(password.toLowerCase()).find()) score -= 10;
        if (hasRepeatedCharacters(password)) score -= 5;
        
        return Math.max(0, score); // Ensure non-negative score
    }
    
    /**
     * Maps a numerical password score to a PasswordStrength enum value.
     * 
     * <p>Score ranges for each strength level:</p>
     * <ul>
     *   <li>0-15: VERY_WEAK</li>
     *   <li>16-25: WEAK</li>
     *   <li>26-35: MEDIUM</li>
     *   <li>36-45: STRONG</li>
     *   <li>46+: VERY_STRONG</li>
     * </ul>
     * 
     * @param score the numerical password score
     * @return corresponding PasswordStrength enum value
     * 
     * @testCase Test boundary values for each strength category
     * @precondition Score is non-negative (ensured by calculatePasswordScore)
     * @postcondition Returns appropriate PasswordStrength for given score
     * @expectedResult Correct mapping of score ranges to strength levels
     */
    private PasswordStrength mapScoreToStrength(int score) {
        if (score <= 15) return PasswordStrength.VERY_WEAK;
        if (score <= 25) return PasswordStrength.WEAK;
        if (score <= 35) return PasswordStrength.MEDIUM;
        if (score <= 45) return PasswordStrength.STRONG;
        return PasswordStrength.VERY_STRONG;
    }
    
    /**
     * Checks if the password contains excessive repeated characters.
     * 
     * <p>This method identifies passwords with poor entropy due to character
     * repetition. It looks for patterns where the same character appears
     * three or more times consecutively, which significantly weakens password
     * security.</p>
     * 
     * @param password the password to check
     * @return true if password has repeated characters (3+ consecutive), false otherwise
     * 
     * @testCase Test passwords with and without repeated character patterns
     * @precondition Password is not null (validated by caller)
     * @postcondition Returns boolean indicating presence of repeated characters
     * @expectedResult True for passwords like "aaa123" or "password111", false for diverse passwords
     */
    private boolean hasRepeatedCharacters(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            if (password.charAt(i) == password.charAt(i + 1) && 
                password.charAt(i + 1) == password.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Provides detailed analysis of a password's characteristics.
     * 
     * <p>This method returns a human-readable description of why a password
     * received its strength rating, including specific feedback about what
     * makes the password strong or weak.</p>
     * 
     * @param password the password to analyze
     * @return detailed analysis string explaining the password's characteristics
     * @throws IllegalArgumentException if password is null
     * 
     * @testCase Verify analysis provides meaningful feedback for various password types
     * @precondition Password parameter is not null
     * @postcondition Returns detailed analysis string
     * @expectedResult Clear explanation of password strengths and weaknesses
     */
    public String getPasswordAnalysis(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        
        StringBuilder analysis = new StringBuilder();
        PasswordStrength strength = evaluatePassword(password);
        
        analysis.append("Password Strength: ").append(strength).append("\n");
        analysis.append("Length: ").append(password.length()).append(" characters\n");
        
        // Character type analysis
        analysis.append("Character Types: ");
        if (UPPERCASE_PATTERN.matcher(password).find()) analysis.append("Uppercase ");
        if (LOWERCASE_PATTERN.matcher(password).find()) analysis.append("Lowercase ");
        if (NUMERIC_PATTERN.matcher(password).find()) analysis.append("Numbers ");
        if (SYMBOL_PATTERN.matcher(password).find()) analysis.append("Symbols ");
        analysis.append("\n");
        
        // Recommendations
        if (strength == PasswordStrength.VERY_WEAK || strength == PasswordStrength.WEAK) {
            analysis.append("Recommendations: Increase length, add character diversity, avoid common patterns");
        } else if (strength == PasswordStrength.MEDIUM) {
            analysis.append("Recommendations: Consider adding more characters for better security");
        } else {
            analysis.append("Good password! Provides strong security protection.");
        }
        
        return analysis.toString();
    }
}