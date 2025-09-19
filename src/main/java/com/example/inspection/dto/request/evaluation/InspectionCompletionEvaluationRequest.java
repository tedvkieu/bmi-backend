package com.example.inspection.dto.request.evaluation;

import java.time.LocalDate;

import lombok.Data;

@Data
public class InspectionCompletionEvaluationRequest {
    private Long dossierId;
    private String inspectionNumber;
    private LocalDate evaluationDate;
    private Long evaluatorUserId;
    private Long supervisorUserId;
    private String status;
    private String notes;
}

