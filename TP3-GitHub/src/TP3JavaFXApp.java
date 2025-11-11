package edu.asu.cse360.hw3;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.concurrent.Task;
import javafx.application.Platform;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * TP3 Enhanced Application - JavaFX GUI Version
 * Complete GUI demonstration of Phase 3 features using JavaFX.
 * 
 * @author Jose Mendoza
 * @version 3.0.0
 * @since 2025-11-09
 */
public class TP3JavaFXApp extends Application {
    
    private UserAuthenticationService authService;
    private RoleBasedAccessController accessController;
    private QuestionSubmissionService questionService;
    private Map<String, UserEntity> userDatabase;
    private List<QuestionEntity> questionDatabase;
    private UserEntity currentUser;
    
    // JavaFX UI Components
    private Stage primaryStage;
    private TabPane mainTabPane;
    private TextArea logArea;
    private Label statusLabel;
    private Label currentUserLabel;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        initializeServices();
        createUI();
        
        primaryStage.setTitle("TP3 Enhanced Application - Phase 3 Demo");
        primaryStage.show();
        
        // Start the demonstration
        startDemo();
    }
    
    private void initializeServices() {
        authService = new UserAuthenticationService();
        accessController = new RoleBasedAccessController();
        questionService = new QuestionSubmissionService();
        
        userDatabase = new HashMap<>();
        questionDatabase = new ArrayList<>();
        
        // Initialize sample data
        UserEntity student = new UserEntity("student1", "student@asu.edu", "hashedPass123", UserRole.STUDENT);
        UserEntity reviewer = new UserEntity("reviewer1", "reviewer@asu.edu", "hashedPass456", UserRole.REVIEWER);
        UserEntity instructor = new UserEntity("instructor1", "instructor@asu.edu", "hashedPass789", UserRole.INSTRUCTOR);
        
        userDatabase.put("student1", student);
        userDatabase.put("reviewer1", reviewer);
        userDatabase.put("instructor1", instructor);
        
        QuestionEntity q1 = new QuestionEntity("What is OOP?", "Define object-oriented programming", "conceptual", "easy", 1);
        QuestionEntity q2 = new QuestionEntity("Java Collections", "Explain ArrayList vs LinkedList", "comparison", "medium", 2);
        
        questionDatabase.add(q1);
        questionDatabase.add(q2);
    }
    
    private void createUI() {
        BorderPane root = new BorderPane();
        
        // Header
        VBox header = createHeader();
        root.setTop(header);
        
        // Main content with tabs
        mainTabPane = createMainTabs();
        root.setCenter(mainTabPane);
        
        // Status bar
        HBox statusBar = createStatusBar();
        root.setBottom(statusBar);
        
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
    }
    
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white;");
        
        Label title = new Label("🎓 TP3 Enhanced Application - Phase 3 Demo");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Label subtitle = new Label("CSE 360 - Introduction to Software Engineering | Student: Jose Mendoza");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #bdc3c7;");
        
        Label dateLabel = new Label("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #95a5a6;");
        
        currentUserLabel = new Label("Current User: Not logged in");
        currentUserLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        
        header.getChildren().addAll(title, subtitle, dateLabel, currentUserLabel);
        return header;
    }
    
    private TabPane createMainTabs() {
        TabPane tabPane = new TabPane();
        
        // Authentication Tab
        Tab authTab = new Tab("🔐 Authentication", createAuthenticationTab());
        authTab.setClosable(false);
        
        // Access Control Tab
        Tab accessTab = new Tab("🛡️ Access Control", createAccessControlTab());
        accessTab.setClosable(false);
        
        // Question Management Tab
        Tab questionTab = new Tab("📝 Questions", createQuestionManagementTab());
        questionTab.setClosable(false);
        
        // System Statistics Tab
        Tab statsTab = new Tab("📊 Statistics", createStatisticsTab());
        statsTab.setClosable(false);
        
        // Demo Log Tab
        Tab logTab = new Tab("📋 Demo Log", createDemoLogTab());
        logTab.setClosable(false);
        
        tabPane.getTabs().addAll(authTab, accessTab, questionTab, statsTab, logTab);
        return tabPane;
    }
    
    private VBox createAuthenticationTab() {
        VBox authBox = new VBox(15);
        authBox.setPadding(new Insets(20));
        
        Label title = new Label("Authentication System Demo");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // User selection
        ComboBox<String> userCombo = new ComboBox<>();
        userCombo.getItems().addAll("student1", "reviewer1", "instructor1", "invalid_user");
        userCombo.setValue("student1");
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setText("password123");
        
        Button loginButton = new Button("🔑 Authenticate User");
        loginButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        
        TextArea authResults = new TextArea();
        authResults.setPrefRowCount(10);
        authResults.setEditable(false);
        
        loginButton.setOnAction(e -> {
            String username = userCombo.getValue();
            String password = passwordField.getText();
            
            if (username.equals("student1")) password = "password123";
            else if (username.equals("reviewer1")) password = "reviewer_pass";
            else if (username.equals("instructor1")) password = "admin_pass";
            
            authResults.appendText("🔑 Attempting login: " + username + "\\n");
            
            AuthenticationResult result = authService.authenticate(username, password);
            
            if (result.isSuccess()) {
                currentUser = result.getUser();
                authResults.appendText("✅ SUCCESS\\n");
                authResults.appendText("   User: " + result.getUsername() + "\\n");
                authResults.appendText("   Role: " + result.getUserRole() + "\\n");
                authResults.appendText("   Session: " + result.getSessionToken().substring(0, 16) + "...\\n\\n");
                
                currentUserLabel.setText("Current User: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
                currentUserLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #27ae60; -fx-font-weight: bold;");
                
                logMessage("User " + username + " authenticated successfully");
            } else {
                authResults.appendText("❌ FAILED: " + result.getErrorMessage() + "\\n\\n");
                logMessage("Authentication failed for " + username);
            }
        });
        
        Button demoAllButton = new Button("🎮 Demo All Users");
        demoAllButton.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold;");
        
        demoAllButton.setOnAction(e -> {
            authResults.clear();
            String[] users = {"student1", "reviewer1", "instructor1", "invalid_user"};
            String[] passwords = {"password123", "reviewer_pass", "admin_pass", "wrong_pass"};
            
            for (int i = 0; i < users.length; i++) {
                authResults.appendText("🔑 Testing: " + users[i] + "\\n");
                AuthenticationResult result = authService.authenticate(users[i], passwords[i]);
                
                if (result.isSuccess()) {
                    authResults.appendText("✅ SUCCESS - " + result.getUserRole() + "\\n");
                    if (i == 0) {
                        currentUser = result.getUser();
                        currentUserLabel.setText("Current User: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
                        currentUserLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    }
                } else {
                    authResults.appendText("❌ FAILED: " + result.getErrorMessage() + "\\n");
                }
                authResults.appendText("\\n");
            }
        });
        
        HBox userInputs = new HBox(10);
        userInputs.getChildren().addAll(new Label("User:"), userCombo, new Label("Password:"), passwordField);
        userInputs.setAlignment(Pos.CENTER_LEFT);
        
        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(loginButton, demoAllButton);
        
        authBox.getChildren().addAll(title, userInputs, buttons, authResults);
        return authBox;
    }
    
    private VBox createAccessControlTab() {
        VBox accessBox = new VBox(15);
        accessBox.setPadding(new Insets(20));
        
        Label title = new Label("Role-Based Access Control Demo");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Button testAllButton = new Button("🛡️ Test All Role Permissions");
        testAllButton.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-font-weight: bold;");
        
        TextArea accessResults = new TextArea();
        accessResults.setPrefRowCount(15);
        accessResults.setEditable(false);
        
        testAllButton.setOnAction(e -> {
            accessResults.clear();
            
            UserRole[] roles = {UserRole.STUDENT, UserRole.REVIEWER, UserRole.INSTRUCTOR};
            Permission[] permissions = {
                Permission.SUBMIT_QUESTION, Permission.VIEW_ALL_QUESTIONS, Permission.EDIT_QUESTIONS,
                Permission.DELETE_QUESTION, Permission.MANAGE_USERS, Permission.ADMIN_PANEL
            };
            
            for (UserRole role : roles) {
                accessResults.appendText("👥 " + role + " Permissions:\\n");
                accessResults.appendText("─".repeat(30) + "\\n");
                
                for (Permission permission : permissions) {
                    AccessResult result = accessController.validateAccess(role, permission);
                    String status = result.isGranted() ? "✅ GRANTED" : "❌ DENIED";
                    accessResults.appendText("   " + permission + ": " + status + "\\n");
                }
                accessResults.appendText("\\n");
            }
            
            logMessage("Access control permissions tested for all roles");
        });
        
        accessBox.getChildren().addAll(title, testAllButton, accessResults);
        return accessBox;
    }
    
    private VBox createQuestionManagementTab() {
        VBox questionBox = new VBox(15);
        questionBox.setPadding(new Insets(20));
        
        Label title = new Label("Question Management Workflow");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Question creation form
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        
        TextField titleField = new TextField();
        titleField.setPromptText("Question title");
        titleField.setText("What is inheritance in Java?");
        
        TextArea contentArea = new TextArea();
        contentArea.setPrefRowCount(3);
        contentArea.setPromptText("Question content");
        contentArea.setText("Explain the concept of inheritance and provide examples.");
        
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("java-oop", "data-structures", "algorithms", "general");
        categoryCombo.setValue("java-oop");
        
        ComboBox<String> difficultyCombo = new ComboBox<>();
        difficultyCombo.getItems().addAll("easy", "medium", "hard");
        difficultyCombo.setValue("medium");
        
        form.add(new Label("Title:"), 0, 0);
        form.add(titleField, 1, 0);
        form.add(new Label("Content:"), 0, 1);
        form.add(contentArea, 1, 1);
        form.add(new Label("Category:"), 0, 2);
        form.add(categoryCombo, 1, 2);
        form.add(new Label("Difficulty:"), 0, 3);
        form.add(difficultyCombo, 1, 3);
        
        Button submitButton = new Button("📝 Submit Question");
        submitButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        
        TextArea questionResults = new TextArea();
        questionResults.setPrefRowCount(8);
        questionResults.setEditable(false);
        
        submitButton.setOnAction(e -> {
            if (currentUser == null) {
                questionResults.appendText("❌ Please login first!\\n\\n");
                return;
            }
            
            // Check permissions
            AccessResult access = accessController.validateAccess(currentUser.getRole(), Permission.SUBMIT_QUESTION);
            
            if (!access.isGranted()) {
                questionResults.appendText("❌ Permission denied: " + access.getDenialReason() + "\\n\\n");
                return;
            }
            
            QuestionSubmissionRequest request = new QuestionSubmissionRequest(
                titleField.getText(),
                contentArea.getText(),
                categoryCombo.getValue(),
                difficultyCombo.getValue()
            );
            
            ValidationResult validation = questionService.validateQuestion(request);
            
            if (validation.isValid()) {
                QuestionEntity newQuestion = new QuestionEntity(
                    request.getTitle(),
                    request.getContent(),
                    request.getType(),
                    request.getDifficulty(),
                    1
                );
                questionDatabase.add(newQuestion);
                
                questionResults.appendText("✅ Question submitted successfully!\\n");
                questionResults.appendText("   Title: " + request.getTitle() + "\\n");
                questionResults.appendText("   Category: " + request.getCategory() + "\\n");
                questionResults.appendText("   📈 Total questions: " + questionDatabase.size() + "\\n\\n");
                
                logMessage("Question submitted: " + request.getTitle());
            } else {
                questionResults.appendText("❌ Validation failed:\\n");
                for (String error : validation.getErrors()) {
                    questionResults.appendText("   - " + error + "\\n");
                }
                questionResults.appendText("\\n");
            }
        });
        
        questionBox.getChildren().addAll(title, form, submitButton, questionResults);
        return questionBox;
    }
    
    private VBox createStatisticsTab() {
        VBox statsBox = new VBox(15);
        statsBox.setPadding(new Insets(20));
        
        Label title = new Label("System Statistics & Analytics");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Button refreshButton = new Button("📊 Refresh Statistics");
        refreshButton.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold;");
        
        TextArea statsArea = new TextArea();
        statsArea.setPrefRowCount(15);
        statsArea.setEditable(false);
        
        refreshButton.setOnAction(e -> {
            statsArea.clear();
            
            statsArea.appendText("📊 SYSTEM STATISTICS\\n");
            statsArea.appendText("═".repeat(50) + "\\n\\n");
            
            // User statistics
            statsArea.appendText("👥 User Statistics:\\n");
            statsArea.appendText("   Total Users: " + userDatabase.size() + "\\n");
            
            Map<UserRole, Long> roleCount = new HashMap<>();
            userDatabase.values().forEach(user -> {
                roleCount.merge(user.getRole(), 1L, Long::sum);
            });
            
            roleCount.forEach((role, count) -> {
                statsArea.appendText("   " + role + ": " + count + " users\\n");
            });
            
            // Question statistics
            statsArea.appendText("\\n📝 Question Statistics:\\n");
            statsArea.appendText("   Total Questions: " + questionDatabase.size() + "\\n");
            statsArea.appendText("   Sample Questions:\\n");
            
            questionDatabase.stream().limit(5).forEach(question -> {
                statsArea.appendText("   - " + question.getTitle() + "\\n");
            });
            
            // Security statistics
            statsArea.appendText("\\n🔐 Security Statistics:\\n");
            statsArea.appendText("   Current User: " + (currentUser != null ? currentUser.getUsername() : "None") + "\\n");
            statsArea.appendText("   Active Session: " + (currentUser != null ? "Yes" : "No") + "\\n");
            
            logMessage("Statistics refreshed");
        });
        
        // Auto-refresh on tab load
        Platform.runLater(() -> refreshButton.fire());
        
        statsBox.getChildren().addAll(title, refreshButton, statsArea);
        return statsBox;
    }
    
    private VBox createDemoLogTab() {
        VBox logBox = new VBox(15);
        logBox.setPadding(new Insets(20));
        
        Label title = new Label("Application Demo Log");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        logArea = new TextArea();
        logArea.setPrefRowCount(20);
        logArea.setEditable(false);
        logArea.setStyle("-fx-font-family: 'Courier New', monospace;");
        
        Button clearLogButton = new Button("🗑️ Clear Log");
        clearLogButton.setOnAction(e -> logArea.clear());
        
        logBox.getChildren().addAll(title, clearLogButton, logArea);
        return logBox;
    }
    
    private HBox createStatusBar() {
        HBox statusBar = new HBox(10);
        statusBar.setPadding(new Insets(5, 15, 5, 15));
        statusBar.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-width: 1 0 0 0;");
        
        statusLabel = new Label("Ready - TP3 Enhanced Application");
        statusLabel.setStyle("-fx-font-size: 12px;");
        
        Label versionLabel = new Label("Version 3.0.0");
        versionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        statusBar.getChildren().addAll(statusLabel, spacer, versionLabel);
        return statusBar;
    }
    
    private void startDemo() {
        logMessage("TP3 Enhanced Application started");
        logMessage("Welcome! Explore the tabs to test Phase 3 features");
        logMessage("🔐 Authentication: Test user login scenarios");
        logMessage("🛡️ Access Control: Check role-based permissions");
        logMessage("📝 Questions: Submit and validate questions");
        logMessage("📊 Statistics: View system analytics");
    }
    
    private void logMessage(String message) {
        if (logArea != null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            Platform.runLater(() -> {
                logArea.appendText("[" + timestamp + "] " + message + "\\n");
                statusLabel.setText(message);
            });
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}