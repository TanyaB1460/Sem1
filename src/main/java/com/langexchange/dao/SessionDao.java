package com.langexchange.dao;

import com.langexchange.model.Session;
import com.langexchange.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionDao {
    public List<Session> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT * FROM sessions WHERE user_id = ? AND session_type = 'language' ORDER BY scheduled_time DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            return mapResultSetToSessions(rs);
        }
    }

    public Session findById(Long sessionId) throws SQLException {
        String sql = "SELECT * FROM sessions WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, sessionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToSession(rs);
            }
            return null;
        }
    }

    public void save(Session session) throws SQLException {
        String sql = "INSERT INTO sessions (user_id, title, description, language, partner_id, " +
                "scheduled_time, duration_minutes, status, session_type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, session.getUserId());
            stmt.setString(2, session.getTitle());
            stmt.setString(3, session.getDescription());
            stmt.setString(4, session.getLanguage());
            setNullableLong(stmt, 5, session.getPartnerId());
            stmt.setObject(6, session.getScheduledTime());
            stmt.setInt(7, session.getDurationMinutes());
            stmt.setString(8, session.getStatus());
            stmt.setString(9, session.getSessionType() != null ? session.getSessionType() : "language");

            stmt.executeUpdate();

            // Получаем сгенерированный ID
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                session.setId(keys.getLong(1));
            }
        }
    }

    public void update(Session session) throws SQLException {
        String sql = "UPDATE sessions SET title = ?, description = ?, language = ?, " +
                "partner_id = ?, scheduled_time = ?, duration_minutes = ?, status = ? " +
                "WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, session.getTitle());
            stmt.setString(2, session.getDescription());
            stmt.setString(3, session.getLanguage());
            setNullableLong(stmt, 4, session.getPartnerId());
            stmt.setObject(5, session.getScheduledTime());
            stmt.setInt(6, session.getDurationMinutes());
            stmt.setString(7, session.getStatus());
            stmt.setLong(8, session.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(Long sessionId) throws SQLException {
        String sql = "DELETE FROM sessions WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, sessionId);
            stmt.executeUpdate();
        }
    }

    public List<Session> findUpcomingByUserId(Long userId) throws SQLException {
        String sql = "SELECT * FROM sessions WHERE user_id = ? AND scheduled_time > CURRENT_TIMESTAMP " +
                "AND status = 'planned' AND session_type = 'language' ORDER BY scheduled_time ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            return mapResultSetToSessions(rs);
        }
    }

    // 🔸 МЕТОДЫ ДЛЯ АУТЕНТИФИКАЦИОННЫХ СЕССИЙ

    public void saveAuthSession(Session session) throws SQLException {
        String sql = "INSERT INTO sessions (user_id, token, expires_at, session_type) VALUES (?, ?, ?, 'auth')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, session.getUserId());
            stmt.setString(2, session.getToken());
            stmt.setObject(3, session.getExpiresAt());
            stmt.executeUpdate();
        }
    }

    public Session findByToken(String token) throws SQLException {
        String sql = "SELECT * FROM sessions WHERE token = ? AND session_type = 'auth'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToAuthSession(rs);
            }
            return null;
        }
    }

    public void deleteByToken(String token) throws SQLException {
        String sql = "DELETE FROM sessions WHERE token = ? AND session_type = 'auth'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            stmt.executeUpdate();
        }
    }

    public List<Session> findByUserIdAndType(Long userId, String sessionType) throws SQLException {
        String sql = "SELECT * FROM sessions WHERE user_id = ? AND session_type = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setString(2, sessionType);
            ResultSet rs = stmt.executeQuery();
            return sessionType.equals("auth") ? mapResultSetToAuthSessions(rs) : mapResultSetToSessions(rs);
        }
    }

    // 🔸 ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ МАППИНГА

    private List<Session> mapResultSetToSessions(ResultSet rs) throws SQLException {
        List<Session> sessions = new ArrayList<>();
        while (rs.next()) {
            sessions.add(mapResultSetToSession(rs));
        }
        return sessions;
    }

    private List<Session> mapResultSetToAuthSessions(ResultSet rs) throws SQLException {
        List<Session> sessions = new ArrayList<>();
        while (rs.next()) {
            sessions.add(mapResultSetToAuthSession(rs));
        }
        return sessions;
    }

    private Session mapResultSetToSession(ResultSet rs) throws SQLException {
        Session session = new Session();
        session.setId(rs.getLong("id"));
        session.setUserId(rs.getLong("user_id"));
        session.setTitle(rs.getString("title"));
        session.setDescription(rs.getString("description"));
        session.setLanguage(rs.getString("language"));
        session.setSessionType(rs.getString("session_type"));

        Long partnerId = rs.getLong("partner_id");
        if (!rs.wasNull()) {
            session.setPartnerId(partnerId);
        }

        Timestamp scheduledTime = rs.getTimestamp("scheduled_time");
        if (scheduledTime != null) {
            session.setScheduledTime(scheduledTime.toLocalDateTime());
        }

        session.setDurationMinutes(rs.getInt("duration_minutes"));
        session.setStatus(rs.getString("status"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            session.setCreatedAt(createdAt.toLocalDateTime());
        }

        return session;
    }

    private Session mapResultSetToAuthSession(ResultSet rs) throws SQLException {
        Session session = new Session();
        session.setId(rs.getLong("id"));
        session.setUserId(rs.getLong("user_id"));
        session.setToken(rs.getString("token"));

        Timestamp expiresAt = rs.getTimestamp("expires_at");
        if (expiresAt != null) {
            session.setExpiresAt(expiresAt.toLocalDateTime());
        }

        session.setSessionType(rs.getString("session_type"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            session.setCreatedAt(createdAt.toLocalDateTime());
        }

        return session;
    }

    private void setNullableLong(PreparedStatement stmt, int parameterIndex, Long value) throws SQLException {
        if (value != null) {
            stmt.setLong(parameterIndex, value);
        } else {
            stmt.setNull(parameterIndex, Types.BIGINT);
        }
    }
}