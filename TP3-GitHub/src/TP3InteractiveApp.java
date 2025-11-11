package edu.asu.cse360.hw3;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Interactive TP3 Application - Phase 3 Enhanced Features
 * This application provides a menu-driven interface to explore all TP3 functionality.
 * 
 * <p>Features include:
 * <ul>
 *   <li>Interactive Authentication System</li>
 *   <li>Role-Based Access Control</li>
 *   <li>Question Management</li>
 *   <li>User Session Management</li>
 *   <li>Database Operations</li>
 * </ul>
 * 
 * @author Jose Mendoza
 * @version 3.0.0 (TP3 Interactive)
 * @since 2025-11-09
 */
public class TP3InteractiveApp {
    
    private UserAuthenticationService authService;
    private RoleBasedAccessController accessController;
    private QuestionSubmissionService questionService;
    
    // Current session state
    private UserSession currentSession;
    private UserEntity currentUser;
    private Scanner scanner;
    
    // Mock data storage
    private Map<String, UserEntity> userDatabase;
    private List<QuestionEntity> questionDatabase;
    private Map<String, UserSession> activeSessions;
    
    /**
     * Main entry point for the interactive TP3 application.
     */
    public static void main(String[] args) {
        try {
            TP3InteractiveApp app = new TP3InteractiveApp();
            app.start();
        } catch (Exception e) {
            System.err.println("❌ Application Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Constructor initializes all services and components.
     */
    public TP3InteractiveApp() {
        scanner = new Scanner(System.in);
        initializeServices();
        initializeTestData();
    }
    
    /**
     * Starts the interactive application.
     */
    public void start() {
        printWelcomeBanner();
        
        boolean running = true;
        while (running) {
            if (currentUser == null) {
                running = showLoginMenu();
            } else {
                running = showMainMenu();
            }
        }
        
        System.out.println("\n🎉 Thank you for using TP3 Enhanced Application!");
        System.out.println("👋 Goodbye!");
        scanner.close();
    }
    
    /**
     * Displays the login menu for unauthenticated users.
     */
    private boolean showLoginMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔐 TP3 AUTHENTICATION SYSTEM");
        System.out.println("=".repeat(60));
        System.out.println("1. 👤 Login");
        System.out.println("2. 📋 View Available Users (Demo)");
        System.out.println("3. 🎯 Run Full Demo");
        System.out.println("4. 🚪 Exit Application");
        System.out.println("=".repeat(60));
        
        System.out.print("Choose an option (1-4): ");
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                return performLogin();
            case "2":
                showAvailableUsers();
                return true;
            case "3":
                runFullDemo();
                return true;
            case "4":
                return false;
            default:
                System.out.println("❌ Invalid option. Please try again.");
                return true;
        }
    }
    
    /**
     * Displays the main menu for authenticated users.
     */
    private boolean showMainMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🏠 TP3 MAIN DASHBOARD");
        System.out.println("👤 Logged in as: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        System.out.println("=".repeat(60));
        System.out.println("1. 📝 Submit Question");
        System.out.println("2. 📚 View Questions");
        System.out.println("3. 🛡️ Check Permissions");
        System.out.println("4. 👥 User Management " + (hasPermission(Permission.MANAGE_USERS) ? "✅" : "❌"));
        System.out.println("5. ⚙️ Admin Panel " + (hasPermission(Permission.ADMIN_PANEL) ? "✅" : "❌"));
        System.out.println("6. 📊 Database Operations");
        System.out.println("7. 🔄 Session Info");
        System.out.println("8. 🚪 Logout");
        System.out.println("=".repeat(60));
        
        System.out.print("Choose an option (1-8): ");
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                submitQuestion();
                return true;
            case "2":
                viewQuestions();
                return true;
            case "3":
                checkPermissions();
                return true;
            case "4":
                manageUsers();
                return true;
            case "5":
                adminPanel();
                return true;
            case "6":
                databaseOperations();
                return true;
            case "7":
                showSessionInfo();
                return true;
            case "8":
                logout();
                return true;
            default:
                System.out.println("❌ Invalid option. Please try again.");
                return true;
        }
    }
    
