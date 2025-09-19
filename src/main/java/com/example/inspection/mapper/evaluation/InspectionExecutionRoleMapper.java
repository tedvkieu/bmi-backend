package com.example.inspection.mapper.evaluation;

import org.springframework.stereotype.Component;

import com.example.inspection.dto.request.evaluation.InspectionExecutionRoleRequest;
import com.example.inspection.dto.response.evaluation.InspectionExecutionRoleResponse;
import com.example.inspection.entity.evaluation.InspectionExecutionRole;

@Component
public class InspectionExecutionRoleMapper {

    public InspectionExecutionRole toEntity(InspectionExecutionRoleRequest request) {
        InspectionExecutionRole entity = new InspectionExecutionRole();
        entity.setRoleCode(request.getRoleCode());
        entity.setRoleName(request.getRoleName());
        entity.setRoleDescription(request.getRoleDescription());
        entity.setIsLeader(request.getIsLeader());
        return entity;
    }

    public void updateEntity(InspectionExecutionRole entity, InspectionExecutionRoleRequest request) {
        entity.setRoleCode(request.getRoleCode());
        entity.setRoleName(request.getRoleName());
        entity.setRoleDescription(request.getRoleDescription());
        entity.setIsLeader(request.getIsLeader());
    }

    public InspectionExecutionRoleResponse toResponse(InspectionExecutionRole entity) {
        return new InspectionExecutionRoleResponse(
                entity.getRoleId(),
                entity.getRoleCode(),
                entity.getRoleName(),
                entity.getRoleDescription(),
                entity.getIsLeader(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}

