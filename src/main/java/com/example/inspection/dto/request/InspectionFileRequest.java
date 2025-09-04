package com.example.inspection.dto.request;

import com.example.inspection.entity.InspectionFile.ObjectType;
import lombok.Data;

@Data
public class InspectionFileRequest {
    private Long customerId;
    private String serviceAddress;
    private String taxCode;
    private String email;
    private ObjectType objectType;
}
