package com.example.inspection.service.evaluation;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.inspection.dto.request.evaluation.InspectionExecutionRoleRequest;
import com.example.inspection.dto.response.evaluation.InspectionExecutionRoleResponse;

public interface InspectionExecutionRoleService {
    InspectionExecutionRoleResponse create(InspectionExecutionRoleRequest request);
    InspectionExecutionRoleResponse getById(Long id);
    List<InspectionExecutionRoleResponse> getAll();
    Page<InspectionExecutionRoleResponse> getAllForPage(int page, int size);
    InspectionExecutionRoleResponse update(Long id, InspectionExecutionRoleRequest request);
    void delete(Long id);
}

