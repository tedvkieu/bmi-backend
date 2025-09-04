package com.example.inspection.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.inspection.dto.request.CustomerRequest;
import com.example.inspection.dto.response.CustomerResponse;

public interface CustomerService {
    CustomerResponse create(CustomerRequest request);

    CustomerResponse getById(Long id);

    List<CustomerResponse> getAll();

    Page<CustomerResponse> getAllForPage(int page, int size);

    CustomerResponse update(Long id, CustomerRequest request);

    void delete(Long id);
}
