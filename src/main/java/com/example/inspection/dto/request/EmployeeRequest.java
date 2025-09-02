package com.example.inspection.dto.request;

import java.time.LocalDate;

import com.example.inspection.entity.Employee.Position;

import lombok.Data;

@Data
public class EmployeeRequest {
    private String fullName;
    private LocalDate dob;
    private Position position;
    private String username;
    private String passwordHash;
    private String email;
    private String phone;
    private String note;
}