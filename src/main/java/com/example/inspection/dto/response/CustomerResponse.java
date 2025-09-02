package com.example.inspection.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.inspection.entity.Customer.CustomerType;

@Data
public class CustomerResponse {
    private Long customerId;
    private String name;
    private String address;
    private String email;
    private LocalDate dob;
    private String phone;
    private String note;
    private CustomerType customerType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}