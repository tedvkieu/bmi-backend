package com.example.inspection.service.evaluation;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.inspection.dto.request.evaluation.EvaluationCategoryRequest;
import com.example.inspection.dto.response.evaluation.EvaluationCategoryResponse;

public interface EvaluationCategoryService {
    EvaluationCategoryResponse create(EvaluationCategoryRequest request);
    EvaluationCategoryResponse getById(Long id);
    List<EvaluationCategoryResponse> getAll();
    Page<EvaluationCategoryResponse> getAllForPage(int page, int size);
    EvaluationCategoryResponse update(Long id, EvaluationCategoryRequest request);
    void delete(Long id);
}

