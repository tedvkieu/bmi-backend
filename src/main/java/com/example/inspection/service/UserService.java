package com.example.inspection.service;

import java.util.List;

import com.example.inspection.dto.request.UserRequest;
import com.example.inspection.dto.response.UserResponse;

public interface UserService {
    UserResponse create(UserRequest request);

    UserResponse getById(Long id);

    List<UserResponse> getAll();

    UserResponse update(Long id, UserRequest request);

    void delete(Long id);

    UserResponse getByUsername(String username);

    UserResponse getByEmail(String email);

    boolean hasAdmin();
}
