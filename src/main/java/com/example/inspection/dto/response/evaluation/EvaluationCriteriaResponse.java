package com.example.inspection.dto.response.evaluation;

import com.example.inspection.entity.evaluation.EvaluationCriteria.InputType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationCriteriaResponse {
    private Long criteriaId;
    private Long categoryId;
    private String criteriaCode;
    private Integer criteriaOrder;
    private String criteriaText;
    private InputType inputType;
    private Boolean isRequired;
    private Boolean isActive;
}

