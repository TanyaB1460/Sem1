package com.langexchange.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("🔐 AuthFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();

        // Пропускаем статические ресурсы и страницы аутентификации
        if (path.startsWith("/css/") || path.startsWith("/js/") ||
                path.equals("/") || path.equals("/login") || path.equals("/register")) {
            chain.doFilter(request, response);
            return;
        }

        // Проверка аутентификации для защищенных путей
        if (path.contains("/profile") || path.contains("/sessions") ||
                path.contains("/find-partner") || path.contains("/create-session")) {

            if (httpRequest.getSession().getAttribute("user") == null) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("🔐 AuthFilter destroyed");
    }
}