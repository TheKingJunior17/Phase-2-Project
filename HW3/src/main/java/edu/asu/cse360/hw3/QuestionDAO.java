package edu.asu.cse360.hw3;

import java.sql.*;
import java.util.*;

/**
 * Data Access Object for Question entities.
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 */
public class QuestionDAO {
    private final Connection connection;
    
    public QuestionDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Creates the questions table if it doesn't exist.
     */
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS questions (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "title VARCHAR(255) NOT NULL, " +
                    "description TEXT NOT NULL, " +
                    "author_id VARCHAR(255) NOT NULL, " +
                    "author_username VARCHAR(255) NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }
    
    /**
     * Saves a question to the database.
     */
    public Question save(Question question) throws SQLException {
        if (question.getId() == null) {
            return insert(question);
        } else {
            return update(question);
        }
    }
    
    private Question insert(Question question) throws SQLException {
        String sql = "INSERT INTO questions (title, description, author_id, author_username, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, question.getTitle());
            stmt.setString(2, question.getDescription());
            stmt.setString(3, question.getAuthorId());
            stmt.setString(4, question.getAuthorUsername());
            stmt.setTimestamp(5, Timestamp.valueOf(question.getCreatedAt()));
            stmt.setTimestamp(6, Timestamp.valueOf(question.getUpdatedAt()));
            
            stmt.executeUpdate();
            
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    question.setId(keys.getLong(1));
                }
            }
        }
        return question;
    }
    
    private Question update(Question question) throws SQLException {
        String sql = "UPDATE questions SET title = ?, description = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, question.getTitle());
            stmt.setString(2, question.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.now()));
            stmt.setLong(4, question.getId());
            
            stmt.executeUpdate();
            question.setUpdatedAt(java.time.LocalDateTime.now());
        }
        return question;
    }
    
    /**
     * Finds a question by ID.
     */
    public Optional<Question> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM questions WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToQuestion(rs));
                }
            }
        }
        return Optional.empty();
    }
    
    /**
     * Finds all questions.
     */
    public List<Question> findAll() throws SQLException {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM questions ORDER BY created_at DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                questions.add(mapResultSetToQuestion(rs));
            }
        }
        return questions;
    }
    
    /**
     * Deletes a question by ID.
     */
    public boolean deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM questions WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    private Question mapResultSetToQuestion(ResultSet rs) throws SQLException {
        Question question = new Question();
        question.setId(rs.getLong("id"));
        question.setTitle(rs.getString("title"));
        question.setDescription(rs.getString("description"));
        question.setAuthorId(rs.getString("author_id"));
        question.setAuthorUsername(rs.getString("author_username"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            question.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            question.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return question;
    }
}