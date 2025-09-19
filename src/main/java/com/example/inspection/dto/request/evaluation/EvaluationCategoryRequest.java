package com.example.inspection.dto.request.evaluation;

import lombok.Data;

@Data
public class EvaluationCategoryRequest {
    private String categoryCode;
    private String categoryName;
    private Integer categoryOrder;
    private Boolean isActive;
}

