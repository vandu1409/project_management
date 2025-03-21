package com.project_managament.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class EmailTemplateUtil {
    public static String loadTemplate(String templateName, String... replacements) {
        String templatePath = "/email-templates/" + templateName;
        try (InputStream inputStream = EmailTemplateUtil.class.getResourceAsStream(templatePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            if (inputStream == null) {
                throw new IOException("Template not found: " + templatePath);
            }

            String content = reader.lines().collect(Collectors.joining("\n"));

            for (int i = 0; i < replacements.length; i += 2) {
                content = content.replace(replacements[i], replacements[i + 1]);
            }
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
