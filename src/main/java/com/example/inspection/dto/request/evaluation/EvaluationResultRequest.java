package com.example.inspection.dto.request.evaluation;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EvaluationResultRequest {
    private Long evaluationId;
    private Long criteriaId;
    private Boolean checkboxValue;
    private String textValue;
    private Double numberValue;
    private LocalDate dateValue;
    private String selectValue;
    private String notes;
}

