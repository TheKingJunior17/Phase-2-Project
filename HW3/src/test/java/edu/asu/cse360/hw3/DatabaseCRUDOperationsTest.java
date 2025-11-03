package edu.asu.cse360.hw3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for database CRUD (Create, Read, Update, Delete) operations.
 * 
 * <p>This test class validates all database operations for core entities including
 * Users, Questions, and Answers. The test suite ensures data integrity, proper
 * transaction handling, and robust error recovery across all database interactions.</p>
 * 
 * <p>The CRUD operations test covers:</p>
 * <ul>
 *   <li><strong>Create Operations:</strong> Entity insertion with data validation</li>
 *   <li><strong>Read Operations:</strong> Data retrieval and query functionality</li>
 *   <li><strong>Update Operations:</strong> Entity modification and versioning</li>
 *   <li><strong>Delete Operations:</strong> Safe entity removal and cascading</li>
 *   <li><strong>Transaction Management:</strong> ACID compliance and rollback handling</li>
 * </ul>
 * 
 * <p>This test implementation uses an embedded H2 database to ensure consistent
 * and isolated testing without external dependencies or data pollution.</p>
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 * 
 * @see DatabaseCRUDService
 * @see UserEntity
 * @see QuestionEntity
 * @see AnswerEntity
 * 
 * @testCase Test 5: Database CRUD Operations Test
 * @precondition Database schema is initialized and CRUD service is properly configured
 * @postcondition All CRUD operations maintain data integrity and handle errors appropriately
 * @expectedResult All database operations complete successfully with proper data persistence and retrieval
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Database CRUD Operations Test Suite")
public class DatabaseCRUDOperationsTest {
    
    /** Database connection for testing */
    private Connection testConnection;
    
    /** The CRUD service under test */
    private DatabaseCRUDService crudService;
    
    /** Test database URL for H2 in-memory database */
    private static final String TEST_DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    /** Test data constants */
    private static final String TEST_USERNAME = "testuser1";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_QUESTION_TITLE = "Test Question Title";
    private static final String TEST_QUESTION_TEXT = "This is a test question for database operations.";
    private static final String TEST_ANSWER_TEXT = "This is a test answer for database operations.";
    
    /**
     * Sets up the test environment before each test method.
     * 
     * <p>This method initializes an in-memory H2 database, creates the required
     * schema, and sets up a fresh DatabaseCRUDService instance. This ensures
     * test isolation and consistent database state for each test case.</p>
     * 
     * @throws SQLException if database setup fails
     * 
     * @testCase Initialize database and CRUD service before each test
     * @precondition H2 database driver is available and functional
     * @postcondition Fresh database with schema and CRUD service ready for testing
     * @expectedResult Database is initialized with clean schema and CRUD service is operational
     */
    @BeforeEach
    void setUp() throws SQLException {
        // Initialize test database connection
        testConnection = DriverManager.getConnection(TEST_DB_URL, DB_USER, DB_PASSWORD);
        testConnection.setAutoCommit(false); // Enable transaction control
        
        // Create database schema
        createTestSchema();
        
        // Initialize CRUD service
        crudService = new DatabaseCRUDService(testConnection);
    }
    
