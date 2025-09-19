package com.example.inspection.dto.response.evaluation;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionExecutionRoleResponse {
    private Long roleId;
    private String roleCode;
    private String roleName;
    private String roleDescription;
    private Boolean isLeader;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

