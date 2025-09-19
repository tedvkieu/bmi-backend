package com.example.inspection.service.evaluation;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.inspection.dto.request.evaluation.EvaluationSignatureRequest;
import com.example.inspection.dto.response.evaluation.EvaluationSignatureResponse;

public interface EvaluationSignatureService {
    EvaluationSignatureResponse create(EvaluationSignatureRequest request);
    EvaluationSignatureResponse getById(Long id);
    List<EvaluationSignatureResponse> getAll();
    Page<EvaluationSignatureResponse> getAllForPage(int page, int size);
    EvaluationSignatureResponse update(Long id, EvaluationSignatureRequest request);
    void delete(Long id);

    List<EvaluationSignatureResponse> getByEvaluationId(Long evaluationId);
}

