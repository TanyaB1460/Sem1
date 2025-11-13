package com.langexchange.service;

import com.langexchange.dao.SessionDao;
import com.langexchange.dao.UserDao;
import com.langexchange.model.Session;

import java.sql.SQLException;
import java.util.List;

public class SessionService {
    private SessionDao sessionDao;
    private UserDao userDao;

    public SessionService(SessionDao sessionDao, UserDao userDao) {
        this.sessionDao = sessionDao;
        this.userDao = userDao;
    }

    public void createSession(Long user1Id, Long user2Id) throws SQLException {
        if (user1Id.equals(user2Id)) {
            throw new IllegalArgumentException("Cannot create session with yourself");
        }

        if (sessionDao.hasPendingSession(user1Id, user2Id)) {
            throw new IllegalArgumentException("Session request already exists");
        }

        sessionDao.createSession(user1Id, user2Id);
    }

    public List<Session> getUserSessions(Long userId) throws SQLException {
        return sessionDao.getUserSessions(userId);
    }

    public void acceptSession(Long sessionId, Long userId) throws SQLException {
        Session session = sessionDao.findById(sessionId);

        if (!session.getUser2Id().equals(userId)) {
            throw new SecurityException("Only the invited user can accept session");
        }

        if (!"PENDING".equals(session.getStatus())) {
            throw new IllegalArgumentException("Session is not in PENDING status");
        }

        sessionDao.updateSessionStatus(sessionId, "ACTIVE");
    }
}