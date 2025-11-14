package com.langexchange.config;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import jakarta.servlet.ServletContext;

public class FreemarkerConfig {
    private static Configuration cfg;

    public static void configure(ServletContext context) {
        cfg = new Configuration(Configuration.VERSION_2_3_32);

        // Загрузка шаблонов из classpath (src/main/resouces/templates)
        cfg.setClassLoaderForTemplateLoading(
                FreemarkerConfig.class.getClassLoader(),
                "templates"
        );

        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        cfg.setLogTemplateExceptions(false);

        // Отладочная информация
        System.out.println("✅ Freemarker configured successfully");
        System.out.println("📁 Loading templates from: classpath:templates/");
        System.out.println("🔍 ClassLoader: " + FreemarkerConfig.class.getClassLoader());

        // Проверка доступности шаблонов
        try {
            java.net.URL templateUrl = FreemarkerConfig.class.getClassLoader().getResource("templates/home.ftlh");
            System.out.println("📄 Home template URL: " + templateUrl);
            if (templateUrl != null) {
                System.out.println("✅ Home template FOUND");
            } else {
                System.out.println("❌ Home template NOT FOUND");
            }
        } catch (Exception e) {
            System.out.println("❌ Error checking templates: " + e.getMessage());
        }
    }

    // 🔥 ДОБАВЬТЕ ЭТОТ МЕТОД!
    public static Configuration getConfiguration() {
        if (cfg == null) {
            throw new IllegalStateException("Freemarker not configured. Call configure() first.");
        }
        return cfg;
    }
}