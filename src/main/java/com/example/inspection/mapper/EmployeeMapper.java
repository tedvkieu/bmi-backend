package com.example.inspection.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.inspection.dto.request.EmployeeRequest;
import com.example.inspection.dto.response.EmployeeResponse;
import com.example.inspection.entity.Employee;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {

    private final PasswordEncoder passwordEncoder;

    public Employee toEntity(EmployeeRequest request) {
        Employee employee = new Employee();
        employee.setFullName(request.getFullName());
        employee.setDob(request.getDob());
        employee.setPosition(request.getPosition());
        employee.setUsername(request.getUsername());
        employee.setPasswordHash(passwordEncoder.encode(request.getPasswordHash()));
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setNote(request.getNote());
        return employee;
    }

    public void updateEntity(Employee employee, EmployeeRequest request) {
        employee.setFullName(request.getFullName());
        employee.setDob(request.getDob());
        employee.setPosition(request.getPosition());
        employee.setUsername(request.getUsername());
        if (request.getPasswordHash() != null && !request.getPasswordHash().isEmpty()) {
            employee.setPasswordHash(passwordEncoder.encode(request.getPasswordHash()));
        }
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setNote(request.getNote());
    }

    public EmployeeResponse toResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setEmployeeId(employee.getEmployeeId());
        response.setFullName(employee.getFullName());
        response.setDob(employee.getDob());
        response.setPosition(employee.getPosition());
        response.setUsername(employee.getUsername());
        response.setEmail(employee.getEmail());
        response.setPhone(employee.getPhone());
        response.setNote(employee.getNote());
        response.setCreatedAt(employee.getCreatedAt());
        response.setUpdatedAt(employee.getUpdatedAt());
        return response;
    }
}