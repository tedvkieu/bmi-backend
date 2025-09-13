package com.example.inspection.implementation;

import com.example.inspection.dto.request.LoginRequest;
import com.example.inspection.dto.response.LoginResponse;
import com.example.inspection.entity.Employee;
import com.example.inspection.exception.ResourceNotFoundException;
import com.example.inspection.repository.EmployeeRepository;
import com.example.inspection.service.AuthService;
import com.example.inspection.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // Find employee by email
        Employee employee = employeeRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email hoặc mật khẩu không chính xác"));

        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), employee.getPasswordHash())) {
            throw new ResourceNotFoundException("Email hoặc mật khẩu không chính xác");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(
                employee.getUsername(),
                employee.getEmail(),
                employee.getPosition().name());

        // Build response
        return new LoginResponse(
                token,
                "Bearer",
                employee.getUsername(),
                employee.getEmail(),
                employee.getPosition().getDescription(),
                employee.getFullName(),
                employee.getEmployeeId());
    }
}
