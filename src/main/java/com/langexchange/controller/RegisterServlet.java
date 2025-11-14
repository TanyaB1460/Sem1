package com.langexchange.controller;

import com.langexchange.model.User;
import com.langexchange.service.AuthService;
import com.langexchange.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/register")
public class RegisterServlet extends BaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserService userService = (UserService) request.getServletContext().getAttribute("userService");

        try {
            Map<String, Object> data = new HashMap<>();
            data.put("title", "Регистрация");
            data.put("interests", userService.getAllInterests());
            renderTemplate(response, "register.ftlh", data);
            data.put("contextPath", request.getContextPath());

        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String nativeLanguage = request.getParameter("nativeLanguage");
        String learningLanguage = request.getParameter("learningLanguage");
        String level = request.getParameter("level");
        String[] interestIds = request.getParameterValues("interests");

        // Создаем пользователя
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(password); // В реальном приложении нужно хешировать!
        user.setNativeLanguage(nativeLanguage);
        user.setLearningLanguage(learningLanguage);
        user.setLevel(level);

        AuthService authService = (AuthService) request.getServletContext().getAttribute("authService");

        try {
            // Преобразуем interestIds в List<Long>
            List<Long> interests = new ArrayList<>();
            if (interestIds != null) {
                for (String interestId : interestIds) {
                    interests.add(Long.parseLong(interestId));
                }
            }

            // Регистрируем пользователя
            authService.registerUser(user, interests);

            // Перенаправляем на страницу логина
            response.sendRedirect("login?message=registered");

        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        } catch (IllegalArgumentException e) {
            // Ошибка валидации (пользователь уже существует и т.д.)
            Map<String, Object> data = new HashMap<>();
            data.put("title", "Регистрация");
            data.put("error", e.getMessage());
            try {
                data.put("interests", ((UserService) request.getServletContext().getAttribute("userService")).getAllInterests());
            } catch (SQLException ex) {
                // Игнорируем, т.к. основная ошибка важнее
            }
            renderTemplate(response, "register.ftlh", data);
        }
    }
}