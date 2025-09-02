package com.example.inspection.service;

import java.util.List;

import com.example.inspection.dto.request.CustomerRequest;
import com.example.inspection.dto.response.CustomerResponse;

public interface CustomerService {
    CustomerResponse create(CustomerRequest request);

    CustomerResponse getById(Long id);

    List<CustomerResponse> getAll();

    CustomerResponse update(Long id, CustomerRequest request);

    void delete(Long id);
}
