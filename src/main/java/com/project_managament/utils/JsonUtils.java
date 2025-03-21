package com.project_managament.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parseJsonRequest(HttpServletRequest request, Class<T> clazz) throws IOException {
        BufferedReader reader = request.getReader();
        return objectMapper.readValue(reader, clazz);
    }

    public static void sendJsonResponse(HttpServletResponse response, int statusCode, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        objectMapper.writeValue(response.getWriter(), data);
    }

    public static Map<String, String> parseJsonRequest(HttpServletRequest request) throws IOException {
        return objectMapper.readValue(request.getInputStream(), Map.class);
    }

}
