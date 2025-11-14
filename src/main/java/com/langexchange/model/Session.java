package com.langexchange.model;

import java.time.LocalDateTime;

public class Session {
    private Long id;
    private Long userId;           // Владелец сессии
    private Long partnerId;        // Партнер (опционально)
    private String token;          // Только для auth сессий
    private LocalDateTime expiresAt; // Только для auth сессий
    private String sessionType;    // "auth" или "language"

    // Поля для языковых сессий
    private String title;
    private String description;
    private String language;
    private LocalDateTime scheduledTime;
    private Integer durationMinutes;
    private String status; // planned, active, completed, cancelled
    private LocalDateTime createdAt;

    // Конструкторы
    public Session() {}

    public Session(Long id, Long userId, String token, LocalDateTime expiresAt, String sessionType) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.sessionType = sessionType;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getPartnerId() { return partnerId; }
    public void setPartnerId(Long partnerId) { this.partnerId = partnerId; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public String getSessionType() { return sessionType; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Вспомогательные методы
    public String getStatusDisplay() {
        switch (status != null ? status : "") {
            case "planned": return "Запланирована";
            case "active": return "В процессе";
            case "completed": return "Завершена";
            case "cancelled": return "Отменена";
            default: return status != null ? status : "Неизвестно";
        }
    }

    public String getStatusColor() {
        switch (status != null ? status : "") {
            case "planned": return "#3498db";
            case "active": return "#f39c12";
            case "completed": return "#27ae60";
            case "cancelled": return "#e74c3c";
            default: return "#95a5a6";
        }
    }

    public boolean isAuthSession() {
        return "auth".equals(sessionType);
    }

    public boolean isLanguageSession() {
        return "language".equals(sessionType);
    }
}