    /**
     * Creates the test database schema with all required tables.
     * 
     * <p>This helper method sets up the database schema for testing including
     * Users, Questions, and Answers tables with proper relationships and
     * constraints. The schema matches the production database structure.</p>
     * 
     * @throws SQLException if schema creation fails
     */
    private void createTestSchema() throws SQLException {
        try (Statement stmt = testConnection.createStatement()) {
            // Users table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(255) UNIQUE NOT NULL,
                    email VARCHAR(255) UNIQUE NOT NULL,
                    password_hash VARCHAR(255) NOT NULL,
                    role VARCHAR(50) NOT NULL DEFAULT 'STUDENT',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Questions table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS questions (
                    id INTEGER AUTO_INCREMENT PRIMARY KEY,
                    title VARCHAR(255) NOT NULL,
                    question_text TEXT NOT NULL,
                    author_id INTEGER NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
                )
            """);
            
            // Answers table  
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS answers (
                    id INTEGER AUTO_INCREMENT PRIMARY KEY,
                    question_id INTEGER NOT NULL,
                    answer_text TEXT NOT NULL,
                    author_id INTEGER NOT NULL,
                    is_accepted BOOLEAN DEFAULT FALSE,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE,
                    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
                )
            """);
            
            testConnection.commit();
        }
    }
    
    /**
     * Tests successful user creation with valid data.
     * 
     * <p>This test validates that the CRUD service can successfully create
     * new user entities with proper data validation and auto-generated IDs.
     * The created users should be immediately retrievable and have all
     * expected attributes properly stored.</p>
     * 
     * @param username the username to test for user creation
     * @param email the email to test for user creation
     * @param role the role to assign to the test user
     * 
     * @testCase Verify successful user entity creation
     * @precondition Database schema is initialized and CRUD service is ready
     * @postcondition User is created successfully with generated ID and proper attributes
     * @expectedResult UserEntity with valid ID and all specified attributes
     */
    @ParameterizedTest(name = "Create user: {0} with role {2}")
    @CsvSource({
        "student1, student1@test.com, STUDENT",
        "admin1, admin1@test.com, ADMIN",
        "reviewer1, reviewer1@test.com, REVIEWER",
        "staff1, staff1@test.com, STAFF"
    })
    @DisplayName("Test Successful User Creation")
    void testSuccessfulUserCreation(String username, String email, String role) throws SQLException {
        // Create user entity
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash("hashed_password_123");
        user.setRole(role);
        
        // Execute create operation
        UserEntity createdUser = crudService.createUser(user);
        
        // Verify successful creation
        assertThat(createdUser)
            .as("Created user should not be null")
            .isNotNull();
            
        assertThat(createdUser.getId())
            .as("Created user should have generated ID")
            .isNotNull()
            .isPositive();
            
        assertThat(createdUser.getUsername())
            .as("Username should match input")
            .isEqualTo(username);
            
        assertThat(createdUser.getEmail())
            .as("Email should match input")
            .isEqualTo(email);
            
        assertThat(createdUser.getRole())
            .as("Role should match input")
            .isEqualTo(role);
            
        assertThat(createdUser.getCreatedAt())
            .as("Created timestamp should be set")
            .isNotNull();
            
        // Commit transaction for verification
        testConnection.commit();
    }
    
    /**
     * Tests user retrieval by ID and username.
     * 
     * <p>This test validates that users can be successfully retrieved from
     * the database using both ID-based and username-based queries. The
     * retrieved user data should match the originally stored information.</p>
     * 
     * @testCase Verify user retrieval operations
     * @precondition User exists in database
     * @postcondition User is retrieved successfully with correct attributes
     * @expectedResult Optional containing UserEntity with matching data
     */
    @Test
    @DisplayName("Test User Retrieval Operations")
    void testUserRetrievalOperations() throws SQLException {
        // Create test user first
        UserEntity user = new UserEntity();
        user.setUsername(TEST_USERNAME);
        user.setEmail(TEST_EMAIL);
        user.setPasswordHash("test_hash");
        user.setRole("STUDENT");
        
        UserEntity createdUser = crudService.createUser(user);
        testConnection.commit();
        
        // Test retrieval by ID
        Optional<UserEntity> retrievedById = crudService.getUserById(createdUser.getId());
        
        assertThat(retrievedById)
            .as("User should be found by ID")
            .isPresent();
            
        assertThat(retrievedById.get().getUsername())
            .as("Retrieved username should match")
            .isEqualTo(TEST_USERNAME);
            
        // Test retrieval by username
        Optional<UserEntity> retrievedByUsername = crudService.getUserByUsername(TEST_USERNAME);
        
        assertThat(retrievedByUsername)
            .as("User should be found by username")
            .isPresent();
            
        assertThat(retrievedByUsername.get().getId())
            .as("Retrieved user ID should match")
            .isEqualTo(createdUser.getId());
    }
    
    /**
     * Tests user update operations with data modification.
     * 
     * <p>This test validates that existing user entities can be successfully
     * updated with new information while maintaining data integrity and
     * proper versioning. Updated timestamps should be automatically managed.</p>
     * 
     * @testCase Verify user entity update operations
     * @precondition User exists in database
     * @postcondition User is updated successfully with new data and updated timestamp
     * @expectedResult Updated UserEntity with modified attributes and new timestamp
     */
    @Test
    @DisplayName("Test User Update Operations")
    void testUserUpdateOperations() throws SQLException {
        // Create test user first
        UserEntity user = new UserEntity();
        user.setUsername(TEST_USERNAME);
        user.setEmail(TEST_EMAIL);
        user.setPasswordHash("original_hash");
        user.setRole("STUDENT");
        
        UserEntity createdUser = crudService.createUser(user);
        testConnection.commit();
        
        // Update user data
        createdUser.setEmail("updated@example.com");
        createdUser.setRole("ADMIN");
        createdUser.setPasswordHash("updated_hash");
        
        // Execute update operation
        UserEntity updatedUser = crudService.updateUser(createdUser);
        
        // Verify successful update
        assertThat(updatedUser)
            .as("Updated user should not be null")
            .isNotNull();
            
        assertThat(updatedUser.getEmail())
            .as("Email should be updated")
            .isEqualTo("updated@example.com");
            
        assertThat(updatedUser.getRole())
            .as("Role should be updated")
            .isEqualTo("ADMIN");
            
        assertThat(updatedUser.getUpdatedAt())
            .as("Updated timestamp should be newer than created timestamp")
            .isAfter(updatedUser.getCreatedAt());
            
        testConnection.commit();
        
        // Verify update persistence
        Optional<UserEntity> retrievedUser = crudService.getUserById(updatedUser.getId());
        assertThat(retrievedUser.get().getEmail())
            .as("Updated email should persist in database")
            .isEqualTo("updated@example.com");
    }
    
    /**
     * Tests question creation and retrieval operations.
     * 
     * <p>This test validates that question entities can be successfully created
     * with proper foreign key relationships to user entities and retrieved
     * with all associated data intact.</p>
     * 
     * @testCase Verify question entity CRUD operations
     * @precondition User exists to be question author
     * @postcondition Question is created and retrievable with proper relationships
     * @expectedResult QuestionEntity with valid ID and author relationship
     */
    @Test
    @DisplayName("Test Question Creation and Retrieval Operations")
    void testQuestionCreationAndRetrieval() throws SQLException {
        // Create author user first
        UserEntity author = new UserEntity();
        author.setUsername(TEST_USERNAME);
        author.setEmail(TEST_EMAIL);
        author.setPasswordHash("test_hash");
        author.setRole("STUDENT");
        
        UserEntity createdAuthor = crudService.createUser(author);
        testConnection.commit();
        
        // Create question
        QuestionEntity question = new QuestionEntity();
        question.setTitle(TEST_QUESTION_TITLE);
        question.setQuestionText(TEST_QUESTION_TEXT);
        question.setAuthorId(createdAuthor.getId());
        
        // Execute create operation
        QuestionEntity createdQuestion = crudService.createQuestion(question);
        
        // Verify successful creation
        assertThat(createdQuestion)
            .as("Created question should not be null")
            .isNotNull();
            
        assertThat(createdQuestion.getId())
            .as("Created question should have generated ID")
            .isNotNull()
            .isPositive();
            
        assertThat(createdQuestion.getTitle())
            .as("Question title should match input")
            .isEqualTo(TEST_QUESTION_TITLE);
            
        assertThat(createdQuestion.getAuthorId())
            .as("Author ID should match")
            .isEqualTo(createdAuthor.getId());
            
        testConnection.commit();
        
        // Test retrieval
        Optional<QuestionEntity> retrievedQuestion = crudService.getQuestionById(createdQuestion.getId());
        
        assertThat(retrievedQuestion)
            .as("Question should be retrievable by ID")
            .isPresent();
            
        assertThat(retrievedQuestion.get().getQuestionText())
            .as("Question text should match")
            .isEqualTo(TEST_QUESTION_TEXT);
    }
    
    /**
     * Tests answer creation with question and author relationships.
     * 
     * <p>This test validates that answer entities can be successfully created
     * with proper foreign key relationships to both question and user entities.
     * The answer should be retrievable with all relationship data intact.</p>
     * 
     * @testCase Verify answer entity creation with relationships
     * @precondition Question and user entities exist for answer relationships
     * @postcondition Answer is created with proper foreign key relationships
     * @expectedResult AnswerEntity with valid relationships to question and author
     */
    @Test
    @DisplayName("Test Answer Creation with Relationships")
    void testAnswerCreationWithRelationships() throws SQLException {
        // Create author and question first
        UserEntity author = crudService.createUser(createTestUser());
        QuestionEntity question = createTestQuestion(author.getId());
        QuestionEntity createdQuestion = crudService.createQuestion(question);
        testConnection.commit();
        
        // Create answer
        AnswerEntity answer = new AnswerEntity();
        answer.setQuestionId(createdQuestion.getId());
        answer.setAnswerText(TEST_ANSWER_TEXT);
        answer.setAuthorId(author.getId());
        answer.setAccepted(false);
        
        // Execute create operation
        AnswerEntity createdAnswer = crudService.createAnswer(answer);
        
        // Verify successful creation
        assertThat(createdAnswer)
            .as("Created answer should not be null")
            .isNotNull();
            
        assertThat(createdAnswer.getId())
            .as("Created answer should have generated ID")
            .isNotNull()
            .isPositive();
            
        assertThat(createdAnswer.getQuestionId())
            .as("Question ID should match")
            .isEqualTo(createdQuestion.getId());
            
        assertThat(createdAnswer.getAuthorId())
            .as("Author ID should match")
            .isEqualTo(author.getId());
            
        assertThat(createdAnswer.getAnswerText())
            .as("Answer text should match input")
            .isEqualTo(TEST_ANSWER_TEXT);
            
        testConnection.commit();
    }
    
    /**
     * Tests cascade delete operations for related entities.
     * 
     * <p>This test validates that deleting a parent entity properly cascades
     * to delete related child entities while maintaining referential integrity.
     * Foreign key constraints should be properly enforced.</p>
     * 
     * @testCase Verify cascade delete operations maintain referential integrity
     * @precondition Related entities exist with foreign key relationships
     * @postcondition Parent deletion cascades to children appropriately
     * @expectedResult Related entities are deleted while maintaining data integrity
     */
    @Test
    @DisplayName("Test Cascade Delete Operations")
    void testCascadeDeleteOperations() throws SQLException {
        // Create test data with relationships
        UserEntity author = crudService.createUser(createTestUser());
        QuestionEntity question = createTestQuestion(author.getId());
        QuestionEntity createdQuestion = crudService.createQuestion(question);
        
        AnswerEntity answer = new AnswerEntity();
        answer.setQuestionId(createdQuestion.getId());
        answer.setAnswerText(TEST_ANSWER_TEXT);
        answer.setAuthorId(author.getId());
        
        AnswerEntity createdAnswer = crudService.createAnswer(answer);
        testConnection.commit();
        
        // Verify entities exist
        assertThat(crudService.getQuestionById(createdQuestion.getId())).isPresent();
        assertThat(crudService.getAnswerById(createdAnswer.getId())).isPresent();
        
        // Delete question (should cascade to answers)
        boolean deleteResult = crudService.deleteQuestion(createdQuestion.getId());
        
        assertThat(deleteResult)
            .as("Question deletion should succeed")
            .isTrue();
            
        testConnection.commit();
        
        // Verify cascade deletion
        assertThat(crudService.getQuestionById(createdQuestion.getId()))
            .as("Question should be deleted")
            .isEmpty();
            
        assertThat(crudService.getAnswerById(createdAnswer.getId()))
            .as("Answer should be cascade deleted")
            .isEmpty();
            
        // User should still exist (not cascade deleted)
        assertThat(crudService.getUserById(author.getId()))
            .as("Author should still exist")
            .isPresent();
    }
    
    /**
     * Tests transaction rollback on database errors.
     * 
     * <p>This test validates that database transactions are properly rolled back
     * when errors occur, maintaining ACID properties and data consistency.
     * Failed operations should not leave partial data in the database.</p>
     * 
     * @testCase Verify transaction rollback on database errors
     * @precondition Database supports transactions and error conditions can be simulated
     * @postcondition Failed operations are rolled back completely
     * @expectedResult No partial data remains after transaction failure
     */
    @Test
    @DisplayName("Test Transaction Rollback on Database Errors")
    void testTransactionRollbackOnErrors() throws SQLException {
        // Create valid user first
        UserEntity validUser = crudService.createUser(createTestUser());
        testConnection.commit();
        
        // Attempt to create user with duplicate username (should fail)
        UserEntity duplicateUser = new UserEntity();
        duplicateUser.setUsername(TEST_USERNAME); // Duplicate username
        duplicateUser.setEmail("different@example.com");
        duplicateUser.setPasswordHash("test_hash");
        duplicateUser.setRole("STUDENT");
        
        // This should fail due to unique constraint
        assertThatThrownBy(() -> {
            crudService.createUser(duplicateUser);
            testConnection.commit();
        })
        .as("Duplicate username should cause constraint violation")
        .isInstanceOf(SQLException.class);
        
        // Verify original user still exists and no partial data
        Optional<UserEntity> originalUser = crudService.getUserByUsername(TEST_USERNAME);
        assertThat(originalUser)
            .as("Original user should still exist after failed duplicate creation")
            .isPresent();
            
        assertThat(originalUser.get().getEmail())
            .as("Original user email should be unchanged")
            .isEqualTo(TEST_EMAIL);
    }
    
    /**
     * Tests bulk operations and batch processing performance.
     * 
     * <p>This test validates that the CRUD service can efficiently handle
     * bulk operations for creating multiple entities in batches. Performance
     * should be acceptable for typical batch processing scenarios.</p>
     * 
     * @param batchSize the number of entities to create in batch
     * 
     * @testCase Verify bulk operations performance and correctness
     * @precondition CRUD service supports batch operations
     * @postcondition Bulk operations complete within reasonable time
     * @expectedResult All entities in batch are created successfully
     */
    @ParameterizedTest(name = "Batch size: {0}")
    @ValueSource(ints = {10, 50, 100})
    @DisplayName("Test Bulk Operations Performance")
    void testBulkOperationsPerformance(int batchSize) throws SQLException {
        long startTime = System.currentTimeMillis();
        
        // Create batch of users
        for (int i = 0; i < batchSize; i++) {
            UserEntity user = new UserEntity();
            user.setUsername("batchuser" + i);
            user.setEmail("batch" + i + "@example.com");
            user.setPasswordHash("hash" + i);
            user.setRole("STUDENT");
            
            crudService.createUser(user);
        }
        
        testConnection.commit();
        long endTime = System.currentTimeMillis();
        
        long executionTime = endTime - startTime;
        
        // Verify performance (should complete within reasonable time)
        assertThat(executionTime)
            .as("Batch creation of %d users should complete within 5 seconds", batchSize)
            .isLessThan(5000);
            
        // Verify all users were created
        List<UserEntity> allUsers = crudService.getAllUsers();
        assertThat(allUsers)
            .as("All batch users should be created")
            .hasSizeGreaterThanOrEqualTo(batchSize);
    }
    
    /**
     * Helper method to create a test user entity.
     * 
     * @return configured UserEntity for testing
     */
    private UserEntity createTestUser() {
        UserEntity user = new UserEntity();
        user.setUsername(TEST_USERNAME);
        user.setEmail(TEST_EMAIL);
        user.setPasswordHash("test_hash");
        user.setRole("STUDENT");
        return user;
    }
    
    /**
     * Helper method to create a test question entity.
     * 
     * @param authorId the ID of the question author
     * @return configured QuestionEntity for testing
     */
    private QuestionEntity createTestQuestion(Integer authorId) {
        QuestionEntity question = new QuestionEntity();
        question.setTitle(TEST_QUESTION_TITLE);
        question.setQuestionText(TEST_QUESTION_TEXT);
        question.setAuthorId(authorId);
        return question;
    }
}