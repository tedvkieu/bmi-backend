package com.example.inspection.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        String body = String.format(
                "{\n  \"timestamp\": \"%s\",\n  \"status\": 401,\n  \"error\": \"Không được xác thực\",\n  \"message\": \"%s\"\n}",
                LocalDateTime.now(),
                authException != null && authException.getMessage() != null ? authException.getMessage()
                        : "Bạn cần đăng nhập để truy cập tài nguyên này");
        response.getWriter().write(body);
    }
}
