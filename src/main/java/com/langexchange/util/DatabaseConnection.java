package com.langexchange.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // ЗАМЕНИ НА СВОИ ДАННЫЕ!
    private static final String URL = "jdbc:postgresql://localhost:5432/language_exchange";
    private static final String USER = "postgres";     // твой пользователь PostgreSQL
    private static final String PASSWORD = "2685"; // твой пароль PostgreSQL

    static {
        try {
            // Регистрируем драйвер PostgreSQL
            Class.forName("org.postgresql.Driver");
            System.out.println("✅ PostgreSQL Driver registered successfully");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("❌ PostgreSQL Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Тестовое соединение
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("✅ PostgreSQL connection successful!");
        } catch (SQLException e) {
            System.err.println("❌ PostgreSQL connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Временный main для тестирования
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        testConnection();
    }
}