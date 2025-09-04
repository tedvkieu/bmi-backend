package com.example.inspection.mapper;

import org.springframework.stereotype.Component;

import com.example.inspection.dto.request.InspectionTypeRequest;
import com.example.inspection.dto.response.InspectionTypeResponse;
import com.example.inspection.entity.InspectionType;

@Component
public class InspectionTypeMapper {

    public InspectionType toEntity(InspectionTypeRequest request) {
        InspectionType inspectionType = new InspectionType();
        inspectionType.setInspectionTypeId(request.getInspectionTypeId());
        inspectionType.setName(request.getName());
        inspectionType.setNote(request.getNote());
        return inspectionType;
    }

    public void updateEntity(InspectionType inspectionType, InspectionTypeRequest request) {
        inspectionType.setName(request.getName());
        inspectionType.setNote(request.getNote());
    }

    public InspectionTypeResponse toResponse(InspectionType inspectionType) {
        InspectionTypeResponse response = new InspectionTypeResponse();
        response.setInspectionTypeId(inspectionType.getInspectionTypeId());
        response.setName(inspectionType.getName());
        response.setNote(inspectionType.getNote());
        response.setCreatedAt(inspectionType.getCreatedAt());
        response.setUpdatedAt(inspectionType.getUpdatedAt());
        return response;
    }
}
