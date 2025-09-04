package com.example.inspection.service;

import java.util.List;

import com.example.inspection.dto.request.InspectionTypeRequest;
import com.example.inspection.dto.response.InspectionTypeResponse;

public interface InspectionTypeService {
    InspectionTypeResponse create(InspectionTypeRequest request);

    InspectionTypeResponse getById(String id);

    List<InspectionTypeResponse> getAll();

    InspectionTypeResponse update(String id, InspectionTypeRequest request);

    void delete(String id);
}
