package com.example.inspection.controller;

import com.example.inspection.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final JwtUtil jwtUtil;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','ISO_STAFF','DOCUMENT_STAFF','INSPECTOR')")
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        // Extract token from "Bearer <token>"
        String token = authHeader.substring(7);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", jwtUtil.extractUserId(token));
        userInfo.put("username", jwtUtil.extractUsername(token));
        userInfo.put("email", jwtUtil.extractEmail(token));
        userInfo.put("role", jwtUtil.extractRole(token));

        return ResponseEntity.ok(userInfo);
    }
}
