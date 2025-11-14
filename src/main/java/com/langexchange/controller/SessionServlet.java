package com.langexchange.controller;

import com.langexchange.model.Session;
import com.langexchange.model.User;
import com.langexchange.service.SessionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/sessions")
public class SessionServlet extends BaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        SessionService sessionService = (SessionService) request.getServletContext().getAttribute("sessionService");

        try {
            String action = request.getParameter("action");
            String sessionIdParam = request.getParameter("id");

            Map<String, Object> data = new HashMap<>();
            data.put("title", "Мои языковые сессии");

            if ("create".equals(action)) {
                // Показываем форму создания сессии
                data.put("formAction", "create");
                renderTemplate(response, "session-form.ftlh", data);
                return;
            } else if ("edit".equals(action) && sessionIdParam != null) {
                // Показываем форму редактирования сессии
                Long sessionId = Long.parseLong(sessionIdParam);
                Session session = sessionService.getSessionById(sessionId);

                if (session != null && session.getUserId().equals(user.getId())) {
                    data.put("session", session);
                    data.put("formAction", "edit");
                    renderTemplate(response, "session-form.ftlh", data);
                    return;
                } else {
                    response.sendRedirect("/sessions?error=access_denied");
                    return;
                }
            }

            // Получаем все сессии пользователя
            List<Session> userSessions = sessionService.getUserSessions(user.getId());
            data.put("sessions", userSessions);

            renderTemplate(response, "sessions.ftlh", data);

        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        } catch (NumberFormatException e) {
            response.sendRedirect("/sessions?error=invalid_id");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        SessionService sessionService = (SessionService) request.getServletContext().getAttribute("sessionService");

        try {
            String action = request.getParameter("action");

            if ("create".equals(action)) {
                // Создание новой сессии
                Session session = new Session();
                session.setUserId(user.getId());
                session.setTitle(request.getParameter("title"));
                session.setDescription(request.getParameter("description"));
                session.setLanguage(request.getParameter("language"));
                session.setPartnerId(parseLongSafe(request.getParameter("partnerId")));
                session.setScheduledTime(LocalDateTime.parse(request.getParameter("scheduledTime")));
                session.setDurationMinutes(Integer.parseInt(request.getParameter("duration")));
                session.setStatus("planned");

                sessionService.createSession(session);
                response.sendRedirect("/sessions?success=created");

            } else if ("update".equals(action)) {
                // Обновление существующей сессии
                Long sessionId = Long.parseLong(request.getParameter("id"));
                Session existingSession = sessionService.getSessionById(sessionId);

                if (existingSession != null && existingSession.getUserId().equals(user.getId())) {
                    existingSession.setTitle(request.getParameter("title"));
                    existingSession.setDescription(request.getParameter("description"));
                    existingSession.setLanguage(request.getParameter("language"));
                    existingSession.setPartnerId(parseLongSafe(request.getParameter("partnerId")));
                    existingSession.setScheduledTime(LocalDateTime.parse(request.getParameter("scheduledTime")));
                    existingSession.setDurationMinutes(Integer.parseInt(request.getParameter("duration")));

                    sessionService.updateSession(existingSession);
                    response.sendRedirect("/sessions?success=updated");
                } else {
                    response.sendRedirect("/sessions?error=access_denied");
                }

            } else if ("delete".equals(action)) {
                // Удаление сессии
                Long sessionId = Long.parseLong(request.getParameter("id"));
                Session session = sessionService.getSessionById(sessionId);

                if (session != null && session.getUserId().equals(user.getId())) {
                    sessionService.deleteSession(sessionId);
                    response.sendRedirect("/sessions?success=deleted");
                } else {
                    response.sendRedirect("/sessions?error=access_denied");
                }
            } else if ("complete".equals(action)) {
                // Завершение сессии
                Long sessionId = Long.parseLong(request.getParameter("id"));
                Session session = sessionService.getSessionById(sessionId);

                if (session != null && session.getUserId().equals(user.getId())) {
                    session.setStatus("completed");
                    sessionService.updateSession(session);
                    response.sendRedirect("/sessions?success=completed");
                } else {
                    response.sendRedirect("/sessions?error=access_denied");
                }
            }

        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        } catch (Exception e) {
            response.sendRedirect("/sessions?error=invalid_data");
        }
    }

    private Long parseLongSafe(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}