package com.example.inspection.dto.request.evaluation;

import com.example.inspection.entity.evaluation.EvaluationCriteria.InputType;

import lombok.Data;

@Data
public class EvaluationCriteriaRequest {
    private Long categoryId;
    private String criteriaCode;
    private Integer criteriaOrder;
    private String criteriaText;
    private InputType inputType;
    private Boolean isRequired;
    private Boolean isActive;
}

