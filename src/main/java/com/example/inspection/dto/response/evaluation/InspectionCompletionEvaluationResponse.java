package com.example.inspection.dto.response.evaluation;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionCompletionEvaluationResponse {
    private Long evaluationId;
    private Long dossierId;
    private String inspectionNumber;
    private LocalDate evaluationDate;
    private Long evaluatorUserId;
    private Long supervisorUserId;
    private String status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

