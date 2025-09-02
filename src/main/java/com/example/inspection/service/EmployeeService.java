package com.example.inspection.service;

import java.util.List;

import com.example.inspection.dto.request.EmployeeRequest;
import com.example.inspection.dto.response.EmployeeResponse;

public interface EmployeeService {
    EmployeeResponse create(EmployeeRequest request);

    EmployeeResponse getById(Long id);

    List<EmployeeResponse> getAll();

    EmployeeResponse update(Long id, EmployeeRequest request);

    void delete(Long id);
}