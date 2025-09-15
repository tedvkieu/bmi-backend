package com.example.inspection.dto.request;

import lombok.Data;

@Data
public class ExecutionUnitRequest {
    private Long dossierId;
    private Long userId;
    private String roleInCase;
}
