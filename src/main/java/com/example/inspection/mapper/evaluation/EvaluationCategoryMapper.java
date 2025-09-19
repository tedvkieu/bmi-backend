package com.example.inspection.mapper.evaluation;

import org.springframework.stereotype.Component;

import com.example.inspection.dto.request.evaluation.EvaluationCategoryRequest;
import com.example.inspection.dto.response.evaluation.EvaluationCategoryResponse;
import com.example.inspection.entity.evaluation.EvaluationCategory;

@Component
public class EvaluationCategoryMapper {

    public EvaluationCategory toEntity(EvaluationCategoryRequest request) {
        EvaluationCategory entity = new EvaluationCategory();
        entity.setCategoryCode(request.getCategoryCode());
        entity.setCategoryName(request.getCategoryName());
        entity.setCategoryOrder(request.getCategoryOrder());
        entity.setIsActive(request.getIsActive());
        return entity;
    }

    public void updateEntity(EvaluationCategory entity, EvaluationCategoryRequest request) {
        entity.setCategoryCode(request.getCategoryCode());
        entity.setCategoryName(request.getCategoryName());
        entity.setCategoryOrder(request.getCategoryOrder());
        entity.setIsActive(request.getIsActive());
    }

    public EvaluationCategoryResponse toResponse(EvaluationCategory entity) {
        return new EvaluationCategoryResponse(
                entity.getCategoryId(),
                entity.getCategoryCode(),
                entity.getCategoryName(),
                entity.getCategoryOrder(),
                entity.getIsActive());
    }
}

