package com.langexchange.service;

import com.langexchange.dao.UserDao;
import com.langexchange.model.User;

import java.sql.SQLException;
import java.util.List;

public class AuthService {
    private UserDao userDao;

    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User authenticate(String username, String password) throws SQLException {
        User user = userDao.findByUsername(username);
        // Простая проверка пароля (в реальном приложении используй BCrypt)
        if (user != null && user.getPasswordHash().equals(password)) {
            return user;
        }
        return null;
    }

    public void registerUser(User user, List<Long> interestIds) throws SQLException {
        // Проверяем, не существует ли уже пользователь с таким username
        if (userDao.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }

        // Проверяем, не существует ли уже пользователь с таким email
        if (userDao.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        // Сохраняем пользователя
        userDao.save(user);

        // Добавляем интересы (M2M связь)
        for (Long interestId : interestIds) {
            userDao.addInterestToUser(user.getId(), interestId);
        }
    }
}