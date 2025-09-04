package com.example.inspection.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InspectionTypeResponse {
    private String inspectionTypeId;
    private String name;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
