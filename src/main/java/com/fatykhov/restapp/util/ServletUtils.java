package com.fatykhov.restapp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class ServletUtils {
    private static final String CONTENT_TYPE = "application/json";

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void sendBadRequest(HttpServletResponse resp, String errorMessage) throws IOException {
        sendError(resp, HttpServletResponse.SC_BAD_REQUEST, errorMessage);
    }

    public static void sendNotFound(HttpServletResponse resp, Long id, String name) throws IOException {
        String errorMessage = String.format("%s with id = %d is not found", name, id);
        sendError(resp, HttpServletResponse.SC_NOT_FOUND, errorMessage);
    }

    public static void sendError(HttpServletResponse resp, int errorCode, String errorMessage) throws IOException {
        Map<String, Object> errorMap = Map.of(
                "errorCode", errorCode,
                "errorMessage", errorMessage
        );
        sendJsonResponse(resp, errorCode, errorMap);
    }

    public static void sendJsonResponse(HttpServletResponse resp, int statusCode, Object responseObject) throws IOException {
        resp.setContentType(CONTENT_TYPE);
        resp.setStatus(statusCode);
        String jsonResponse = mapper.writeValueAsString(responseObject);
        resp.getWriter().write(jsonResponse);
    }

    public static String readJson(BufferedReader reader) {
        return reader.lines().collect(Collectors.joining());
    }
}
