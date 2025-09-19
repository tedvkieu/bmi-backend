package com.example.inspection.dto.request.evaluation;

import lombok.Data;

@Data
public class InspectionExecutionRoleRequest {
    private String roleCode;
    private String roleName;
    private String roleDescription;
    private Boolean isLeader;
}

