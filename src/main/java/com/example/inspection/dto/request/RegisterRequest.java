package com.example.inspection.dto.request;

import com.example.inspection.entity.User.Role;

import lombok.Data;

@Data
public class RegisterRequest {
    private String fullName;
    private String email;
    private String phone;
    private Role role = Role.CUSTOMER;
    private String password;
}
