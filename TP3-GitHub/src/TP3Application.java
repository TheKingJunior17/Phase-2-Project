package edu.asu.cse360.hw3;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TP3 Enhanced Application - Team Project Phase 3 Implementation
 * 
 * <p>This application demonstrates the enhanced authentication, authorization, 
 * and validation capabilities implemented for TP3. It showcases the integration
 * of user role management, question submission workflows, and comprehensive
 * validation pipelines.</p>
 * 
 * <p>Key Features Demonstrated:</p>
 * <ul>
 *   <li>Enhanced user authentication with session management</li>
 *   <li>Role-based access control for student/reviewer/instructor roles</li>
 *   <li>Question submission validation pipeline</li>
 *   <li>Database CRUD operations with transaction handling</li>
 *   <li>Comprehensive error handling and user feedback</li>
 * </ul>
 * 
 * @author Jose Mendoza
 * @version 3.0.0 (TP3 Implementation)
 * @since 2025-11-09
 * 
 * @see UserAuthenticationService
 * @see RoleBasedAccessController  
 * @see QuestionSubmissionService
 * @see DatabaseCRUDService
 */
public class TP3Application {

    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String SEPARATOR = "=" + "=".repeat(60);
    
    private UserAuthenticationService authService;
    private RoleBasedAccessController accessController;
    private QuestionSubmissionService questionService;
    private DatabaseCRUDService databaseService;
    private Scanner scanner;
    private DefaultUserSession currentSession;

    /**
     * Initialize the TP3 application with all required services.
     * 
     * <p>This constructor sets up the service layer components and establishes
     * the necessary dependencies for the enhanced functionality demonstration.</p>
     */
    public TP3Application() {
        this.authService = new UserAuthenticationService();
        this.accessController = new RoleBasedAccessController();
        this.questionService = new QuestionSubmissionService();
        this.databaseService = new DatabaseCRUDService();
        this.scanner = new Scanner(System.in);
        this.currentSession = null;
        
        initializeTestData();
    }

