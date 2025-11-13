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

    public List<Interest> getAllInterests() throws SQLException {
        return interestDao.findAll();
    }

    public List<User> getAllUsersExceptCurrent(Long currentUserId) throws SQLException {
        // Простой метод для тестирования - возвращает всех пользователей кроме текущего
        List<User> allUsers = new ArrayList<>();

        // В реальном приложении здесь был бы SQL запрос
        // Для теста просто возвращаем пустой список
        return allUsers;
    }
}