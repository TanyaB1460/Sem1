package com.langexchange.service;

import com.langexchange.dao.UserDao;
import com.langexchange.dao.InterestDao;
import com.langexchange.model.User;
import com.langexchange.model.Interest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private UserDao userDao;
    private InterestDao interestDao;

    public UserService(UserDao userDao, InterestDao interestDao) {
        this.userDao = userDao;
        this.interestDao = interestDao;
    }

    public List<User> findCompatiblePartners(Long currentUserId) throws SQLException {
        User currentUser = userDao.findById(currentUserId);
        if (currentUser == null) {
            return List.of();
        }

        // Ищем пользователей, которые изучают наш родной язык и говорят на языке, который мы изучаем
        List<User> potentialPartners = userDao.findByLanguages(
                currentUser.getNativeLanguage(),  // они изучают наш родной язык
                currentUser.getLearningLanguage() // они говорят на языке, который мы изучаем
        );

        // Убираем текущего пользователя из результатов
        return potentialPartners.stream()
                .filter(partner -> !partner.getId().equals(currentUserId))
                .collect(Collectors.toList());
    }

    // 🔸 Получение всех интересов
    public List<Interest> getAllInterests() throws SQLException {
        return interestDao.findAll();
    }

    // 🔸 Поиск пользователей по критериям (базовая версия)
    public List<User> findUsersByCriteria(String nativeLanguage, String learningLanguage, List<Long> interestIds)
            throws SQLException {
        return userDao.findByCriteria(nativeLanguage, learningLanguage, interestIds);
    }

    // 🔸 Поиск пользователей по критериям с исключением текущего пользователя
    public List<User> findUsersByCriteria(String nativeLanguage, String learningLanguage,
                                          List<Long> interestIds, Long excludeUserId) throws SQLException {
        return userDao.findByCriteria(nativeLanguage, learningLanguage, interestIds, excludeUserId);
    }

    // 🔸 Получение интересов пользователя
    public List<Interest> getUserInterests(Long userId) throws SQLException {
        return userDao.findUserInterests(userId);
    }

    // 🔸 Получение всех пользователей кроме текущего (для тестирования)
    public List<User> getAllUsersExceptCurrent(Long currentUserId) throws SQLException {
        // В реальном приложении здесь был бы SQL запрос
        // Для теста используем поиск по пустым критериям с исключением текущего пользователя
        return userDao.findByCriteria(null, null, null, currentUserId);
    }

    // 🔸 Дополнительные методы для работы с пользователями

    public User getUserById(Long userId) throws SQLException {
        return userDao.findById(userId);
    }

    public User getUserByUsername(String username) throws SQLException {
        return userDao.findByUsername(username);
    }

    public User getUserByEmail(String email) throws SQLException {
        return userDao.findByEmail(email);
    }

    // 🔸 Регистрация нового пользователя с интересами
    public Long registerUser(User user, List<Long> interestIds) throws SQLException {
        // Сохраняем пользователя
        Long userId = userDao.save(user);

        // Добавляем интересы
        if (interestIds != null) {
            for (Long interestId : interestIds) {
                userDao.addUserInterest(userId, interestId);
            }
        }

        return userId;
    }

    // 🔸 Проверка существования пользователя
    public boolean isUsernameExists(String username) throws SQLException {
        return userDao.findByUsername(username) != null;
    }

    public boolean isEmailExists(String email) throws SQLException {
        return userDao.findByEmail(email) != null;
    }
}