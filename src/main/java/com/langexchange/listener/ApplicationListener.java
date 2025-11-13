package com.langexchange.listener;

import com.langexchange.config.FreemarkerConfig;
import com.langexchange.dao.UserDao;
import com.langexchange.dao.InterestDao;
import com.langexchange.dao.SessionDao;
import com.langexchange.service.AuthService;
import com.langexchange.service.UserService;
import com.langexchange.service.SessionService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("🚀 Application starting...");

        try {
            ServletContext context = sce.getServletContext();

            // Инициализация DAO
            UserDao userDao = new UserDao();
            InterestDao interestDao = new InterestDao();
            SessionDao sessionDao = new SessionDao();

            // Инициализация сервисов
            AuthService authService = new AuthService(userDao, sessionDao);
            UserService userService = new UserService(userDao, interestDao);
            SessionService sessionService = new SessionService(sessionDao);

            // Сохраняем сервисы в контекст приложения
            context.setAttribute("authService", authService);
            context.setAttribute("userService", userService);
            context.setAttribute("sessionService", sessionService);

            // Инициализация Freemarker - БЕЗ ПАРАМЕТРА
            FreemarkerConfig.configure();

            System.out.println("✅ All services initialized successfully");

        } catch (Exception e) {
            System.err.println("❌ Application initialization failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Application initialization failed", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("🔚 Application shutting down...");
    }
}