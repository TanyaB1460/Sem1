package com.langexchange.dao;

import com.langexchange.model.User;
import com.langexchange.model.Interest;
import com.langexchange.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
        }
    }

    public User findById(Long id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
        }
    }

    public void save(User user) throws SQLException {
        String sql = "INSERT INTO users (username, email, password_hash, native_language, learning_language, level) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());
            stmt.setString(4, user.getNativeLanguage());
            stmt.setString(5, user.getLearningLanguage());
            stmt.setString(6, user.getLevel());

            stmt.executeUpdate();

            // Получаем сгенерированный ID (для PostgreSQL)
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                user.setId(keys.getLong(1));
            }
        }
    }

    // M2M методы для работы с интересами
    public void addInterestToUser(Long userId, Long interestId) throws SQLException {
        String sql = "INSERT INTO user_interests (user_id, interest_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, interestId);
            stmt.executeUpdate();
        }
    }

    public List<Interest> getUserInterests(Long userId) throws SQLException {
        String sql = "SELECT i.* FROM interests i " +
                "JOIN user_interests ui ON i.id = ui.interest_id " +
                "WHERE ui.user_id = ?";

        List<Interest> interests = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Interest interest = new Interest();
                interest.setId(rs.getLong("id"));
                interest.setName(rs.getString("name"));
                interest.setCategory(rs.getString("category"));
                interests.add(interest);
            }
        }
        return interests;
    }

    public List<User> findByLanguages(String learningLanguage, String nativeLanguage) throws SQLException {
        String sql = "SELECT * FROM users WHERE native_language = ? AND learning_language = ?";

        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nativeLanguage);
            stmt.setString(2, learningLanguage);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }
        return users;
    }

    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setNativeLanguage(rs.getString("native_language"));
        user.setLearningLanguage(rs.getString("learning_language"));
        user.setLevel(rs.getString("level"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return user;
    }
}