package com.langexchange.dao;

import com.langexchange.model.Interest;
import com.langexchange.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterestDao {

    // 🔸 Основной метод получения всех интересов
    public List<Interest> findAll() throws SQLException {
        String sql = "SELECT * FROM interests ORDER BY name";

        List<Interest> interests = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Interest interest = mapResultSetToInterest(rs);
                interests.add(interest);
            }
        }
        return interests;
    }

    public Interest findById(Long id) throws SQLException {
        String sql = "SELECT * FROM interests WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToInterest(rs);
            }
            return null;
        }
    }

    // 🔸 Поиск интересов по имени
    public List<Interest> findByName(String name) throws SQLException {
        String sql = "SELECT * FROM interests WHERE name LIKE ? ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            List<Interest> interests = new ArrayList<>();
            while (rs.next()) {
                interests.add(mapResultSetToInterest(rs));
            }
            return interests;
        }
    }

    // 🔸 Поиск интересов по категории
    public List<Interest> findByCategory(String category) throws SQLException {
        String sql = "SELECT * FROM interests WHERE category = ? ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();

            List<Interest> interests = new ArrayList<>();
            while (rs.next()) {
                interests.add(mapResultSetToInterest(rs));
            }
            return interests;
        }
    }

    // 🔸 Получение всех категорий
    public List<String> findAllCategories() throws SQLException {
        String sql = "SELECT DISTINCT category FROM interests WHERE category IS NOT NULL ORDER BY category";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<String> categories = new ArrayList<>();
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
            return categories;
        }
    }

    // 🔸 Создание нового интереса
    public Long save(Interest interest) throws SQLException {
        String sql = "INSERT INTO interests (name, category) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, interest.getName());
            stmt.setString(2, interest.getCategory());
            stmt.executeUpdate();

            // Получаем сгенерированный ID
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getLong(1);
            }
            throw new SQLException("Failed to get generated interest ID");
        }
    }

    // 🔸 Вспомогательный метод для маппинга ResultSet в Interest
    private Interest mapResultSetToInterest(ResultSet rs) throws SQLException {
        Interest interest = new Interest();
        interest.setId(rs.getLong("id"));
        interest.setName(rs.getString("name"));

        // Обрабатываем возможный null в category
        String category = rs.getString("category");
        if (category != null) {
            interest.setCategory(category);
        }

        return interest;
    }

    // 🔸 Метод для инициализации базовых интересов (для тестирования)
    public void initializeDefaultInterests() throws SQLException {
        List<Interest> defaultInterests = List.of(
                createInterest("Фильмы", "Развлечения"),
                createInterest("Музыка", "Развлечения"),
                createInterest("Книги", "Образование"),
                createInterest("Путешествия", "Хобби"),
                createInterest("Спорт", "Здоровье"),
                createInterest("Кулинария", "Хобби"),
                createInterest("Технологии", "Наука"),
                createInterest("Искусство", "Культура"),
                createInterest("Наука", "Образование"),
                createInterest("История", "Образование"),
                createInterest("Программирование", "Технологии"),
                createInterest("Фотография", "Хобби"),
                createInterest("Игры", "Развлечения"),
                createInterest("Мода", "Стиль"),
                createInterest("Бизнес", "Карьера")
        );

        // Проверяем, есть ли уже интересы в базе
        if (findAll().isEmpty()) {
            for (Interest interest : defaultInterests) {
                save(interest);
            }
        }
    }

    private Interest createInterest(String name, String category) {
        Interest interest = new Interest();
        interest.setName(name);
        interest.setCategory(category);
        return interest;
    }
}