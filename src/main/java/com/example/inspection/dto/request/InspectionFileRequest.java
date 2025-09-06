package com.example.inspection.dto.request;

import lombok.Data;

@Data
public class InspectionFileRequest {
    private Long customerId;
    private String serviceAddress;
    private String taxCode;
    private String email;
    // private ObjectType objectType;
}
