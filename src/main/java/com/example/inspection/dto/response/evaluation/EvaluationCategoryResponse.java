package com.example.inspection.dto.response.evaluation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationCategoryResponse {
    private Long categoryId;
    private String categoryCode;
    private String categoryName;
    private Integer categoryOrder;
    private Boolean isActive;
}

