package com.langexchange.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/home")
public class HomeServlet extends BaseController {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("title", "Главная");

        renderTemplate(response, "home.ftlh", data);
    }
}