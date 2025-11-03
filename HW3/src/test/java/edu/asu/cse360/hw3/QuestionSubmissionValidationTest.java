package edu.asu.cse360.hw3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive validation test suite for question submission functionality.
 * 
 * <p>This test class validates the complete question submission workflow including
 * form processing, data validation, persistence operations, and error handling.
 * The test suite ensures that the question submission service properly handles
 * valid submissions while rejecting invalid data with appropriate error messages.</p>
 * 
 * <p>The validation test covers:</p>
 * <ul>
 *   <li><strong>Input Validation:</strong> Title and description format verification</li>
 *   <li><strong>Data Sanitization:</strong> XSS prevention and content filtering</li>
 *   <li><strong>Database Persistence:</strong> Proper storage of valid questions</li>
 *   <li><strong>Error Handling:</strong> Clear feedback for validation failures</li>
 *   <li><strong>User Authorization:</strong> Permission verification for submission</li>
 * </ul>
 * 
 * <p>This test implementation uses comprehensive validation scenarios to ensure
 * robust question submission processing in all supported use cases.</p>
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 * 
 * @see QuestionSubmissionService
 * @see ValidationResult
 * 
 * @testCase Test 3: Question Submission Validation Test
 * @precondition Question submission service and validation components are properly configured
 * @postcondition All submission scenarios return appropriate validation results
 * @expectedResult Valid questions are stored successfully, invalid submissions are rejected with clear errors
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Question Submission Validation Test Suite")
public class QuestionSubmissionValidationTest {
    
    /** Mock database connection for testing */
    @Mock
    private Connection mockConnection;
    
    /** Mock prepared statement for database operations */
    @Mock
    private PreparedStatement mockPreparedStatement;
    
    /** Mock result set for query results */
    @Mock
    private ResultSet mockResultSet;
    
    /** The question submission service under test */
    private QuestionSubmissionService submissionService;
    
    /** Test user information for submissions */
    private static final String TEST_USERNAME = "student1";
    private static final String TEST_USER_ID = "123";
    private static final String TEST_USER_ROLE = "STUDENT";
    
    /**
     * Sets up the test environment before each test method.
     * 
     * <p>This method initializes all mock objects and creates a fresh instance
     * of the QuestionSubmissionService with mocked dependencies. This ensures
     * test isolation and consistent behavior across all validation test cases.</p>
     * 
     * @testCase Initialize submission service and mocks before each test
     * @precondition Test framework and Mockito are properly configured
     * @postcondition Fresh submission service instance with mocked dependencies
     * @expectedResult Submission service is ready for testing with isolated database
     */
    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        submissionService = new QuestionSubmissionService(mockConnection);
        
