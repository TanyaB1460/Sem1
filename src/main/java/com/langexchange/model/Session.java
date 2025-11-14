package com.langexchange.model;

import java.time.LocalDateTime;

public class Session {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String language;
    private Long partnerId;
    private LocalDateTime scheduledTime;
    private Integer durationMinutes;
    private String status; // planned, in_progress, completed, cancelled
    private LocalDateTime createdAt;
    private String sessionType;

    // Конструкторы
    public Session() {}

    public Session(Long id, Long userId, String title, String description, String language,
                   Long partnerId, LocalDateTime scheduledTime, Integer durationMinutes,
                   String status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.language = language;
        this.partnerId = partnerId;
        this.scheduledTime = scheduledTime;
        this.durationMinutes = durationMinutes;
        this.status = status;
        this.createdAt = createdAt;
    }
    public String getSessionType() { return sessionType; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public Long getPartnerId() { return partnerId; }
    public void setPartnerId(Long partnerId) { this.partnerId = partnerId; }

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
        switch (status) {
            case "planned": return "Запланирована";
            case "in_progress": return "В процессе";
            case "completed": return "Завершена";
            case "cancelled": return "Отменена";
            default: return status;
        }
    }

    public String getStatusColor() {
        switch (status) {
            case "planned": return "#3498db";
            case "in_progress": return "#f39c12";
            case "completed": return "#27ae60";
            case "cancelled": return "#e74c3c";
            default: return "#95a5a6";
        }
    }
}