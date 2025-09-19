package com.example.inspection.dto.response.evaluation;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationResultResponse {
    private Long resultId;
    private Long evaluationId;
    private Long criteriaId;
    private Boolean checkboxValue;
    private String textValue;
    private Double numberValue;
    private LocalDate dateValue;
    private String selectValue;
    private String notes;
}

