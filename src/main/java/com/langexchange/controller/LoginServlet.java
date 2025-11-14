package com.langexchange.controller;

import com.langexchange.model.User;
import com.langexchange.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends BaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Показываем форму логина
        Map<String, Object> data = new HashMap<>();
        data.put("title", "Вход в систему");

        renderTemplate(response, "login.ftlh", data);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        AuthService authService = (AuthService) request.getServletContext().getAttribute("authService");

        try {
            User user = authService.authenticate(username, password);
            if (user != null) {
                // Успешная аутентификация
                request.getSession().setAttribute("user", user);
                response.sendRedirect("profile");
            } else {
                // Ошибка аутентификации
                Map<String, Object> data = new HashMap<>();
                data.put("title", "Вход в систему");
                data.put("error", "Неверное имя пользователя или пароль");

                renderTemplate(response, "login.ftlh", data);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}