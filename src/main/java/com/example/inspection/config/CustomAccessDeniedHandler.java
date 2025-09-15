package com.example.inspection.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        String body = String.format(
                "{\n  \"timestamp\": \"%s\",\n  \"status\": 403,\n  \"error\": \"Không có quyền truy cập\",\n  \"message\": \"%s\"\n}",
                LocalDateTime.now(),
                accessDeniedException != null && accessDeniedException.getMessage() != null
                        ? accessDeniedException.getMessage()
                        : "Bạn không có quyền thực hiện hành động này");
        response.getWriter().write(body);
    }
}