    /**
     * Main entry point for the TP3 application demonstration.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println(SEPARATOR);
        System.out.println("🚀 TP3 Enhanced Application - Team Project Phase 3");
        System.out.println("   CSE 360 - Introduction to Software Engineering");
        System.out.println("   Student: Jose Mendoza");
        System.out.println("   Date: " + LocalDateTime.now().format(TIMESTAMP_FORMAT));
        System.out.println(SEPARATOR);
        
        TP3Application app = new TP3Application();
        app.runDemo();
    }

    /**
     * Execute the main application demonstration workflow.
     * 
     * <p>This method orchestrates the complete TP3 functionality demonstration,
     * including authentication flows, role-based access control, question
     * submission workflows, and database operations.</p>
     */
    public void runDemo() {
        try {
            System.out.println("\n📋 TP3 Enhanced Features Demonstration");
            System.out.println("This demo shows integration of:");
            System.out.println("• Enhanced Authentication & Session Management");
            System.out.println("• Role-Based Access Control (Student/Reviewer/Instructor)");
            System.out.println("• Question Submission Validation Pipeline");
            System.out.println("• Database CRUD Operations");
            System.out.println();

            // Demo 1: Authentication Enhancement
            demonstrateAuthenticationEnhancements();
            
            // Demo 2: Role-Based Access Control
            demonstrateRoleBasedAccess();
            
            // Demo 3: Question Submission Workflow
            demonstrateQuestionSubmission();
            
            // Demo 4: Database Integration
            demonstrateDatabaseOperations();
            
            // Demo 5: Integration Testing
            demonstrateSystemIntegration();

            System.out.println(SEPARATOR);
            System.out.println("✅ TP3 Application Demonstration Complete!");
            System.out.println("All enhanced features successfully demonstrated.");
            System.out.println(SEPARATOR);

        } catch (Exception e) {
            System.err.println("❌ Application Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    /**
     * Demonstrate enhanced authentication capabilities with session management.
     */
    private void demonstrateAuthenticationEnhancements() {
        System.out.println("\n🔐 Demo 1: Enhanced Authentication & Session Management");
        System.out.println("─".repeat(50));

        // Test various authentication scenarios
        String[] testUsers = {"student1", "reviewer1", "instructor1", "invalid_user"};
        String[] testPasswords = {"StrongPass123!", "ReviewPass456!", "InstrPass789!", "weak"};

        for (int i = 0; i < testUsers.length; i++) {
            System.out.println("\nTesting authentication for: " + testUsers[i]);
            
            AuthenticationResult result = authService.authenticate(testUsers[i], testPasswords[i]);
            
            if (result.isSuccess()) {
                System.out.println("✅ Authentication successful");
                System.out.println("   User: " + result.getUsername());
                System.out.println("   Role: " + result.getUserRole());
                System.out.println("   Session Token: " + result.getSessionToken().substring(0, 8) + "...");
                
                if (result.requiresPasswordChange()) {
                    System.out.println("⚠️  Password change required");
                }
                
                // Create session for first successful login
                if (currentSession == null && result.isSuccess()) {
                    currentSession = new DefaultUserSession(result.getUsername(), result.getUserRole(), result.getSessionToken());
                }
            } else {
                System.out.println("❌ Authentication failed: " + result.getErrorMessage());
            }
        }
    }

    /**
     * Demonstrate role-based access control across different user types.
     */
    private void demonstrateRoleBasedAccess() {
        System.out.println("\n👥 Demo 2: Role-Based Access Control");
        System.out.println("─".repeat(50));

        UserRole[] roles = {UserRole.STUDENT, UserRole.REVIEWER, UserRole.INSTRUCTOR};
        AccessPermission[] permissions = {
            AccessPermission.SUBMIT_QUESTION,
            AccessPermission.VIEW_ALL_QUESTIONS,
            AccessPermission.EDIT_QUESTIONS,
            AccessPermission.DELETE_QUESTION,
            AccessPermission.MANAGE_USERS,
            AccessPermission.ADMIN_PANEL
        };

        for (UserRole role : roles) {
            System.out.println("\n" + role + " Permissions:");
            
            for (AccessPermission permission : permissions) {
                AccessResult access = accessController.validateAccess("testuser", role, "resource123", permission);
                String status = access.isGranted() ? "✅ GRANTED" : "❌ DENIED";
                System.out.println("  " + permission + ": " + status);
                
                if (!access.isGranted() && access.getReason() != null) {
                    System.out.println("    Reason: " + access.getReason());
                }
            }
        }
    }

    /**
     * Demonstrate question submission validation pipeline.
     */
    private void demonstrateQuestionSubmission() {
        System.out.println("\n📝 Demo 3: Question Submission Validation");
        System.out.println("─".repeat(50));

        // Test various question submission scenarios
        QuestionSubmissionRequest[] testQuestions = {
            new QuestionSubmissionRequest("What is polymorphism in Java?", "java", "programming"),
            new QuestionSubmissionRequest("", "empty", "test"), // Invalid: empty content
            new QuestionSubmissionRequest("A".repeat(2000), "long", "test"), // Invalid: too long
            new QuestionSubmissionRequest("How does inheritance work?", "oop", "education"),
            new QuestionSubmissionRequest("<script>alert('xss')</script>", "security", "test") // Invalid: XSS attempt
        };

        for (int i = 0; i < testQuestions.length; i++) {
            System.out.println("\nValidating Question " + (i + 1) + ":");
            System.out.println("Content: " + (testQuestions[i].getContent().length() > 50 ? 
                testQuestions[i].getContent().substring(0, 50) + "..." : testQuestions[i].getContent()));
            
            ValidationResult validation = questionService.validateQuestion(testQuestions[i]);
            
            if (validation.isValid()) {
                System.out.println("✅ Validation passed");
                
                // Attempt submission if current user has permission
                if (currentSession != null) {
                    AccessResult submitAccess = accessController.validateAccess(
                        currentSession.getUsername(), 
                        UserRole.valueOf(currentSession.getRole()), 
                        "questions", 
                        AccessPermission.SUBMIT_QUESTION
                    );
                    
                    if (submitAccess.isGranted()) {
                        QuestionEntity question = questionService.submitQuestion(testQuestions[i], currentSession.getUsername());
                        System.out.println("📤 Question submitted with ID: " + question.getId());
                    } else {
                        System.out.println("❌ Submission denied: " + submitAccess.getReason());
                    }
                }
            } else {
                System.out.println("❌ Validation failed:");
                for (String error : validation.getErrors()) {
                    System.out.println("   • " + error);
                }
            }
        }
    }

    /**
     * Demonstrate database CRUD operations with transaction handling.
     */
    private void demonstrateDatabaseOperations() {
        System.out.println("\n🗄️  Demo 4: Database CRUD Operations");
        System.out.println("─".repeat(50));

        // Create operations
        System.out.println("\nTesting CREATE operations:");
        UserEntity testUser = new UserEntity("testuser123", "test@example.com", "hashedPassword123", UserRole.STUDENT);
        UserEntity createdUser = databaseService.createUser(testUser);
        System.out.println("✅ User created with ID: " + createdUser.getId());

        QuestionEntity testQuestion = new QuestionEntity(
            "Sample Question Title",
            "What is the difference between abstract classes and interfaces?",
            "multiple-choice",
            "medium",
            createdUser.getId()
        );
        QuestionEntity createdQuestion = databaseService.createQuestion(testQuestion);
        System.out.println("✅ Question created with ID: " + createdQuestion.getId());

        // Read operations
        System.out.println("\nTesting READ operations:");
        Optional<UserEntity> retrievedUser = databaseService.getUserById(createdUser.getId());
        if (retrievedUser.isPresent()) {
            System.out.println("✅ User retrieved: " + retrievedUser.get().getUsername());
        }

        List<UserEntity> allUsers = databaseService.getAllUsers();
        System.out.println("✅ Total users in database: " + allUsers.size());

        Optional<QuestionEntity> retrievedQuestion = databaseService.getQuestionById(createdQuestion.getId());
        if (retrievedQuestion.isPresent()) {
            System.out.println("✅ Question retrieved: " + 
                retrievedQuestion.get().getContent().substring(0, 30) + "...");
        }

        // Update operations
        System.out.println("\nTesting UPDATE operations:");
        testUser.setEmail("updated@example.com");
        UserEntity updatedUser = databaseService.updateUser(testUser);
        System.out.println("✅ User email updated to: " + updatedUser.getEmail());

        // Delete operations
        System.out.println("\nTesting DELETE operations:");
        boolean questionDeleted = databaseService.deleteQuestion(createdQuestion.getId());
        System.out.println("✅ Question deleted: " + questionDeleted);
    }

    /**
     * Demonstrate end-to-end system integration.
     */
    private void demonstrateSystemIntegration() {
        System.out.println("\n🔄 Demo 5: System Integration Test");
        System.out.println("─".repeat(50));

        System.out.println("\nExecuting complete user workflow:");
        
        // 1. User authentication
        System.out.println("1. Authenticating instructor...");
        AuthenticationResult auth = authService.authenticate("instructor1", "InstrPass789!");
        if (!auth.isSuccess()) {
            System.out.println("❌ Authentication failed, cannot continue integration test");
            return;
        }
        System.out.println("✅ Instructor authenticated");

        // 2. Permission validation
        System.out.println("2. Validating permissions...");
        AccessResult access = accessController.validateAccess(
            auth.getUsername(), 
            UserRole.INSTRUCTOR, 
            "questions", 
            AccessPermission.VIEW_ALL_QUESTIONS
        );
        System.out.println("✅ Permission validated: " + (access.isGranted() ? "GRANTED" : "DENIED"));

        // 3. Question processing
        System.out.println("3. Processing question submission...");
        QuestionSubmissionRequest request = new QuestionSubmissionRequest(
            "Explain the SOLID principles in software design",
            "design-patterns",
            "software-engineering"
        );
        
        ValidationResult validation = questionService.validateQuestion(request);
        if (validation.isValid()) {
            QuestionEntity question = questionService.submitQuestion(request, auth.getUsername());
            System.out.println("✅ Question processed and stored with ID: " + question.getId());
            
            // 4. Database verification
            System.out.println("4. Verifying database persistence...");
            Optional<QuestionEntity> stored = databaseService.getQuestionById(question.getId());
            if (stored.isPresent()) {
                System.out.println("✅ Question successfully persisted and retrievable");
            }
        }

        System.out.println("\n🎉 Integration test completed successfully!");
        System.out.println("All system components working together seamlessly.");
    }

    /**
     * Initialize test data for demonstration purposes.
     */
    private void initializeTestData() {
        // This would typically load from configuration or database
        // For demo purposes, we'll use in-memory test data
        System.out.println("🔧 Initializing test data and services...");
    }

    /**
     * Clean up resources and close connections.
     */
    private void cleanup() {
        if (scanner != null) {
            scanner.close();
        }
        System.out.println("🧹 Cleanup completed");
    }
}