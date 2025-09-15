package com.example.inspection.implementation;

import com.example.inspection.dto.request.LoginRequest;
import com.example.inspection.dto.response.LoginResponse;
import com.example.inspection.entity.User;
import com.example.inspection.exception.ResourceNotFoundException;
import com.example.inspection.repository.UserRepository;
import com.example.inspection.service.AuthService;
import com.example.inspection.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // Find user by email
        User user = userRepository.findByEmailAndIsActiveTrue(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email hoặc mật khẩu không chính xác"));

        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new ResourceNotFoundException("Email hoặc mật khẩu không chính xác");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.getUserId());

        // Build response
        return new LoginResponse(
                token,
                "Bearer",
                user.getUsername(),
                user.getEmail(),
                user.getRole().getDescription(),
                user.getFullName(),
                user.getUserId());
    }
}
