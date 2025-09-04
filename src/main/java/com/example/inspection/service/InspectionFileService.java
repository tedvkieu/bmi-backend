package com.example.inspection.service;

import org.springframework.data.domain.Page;

import com.example.inspection.dto.request.InspectionFileRequest;
import com.example.inspection.dto.response.InspectionFileResponse;

public interface InspectionFileService {
    InspectionFileResponse create(InspectionFileRequest request);

    Page<InspectionFileResponse> getAll(int page, int size);

    InspectionFileResponse getById(Long id);

    InspectionFileResponse update(Long id, InspectionFileRequest request);

    void delete(Long id);
}
