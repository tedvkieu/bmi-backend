package com.example.inspection.dto.request;

import lombok.Data;
import java.time.LocalDate;

import com.example.inspection.entity.Customer.CustomerType;

@Data
public class CustomerRequest {
    private String name;
    private String address;
    private String email;
    private LocalDate dob;
    private String phone;
    private String note;
    private String taxCode;
    private CustomerType customerType;
}