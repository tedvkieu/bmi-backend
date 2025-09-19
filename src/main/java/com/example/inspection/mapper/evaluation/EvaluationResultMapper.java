package com.example.inspection.mapper.evaluation;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.example.inspection.dto.request.evaluation.EvaluationResultRequest;
import com.example.inspection.dto.response.evaluation.EvaluationResultResponse;
import com.example.inspection.entity.evaluation.EvaluationResult;

@Component
public class EvaluationResultMapper {

    public EvaluationResult toEntity(EvaluationResultRequest request) {
        EvaluationResult entity = new EvaluationResult();
        entity.setEvaluationId(request.getEvaluationId());
        entity.setCriteriaId(request.getCriteriaId());
        entity.setCheckboxValue(request.getCheckboxValue());
        entity.setTextValue(request.getTextValue());
        entity.setNumberValue(toBigDecimal(request.getNumberValue()));
        entity.setDateValue(request.getDateValue());
        entity.setSelectValue(request.getSelectValue());
        entity.setNotes(request.getNotes());
        return entity;
    }

    public void updateEntity(EvaluationResult entity, EvaluationResultRequest request) {
        entity.setEvaluationId(request.getEvaluationId());
        entity.setCriteriaId(request.getCriteriaId());
        entity.setCheckboxValue(request.getCheckboxValue());
        entity.setTextValue(request.getTextValue());
        entity.setNumberValue(toBigDecimal(request.getNumberValue()));
        entity.setDateValue(request.getDateValue());
        entity.setSelectValue(request.getSelectValue());
        entity.setNotes(request.getNotes());
    }

    public EvaluationResultResponse toResponse(EvaluationResult entity) {
        return new EvaluationResultResponse(
                entity.getResultId(),
                entity.getEvaluationId(),
                entity.getCriteriaId(),
                entity.getCheckboxValue(),
                entity.getTextValue(),
                toDouble(entity.getNumberValue()),
                entity.getDateValue(),
                entity.getSelectValue(),
                entity.getNotes());
    }

    private BigDecimal toBigDecimal(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    private Double toDouble(BigDecimal value) {
        return value == null ? null : value.doubleValue();
    }
}
