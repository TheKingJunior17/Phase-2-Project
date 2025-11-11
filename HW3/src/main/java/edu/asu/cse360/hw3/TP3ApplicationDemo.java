package edu.asu.cse360.hw3;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * TP3 Enhanced Application Demo - Complete demonstration of Phase 3 features.
 * This version includes mock implementations for database operations to show
 * the full functionality without requiring actual database connections.
 * 
 * <p>This application demonstrates:
 * <ul>
 *   <li>Enhanced Authentication & Session Management</li>
 *   <li>Role-Based Access Control</li>
 *   <li>Question Submission Validation Pipeline</li>
 *   <li>Mock Database CRUD Operations</li>
 *   <li>System Integration Testing</li>
 * </ul>
 * 
 * @author Jose Mendoza
 * @version 3.0.0
 * @since 2025-11-09
 */
public class TP3ApplicationDemo {
    
    private UserAuthenticationService authService;
    private RoleBasedAccessController accessController;
    private QuestionSubmissionService questionService;
    private MockDatabaseService databaseService;
    
    // Mock data storage
    private Map<String, UserEntity> userDatabase;
    private List<QuestionEntity> questionDatabase;
    private Map<String, UserSession> activeSessions;
    
    /**
     * Main entry point for the TP3 application demo.
     */
    public static void main(String[] args) {
        try {
            TP3ApplicationDemo app = new TP3ApplicationDemo();
            app.runDemo();
        } catch (Exception e) {
            System.err.println("❌ Application Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Constructor initializes all services and test data.
     */
    public TP3ApplicationDemo() {
        printHeader();
        initializeServices();
        initializeTestData();
    }
    
    /**
     * Runs the complete TP3 feature demonstration.
     */
    public void runDemo() {
        System.out.println("🚀 TP3 Enhanced Features Demonstration");
        System.out.println("This demo shows integration of:");
        System.out.println("✨ Enhanced Authentication & Session Management");
        System.out.println("✨ Role-Based Access Control (Student/Reviewer/Instructor)");
        System.out.println("✨ Question Submission Validation Pipeline");
        System.out.println("✨ Mock Database CRUD Operations");
        System.out.println();
        
        demonstrateAuthentication();
        demonstrateAccessControl();
        demonstrateQuestionSubmission();
        demonstrateDatabaseOperations();
        demonstrateSystemIntegration();
        
        System.out.println("🎉 TP3 Demo completed successfully!");
        System.out.println("📝 All enhanced features working as expected.");
    }
    
    /**
     * Demonstrates enhanced authentication and session management.
     */
    private void demonstrateAuthentication() {
        System.out.println("🔐 Demo 1: Enhanced Authentication & Session Management");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println();
        
        // Test various user authentications
        String[] testUsers = {"student1", "reviewer1", "instructor1", "invalid_user"};
        String[] passwords = {"password123", "reviewer_pass", "admin_pass", "wrong_pass"};
        
        for (int i = 0; i < testUsers.length; i++) {
            System.out.println("Testing authentication for: " + testUsers[i]);
            
            UserCredentials credentials = new UserCredentials(testUsers[i], passwords[i]);
            AuthenticationResult result = authService.authenticate(credentials.getUsername(), credentials.getPassword());
            
            if (result.isSuccess()) {
                System.out.println("✅ Authentication successful");
                System.out.println("   User: " + result.getUser().getUsername());
                System.out.println("   Role: " + result.getUser().getRole());
                System.out.println("   Session Token: " + result.getSessionToken().substring(0, 12) + "...");
                
                // Store active session
                UserSession session = new DefaultUserSession(
                    result.getUsername(),
                    result.getUserRole(),
                    result.getSessionToken()
                );
                activeSessions.put(result.getSessionToken(), session);
            } else {
                System.out.println("❌ Authentication failed: " + result.getErrorMessage());
            }
            System.out.println();
        }
    }
    
    /**
     * Demonstrates role-based access control for different user types.
     */
    private void demonstrateAccessControl() {
        System.out.println("🛡️ Demo 2: Role-Based Access Control");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println();
        
        UserRole[] roles = {UserRole.STUDENT, UserRole.REVIEWER, UserRole.INSTRUCTOR};
        Permission[] permissions = {
            Permission.SUBMIT_QUESTION,
            Permission.VIEW_ALL_QUESTIONS,
            Permission.EDIT_QUESTIONS,
            Permission.DELETE_QUESTION,
            Permission.MANAGE_USERS,
            Permission.ADMIN_PANEL
        };
        
        for (UserRole role : roles) {
            System.out.println(role + " Permissions:");
            
            for (Permission permission : permissions) {
                AccessResult result = accessController.validateAccess(role, permission);
                
                if (result.isGranted()) {
                    System.out.println("  " + permission + ": ✅ GRANTED");
                } else {
                    System.out.println("  " + permission + ": ❌ DENIED");
                    System.out.println("    Reason: " + result.getDenialReason());
                }
            }
            System.out.println();
        }
    }
    
    /**
     * Demonstrates question submission and validation pipeline.
     */
    private void demonstrateQuestionSubmission() {
        System.out.println("📝 Demo 3: Question Submission Validation");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println();
        
        // Create test questions with different validation scenarios
        QuestionSubmissionRequest[] testQuestions = {
            new QuestionSubmissionRequest(
                "What is polymorphism in Java?",
                "Explain the concept of polymorphism and provide examples.",
                "java-oop",
                "medium"
            ),
            new QuestionSubmissionRequest(
                "", // Empty title - should fail validation
                "This question has no title.",
                "general",
                "easy"
            ),
            new QuestionSubmissionRequest(
                "What is encapsulation?",
                "A".repeat(5001), // Too long content - should fail validation
                "java-oop",
                "hard"
            )
        };
        
        for (int i = 0; i < testQuestions.length; i++) {
            System.out.println("Validating Question " + (i + 1) + ":");
            System.out.println("Title: " + (testQuestions[i].getTitle().isEmpty() ? 
                "[EMPTY]" : testQuestions[i].getTitle()));
            System.out.println("Content: " + (testQuestions[i].getContent().length() > 50 ? 
                testQuestions[i].getContent().substring(0, 50) + "..." : 
                testQuestions[i].getContent()));
            
            ValidationResult validationResult = questionService.validateQuestion(testQuestions[i]);
            
            if (validationResult.isValid()) {
                System.out.println("✅ Validation passed");
                
                // Simulate successful submission to mock database
                QuestionEntity question = new QuestionEntity(
                    testQuestions[i].getTitle(),
                    testQuestions[i].getContent(),
                    testQuestions[i].getType(),
                    testQuestions[i].getDifficulty(),
                    1 // Mock author ID
                );
                question.setCategory(testQuestions[i].getCategory());
                
                questionDatabase.add(question);
                System.out.println("✅ Question submitted successfully (ID: " + questionDatabase.size() + ")");
            } else {
                System.out.println("❌ Validation failed:");
                for (String error : validationResult.getErrors()) {
                    System.out.println("    - " + error);
                }
            }
            System.out.println();
        }
    }
    
    /**
     * Demonstrates mock database CRUD operations.
     */
    private void demonstrateDatabaseOperations() {
        System.out.println("🗄️ Demo 4: Mock Database CRUD Operations");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println();
        
        // CREATE operations
        System.out.println("Testing CREATE operations:");
        UserEntity newUser = new UserEntity("newuser", "new@example.com", "hashedpass", UserRole.STUDENT);
        userDatabase.put(newUser.getUsername(), newUser);
        System.out.println("✅ User created: " + newUser.getUsername());
        
        QuestionEntity newQuestion = new QuestionEntity(
            "What is inheritance?",
            "Explain inheritance in object-oriented programming.",
            "multiple-choice",
            "easy",
            1
        );
        questionDatabase.add(newQuestion);
        System.out.println("✅ Question created: " + newQuestion.getTitle());
        System.out.println();
        
        // READ operations
        System.out.println("Testing READ operations:");
        System.out.println("Total users in database: " + userDatabase.size());
        System.out.println("Total questions in database: " + questionDatabase.size());
        
        System.out.println("\nSample users:");
        userDatabase.values().stream()
            .limit(3)
            .forEach(user -> System.out.println("  - " + user.getUsername() + " (" + user.getRole() + ")"));
        
        System.out.println("\nSample questions:");
        questionDatabase.stream()
            .limit(3)
            .forEach(question -> System.out.println("  - " + question.getTitle()));
        System.out.println();
        
        // UPDATE operations
        System.out.println("Testing UPDATE operations:");
        if (!questionDatabase.isEmpty()) {
            QuestionEntity questionToUpdate = questionDatabase.get(0);
            String oldTitle = questionToUpdate.getTitle();
            questionToUpdate.setTitle(oldTitle + " [UPDATED]");
            System.out.println("✅ Question updated: " + oldTitle + " → " + questionToUpdate.getTitle());
        }
        System.out.println();
        
        // DELETE operations
        System.out.println("Testing DELETE operations:");
        if (questionDatabase.size() > 1) {
            QuestionEntity removedQuestion = questionDatabase.remove(questionDatabase.size() - 1);
            System.out.println("✅ Question deleted: " + removedQuestion.getTitle());
            System.out.println("Remaining questions: " + questionDatabase.size());
        }
        System.out.println();
    }
    
    /**
     * Demonstrates system integration across all components.
     */
    private void demonstrateSystemIntegration() {
        System.out.println("🔗 Demo 5: System Integration Testing");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println();
        
        // Complete workflow: Authentication → Authorization → Question Submission
        System.out.println("Complete Workflow Test:");
        System.out.println("1. Authenticate student");
        
        UserCredentials studentCreds = new UserCredentials("student1", "password123");
        AuthenticationResult authResult = authService.authenticate(studentCreds.getUsername(), studentCreds.getPassword());
        
        if (authResult.isSuccess()) {
            System.out.println("   ✅ Student authenticated: " + authResult.getUser().getUsername());
            
            System.out.println("2. Check question submission permission");
                AccessResult accessResult = accessController.validateAccess(
                    UserRole.valueOf(authResult.getUserRole()), 
                    Permission.SUBMIT_QUESTION
                );            if (accessResult.isGranted()) {
                System.out.println("   ✅ Permission granted for question submission");
                
                System.out.println("3. Submit question");
                QuestionSubmissionRequest request = new QuestionSubmissionRequest(
                    "Integration Test Question",
                    "This question was submitted through the complete workflow.",
                    "system-integration",
                    "medium"
                );
                
                ValidationResult validation = questionService.validateQuestion(request);
                if (validation.isValid()) {
                    QuestionEntity question = new QuestionEntity(
                        request.getTitle(),
                        request.getContent(),
                        request.getType(),
                        request.getDifficulty(),
                        1
                    );
                    questionDatabase.add(question);
                    System.out.println("   ✅ Question submitted successfully");
                    System.out.println("   📊 Total questions now: " + questionDatabase.size());
                } else {
                    System.out.println("   ❌ Question validation failed");
                }
            } else {
                System.out.println("   ❌ Permission denied: " + accessResult.getDenialReason());
            }
        } else {
            System.out.println("   ❌ Authentication failed: " + authResult.getErrorMessage());
        }
        System.out.println();
    }
    
    /**
     * Initializes all service components.
     */
    private void initializeServices() {
        System.out.println("🔧 Initializing services...");
        
        // Initialize data structures
        userDatabase = new HashMap<>();
        questionDatabase = new ArrayList<>();
        activeSessions = new HashMap<>();
        
        // Initialize services
        authService = new UserAuthenticationService();
        accessController = new RoleBasedAccessController();
        questionService = new QuestionSubmissionService();
        databaseService = new MockDatabaseService();
        
        System.out.println("✅ All services initialized successfully");
        System.out.println();
    }
    
    /**
     * Creates test users and data for the demonstration.
     */
    private void initializeTestData() {
        System.out.println("📊 Loading test data...");
        
        // Create test users
        UserEntity student = new UserEntity("student1", "student@asu.edu", "hashed_password_123", UserRole.STUDENT);
        UserEntity reviewer = new UserEntity("reviewer1", "reviewer@asu.edu", "hashed_reviewer_pass", UserRole.REVIEWER);
        UserEntity instructor = new UserEntity("instructor1", "instructor@asu.edu", "hashed_admin_pass", UserRole.INSTRUCTOR);
        
        userDatabase.put("student1", student);
        userDatabase.put("reviewer1", reviewer);
        userDatabase.put("instructor1", instructor);
        
        // Create sample questions
        QuestionEntity q1 = new QuestionEntity(
            "What is object-oriented programming?",
            "Define OOP and explain its main principles.",
            "conceptual",
            "easy",
            1
        );
        q1.setCategory("java-basics");
        
        QuestionEntity q2 = new QuestionEntity(
            "Explain the difference between ArrayList and LinkedList",
            "Compare performance characteristics and use cases.",
            "comparison",
            "medium",
            2
        );
        q2.setCategory("data-structures");
        
        questionDatabase.add(q1);
        questionDatabase.add(q2);
        
        System.out.println("✅ Test data loaded: " + userDatabase.size() + " users, " + questionDatabase.size() + " questions");
        System.out.println();
    }
    
    /**
     * Prints the application header with branding and metadata.
     */
    private void printHeader() {
        System.out.println("=============================================================");
        System.out.println("🎓 TP3 Enhanced Application Demo - Team Project Phase 3");
        System.out.println("   CSE 360 - Introduction to Software Engineering");
        System.out.println("   Student: Jose Mendoza");
        System.out.println("   Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("=============================================================");
        System.out.println();
    }
    
    /**
     * Mock database service for demonstration purposes.
     */
    private class MockDatabaseService {
        // Simplified mock service - in real implementation would interface with actual database
        public void initialize() {
            System.out.println("Mock database service initialized");
        }
    }
}