package com.example.inspection.service.evaluation;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.inspection.dto.request.evaluation.EvaluationCriteriaRequest;
import com.example.inspection.dto.response.evaluation.EvaluationCriteriaResponse;

public interface EvaluationCriteriaService {
    EvaluationCriteriaResponse create(EvaluationCriteriaRequest request);
    EvaluationCriteriaResponse getById(Long id);
    List<EvaluationCriteriaResponse> getAll();
    Page<EvaluationCriteriaResponse> getAllForPage(int page, int size);
    EvaluationCriteriaResponse update(Long id, EvaluationCriteriaRequest request);
    void delete(Long id);

    List<EvaluationCriteriaResponse> getByCategoryId(Long categoryId);
}

