package com.langexchange.service;

import com.langexchange.dao.UserDao;
import com.langexchange.dao.SessionDao;
import com.langexchange.model.User;
import com.langexchange.model.Session;
import com.langexchange.util.PasswordHasher;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AuthService {
    private final UserDao userDao;
    private final SessionDao sessionDao;

    public AuthService(UserDao userDao, SessionDao sessionDao) {
        this.userDao = userDao;
        this.sessionDao = sessionDao;
    }

    public User authenticate(String username, String password) throws SQLException {
        User user = userDao.findByUsername(username);
        if (user != null && PasswordHasher.checkPassword(password, user.getPasswordHash())) {
            // Создаем сессию аутентификации
            Session authSession = new Session();
            authSession.setUserId(user.getId());
            authSession.setToken(UUID.randomUUID().toString());
            authSession.setExpiresAt(LocalDateTime.now().plusHours(24));
            authSession.setSessionType("auth");
            sessionDao.saveAuthSession(authSession);

            return user;
        }
        return null;
    }

    public void registerUser(User user, List<Long> interestIds) throws SQLException {
        // Проверяем, не существует ли пользователь
        if (userDao.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
        if (userDao.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        // Хешируем пароль
        String plainPassword = user.getPasswordHash();
        user.setPasswordHash(PasswordHasher.hashPassword(plainPassword));

        // Сохраняем пользователя
        Long userId = userDao.save(user);
        user.setId(userId);

        // Добавляем интересы
        if (interestIds != null) {
            for (Long interestId : interestIds) {
                userDao.addUserInterest(userId, interestId);
            }
        }
    }

    public void logout(String token) throws SQLException {
        sessionDao.deleteByToken(token);
    }

    public User validateSession(String token) throws SQLException {
        Session session = sessionDao.findByToken(token);
        if (session != null &&
                "auth".equals(session.getSessionType()) &&
                session.getExpiresAt() != null &&
                session.getExpiresAt().isAfter(LocalDateTime.now())) {
            return userDao.findById(session.getUserId());
        }
        return null;
    }

    public void logoutUser(Long userId) throws SQLException {
        List<Session> userSessions = sessionDao.findByUserIdAndType(userId, "auth");
        for (Session session : userSessions) {
            sessionDao.deleteByToken(session.getToken());
        }
    }

    public boolean isTokenValid(String token) throws SQLException {
        Session session = sessionDao.findByToken(token);
        return session != null &&
                "auth".equals(session.getSessionType()) &&
                session.getExpiresAt() != null &&
                session.getExpiresAt().isAfter(LocalDateTime.now());
    }

    public void extendSession(String token, int hours) throws SQLException {
        Session session = sessionDao.findByToken(token);
        if (session != null && "auth".equals(session.getSessionType())) {
            session.setExpiresAt(LocalDateTime.now().plusHours(hours));
            // Нужно добавить метод updateAuthSession в SessionDao
        }
    }

    // Валидация данных
    public boolean isValidUsername(String username) {
        return username != null && username.length() >= 3 && username.matches("^[a-zA-Z0-9_]+$");
    }

    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}