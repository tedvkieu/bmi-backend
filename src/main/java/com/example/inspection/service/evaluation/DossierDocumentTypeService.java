package com.example.inspection.service.evaluation;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.inspection.dto.request.evaluation.DossierDocumentTypeRequest;
import com.example.inspection.dto.response.evaluation.DossierDocumentTypeResponse;

public interface DossierDocumentTypeService {
    DossierDocumentTypeResponse create(DossierDocumentTypeRequest request);
    DossierDocumentTypeResponse getById(Long id);
    List<DossierDocumentTypeResponse> getAll();
    Page<DossierDocumentTypeResponse> getAllForPage(int page, int size);
    DossierDocumentTypeResponse update(Long id, DossierDocumentTypeRequest request);
    void delete(Long id);
}

