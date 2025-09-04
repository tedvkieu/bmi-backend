package com.example.inspection.dto.request;

import lombok.Data;

@Data
public class InspectionTypeRequest {
    private String inspectionTypeId;
    private String name;
    private String note;
}