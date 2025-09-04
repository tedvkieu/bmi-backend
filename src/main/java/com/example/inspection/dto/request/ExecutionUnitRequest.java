package com.example.inspection.dto.request;

import lombok.Data;

@Data
public class ExecutionUnitRequest {
    private Long receiptId;
    private Long employeeId;
    private String roleInCase;
}
