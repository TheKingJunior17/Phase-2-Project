package edu.asu.cse360.hw3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Service for performing CRUD operations on the database.
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public class DatabaseCRUDService {
    
    private final DataSource dataSource;
    private final Connection connection;
    
    /**
     * Default constructor for TP3 demonstration (mock mode).
     */
    public DatabaseCRUDService() {
        this.dataSource = null;
        this.connection = null;
    }
    
    public DatabaseCRUDService(DataSource dataSource) {
        this.dataSource = dataSource;
        this.connection = null;
    }
    
    public DatabaseCRUDService(Connection connection) {
        this.dataSource = null;
        this.connection = connection;
    }
    
    private Connection getConnection() throws SQLException {
        return connection != null ? connection : dataSource.getConnection();
    }
    
    // User CRUD operations
    public UserEntity createUser(UserEntity user) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO users (username, email, password_hash, role, active) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getEmail());
                stmt.setString(3, user.getPasswordHash());
                stmt.setString(4, user.getRole().name());
                stmt.setBoolean(5, user.isActive());
                
                stmt.executeUpdate();
                
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        user.setId(keys.getInt(1));
                    }
                }
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create user", e);
        }
    }
    
    public Optional<UserEntity> getUserById(Integer id) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM users WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapUserFromResultSet(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get user by ID", e);
        }
        return Optional.empty();
    }
    
    public Optional<UserEntity> getUserByUsername(String username) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapUserFromResultSet(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get user by username", e);
        }
        return Optional.empty();
    }
    
    public UserEntity updateUser(UserEntity user) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE users SET username = ?, email = ?, password_hash = ?, role = ?, active = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getEmail());
                stmt.setString(3, user.getPasswordHash());
                stmt.setString(4, user.getRole().name());
                stmt.setBoolean(5, user.isActive());
                stmt.setInt(6, user.getId());
                
                stmt.executeUpdate();
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }
    
    public List<UserEntity> getAllUsers() {
        List<UserEntity> users = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM users";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        users.add(mapUserFromResultSet(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all users", e);
        }
        return users;
    }
    
    // Question CRUD operations
    public QuestionEntity createQuestion(QuestionEntity question) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO questions (title, content, type, difficulty, author_id) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, question.getTitle());
                stmt.setString(2, question.getContent());
                stmt.setString(3, question.getType());
                stmt.setString(4, question.getDifficulty());
                stmt.setInt(5, question.getAuthorId());
                
                stmt.executeUpdate();
                
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        question.setId(keys.getInt(1));
                    }
                }
            }
            return question;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create question", e);
        }
    }
    
    public Optional<QuestionEntity> getQuestionById(Integer id) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM questions WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapQuestionFromResultSet(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get question by ID", e);
        }
        return Optional.empty();
    }
    
    // Answer CRUD operations
    public AnswerEntity createAnswer(AnswerEntity answer) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO answers (content, question_id, author_id, is_correct) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, answer.getContent());
                stmt.setInt(2, answer.getQuestionId());
                stmt.setInt(3, answer.getAuthorId());
                stmt.setBoolean(4, answer.isCorrect());
                
                stmt.executeUpdate();
                
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        answer.setId(keys.getInt(1));
                    }
                }
            }
            return answer;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create answer", e);
        }
    }
    
    public Optional<AnswerEntity> getAnswerById(Integer id) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM answers WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapAnswerFromResultSet(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get answer by ID", e);
        }
        return Optional.empty();
    }
    
    public boolean deleteQuestion(Integer id) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM questions WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete question", e);
        }
    }
    
    private UserEntity mapUserFromResultSet(ResultSet rs) throws SQLException {
        UserEntity user = new UserEntity();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setRole(UserRole.valueOf(rs.getString("role")));
        user.setActive(rs.getBoolean("active"));
        return user;
    }
    
    private QuestionEntity mapQuestionFromResultSet(ResultSet rs) throws SQLException {
        QuestionEntity question = new QuestionEntity();
        question.setId(rs.getInt("id"));
        question.setTitle(rs.getString("title"));
        question.setContent(rs.getString("content"));
        question.setType(rs.getString("type"));
        question.setDifficulty(rs.getString("difficulty"));
        question.setAuthorId(rs.getInt("author_id"));
        return question;
    }
    
    private AnswerEntity mapAnswerFromResultSet(ResultSet rs) throws SQLException {
        AnswerEntity answer = new AnswerEntity();
        answer.setId(rs.getInt("id"));
        answer.setContent(rs.getString("content"));
        answer.setQuestionId(rs.getInt("question_id"));
        answer.setAuthorId(rs.getInt("author_id"));
        answer.setCorrect(rs.getBoolean("is_correct"));
        return answer;
    }
}