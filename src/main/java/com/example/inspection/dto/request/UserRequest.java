package com.example.inspection.dto.request;

import java.time.LocalDate;

import com.example.inspection.entity.User.Role;

import lombok.Data;

@Data
public class UserRequest {
    private String fullName;
    private LocalDate dob;
    private Role role;
    private String username;
    private String passwordHash;
    private String email;
    private String phone;
    private String note;
    private Boolean isActive;
}
