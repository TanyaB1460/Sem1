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
            // Создаем сессию
            Session session = new Session();
            session.setUserId(user.getId());
            session.setToken(UUID.randomUUID().toString());
            session.setExpiresAt(LocalDateTime.now().plusHours(24));
            sessionDao.save(session);

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

        // Хешируем пароль перед сохранением
        user.setPasswordHash(PasswordHasher.hashPassword(user.getPasswordHash()));

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
        if (session != null && session.getExpiresAt().isAfter(LocalDateTime.now())) {
            return userDao.findById(session.getUserId());
        }
        return null;
    }
}