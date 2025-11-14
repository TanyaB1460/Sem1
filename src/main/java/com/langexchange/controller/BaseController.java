package com.langexchange.controller;

import com.langexchange.config.FreemarkerConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public abstract class BaseController {
    protected void renderTemplate(HttpServletResponse response,
                                  String templateName,
                                  Map<String, Object> data) throws IOException {
        try {
            System.out.println("● BaseController rendering: " + templateName);
            Configuration cfg = FreemarkerConfig.getConfiguration();
            System.out.println("● FreeMarker config obtained");

            // ИСПРАВЛЕНО: templateName вместо tempDateName
            Template template = cfg.getTemplate(templateName);
            System.out.println("● Template loaded: " + templateName);

            response.setContentType("text/html;charset=UTF-8");
            template.process(data, response.getWriter());
            System.out.println("● Template processed successfully");

        } catch (Exception e) {
            System.err.println("✗ Template error: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Template processing error: " + e.getMessage(), e);
        }
    }

    protected abstract void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
}