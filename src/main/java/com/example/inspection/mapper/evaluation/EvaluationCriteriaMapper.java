package com.example.inspection.mapper.evaluation;

import org.springframework.stereotype.Component;

import com.example.inspection.dto.request.evaluation.EvaluationCriteriaRequest;
import com.example.inspection.dto.response.evaluation.EvaluationCriteriaResponse;
import com.example.inspection.entity.evaluation.EvaluationCriteria;

@Component
public class EvaluationCriteriaMapper {

    public EvaluationCriteria toEntity(EvaluationCriteriaRequest request) {
        EvaluationCriteria entity = new EvaluationCriteria();
        entity.setCategoryId(request.getCategoryId());
        entity.setCriteriaCode(request.getCriteriaCode());
        entity.setCriteriaOrder(request.getCriteriaOrder());
        entity.setCriteriaText(request.getCriteriaText());
        entity.setInputType(request.getInputType());
        entity.setIsRequired(request.getIsRequired());
        entity.setIsActive(request.getIsActive());
        return entity;
    }

    public void updateEntity(EvaluationCriteria entity, EvaluationCriteriaRequest request) {
        entity.setCategoryId(request.getCategoryId());
        entity.setCriteriaCode(request.getCriteriaCode());
        entity.setCriteriaOrder(request.getCriteriaOrder());
        entity.setCriteriaText(request.getCriteriaText());
        entity.setInputType(request.getInputType());
        entity.setIsRequired(request.getIsRequired());
        entity.setIsActive(request.getIsActive());
    }

    public EvaluationCriteriaResponse toResponse(EvaluationCriteria entity) {
        return new EvaluationCriteriaResponse(
                entity.getCriteriaId(),
                entity.getCategoryId(),
                entity.getCriteriaCode(),
                entity.getCriteriaOrder(),
                entity.getCriteriaText(),
                entity.getInputType(),
                entity.getIsRequired(),
                entity.getIsActive());
    }
}

