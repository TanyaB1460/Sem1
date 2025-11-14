package com.langexchange.filter;

import com.langexchange.model.User;
import com.langexchange.service.SessionService;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // Публичные пути, которые доступны без авторизации
        if (path.startsWith("/login") || path.startsWith("/register") ||
                path.startsWith("/error") || path.equals("/") || path.equals("/home") ||
                path.startsWith("/resources") || path.startsWith("/css") || path.startsWith("/js")) {
            chain.doFilter(request, response);
            return;
        }

        // Проверяем авторизацию для защищенных путей
        if (session != null && session.getAttribute("user") != null) {
            // Пользователь авторизован
            chain.doFilter(request, response);
        } else {
            // Перенаправляем на страницу логина
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?redirect=" + path);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("✅ AuthFilter initialized");
    }

    @Override
    public void destroy() {
        System.out.println("🔚 AuthFilter destroyed");
    }
}