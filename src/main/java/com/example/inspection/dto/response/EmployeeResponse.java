package com.example.inspection.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.inspection.entity.Employee.Position;

import lombok.Data;

@Data
public class EmployeeResponse {
    private Long employeeId;
    private String fullName;
    private LocalDate dob;
    private Position position;
    private String username;
    private String email;
    private String phone;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}