package com.example.inspection.service;

import java.util.List;

import com.example.inspection.dto.request.ExecutionUnitRequest;
import com.example.inspection.dto.request.ExecutionUnitResponse;

public interface ExecutionUnitService {
    ExecutionUnitResponse create(ExecutionUnitRequest request);

    ExecutionUnitResponse getById(Long id);

    List<ExecutionUnitResponse> getAll();

    ExecutionUnitResponse update(Long id, ExecutionUnitRequest request);

    void delete(Long id);
}