        // Setup default mock behavior for successful database operations
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1001); // Generated question ID
    }
    
    /**
     * Tests successful question submission with valid input data.
     * 
     * <p>This test validates that properly formatted questions with valid titles
     * and descriptions are successfully processed and stored in the database.
     * The submission service should accept well-formed questions and return
     * successful validation results with generated question IDs.</p>
     * 
     * @param title the valid question title to test
     * @param description the valid question description to test
     * 
     * @testCase Verify successful submission of valid questions
     * @precondition Submission service is initialized and user is authenticated
     * @postcondition Valid questions are stored successfully in database
     * @expectedResult ValidationResult.SUCCESS with generated question ID
     */
    @ParameterizedTest(name = "Valid question: ''{0}''")
    @CsvSource({
        "'How to implement binary search?', 'I need help understanding how to implement binary search algorithm in Java'",
        "'Database normalization question', 'Can someone explain the difference between 1NF, 2NF, and 3NF?'",
        "'CSS flexbox layout help', 'How do I center a div using flexbox properties?'",
        "'Python list comprehension', 'What is the most efficient way to filter a list in Python?'",
        "'Git merge vs rebase', 'When should I use git merge versus git rebase?'"
    })
    @DisplayName("Test Successful Question Submission with Valid Data")
    void testSuccessfulQuestionSubmission(String title, String description) throws SQLException {
        // Create question submission request
        QuestionSubmissionRequest request = new QuestionSubmissionRequest(
            title, description, TEST_USERNAME, TEST_USER_ID, TEST_USER_ROLE
        );
        
        // Execute submission
        ValidationResult result = submissionService.submitQuestion(request);
        
        // Verify successful submission
        assertThat(result.isValid())
            .as("Valid question submission should succeed")
            .isTrue();
            
        assertThat(result.getQuestionId())
            .as("Successful submission should return generated question ID")
            .isNotNull()
            .isPositive();
            
        assertThat(result.getErrors())
            .as("Successful submission should have no validation errors")
            .isEmpty();
            
        // Verify database interaction
        verify(mockPreparedStatement).setString(eq(1), eq(title));
        verify(mockPreparedStatement).setString(eq(2), eq(description));
        verify(mockPreparedStatement).setString(eq(3), eq(TEST_USERNAME));
        verify(mockPreparedStatement).executeUpdate();
    }
    
    /**
     * Tests validation failure for questions with invalid titles.
     * 
     * <p>This test ensures that the submission service properly validates question
     * titles and rejects submissions with titles that are too short, too long,
     * empty, or contain inappropriate content. Clear validation error messages
     * should be provided for each failure type.</p>
     * 
     * @param invalidTitle the invalid title to test
     * 
     * @testCase Verify rejection of questions with invalid titles
     * @precondition Submission service is initialized with validation rules
     * @postcondition Invalid titles are rejected with appropriate error messages
     * @expectedResult ValidationResult.INVALID with specific title validation errors
     */
    @ParameterizedTest(name = "Invalid title: ''{0}''")
    @ValueSource(strings = {
        "",                                    // Empty title
        "   ",                                // Whitespace only
        "Hi",                                 // Too short (less than 5 characters)
        "This is an extremely long question title that exceeds the maximum allowed length for question titles in the system and should be rejected", // Too long
        "<script>alert('xss')</script>",      // XSS attempt
        "Help!!!!!!!!!!!!!!!!!!!!!!!!!!!",  // Excessive punctuation
        "URGENT HELP NEEDED NOW!!!!!!"       // All caps with excessive punctuation
    })
    @DisplayName("Test Title Validation Failure")
    void testInvalidTitleValidation(String invalidTitle) throws SQLException {
        String validDescription = "This is a valid description with enough content to pass validation.";
        
        QuestionSubmissionRequest request = new QuestionSubmissionRequest(
            invalidTitle, validDescription, TEST_USERNAME, TEST_USER_ID, TEST_USER_ROLE
        );
        
        // Execute submission
        ValidationResult result = submissionService.submitQuestion(request);
        
        // Verify validation failure
        assertThat(result.isValid())
            .as("Question with invalid title should be rejected")
            .isFalse();
            
        assertThat(result.getErrors())
            .as("Invalid title should produce validation errors")
            .isNotEmpty()
            .anyMatch(error -> error.contains("title") || error.contains("Title"));
            
        assertThat(result.getQuestionId())
            .as("Failed validation should not return question ID")
            .isNull();
            
        // Verify no database insertion for invalid data
        verify(mockPreparedStatement, never()).executeUpdate();
    }
    
    /**
     * Tests validation failure for questions with invalid descriptions.
     * 
     * <p>This test validates that question descriptions meet minimum content
     * requirements and formatting standards. Descriptions that are too short,
     * contain inappropriate content, or exceed length limits should be properly
     * rejected with informative error messages.</p>
     * 
     * @param invalidDescription the invalid description to test
     * 
     * @testCase Verify rejection of questions with invalid descriptions
     * @precondition Submission service validates description content and length
     * @postcondition Invalid descriptions are rejected with specific error messages
     * @expectedResult ValidationResult.INVALID with description-specific validation errors
     */
    @ParameterizedTest(name = "Invalid description: ''{0}''")
    @ValueSource(strings = {
        "",                          // Empty description
        "Help",                      // Too short
        "   \n\t   ",               // Whitespace only with various characters
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. This description is way too long and exceeds the maximum allowed length.", // Too long
        "<script>alert('xss')</script>Please help", // XSS attempt in description
        "HELP ME URGENT URGENT URGENT!!!!!!!"      // Poor formatting
    })
    @DisplayName("Test Description Validation Failure")
    void testInvalidDescriptionValidation(String invalidDescription) throws SQLException {
        String validTitle = "Valid Question Title";
        
        QuestionSubmissionRequest request = new QuestionSubmissionRequest(
            validTitle, invalidDescription, TEST_USERNAME, TEST_USER_ID, TEST_USER_ROLE
        );
        
        // Execute submission
        ValidationResult result = submissionService.submitQuestion(request);
        
        // Verify validation failure
        assertThat(result.isValid())
            .as("Question with invalid description should be rejected")
            .isFalse();
            
        assertThat(result.getErrors())
            .as("Invalid description should produce validation errors")
            .isNotEmpty()
            .anyMatch(error -> error.contains("description") || error.contains("Description"));
            
        // Verify no database insertion
        verify(mockPreparedStatement, never()).executeUpdate();
    }
    
    /**
     * Tests XSS prevention and content sanitization.
     * 
     * <p>This test validates that the submission service properly sanitizes
     * user input to prevent cross-site scripting (XSS) attacks and other
     * malicious content injection. All HTML tags and script content should
     * be properly escaped or removed.</p>
     * 
     * @testCase Verify XSS prevention and content sanitization
     * @precondition Submission service implements content sanitization
     * @postcondition Malicious content is sanitized or rejected appropriately
     * @expectedResult Safe content is preserved, malicious content is neutralized
     */
    @Test
    @DisplayName("Test XSS Prevention and Content Sanitization")
    void testXSSPreventionAndSanitization() throws SQLException {
        String maliciousTitle = "Help with <script>alert('xss')</script> JavaScript";
        String maliciousDescription = "I need help with <img src='x' onerror='alert(1)'> images and <a href='javascript:alert(2)'>links</a>";
        
        QuestionSubmissionRequest request = new QuestionSubmissionRequest(
            maliciousTitle, maliciousDescription, TEST_USERNAME, TEST_USER_ID, TEST_USER_ROLE
        );
        
        // Execute submission
        ValidationResult result = submissionService.submitQuestion(request);
        
        if (result.isValid()) {
            // If sanitization is used, verify content is cleaned
            verify(mockPreparedStatement).setString(eq(1), argThat(s -> !s.contains("<script>")));
            verify(mockPreparedStatement).setString(eq(2), argThat(s -> !s.contains("javascript:")));
        } else {
            // If rejection is used, verify appropriate error messages
            assertThat(result.getErrors())
                .as("XSS content should be rejected with security error")
                .anyMatch(error -> error.toLowerCase().contains("invalid") || 
                                 error.toLowerCase().contains("character"));
        }
    }
    
    /**
     * Tests user authorization for question submission.
     * 
     * <p>This test validates that only authorized users can submit questions
     * and that proper role-based access control is enforced. Users without
     * appropriate permissions should be unable to submit questions.</p>
     * 
     * @param userRole the user role to test for submission authorization
     * @param shouldSucceed whether the role should be authorized to submit
     * 
     * @testCase Verify user authorization for question submission
     * @precondition Submission service enforces role-based access control
     * @postcondition Only authorized users can submit questions successfully
     * @expectedResult Authorized roles succeed, unauthorized roles are rejected
     */
    @ParameterizedTest(name = "Role authorization: {0} should {1}")
    @CsvSource({
        "STUDENT, succeed",
        "ADMIN, succeed", 
        "REVIEWER, succeed",
        "STAFF, succeed",
        "GUEST, fail",
        "SUSPENDED, fail",
        "BANNED, fail"
    })
    @DisplayName("Test User Authorization for Question Submission")
    void testUserAuthorizationForSubmission(String userRole, String expectedOutcome) throws SQLException {
        boolean shouldSucceed = "succeed".equals(expectedOutcome);
        
        String validTitle = "Authorization Test Question";
        String validDescription = "This is a test question to verify role-based authorization.";
        
        QuestionSubmissionRequest request = new QuestionSubmissionRequest(
            validTitle, validDescription, TEST_USERNAME, TEST_USER_ID, userRole
        );
        
        // Execute submission
        ValidationResult result = submissionService.submitQuestion(request);
        
        if (shouldSucceed) {
            assertThat(result.isValid())
                .as("User with role %s should be authorized to submit questions", userRole)
                .isTrue();
        } else {
            assertThat(result.isValid())
                .as("User with role %s should not be authorized to submit questions", userRole)
                .isFalse();
                
            assertThat(result.getErrors())
                .as("Unauthorized submission should include authorization error")
                .anyMatch(error -> error.toLowerCase().contains("authorization") || 
                                 error.toLowerCase().contains("permission"));
        }
    }
    
    /**
     * Tests database transaction handling and rollback scenarios.
     * 
     * <p>This test validates that the submission service properly handles
     * database transaction failures and performs appropriate rollback operations
     * to maintain data integrity. Failed submissions should not leave partial
     * data in the database.</p>
     * 
     * @testCase Verify database transaction handling and rollback
     * @precondition Submission service uses database transactions
     * @postcondition Failed transactions are properly rolled back
     * @expectedResult Database integrity is maintained during failure scenarios
     */
    @Test
    @DisplayName("Test Database Transaction Handling and Rollback")
    void testDatabaseTransactionHandling() throws SQLException {
        String validTitle = "Transaction Test Question";
        String validDescription = "This question tests database transaction handling.";
        
        // Setup mock to simulate database failure
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Database constraint violation"));
        
        QuestionSubmissionRequest request = new QuestionSubmissionRequest(
            validTitle, validDescription, TEST_USERNAME, TEST_USER_ID, TEST_USER_ROLE
        );
        
        // Execute submission
        ValidationResult result = submissionService.submitQuestion(request);
        
        // Verify graceful failure handling
        assertThat(result.isValid())
            .as("Database failure should result in validation failure")
            .isFalse();
            
        assertThat(result.getErrors())
            .as("Database failure should include system error message")
            .anyMatch(error -> error.toLowerCase().contains("system") || 
                             error.toLowerCase().contains("database"));
            
        // Verify rollback was attempted (implementation-specific)
        verify(mockConnection, atLeastOnce()).rollback();
    }
    
    /**
     * Tests concurrent question submission handling.
     * 
     * <p>This test validates that the submission service handles concurrent
     * submission requests safely without race conditions or data corruption.
     * Multiple simultaneous submissions should be processed independently.</p>
     * 
     * @testCase Verify thread safety for concurrent question submissions
     * @precondition Submission service supports concurrent access
     * @postcondition Concurrent submissions are handled safely without conflicts
     * @expectedResult All concurrent submissions are processed correctly
     */
    @Test
    @DisplayName("Test Concurrent Question Submission Handling")
    void testConcurrentSubmissionHandling() throws SQLException, InterruptedException {
        String baseTitle = "Concurrent Test Question";
        String baseDescription = "This is a concurrent submission test question.";
        
        // Create multiple submission threads
        Thread[] threads = new Thread[5];
        ValidationResult[] results = new ValidationResult[5];
        
        for (int i = 0; i < 5; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    QuestionSubmissionRequest request = new QuestionSubmissionRequest(
                        baseTitle + " " + index,
                        baseDescription + " Number " + index,
                        TEST_USERNAME + index,
                        TEST_USER_ID + index,
                        TEST_USER_ROLE
                    );
                    results[index] = submissionService.submitQuestion(request);
                } catch (Exception e) {
                    fail("Concurrent submission should not throw exceptions");
                }
            });
        }
        
        // Start all threads
        for (Thread thread : threads) {
            thread.start();
        }
        
        // Wait for completion
        for (Thread thread : threads) {
            thread.join(5000); // 5 second timeout
        }
        
        // Verify all submissions completed
        for (int i = 0; i < 5; i++) {
            assertThat(results[i])
                .as("Concurrent submission %d should not be null", i)
                .isNotNull();
                
            assertThat(results[i].isValid())
                .as("Concurrent submission %d should succeed", i)
                .isTrue();
        }
        
        // Verify database operations were called correctly
        verify(mockPreparedStatement, times(5)).executeUpdate();
    }
    
    /**
     * Tests question submission performance characteristics.
     * 
     * <p>This test ensures that question submission processing completes within
     * reasonable time limits to provide good user experience. The submission
     * service should process valid questions quickly and efficiently.</p>
     * 
     * @testCase Verify acceptable performance for question submission
     * @precondition Submission service is optimized for performance
     * @postcondition Question submissions complete within time limits
     * @expectedResult Submission processing completes in less than 500ms
     */
    @Test
    @DisplayName("Test Question Submission Performance")
    void testQuestionSubmissionPerformance() throws SQLException {
        String title = "Performance Test Question";
        String description = "This question is used to test submission performance characteristics.";
        
        QuestionSubmissionRequest request = new QuestionSubmissionRequest(
            title, description, TEST_USERNAME, TEST_USER_ID, TEST_USER_ROLE
        );
        
        // Measure submission time
        long startTime = System.currentTimeMillis();
        ValidationResult result = submissionService.submitQuestion(request);
        long endTime = System.currentTimeMillis();
        
        long executionTime = endTime - startTime;
        
        // Verify performance
        assertThat(result.isValid())
            .as("Performance test should return valid result")
            .isTrue();
            
        assertThat(executionTime)
            .as("Question submission should complete within 500ms")
            .isLessThan(500);
    }
}