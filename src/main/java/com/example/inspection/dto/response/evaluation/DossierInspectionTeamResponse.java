package com.example.inspection.dto.response.evaluation;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DossierInspectionTeamResponse {
    private Long teamId;
    private Long dossierId;

    // User info
    private Long userId;
    private String fullName;

    // Role info
    private Long roleId;
    private String roleCode;
    private String roleName;

    private LocalDate assignedDate;
    private String assignTask;
    private Boolean isActive;
}
