package com.example.inspection.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class InspectionFileResponse {
    private Long inspectionFileId;
    private Long customerId;
    private String customerName;
    private String serviceAddress;
    private String taxCode;
    private String email;
    private String objectType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
