package com.example.inspection.service;

import com.example.inspection.dto.request.LoginRequest;
import com.example.inspection.dto.request.RegisterRequest;
import com.example.inspection.dto.response.ApiResponse;
import com.example.inspection.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    ApiResponse register(RegisterRequest registerRequest);
}
