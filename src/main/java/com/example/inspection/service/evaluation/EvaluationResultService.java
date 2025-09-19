package com.example.inspection.service.evaluation;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.inspection.dto.request.evaluation.EvaluationResultRequest;
import com.example.inspection.dto.response.evaluation.EvaluationResultResponse;

public interface EvaluationResultService {
    EvaluationResultResponse create(EvaluationResultRequest request);
    EvaluationResultResponse getById(Long id);
    List<EvaluationResultResponse> getAll();
    Page<EvaluationResultResponse> getAllForPage(int page, int size);
    EvaluationResultResponse update(Long id, EvaluationResultRequest request);
    void delete(Long id);

    List<EvaluationResultResponse> getByEvaluationId(Long evaluationId);
}

