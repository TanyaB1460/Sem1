package com.langexchange.service;

import com.langexchange.dao.SessionDao;
import com.langexchange.dao.UserDao;
import com.langexchange.model.Session;

import java.sql.SQLException;
import java.util.List;

public class SessionService {
    private SessionDao sessionDao;
    private UserDao userDao;

    public SessionService(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    public SessionService(SessionDao sessionDao, UserDao userDao) {
        this.sessionDao = sessionDao;
        this.userDao = userDao;
    }

    // 🔸 Старые методы (для обратной совместимости)
    public void createSession(Long user1Id, Long user2Id) throws SQLException {
        if (user1Id.equals(user2Id)) {
            throw new IllegalArgumentException("Cannot create session with yourself");
        }

        // Создаем новую сессию в новой структуре
        Session session = new Session();
        session.setUserId(user1Id);
        session.setPartnerId(user2Id);
        session.setTitle("Language Practice Session");
        session.setDescription("Language exchange session between users");
        session.setLanguage("English"); // По умолчанию
        session.setStatus("planned");

        sessionDao.save(session);
    }

    public void acceptSession(Long sessionId, Long userId) throws SQLException {
        Session session = sessionDao.findById(sessionId);

        if (session == null) {
            throw new IllegalArgumentException("Session not found");
        }

        if (!session.getPartnerId().equals(userId)) {
            throw new SecurityException("Only the invited user can accept session");
        }

        if (!"planned".equals(session.getStatus())) {
            throw new IllegalArgumentException("Session is not in planned status");
        }

        session.setStatus("active");
        sessionDao.update(session);
    }

    // 🔸 Новые методы для CRUD операций

    public List<Session> getUserSessions(Long userId) throws SQLException {
        return sessionDao.findByUserId(userId);
    }

    public Session getSessionById(Long sessionId) throws SQLException {
        return sessionDao.findById(sessionId);
    }

    public void createSession(Session session) throws SQLException {
        // Валидация
        if (session.getUserId() == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        if (session.getUserId().equals(session.getPartnerId())) {
            throw new IllegalArgumentException("Cannot create session with yourself");
        }

        sessionDao.save(session);
    }

    public void updateSession(Session session) throws SQLException {
        if (session.getId() == null) {
            throw new IllegalArgumentException("Session ID is required for update");
        }

        sessionDao.update(session);
    }

    public void deleteSession(Long sessionId) throws SQLException {
        sessionDao.delete(sessionId);
    }

    public List<Session> getUpcomingSessions(Long userId) throws SQLException {
        return sessionDao.findUpcomingByUserId(userId);
    }

    // 🔸 Дополнительные методы управления сессиями

    public void completeSession(Long sessionId, Long userId) throws SQLException {
        Session session = sessionDao.findById(sessionId);

        if (session == null) {
            throw new IllegalArgumentException("Session not found");
        }

        if (!session.getUserId().equals(userId)) {
            throw new SecurityException("Only session owner can complete session");
        }

        session.setStatus("completed");
        sessionDao.update(session);
    }

    public void cancelSession(Long sessionId, Long userId) throws SQLException {
        Session session = sessionDao.findById(sessionId);

        if (session == null) {
            throw new IllegalArgumentException("Session not found");
        }

        // Проверяем, что пользователь является владельцем или партнером
        if (!session.getUserId().equals(userId) &&
                !(session.getPartnerId() != null && session.getPartnerId().equals(userId))) {
            throw new SecurityException("Only session participants can cancel session");
        }

        session.setStatus("cancelled");
        sessionDao.update(session);
    }

    // 🔸 Методы для проверки доступов

    public boolean canUserAccessSession(Long sessionId, Long userId) throws SQLException {
        Session session = sessionDao.findById(sessionId);
        if (session == null) return false;

        return session.getUserId().equals(userId) ||
                (session.getPartnerId() != null && session.getPartnerId().equals(userId));
    }

    public boolean isSessionOwner(Long sessionId, Long userId) throws SQLException {
        Session session = sessionDao.findById(sessionId);
        return session != null && session.getUserId().equals(userId);
    }
}