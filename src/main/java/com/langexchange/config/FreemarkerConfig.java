package com.langexchange.config;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;

public class FreemarkerConfig {
    private static Configuration cfg;

    public static void configure() throws IOException {
        cfg = new Configuration(Configuration.VERSION_2_3_32);

        // Загрузка шаблонов из classpath (src/main/resources/templates)
        cfg.setClassLoaderForTemplateLoading(
                FreemarkerConfig.class.getClassLoader(),
                "templates"
        );

        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        cfg.setLogTemplateExceptions(false);

        System.out.println("✅ Freemarker configured successfully - loading from classpath:templates/");
    }

    public static Configuration getConfiguration() {
        if (cfg == null) {
            throw new IllegalStateException("Freemarker not configured. Call configure() first.");
        }
        return cfg;
    }
}