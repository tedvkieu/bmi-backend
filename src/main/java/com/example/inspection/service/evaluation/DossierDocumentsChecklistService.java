package com.example.inspection.service.evaluation;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.inspection.dto.request.evaluation.DossierDocumentsChecklistRequest;
import com.example.inspection.dto.response.evaluation.DossierDocumentsChecklistResponse;

public interface DossierDocumentsChecklistService {
    DossierDocumentsChecklistResponse create(DossierDocumentsChecklistRequest request);
    DossierDocumentsChecklistResponse getById(Long id);
    List<DossierDocumentsChecklistResponse> getAll();
    Page<DossierDocumentsChecklistResponse> getAllForPage(int page, int size);
    DossierDocumentsChecklistResponse update(Long id, DossierDocumentsChecklistRequest request);
    void delete(Long id);

    List<DossierDocumentsChecklistResponse> getByEvaluationId(Long evaluationId);
}

