package com.example.inspection.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.inspection.entity.User.Role;

import lombok.Data;

@Data
public class UserResponse {
    private Long userId;
    private String fullName;
    private LocalDate dob;
    private Role role;
    private String username;
    private String email;
    private String phone;
    private String note;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
