package com.langexchange.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet({"/home", "/"})
public class HomeServlet extends BaseController {

    public HomeServlet() {
        System.out.println("🎯🎯🎯 HomeServlet CONSTRUCTOR! 🎯🎯🎯");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("🔄 HomeServlet: processing request to " + request.getRequestURI());

        try {
            Map<String, Object> data = new HashMap<>();
            data.put("title", "Главная - LangExchange");

            Object user = request.getSession().getAttribute("user");
            if (user != null) {
                data.put("user", user);
            }

            System.out.println("🔄 Rendering template: home.ftlh");
            System.out.println("📊 Data: " + data);

            renderTemplate(response, "home.ftlh", data);
            System.out.println("✅ Template rendered successfully");

        } catch (Exception e) {
            System.err.println("❌ HomeServlet error: " + e.getMessage());
            e.printStackTrace();
            response.sendError(500, "Server error: " + e.getMessage());
        }
    }
}