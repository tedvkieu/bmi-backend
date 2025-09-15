// package com.example.inspection.mapper;

// import org.springframework.stereotype.Component;

// import com.example.inspection.dto.request.ExecutionUnitRequest;
// import com.example.inspection.dto.request.ExecutionUnitResponse;
// import com.example.inspection.entity.User;
// import com.example.inspection.entity.Dossier;
// import com.example.inspection.entity.ExecutionUnit;

// @Component
// public class ExecutionUnitMapper {

// public ExecutionUnit toEntity(ExecutionUnitRequest request, Dossier dossier,
// User user) {
// ExecutionUnit executionUnit = new ExecutionUnit();
// executionUnit.setDossier(dossier);
// executionUnit.setUser(user);
// executionUnit.setRoleInCase(request.getRoleInCase());
// return executionUnit;
// }

// public void updateEntity(ExecutionUnit executionUnit, ExecutionUnitRequest
// request, Dossier dossier,
// User user) {
// executionUnit.setDossier(dossier);
// executionUnit.setUser(user);
// executionUnit.setRoleInCase(request.getRoleInCase());
// }

// public ExecutionUnitResponse toResponse(ExecutionUnit executionUnit) {
// ExecutionUnitResponse response = new ExecutionUnitResponse();
// response.setExecutionUnitId(executionUnit.getExecutionUnitId());
// response.setDossierId(executionUnit.getDossier().getDossierId());
// response.setUserId(executionUnit.getUser().getUserId());
// response.setRoleInCase(executionUnit.getRoleInCase());
// response.setCreatedAt(executionUnit.getCreatedAt());
// response.setUpdatedAt(executionUnit.getUpdatedAt());
// return response;
// }
// }
