package com.langexchange.controller;

import com.langexchange.model.User;
import com.langexchange.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/profile")
public class ProfileServlet extends BaseController {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        UserService userService = (UserService) request.getServletContext().getAttribute("userService");

        try {
            Map<String, Object> data = new HashMap<>();
            data.put("title", "Профиль пользователя");
            data.put("user", user);
            data.put("userInterests", userService.getUserInterests(user.getId()));

            renderTemplate(response, "profile.ftlh", data);

        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}