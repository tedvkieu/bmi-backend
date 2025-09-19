package com.example.inspection.mapper.evaluation;

import org.springframework.stereotype.Component;

import com.example.inspection.dto.request.evaluation.DossierInspectionTeamRequest;
import com.example.inspection.dto.response.evaluation.DossierInspectionTeamResponse;
import com.example.inspection.entity.evaluation.DossierInspectionTeam;

@Component
public class DossierInspectionTeamMapper {

    public DossierInspectionTeam toEntity(DossierInspectionTeamRequest request) {
        DossierInspectionTeam entity = new DossierInspectionTeam();
        entity.setDossierId(request.getDossierId());
        entity.setUserId(request.getUserId());
        entity.setExecutionRoleId(request.getExecutionRoleId());
        entity.setAssignedDate(request.getAssignedDate());
        entity.setIsActive(request.getIsActive());
        return entity;
    }

    public void updateEntity(DossierInspectionTeam entity, DossierInspectionTeamRequest request) {
        entity.setDossierId(request.getDossierId());
        entity.setUserId(request.getUserId());
        entity.setExecutionRoleId(request.getExecutionRoleId());
        entity.setAssignedDate(request.getAssignedDate());
        entity.setIsActive(request.getIsActive());
    }

    public DossierInspectionTeamResponse toResponse(DossierInspectionTeam entity) {
        String fullName = entity.getUser() != null ? entity.getUser().getFullName() : null;
        Long roleId = entity.getExecutionRole() != null ? entity.getExecutionRole().getRoleId()
                : entity.getExecutionRoleId();
        String roleCode = entity.getExecutionRole() != null ? entity.getExecutionRole().getRoleCode() : null;
        String roleName = entity.getExecutionRole() != null ? entity.getExecutionRole().getRoleName() : null;

        return new DossierInspectionTeamResponse(
                entity.getTeamId(),
                entity.getDossierId(),
                entity.getUserId(),
                fullName,
                roleId,
                roleCode,
                roleName,
                entity.getAssignedDate(),
                entity.getAssignedTask(),
                entity.getIsActive());
    }
}