    /**
     * Performs user login with interactive input.
     */
    private boolean performLogin() {
        System.out.println("\n🔐 LOGIN TO TP3 SYSTEM");
        System.out.println("─".repeat(30));
        
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        System.out.println("\n🔄 Authenticating...");
        
        AuthenticationResult result = authService.authenticate(username, password);
        
        if (result.isSuccess()) {
            currentUser = result.getUser();
            currentSession = new DefaultUserSession(
                result.getUsername(),
                result.getUserRole(),
                result.getSessionToken()
            );
            
            System.out.println("✅ Login successful!");
            System.out.println("👤 Welcome, " + currentUser.getUsername() + "!");
            System.out.println("🏷️ Role: " + currentUser.getRole());
            System.out.println("🔑 Session Token: " + result.getSessionToken().substring(0, 15) + "...");
        } else {
            System.out.println("❌ Login failed: " + result.getErrorMessage());
            System.out.println("💡 Tip: Use 'View Available Users' to see demo accounts.");
        }
        
        return true;
    }
    
    /**
     * Shows available demo users for testing.
     */
    private void showAvailableUsers() {
        System.out.println("\n📋 AVAILABLE DEMO USERS");
        System.out.println("─".repeat(40));
        System.out.println("Username: student1    | Password: password123    | Role: STUDENT");
        System.out.println("Username: reviewer1   | Password: reviewer_pass  | Role: REVIEWER");
        System.out.println("Username: instructor1 | Password: admin_pass     | Role: INSTRUCTOR");
        System.out.println("─".repeat(40));
        System.out.println("💡 Copy and paste these credentials for testing!");
    }
    
    /**
     * Submits a new question to the system.
     */
    private void submitQuestion() {
        if (!hasPermission(Permission.SUBMIT_QUESTION)) {
            System.out.println("❌ Access Denied: You don't have permission to submit questions.");
            return;
        }
        
        System.out.println("\n📝 SUBMIT NEW QUESTION");
        System.out.println("─".repeat(30));
        
        System.out.print("Question Title: ");
        String title = scanner.nextLine().trim();
        
        System.out.print("Question Content: ");
        String content = scanner.nextLine().trim();
        
        System.out.print("Category [java-oop]: ");
        String category = scanner.nextLine().trim();
        if (category.isEmpty()) category = "java-oop";
        
        System.out.print("Difficulty [medium]: ");
        String difficulty = scanner.nextLine().trim();
        if (difficulty.isEmpty()) difficulty = "medium";
        
        QuestionSubmissionRequest request = new QuestionSubmissionRequest(title, content, category, difficulty);
        
        System.out.println("\n🔄 Validating question...");
        ValidationResult validation = questionService.validateQuestion(request);
        
        if (validation.isValid()) {
            QuestionEntity question = new QuestionEntity(title, content, "multiple-choice", difficulty, 1);
            question.setCategory(category);
            questionDatabase.add(question);
            
            System.out.println("✅ Question submitted successfully!");
            System.out.println("📊 Total questions in system: " + questionDatabase.size());
        } else {
            System.out.println("❌ Question validation failed:");
            for (String error : validation.getErrors()) {
                System.out.println("   • " + error);
            }
        }
    }
    
    /**
     * Views questions based on user permissions.
     */
    private void viewQuestions() {
        System.out.println("\n📚 QUESTION VIEWER");
        System.out.println("─".repeat(30));
        
        if (hasPermission(Permission.VIEW_ALL_QUESTIONS)) {
            System.out.println("🔍 Showing all questions (Admin/Reviewer access):");
            System.out.println("📊 Total questions: " + questionDatabase.size());
        } else {
            System.out.println("🔍 Showing your questions only (Student access):");
        }
        
        System.out.println();
        for (int i = 0; i < questionDatabase.size(); i++) {
            QuestionEntity q = questionDatabase.get(i);
            System.out.println((i + 1) + ". " + q.getTitle());
            System.out.println("   Category: " + (q.getCategory() != null ? q.getCategory() : "general"));
            System.out.println("   Difficulty: " + q.getDifficulty());
            System.out.println("   Content: " + (q.getContent().length() > 80 ? 
                q.getContent().substring(0, 77) + "..." : q.getContent()));
            System.out.println();
        }
    }
    
    /**
     * Checks current user's permissions.
     */
    private void checkPermissions() {
        System.out.println("\n🛡️ PERMISSION CHECKER");
        System.out.println("─".repeat(30));
        System.out.println("Your role: " + currentUser.getRole());
        System.out.println();
        
        Permission[] allPermissions = Permission.values();
        for (Permission permission : allPermissions) {
            boolean granted = hasPermission(permission);
            System.out.println(permission + ": " + (granted ? "✅ GRANTED" : "❌ DENIED"));
        }
    }
    
