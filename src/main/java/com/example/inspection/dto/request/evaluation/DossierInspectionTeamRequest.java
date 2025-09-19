package com.example.inspection.dto.request.evaluation;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DossierInspectionTeamRequest {
    private Long dossierId;
    private Long userId;
    private Long executionRoleId;
    private LocalDate assignedDate;
    private Boolean isActive;
}

