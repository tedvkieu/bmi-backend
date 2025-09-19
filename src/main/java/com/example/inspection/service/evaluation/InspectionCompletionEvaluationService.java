package com.example.inspection.service.evaluation;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.inspection.dto.request.evaluation.InspectionCompletionEvaluationRequest;
import com.example.inspection.dto.response.evaluation.InspectionCompletionEvaluationResponse;

public interface InspectionCompletionEvaluationService {
    InspectionCompletionEvaluationResponse create(InspectionCompletionEvaluationRequest request);
    InspectionCompletionEvaluationResponse getById(Long id);
    List<InspectionCompletionEvaluationResponse> getAll();
    Page<InspectionCompletionEvaluationResponse> getAllForPage(int page, int size);
    InspectionCompletionEvaluationResponse update(Long id, InspectionCompletionEvaluationRequest request);
    void delete(Long id);

    List<InspectionCompletionEvaluationResponse> getByDossierId(Long dossierId);
}