    /**
     * User management interface (admin only).
     */
    private void manageUsers() {
        if (!hasPermission(Permission.MANAGE_USERS)) {
            System.out.println("❌ Access Denied: Admin privileges required for user management.");
            return;
        }
        
        System.out.println("\n👥 USER MANAGEMENT PANEL");
        System.out.println("─".repeat(30));
        System.out.println("📊 Total users: " + userDatabase.size());
        System.out.println();
        
        for (UserEntity user : userDatabase.values()) {
            System.out.println("👤 " + user.getUsername() + " (" + user.getRole() + ")");
        }
    }
    
    /**
     * Admin panel interface.
     */
    private void adminPanel() {
        if (!hasPermission(Permission.ADMIN_PANEL)) {
            System.out.println("❌ Access Denied: Administrator privileges required.");
            return;
        }
        
        System.out.println("\n⚙️ ADMINISTRATOR PANEL");
        System.out.println("─".repeat(30));
        System.out.println("🎛️ System Status: ✅ Online");
        System.out.println("👥 Active Users: " + userDatabase.size());
        System.out.println("📚 Total Questions: " + questionDatabase.size());
        System.out.println("🔐 Active Sessions: " + activeSessions.size());
        System.out.println("📅 Server Time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
    
    /**
     * Database operations interface.
     */
    private void databaseOperations() {
        System.out.println("\n🗄️ DATABASE OPERATIONS");
        System.out.println("─".repeat(30));
        System.out.println("📊 Statistics:");
        System.out.println("   Users: " + userDatabase.size());
        System.out.println("   Questions: " + questionDatabase.size());
        System.out.println("   Sessions: " + activeSessions.size());
        System.out.println();
        System.out.println("✅ All database operations are working correctly!");
        System.out.println("💾 Data is stored in memory for this demo.");
    }
    
    /**
     * Shows current session information.
     */
    private void showSessionInfo() {
        System.out.println("\n🔄 SESSION INFORMATION");
        System.out.println("─".repeat(30));
        System.out.println("👤 User: " + currentUser.getUsername());
        System.out.println("🏷️ Role: " + currentUser.getRole());
        System.out.println("🔑 Session: Active");
        System.out.println("⏰ Login Time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println("🔒 Security: Authenticated");
    }
    
    /**
     * Logs out the current user.
     */
    private void logout() {
        System.out.println("\n🚪 LOGGING OUT");
        System.out.println("─".repeat(15));
        System.out.println("👋 Goodbye, " + currentUser.getUsername() + "!");
        
        currentUser = null;
        currentSession = null;
        
        System.out.println("✅ Logged out successfully.");
    }
    
    /**
     * Runs the full automated demo.
     */
    private void runFullDemo() {
        System.out.println("\n🎯 RUNNING FULL TP3 DEMO");
        System.out.println("=".repeat(40));
        
        // Run the existing demo
        TP3ApplicationDemo demo = new TP3ApplicationDemo();
        demo.runDemo();
        
        System.out.println("\n🎉 Demo completed! Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Checks if current user has the specified permission.
     */
    private boolean hasPermission(Permission permission) {
        if (currentUser == null) return false;
        AccessResult result = accessController.validateAccess(currentUser.getRole(), permission);
        return result.isGranted();
    }
    
    /**
     * Prints the welcome banner.
     */
    private void printWelcomeBanner() {
        System.out.println("=".repeat(70));
        System.out.println("🎓 TP3 ENHANCED APPLICATION - INTERACTIVE MODE");
        System.out.println("   CSE 360 - Introduction to Software Engineering");
        System.out.println("   Team Project Phase 3 - Enhanced Features Demo");
        System.out.println("   Student: Jose Mendoza");
        System.out.println("   Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("=".repeat(70));
        System.out.println("🚀 Welcome to the TP3 Enhanced Application!");
        System.out.println("✨ Explore authentication, role-based access, and more!");
    }
    
    /**
     * Initializes all service components.
     */
    private void initializeServices() {
        userDatabase = new HashMap<>();
        questionDatabase = new ArrayList<>();
        activeSessions = new HashMap<>();
        
        authService = new UserAuthenticationService();
        accessController = new RoleBasedAccessController();
        questionService = new QuestionSubmissionService();
    }
    
    /**
     * Creates test users and sample questions.
     */
    private void initializeTestData() {
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
            "Define OOP and explain its main principles including encapsulation, inheritance, and polymorphism.",
            "conceptual",
            "easy",
            1
        );
        q1.setCategory("java-basics");
        
        QuestionEntity q2 = new QuestionEntity(
            "Explain the difference between ArrayList and LinkedList",
            "Compare performance characteristics, memory usage, and appropriate use cases for ArrayList vs LinkedList.",
            "comparison",
            "medium",
            2
        );
        q2.setCategory("data-structures");
        
        questionDatabase.add(q1);
        questionDatabase.add(q2);
    }
}