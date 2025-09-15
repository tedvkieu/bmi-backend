package com.example.inspection.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExecutionUnitResponse {
    private Long executionUnitId;
    private Long dossierId;
    private Long userId;
    private String roleInCase;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
