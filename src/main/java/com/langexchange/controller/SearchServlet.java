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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/search")
public class SearchServlet extends BaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserService userService = (UserService) request.getServletContext().getAttribute("userService");
        User currentUser = (User) request.getSession().getAttribute("user");

        try {
            Map<String, Object> data = new HashMap<>();
            data.put("title", "Поиск партнеров");
            data.put("interests", userService.getAllInterests());
            data.put("contextPath", request.getContextPath());

            // Если есть параметры поиска - выполняем поиск
            String nativeLanguage = request.getParameter("nativeLanguage");
            String learningLanguage = request.getParameter("learningLanguage");
            String[] interestParams = request.getParameterValues("interests");

            if (nativeLanguage != null || learningLanguage != null || interestParams != null) {
                List<Long> interestIds = new ArrayList<>();
                if (interestParams != null) {
                    for (String interestId : interestParams) {
                        interestIds.add(Long.parseLong(interestId));
                    }
                }

                // Используем обновленный метод с передачей ID текущего пользователя
                List<User> partners = userService.findUsersByCriteria(
                        nativeLanguage,
                        learningLanguage,
                        interestIds,
                        currentUser.getId()
                );

                data.put("partners", partners);
                data.put("searchPerformed", true);
                data.put("searchNativeLanguage", nativeLanguage);
                data.put("searchLearningLanguage", learningLanguage);
                data.put("searchInterests", interestIds);
            }

            renderTemplate(response, "search.ftlh", data);

        } catch (SQLException e) {
            throw new ServletException("Database error during search", e);
        }
    }
}