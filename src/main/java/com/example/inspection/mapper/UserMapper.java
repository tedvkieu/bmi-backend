package com.example.inspection.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.inspection.dto.request.UserRequest;
import com.example.inspection.dto.response.UserResponse;
import com.example.inspection.entity.User;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User toEntity(UserRequest request) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setDob(request.getDob());
        user.setRole(request.getRole());
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPasswordHash()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setNote(request.getNote());
        user.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        return user;
    }

    public void updateEntity(User user, UserRequest request) {
        user.setFullName(request.getFullName());
        user.setDob(request.getDob());
        user.setRole(request.getRole());
        user.setUsername(request.getUsername());
        if (request.getPasswordHash() != null && !request.getPasswordHash().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPasswordHash()));
        }
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setNote(request.getNote());
        if (request.getIsActive() != null) {
            user.setIsActive(request.getIsActive());
        }
    }

    public UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setFullName(user.getFullName());
        response.setDob(user.getDob());
        response.setRole(user.getRole());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setNote(user.getNote());
        response.setIsActive(user.getIsActive());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
