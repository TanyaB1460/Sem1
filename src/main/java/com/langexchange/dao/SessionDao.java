package com.langexchange.dao;

import com.langexchange.model.Session;
import com.langexchange.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionDao {

    public void createSession(Long user1Id, Long user2Id) throws SQLException {
        String sql = "INSERT INTO sessions (user1_id, user2_id, status) VALUES (?, ?, 'PENDING')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, user1Id);
            stmt.setLong(2, user2Id);
            stmt.executeUpdate();
        }
    }

    public List<Session> getUserSessions(Long userId) throws SQLException {
        String sql = "SELECT * FROM sessions WHERE user1_id = ? OR user2_id = ? ORDER BY created_at DESC";

        List<Session> sessions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sessions.add(mapResultSetToSession(rs));
            }
        }
        return sessions;
    }

    public void updateSessionStatus(Long sessionId, String status) throws SQLException {
        String sql = "UPDATE sessions SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setLong(2, sessionId);
            stmt.executeUpdate();
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

    public boolean hasPendingSession(Long user1Id, Long user2Id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM sessions WHERE user1_id = ? AND user2_id = ? AND status = 'PENDING'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, user1Id);
            stmt.setLong(2, user2Id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    private Session mapResultSetToSession(ResultSet rs) throws SQLException {
        Session session = new Session();
        session.setId(rs.getLong("id"));
        session.setUser1Id(rs.getLong("user1_id"));
        session.setUser2Id(rs.getLong("user2_id"));
        session.setStatus(rs.getString("status"));
        session.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return session;
    }
}