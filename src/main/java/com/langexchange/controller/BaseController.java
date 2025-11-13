package com.langexchange.controller;

import com.langexchange.config.FreemarkerConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    protected void renderTemplate(HttpServletResponse response,
                                  String templateName,
                                  Map<String, Object> data) throws IOException {
        try {
            Configuration cfg = FreemarkerConfig.getConfiguration();
            Template template = cfg.getTemplate(templateName);

            response.setContentType("text/html;charset=UTF-8");
            template.process(data, response.getWriter());

        } catch (TemplateException e) {
            throw new IOException("Template processing error: " + e.getMessage(), e);
        }
    }
}